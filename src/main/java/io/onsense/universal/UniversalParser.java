package io.onsense.universal;

import ch.obermuhlner.math.big.BigDecimalMath;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import com.squareup.javapoet.CodeBlock;
import io.onsense.universal.grammar.UniversalBaseVisitor;
import io.onsense.universal.grammar.UniversalLexer;
import io.onsense.universal.grammar.UniversalVisitor;
import io.onsense.universe.*;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

/**
 * @author Koen Rooijmans
 */
public class UniversalParser {

    public static CodeBlock parse(String universal) {
        return parse(Collections.emptyMap(), universal);
    }

    public static CodeBlock parse(Map<String, Class<?>> propertyNameTypeMap, String universal) {
        return parse(propertyNameTypeMap, universal, false);
    }

    public static CodeBlock parse(Map<String, Class<?>> propertyNameTypeMap, String universal, boolean nullify) {

        final String preParsedUniversal = UniversalPreParser.preParse(universal);
        final UniversalLexer lexer = new UniversalLexer(CharStreams.fromString(preParsedUniversal));
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final io.onsense.universal.grammar.UniversalParser parser = new io.onsense.universal.grammar.UniversalParser(tokens);
        final io.onsense.universal.grammar.UniversalParser.UniversalContext expression = parser.universal();
        parser.setErrorHandler(new BailErrorStrategy());

        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new IllegalArgumentException("invalid.universal");
        }

        final Class<?> parsedExpectedType = UniversalTypeParser.parse(propertyNameTypeMap, universal);
        final boolean expectingArray = UniversalArrayTypeParser.parse(propertyNameTypeMap, universal);
        final Class<?> expectedType;
        if (parsedExpectedType.isArray() && !expectingArray) {
            expectedType = parsedExpectedType.getComponentType();
        } else {
            expectedType = parsedExpectedType;
        }

        final UniversalVisitor<CodeBlock> visitor = new CodeBlockUniversalVisitor(expectedType, propertyNameTypeMap);

        visitor.visit(expression);
        final CodeBlock block = ((CodeBlockUniversalVisitor) visitor).getBuilder().build();

        final ImmutableSet<String> properties = ((CodeBlockUniversalVisitor) visitor).propertiesBuilder.build();

        final CodeBlock.Builder propertiesCheckBlock = CodeBlock.builder();
        if (properties.isEmpty()) {
            propertiesCheckBlock.add("($T) $T.universal(", expectedType, Universals.class).add(block).add(")");
        } else {
            propertiesCheckBlock.add("(");
            for (UnmodifiableIterator<String> iterator = properties.iterator(); iterator.hasNext(); ) {
                final String property = iterator.next();

                propertiesCheckBlock.add("$L == null", property);

                if (iterator.hasNext()) {
                    propertiesCheckBlock.add(" || ");
                } else {
                    if (nullify) {
                        if (Number.class.isAssignableFrom(expectedType) && !BigDecimal.class.isAssignableFrom(expectedType)) {
                            propertiesCheckBlock.add(") ? null : (")
                                    .add("($T) $T.universal($T.class", expectedType, Universals.class, expectedType).add(block).add(")")
                                    .add(")");
                        } else {
                            propertiesCheckBlock.add(") ? null : (")
                                    .add("($T) $T.universal(", expectedType, Universals.class).add(block).add(")")
                                    .add(")");
                        }
                    } else {
                        if (Number.class.isAssignableFrom(expectedType) && !BigDecimal.class.isAssignableFrom(expectedType)) {
                            propertiesCheckBlock.add(") ? null : (")
                                    .add("($T) $T.universal($T.class, ", expectedType, Universals.class, expectedType).add(block).add(")")
                                    .add(")");
                        } else {
                            propertiesCheckBlock.add(") ? null : (")
                                    .add("($T) $T.universal(", expectedType, Universals.class).add(block).add(")")
                                    .add(")");
                        }
                    }
                }
            }
        }

        return propertiesCheckBlock.build();
    }

    public static class UniversalSuffix {

        private final UUID uuid;
        private final CodeBlock codeBlock;
        private int nesting;

        public UniversalSuffix(int nesting, UUID uuid, CodeBlock codeBlock) {
            this.nesting = nesting;
            this.uuid = uuid;
            this.codeBlock = codeBlock;
        }

        public UniversalSuffix(int nesting, CodeBlock codeBlock) {
            this(nesting, UUID.randomUUID(), codeBlock);
        }

        public UniversalSuffix(int nesting) {
            this(nesting, UUID.randomUUID(), null);
        }

        public int getNesting() {
            return nesting;
        }

        public UUID getUuid() {
            return uuid;
        }

        @Nullable
        public CodeBlock getCodeBlock() {
            return codeBlock;
        }
    }

    protected static class CodeBlockUniversalVisitor extends UniversalBaseVisitor<CodeBlock> implements UniversalVisitor<CodeBlock> {

        private final ImmutableSet.Builder<String> propertiesBuilder = ImmutableSet.builder();
        private final Deque<Deque<UniversalSuffix>> stackOfStacks = new ArrayDeque<>();
        private final CodeBlock.Builder builder = CodeBlock.builder();
        private Deque<UniversalSuffix> stack = new ArrayDeque<>();
        private Map<String, Class<?>> propertyNameTypeMap;
        private Class<?> expectedType;

        public CodeBlockUniversalVisitor() {
            this(Collections.emptyMap());
        }

        public CodeBlockUniversalVisitor(Map<String, Class<?>> propertyNameTypeMap) {
            this.propertyNameTypeMap = propertyNameTypeMap;
        }

        public CodeBlockUniversalVisitor(Class<?> expectedType, Map<String, Class<?>> propertyNameTypeMap) {
            this.expectedType = expectedType;
            this.propertyNameTypeMap = propertyNameTypeMap;
        }

        @Override
        public CodeBlock visitEquation(io.onsense.universal.grammar.UniversalParser.EquationContext ctx) {
            final CodeBlock codeBlock = super.visitEquation(ctx);

            while (codeBlock == null && stack != null && stack.peek() != null) {
                builder.add(stack.poll().getCodeBlock());
            }

            return codeBlock;
        }

        @Override
        public CodeBlock visitExpression(io.onsense.universal.grammar.UniversalParser.ExpressionContext ctx) {
            final CodeBlock codeBlock = super.visitExpression(ctx);

            while (codeBlock == null && stack != null && stack.peek() != null) {
                builder.add(stack.poll().getCodeBlock());
            }

            return codeBlock;
        }

        @Override
        public CodeBlock visitMultiplyingExpression(io.onsense.universal.grammar.UniversalParser.MultiplyingExpressionContext ctx) {
            final CodeBlock codeBlock = super.visitMultiplyingExpression(ctx);

            while (codeBlock == null && stack != null && stack.peek() != null) {
                builder.add(stack.poll().getCodeBlock());
            }

            return codeBlock;
        }

        @Override
        public CodeBlock visitPowExpression(io.onsense.universal.grammar.UniversalParser.PowExpressionContext ctx) {
            final CodeBlock codeBlock = super.visitPowExpression(ctx);

            while (codeBlock == null && stack != null && stack.peek() != null) {
                builder.add(stack.poll().getCodeBlock());
            }

            return codeBlock;
        }

        @Override
        public CodeBlock visitSignedAtom(io.onsense.universal.grammar.UniversalParser.SignedAtomContext ctx) {
            final CodeBlock codeBlock = super.visitSignedAtom(ctx);

            while (codeBlock == null && stack != null && stack.peek() != null) {
                builder.add(stack.poll().getCodeBlock());
            }

            return codeBlock;
        }

        @Override
        public CodeBlock visitAtom(io.onsense.universal.grammar.UniversalParser.AtomContext ctx) {
            final CodeBlock codeBlock = super.visitAtom(ctx);

            while (codeBlock == null && stack != null && stack.peek() != null) {
                builder.add(stack.poll().getCodeBlock());
            }

            return codeBlock;
        }

        @Override
        public CodeBlock visitScientific(io.onsense.universal.grammar.UniversalParser.ScientificContext ctx) {
            final CodeBlock codeBlock = super.visitScientific(ctx);

            return codeBlock;
        }

        @Override
        public CodeBlock visitConstant(io.onsense.universal.grammar.UniversalParser.ConstantContext ctx) {
            final CodeBlock codeBlock = super.visitConstant(ctx);

            return codeBlock;
        }

        @Override
        public CodeBlock visitVariable(io.onsense.universal.grammar.UniversalParser.VariableContext ctx) {
            final CodeBlock codeBlock = super.visitVariable(ctx);

            return codeBlock;
        }

        @Override
        public CodeBlock visitFunc(io.onsense.universal.grammar.UniversalParser.FuncContext ctx) {
            final CodeBlock codeBlock = super.visitFunc(ctx);

            return codeBlock;
        }

        @Override
        public CodeBlock visitFuncname(io.onsense.universal.grammar.UniversalParser.FuncnameContext ctx) {
            final CodeBlock codeBlock = super.visitFuncname(ctx);

            return codeBlock;
        }

        @Override
        public CodeBlock visitRelop(io.onsense.universal.grammar.UniversalParser.RelopContext ctx) {
            final CodeBlock codeBlock = super.visitRelop(ctx);

            return codeBlock;
        }

        public CodeBlock.Builder getBuilder() {
            return builder;
        }

        public ImmutableSet.Builder<String> getPropertiesBuilder() {
            return propertiesBuilder;
        }

        @Override
        public CodeBlock visitTerminal(TerminalNode node) {
            switch (node.getSymbol().getType()) {
                // math & trig
                case UniversalLexer.ABS:
                    builder.add("$T.abs", Formulas.class);
                    break;
                case UniversalLexer.ACOS:
                    builder.add("$T.acos", Formulas.class);
                    break;
                case UniversalLexer.ACOSH:
                    builder.add("$T.acosh", Formulas.class);
                    break;
                case UniversalLexer.ACOT:
                    builder.add("$T.acot", Formulas.class);
                    break;
                case UniversalLexer.ACOTH:
                    builder.add("$T.acoth", Formulas.class);
                    break;
                case UniversalLexer.AGGREGATE:
                    builder.add("$T.aggregate", Formulas.class);
                    break;
                case UniversalLexer.ARABIC:
                    builder.add("$T.arabic", Formulas.class);
                    break;
                case UniversalLexer.ASIN:
                    builder.add("$T.asin", Formulas.class);
                    break;
                case UniversalLexer.ASINH:
                    builder.add("$T.asinh", Formulas.class);
                    break;
                case UniversalLexer.ATAN:
                    builder.add("$T.atan", Formulas.class);
                    break;
                case UniversalLexer.ATAN2:
                    builder.add("$T.atan2", Formulas.class);
                    break;
                case UniversalLexer.ATANH:
                    builder.add("$T.atanh", Formulas.class);
                    break;
                case UniversalLexer.BASE:
                    builder.add("$T.base", Formulas.class);
                    break;
                case UniversalLexer.CEILING:
                    builder.add("$T.ceiling", Formulas.class);
                    break;
                case UniversalLexer.CEILING_MATH:
                    builder.add("$T.ceiling_math", Formulas.class);
                    break;
                case UniversalLexer.CEILING_PRECISE:
                    builder.add("$T.ceiling_precise", Formulas.class);
                    break;
                case UniversalLexer.COMBIN:
                    builder.add("$T.combin", Formulas.class);
                    break;
                case UniversalLexer.COMBINA:
                    builder.add("$T.combina", Formulas.class);
                    break;
                case UniversalLexer.COS:
                    builder.add("$T.cos", Formulas.class);
                    break;
                case UniversalLexer.COSH:
                    builder.add("$T.cosh", Formulas.class);
                    break;
                case UniversalLexer.COT:
                    builder.add("$T.cot", Formulas.class);
                    break;
                case UniversalLexer.COTH:
                    builder.add("$T.coth", Formulas.class);
                    break;
                case UniversalLexer.CSC:
                    builder.add("$T.csc", Formulas.class);
                    break;
                case UniversalLexer.CSCH:
                    builder.add("$T.csch", Formulas.class);
                    break;
                case UniversalLexer.DECIMAL:
                    builder.add("$T.decimal", Formulas.class);
                    break;
                case UniversalLexer.DEGREES:
                    builder.add("$T.degrees", Formulas.class);
                    break;
                case UniversalLexer.EVEN:
                    builder.add("$T.even", Formulas.class);
                    break;
                case UniversalLexer.EXP:
                    builder.add("$T.exp", Formulas.class);
                    break;
                case UniversalLexer.FACT:
                    builder.add("$T.fact", Formulas.class);
                    break;
                case UniversalLexer.FACTDOUBLE:
                    builder.add("$T.factdouble", Formulas.class);
                    break;
                case UniversalLexer.FLOOR:
                    builder.add("$T.floor", Formulas.class);
                    break;
                case UniversalLexer.FLOOR_MATH:
                    builder.add("$T.floor_math", Formulas.class);
                    break;
                case UniversalLexer.FLOOR_PRECISE:
                    builder.add("$T.floor_precise", Formulas.class);
                    break;
                case UniversalLexer.GCD:
                    builder.add("$T.gcd", Formulas.class);
                    break;
                case UniversalLexer.INT:
                    builder.add("$T.toInt", Formulas.class);
                    break;
                case UniversalLexer.ISO_CEILING:
                    builder.add("$T.iso_ceiling", Formulas.class);
                    break;
                case UniversalLexer.LCM:
                    builder.add("$T.lcm", Formulas.class);
                    break;
                case UniversalLexer.LN:
                    builder.add("$T.ln", Formulas.class);
                    break;
                case UniversalLexer.LOG:
                    builder.add("$T.log", Formulas.class);
                    break;
                case UniversalLexer.LOG2:
                    builder.add("$T.log2", Formulas.class);
                    break;
                case UniversalLexer.LOG10:
                    builder.add("$T.log10", Formulas.class);
                    break;
                case UniversalLexer.MDETERM:
                    builder.add("$T.mdeterm", Formulas.class);
                    break;
                case UniversalLexer.MINVERSE:
                    builder.add("$T.minverse", Formulas.class);
                    break;
                case UniversalLexer.MMULT:
                    builder.add("$T.mmult", Formulas.class);
                    break;
                case UniversalLexer.MOD:
                    builder.add("$T.mod", Formulas.class);
                    break;
                case UniversalLexer.MROUND:
                    builder.add("$T.mround", Formulas.class);
                    break;
                case UniversalLexer.MULTINOMIAL:
                    builder.add("$T.multinomial", Formulas.class);
                    break;
                case UniversalLexer.MUNIT:
                    builder.add("$T.munit", Formulas.class);
                    break;
                case UniversalLexer.ODD:
                    builder.add("$T.odd", Formulas.class);
                    break;
                case UniversalLexer.POWER:
                    builder.add("$T.power", Formulas.class);
                    break;
                case UniversalLexer.PRODUCT:
                    builder.add("$T.product", Formulas.class);
                    break;
                case UniversalLexer.QUOTIENT:
                    builder.add("$T.quotient", Formulas.class);
                    break;
                case UniversalLexer.RADIANS:
                    builder.add("$T.radians", Formulas.class);
                    break;
                case UniversalLexer.RAND:
                    builder.add("$T.rand", Formulas.class);
                    break;
                case UniversalLexer.RANDBETWEEN:
                    builder.add("$T.randbetween", Formulas.class);
                    break;
                case UniversalLexer.ROMAN:
                    builder.add("$T.roman", Formulas.class);
                    break;
                case UniversalLexer.ROUND:
                    builder.add("$T.round", Formulas.class);
                    break;
                case UniversalLexer.ROUNDDOWN:
                    builder.add("$T.rounddown", Formulas.class);
                    break;
                case UniversalLexer.ROUNDUP:
                    builder.add("$T.roundup", Formulas.class);
                    break;
                case UniversalLexer.SEC:
                    builder.add("$T.sec", Formulas.class);
                    break;
                case UniversalLexer.SECH:
                    builder.add("$T.sech", Formulas.class);
                    break;
                case UniversalLexer.SERIESSUM:
                    builder.add("$T.seriessum", Formulas.class);
                    break;
                case UniversalLexer.NUMSIGN:
                    builder.add("$T.sign", Formulas.class);
                    break;
                case UniversalLexer.SIN:
                    builder.add("$T.sin", Formulas.class);
                    break;
                case UniversalLexer.SINH:
                    builder.add("$T.sinh", Formulas.class);
                    break;
                case UniversalLexer.SQRT:
                    builder.add("$T.sqrt", Formulas.class);
                    break;
                case UniversalLexer.SQRTPI:
                    builder.add("$T.sqrtpi", Formulas.class);
                    break;
                case UniversalLexer.SUBTOTAL:
                    builder.add("$T.subtotal", Formulas.class);
                    break;
                case UniversalLexer.SUM:
                    builder.add("$T.sum", Formulas.class);
                    break;
                case UniversalLexer.SUMIF:
                    builder.add("$T.sumif", Formulas.class);
                    break;
                case UniversalLexer.SUMIFS:
                    builder.add("$T.sumifs", Formulas.class);
                    break;
                case UniversalLexer.SUMPRODUCT:
                    builder.add("$T.sumproduct", Formulas.class);
                    break;
                case UniversalLexer.SUMSQ:
                    builder.add("$T.sumsq", Formulas.class);
                    break;
                case UniversalLexer.SUMX2MY2:
                    builder.add("$T.sumx2my2", Formulas.class);
                    break;
                case UniversalLexer.SUMX2PY2:
                    builder.add("$T.sumx2py2", Formulas.class);
                    break;
                case UniversalLexer.SUMXMY2:
                    builder.add("$T.sumxmy2", Formulas.class);
                    break;
                case UniversalLexer.TAN:
                    builder.add("$T.tan", Formulas.class);
                    break;
                case UniversalLexer.TANH:
                    builder.add("$T.tanh", Formulas.class);
                    break;
                case UniversalLexer.TRUNC:
                    builder.add("$T.trunc", Formulas.class);
                    break;
                // TODO category
                case UniversalLexer.CBRT:
                    builder.add("$T.cbrt", Formulas.class);
                    break;
                case UniversalLexer.HYPOT:
                    builder.add("$T.hypot", Formulas.class);
                    break;
                case UniversalLexer.ROOT:
                    builder.add("$T.root", Formulas.class);
                    break;
                case UniversalLexer.FACTORIAL:
                    builder.add("$T.factorial", Formulas.class);
                    break;
                case UniversalLexer.BERNOULLI:
                    builder.add("$T.bernoulli", Formulas.class);
                    break;
                case UniversalLexer.MANTISSA:
                    builder.add("$T.mantissa", Formulas.class);
                    break;
                case UniversalLexer.INTEGRAL:
                    builder.add("$T.integral", Formulas.class);
                    break;
                case UniversalLexer.FRACTION:
                    builder.add("$T.fraction", Formulas.class);
                    break;
                case UniversalLexer.AVG:
                    builder.add("$T.avg", Formulas.class);
                    break;
                case UniversalLexer.MEAN:
                    builder.add("$T.mean", Formulas.class);
                    break;
                case UniversalLexer.MODE:
                    builder.add("$T.mode", Formulas.class);
                    break;
                case UniversalLexer.RANGE:
                    builder.add("$T.range", Formulas.class);
                    break;
                case UniversalLexer.MULTIPLY:
                    builder.add("$T.multiply", Formulas.class);
                    break;
                case UniversalLexer.DIVIDE:
                    builder.add("$T.divide", Formulas.class);
                    break;
                // textual
                case UniversalLexer.ASC:
                    builder.add("$T.asc", Expressions.class);
                    break;
                case UniversalLexer.BAHTTEXT:
                    builder.add("$T.bahttext", Expressions.class);
                    break;
                case UniversalLexer.CHAR:
                    builder.add("$T.charr", Expressions.class);
                    break;
                case UniversalLexer.CLEAN:
                    builder.add("$T.clean", Expressions.class);
                    break;
                case UniversalLexer.CODE:
                    builder.add("$T.code", Expressions.class);
                    break;
                case UniversalLexer.CONCAT:
                    builder.add("$T.concat", Expressions.class);
                    break;
                case UniversalLexer.CONCATENATE:
                    builder.add("$T.concatenate", Expressions.class);
                    break;
                case UniversalLexer.DBCS:
                    builder.add("$T.dbcs", Expressions.class);
                    break;
                case UniversalLexer.DOLLAR:
                    builder.add("$T.dollar", Expressions.class);
                    break;
                case UniversalLexer.EXACT:
                    builder.add("$T.exact", Expressions.class);
                    break;
                case UniversalLexer.FIND:
                    builder.add("$T.find", Expressions.class);
                    break;
                case UniversalLexer.FINDB:
                    builder.add("$T.findb", Expressions.class);
                    break;
                case UniversalLexer.FIXED:
                    builder.add("$T.fixed", Expressions.class);
                    break;
                case UniversalLexer.LEFT:
                    builder.add("$T.left", Expressions.class);
                    break;
                case UniversalLexer.LEFTB:
                    builder.add("$T.leftb", Expressions.class);
                    break;
                case UniversalLexer.LEN:
                    builder.add("$T.len", Expressions.class);
                    break;
                case UniversalLexer.LENB:
                    builder.add("$T.lenb", Expressions.class);
                    break;
                case UniversalLexer.LOWER:
                    builder.add("$T.lower", Expressions.class);
                    break;
                case UniversalLexer.MID:
                    builder.add("$T.mid", Expressions.class);
                    break;
                case UniversalLexer.MIDB:
                    builder.add("$T.midb", Expressions.class);
                    break;
                case UniversalLexer.NUMBERVALUE:
                    builder.add("$T.numbervalue", Expressions.class);
                    break;
                case UniversalLexer.PHONETIC:
                    builder.add("$T.phonetic", Expressions.class);
                    break;
                case UniversalLexer.PROPER:
                    builder.add("$T.proper", Expressions.class);
                    break;
                case UniversalLexer.REPLACE:
                    builder.add("$T.replace", Expressions.class);
                    break;
                case UniversalLexer.REPLACEB:
                    builder.add("$T.replaceb", Expressions.class);
                    break;
                case UniversalLexer.REPT:
                    builder.add("$T.rept", Expressions.class);
                    break;
                case UniversalLexer.RIGHT:
                    builder.add("$T.right", Expressions.class);
                    break;
                case UniversalLexer.RIGHTB:
                    builder.add("$T.rightb", Expressions.class);
                    break;
                case UniversalLexer.SEARCH:
                    builder.add("$T.search", Expressions.class);
                    break;
                case UniversalLexer.SEARCHB:
                    builder.add("$T.searchb", Expressions.class);
                    break;
                case UniversalLexer.SUBSTITUTE:
                    builder.add("$T.substitute", Expressions.class);
                    break;
                case UniversalLexer.T:
                    builder.add("$T.t", Expressions.class);
                    break;
                case UniversalLexer.TEXT:
                    builder.add("$T.text", Expressions.class);
                    break;
                case UniversalLexer.TEXTJOIN:
                    builder.add("$T.textjoin", Expressions.class);
                    break;
                case UniversalLexer.TRIM:
                    builder.add("$T.trim", Expressions.class);
                    break;
                case UniversalLexer.UNICHAR:
                    builder.add("$T.unichar", Expressions.class);
                    break;
                case UniversalLexer.UNICODE:
                    builder.add("$T.unicode", Expressions.class);
                    break;
                case UniversalLexer.UPPER:
                    builder.add("$T.upper", Expressions.class);
                    break;
                case UniversalLexer.VALUE:
                    builder.add("$T.value", Expressions.class);
                    break;
                // additional textual
                case UniversalLexer.FORMAT:
                    builder.add("$T.format", Expressions.class);
                    break;
                case UniversalLexer.LTRIM:
                    builder.add("$T.ltrim", Expressions.class);
                    break;
                case UniversalLexer.RTRIM:
                    builder.add("$T.rtrim", Expressions.class);
                    break;
                case UniversalLexer.CHOP:
                    builder.add("$T.chop", Expressions.class);
                    break;
                case UniversalLexer.REPEAT:
                    builder.add("$T.repeat", Expressions.class);
                    break;
                case UniversalLexer.PREFIX:
                    builder.add("$T.prefix", Expressions.class);
                    break;
                case UniversalLexer.SUFFIX:
                    builder.add("$T.suffix", Expressions.class);
                    break;
                case UniversalLexer.CAPITALIZE:
                    builder.add("$T.capitalize", Expressions.class);
                    break;
                case UniversalLexer.CAPITALIZE_FULLY:
                    builder.add("$T.capitalizeFully", Expressions.class);
                    break;
                case UniversalLexer.DECAPITALIZE:
                    builder.add("$T.decapitalize", Expressions.class);
                    break;
                case UniversalLexer.DECAPITALIZE_FULLY:
                    builder.add("$T.decapitalizeFully", Expressions.class);
                    break;
                case UniversalLexer.SUBSTRING:
                    builder.add("$T.substring", Expressions.class);
                    break;
                case UniversalLexer.REMOVE:
                    builder.add("$T.remove", Expressions.class);
                    break;
                case UniversalLexer.REMOVE_FIRST:
                    builder.add("$T.removeFirst", Expressions.class);
                    break;
                case UniversalLexer.REMOVE_LAST:
                    builder.add("$T.removeLast", Expressions.class);
                    break;
                case UniversalLexer.REPLACE_FIRST:
                    builder.add("$T.replaceFirst", Expressions.class);
                    break;
                case UniversalLexer.REPLACE_LAST:
                    builder.add("$T.replaceLast", Expressions.class);
                    break;
                case UniversalLexer.REVERSE:
                    builder.add("$T.reverse", Expressions.class);
                    break;
                case UniversalLexer.INITIALS:
                    builder.add("$T.initials", Expressions.class);
                    break;
                case UniversalLexer.SWAP:
                    builder.add("$T.swap", Expressions.class);
                    break;
                case UniversalLexer.QUOTE:
                    builder.add("$T.quote", Expressions.class);
                    break;
                case UniversalLexer.UNQUOTE:
                    builder.add("$T.unquote", Expressions.class);
                    break;
                case UniversalLexer.ENCODE:
                    builder.add("$T.encode", Expressions.class);
                    break;
                case UniversalLexer.DECODE:
                    builder.add("$T.decode", Expressions.class);
                    break;
                case UniversalLexer.RANDOM:
                    builder.add("$T.random", Expressions.class);
                    break;
                case UniversalLexer.ATOB:
                    builder.add("$T.atob", Expressions.class);
                    break;
                case UniversalLexer.BTOA:
                    builder.add("$T.btoa", Expressions.class);
                    break;
                case UniversalLexer.HEX:
                    builder.add("$T.hex", Expressions.class);
                    break;
                case UniversalLexer.MD2:
                    builder.add("$T.md2", Expressions.class);
                    break;
                case UniversalLexer.MD5:
                    builder.add("$T.md5", Expressions.class);
                    break;
                case UniversalLexer.SHA256:
                    builder.add("$T.sha256", Expressions.class);
                    break;
                case UniversalLexer.CRC32:
                    builder.add("$T.crc32", Expressions.class);
                    break;
                case UniversalLexer.HTML2TEXT:
                    builder.add("$T.html2text", Expressions.class);
                    break;
                // logical
                case UniversalLexer.AND:
                    builder.add("$T.logicalAnd", Logicals.class);
                    break;
                case UniversalLexer.FALSE:
                    builder.add("$T.logicalFalse", Logicals.class);
                    break;
                case UniversalLexer.IF:
                    builder.add("$T.logicalIf", Logicals.class);
                    break;
                case UniversalLexer.IFERROR:
                    builder.add("$T.logicalIferror", Logicals.class);
                    break;
                case UniversalLexer.IFNA:
                    builder.add("$T.logicalIfna", Logicals.class);
                    break;
                case UniversalLexer.IFS:
                    builder.add("$T.logicalIfs", Logicals.class);
                    break;
                case UniversalLexer.NOT:
                    builder.add("$T.logicalNot", Logicals.class);
                    break;
                case UniversalLexer.OR:
                    builder.add("$T.logicalOr", Logicals.class);
                    break;
                case UniversalLexer.SWITCH:
                    builder.add("$T.logicalSwitch", Logicals.class);
                    break;
                case UniversalLexer.TRUE:
                    builder.add("$T.logicalTrue", Logicals.class);
                    break;
                case UniversalLexer.XOR:
                    builder.add("$T.logicalXor", Logicals.class);
                    break;
                // additional logical
                case UniversalLexer.ISNULL:
                    builder.add("$T.logicalIsNull", Logicals.class);
                    break;
                case UniversalLexer.ISNOTNULL:
                    builder.add("$T.logicalIsNotNull", Logicals.class);
                    break;
                // date & time
                case UniversalLexer.DATE:
                    builder.add("$T.date", Moments.class);
                    break;
                case UniversalLexer.DATEDIF:
                    builder.add("$T.datedif", Moments.class);
                    break;
                case UniversalLexer.DATEVALUE:
                    builder.add("$T.datevalue", Moments.class);
                    break;
                case UniversalLexer.DAY:
                    builder.add("$T.day", Moments.class);
                    break;
                case UniversalLexer.DAYS:
                    builder.add("$T.days", Moments.class);
                    break;
                case UniversalLexer.DAYS360:
                    builder.add("$T.days360", Moments.class);
                    break;
                case UniversalLexer.EDATE:
                    builder.add("$T.edate", Moments.class);
                    break;
                case UniversalLexer.EOMONTH:
                    builder.add("$T.eomonth", Moments.class);
                    break;
                case UniversalLexer.HOUR:
                    builder.add("$T.hour", Moments.class);
                    break;
                case UniversalLexer.ISOWEEKNUM:
                    builder.add("$T.isoweeknum", Moments.class);
                    break;
                case UniversalLexer.MINUTE:
                    builder.add("$T.minute", Moments.class);
                    break;
                case UniversalLexer.MONTH:
                    builder.add("$T.month", Moments.class);
                    break;
                case UniversalLexer.NETWORKDAYS:
                    builder.add("$T.networkdays", Moments.class);
                    break;
                case UniversalLexer.NETWORKDAYS_INTL:
                    builder.add("$T.networkdays_intl", Moments.class);
                    break;
                case UniversalLexer.NOW:
                    builder.add("$T.now", Moments.class);
                    break;
                case UniversalLexer.SECOND:
                    builder.add("$T.second", Moments.class);
                    break;
                case UniversalLexer.TIME:
                    builder.add("$T.time", Moments.class);
                    break;
                case UniversalLexer.TIMEVALUE:
                    builder.add("$T.timevalue", Moments.class);
                    break;
                case UniversalLexer.TODAY:
                    builder.add("$T.today", Moments.class);
                    break;
                case UniversalLexer.WEEKDAY:
                    builder.add("$T.weekday", Moments.class);
                    break;
                case UniversalLexer.WEEKNUM:
                    builder.add("$T.weeknum", Moments.class);
                    break;
                case UniversalLexer.WORKDAY:
                    builder.add("$T.workday", Moments.class);
                    break;
                case UniversalLexer.WORKDAY_INTL:
                    builder.add("$T.workday_intl", Moments.class);
                    break;
                case UniversalLexer.YEAR:
                    builder.add("$T.year", Moments.class);
                    break;
                case UniversalLexer.YEARFRAC:
                    builder.add("$T.yearfrac", Moments.class);
                    break;
                case UniversalLexer.WEEK:
                    builder.add("$T.week", Moments.class);
                    break;
                case UniversalLexer.TIMENOW:
                    builder.add("$T.timenow", Moments.class);
                    break;
                case UniversalLexer.DATETIMEVALUE:
                    builder.add("$T.datetimevalue", Moments.class);
                    break;
                // engineering
                case UniversalLexer.BESSELI:
                    builder.add("$T.besseli", Formulas.class);
                    break;
                case UniversalLexer.BESSELJ:
                    builder.add("$T.besselj", Formulas.class);
                    break;
                case UniversalLexer.BESSELK:
                    builder.add("$T.besselk", Formulas.class);
                    break;
                case UniversalLexer.BESSELY:
                    builder.add("$T.bessely", Formulas.class);
                    break;
                case UniversalLexer.BIN2DEC:
                    builder.add("$T.bin2dec", Formulas.class);
                    break;
                case UniversalLexer.BIN2HEX:
                    builder.add("$T.bin2hex", Formulas.class);
                    break;
                case UniversalLexer.BITAND:
                    builder.add("$T.bitand", Formulas.class);
                    break;
                case UniversalLexer.BITLSHIFT:
                    builder.add("$T.bitlshift", Formulas.class);
                    break;
                case UniversalLexer.BITOR:
                    builder.add("$T.bitor", Formulas.class);
                    break;
                case UniversalLexer.BITRSHIFT:
                    builder.add("$T.bitrshift", Formulas.class);
                    break;
                case UniversalLexer.BITXOR:
                    builder.add("$T.bitxor", Formulas.class);
                    break;
                case UniversalLexer.COMPLEX:
                    builder.add("$T.complex", Formulas.class);
                    break;
                case UniversalLexer.CONVERT:
                    builder.add("$T.convert", Formulas.class);
                    break;
                case UniversalLexer.DEC2BIN:
                    builder.add("$T.dec2bin", Formulas.class);
                    break;
                case UniversalLexer.DEC2HEX:
                    builder.add("$T.dec2hex", Formulas.class);
                    break;
                case UniversalLexer.DEC2OCT:
                    builder.add("$T.dec2oct", Formulas.class);
                    break;
                case UniversalLexer.DELTA:
                    builder.add("$T.delta", Formulas.class);
                    break;
                case UniversalLexer.ERF:
                    builder.add("$T.erf", Formulas.class);
                    break;
                case UniversalLexer.ERF_PRECISE:
                    builder.add("$T.erf_precise", Formulas.class);
                    break;
                case UniversalLexer.ERFC:
                    builder.add("$T.erfc", Formulas.class);
                    break;
                case UniversalLexer.ERFC_PRECISE:
                    builder.add("$T.erfc_precise", Formulas.class);
                    break;
                case UniversalLexer.GESTEP:
                    builder.add("$T.gestep", Formulas.class);
                    break;
                case UniversalLexer.HEX2BIN:
                    builder.add("$T.hex2bin", Formulas.class);
                    break;
                case UniversalLexer.HEX2DEC:
                    builder.add("$T.hex2dec", Formulas.class);
                    break;
                case UniversalLexer.HEX2OCT:
                    builder.add("$T.hex2oct", Formulas.class);
                    break;
                case UniversalLexer.IMABS:
                    builder.add("$T.imabs", Formulas.class);
                    break;
                case UniversalLexer.IMAGINARY:
                    builder.add("$T.imaginary", Formulas.class);
                    break;
                case UniversalLexer.IMARGUMENT:
                    builder.add("$T.imargument", Formulas.class);
                    break;
                case UniversalLexer.IMCONJUGATE:
                    builder.add("$T.imconjugate", Formulas.class);
                    break;
                case UniversalLexer.IMCOS:
                    builder.add("$T.imcos", Formulas.class);
                    break;
                case UniversalLexer.IMCOSH:
                    builder.add("$T.imcosh", Formulas.class);
                    break;
                case UniversalLexer.IMCOT:
                    builder.add("$T.imcot", Formulas.class);
                    break;
                case UniversalLexer.IMCSC:
                    builder.add("$T.imcsc", Formulas.class);
                    break;
                case UniversalLexer.IMCSCH:
                    builder.add("$T.imcsch", Formulas.class);
                    break;
                case UniversalLexer.IMDIV:
                    builder.add("$T.imdiv", Formulas.class);
                    break;
                case UniversalLexer.IMEXP:
                    builder.add("$T.imexp", Formulas.class);
                    break;
                case UniversalLexer.IMLN:
                    builder.add("$T.imln", Formulas.class);
                    break;
                case UniversalLexer.IMLOG10:
                    builder.add("$T.imlog10", Formulas.class);
                    break;
                case UniversalLexer.IMLOG2:
                    builder.add("$T.imlog2", Formulas.class);
                    break;
                case UniversalLexer.IMPOWER:
                    builder.add("$T.impower", Formulas.class);
                    break;
                case UniversalLexer.IMPRODUCT:
                    builder.add("$T.improduct", Formulas.class);
                    break;
                case UniversalLexer.IMREAL:
                    builder.add("$T.imreal", Formulas.class);
                    break;
                case UniversalLexer.IMSEC:
                    builder.add("$T.imsec", Formulas.class);
                    break;
                case UniversalLexer.IMSECH:
                    builder.add("$T.imsech", Formulas.class);
                    break;
                case UniversalLexer.IMSIN:
                    builder.add("$T.imsin", Formulas.class);
                    break;
                case UniversalLexer.IMSINH:
                    builder.add("$T.imsinh", Formulas.class);
                    break;
                case UniversalLexer.IMSQRT:
                    builder.add("$T.imsqrt", Formulas.class);
                    break;
                case UniversalLexer.IMSUB:
                    builder.add("$T.imsub", Formulas.class);
                    break;
                case UniversalLexer.IMSUM:
                    builder.add("$T.imsum", Formulas.class);
                    break;
                case UniversalLexer.IMTAN:
                    builder.add("$T.imtan", Formulas.class);
                    break;
                case UniversalLexer.OCT2BIN:
                    builder.add("$T.oct2bin", Formulas.class);
                    break;
                case UniversalLexer.OCT2DEC:
                    builder.add("$T.oct2dec", Formulas.class);
                    break;
                case UniversalLexer.OCT2HEX:
                    builder.add("$T.oct2hex", Formulas.class);
                    break;
                // additional engineering
                case UniversalLexer.BIN2OCT:
                    builder.add("$T.bin2oct", Formulas.class);
                    break;
                // financial
                case UniversalLexer.ACCRINT:
                    builder.add("$T.accrint", Financials.class);
                    break;
                case UniversalLexer.ACCRINTM:
                    builder.add("$T.accrintm", Financials.class);
                    break;
                case UniversalLexer.AMORDEGRC:
                    builder.add("$T.amordegrc", Financials.class);
                    break;
                case UniversalLexer.AMORLINC:
                    builder.add("$T.amorlinc", Financials.class);
                    break;
                case UniversalLexer.COUPDAYBS:
                    builder.add("$T.coupdaybs", Financials.class);
                    break;
                case UniversalLexer.COUPDAYS:
                    builder.add("$T.coupdays", Financials.class);
                    break;
                case UniversalLexer.COUPDAYSNC:
                    builder.add("$T.coupdaysnc", Financials.class);
                    break;
                case UniversalLexer.COUPNCD:
                    builder.add("$T.coupncd", Financials.class);
                    break;
                case UniversalLexer.COUPNUM:
                    builder.add("$T.coupnum", Financials.class);
                    break;
                case UniversalLexer.COUPPCD:
                    builder.add("$T.couppcd", Financials.class);
                    break;
                case UniversalLexer.CUMIPMT:
                    builder.add("$T.cumipmt", Financials.class);
                    break;
                case UniversalLexer.CUMPRINC:
                    builder.add("$T.cumprinc", Financials.class);
                    break;
                case UniversalLexer.DB:
                    builder.add("$T.db", Financials.class);
                    break;
                case UniversalLexer.DDB:
                    builder.add("$T.ddb", Financials.class);
                    break;
                case UniversalLexer.DISC:
                    builder.add("$T.disc", Financials.class);
                    break;
                case UniversalLexer.DOLLARDE:
                    builder.add("$T.dollarde", Financials.class);
                    break;
                case UniversalLexer.DOLLARFR:
                    builder.add("$T.dollarfr", Financials.class);
                    break;
                case UniversalLexer.DURATION:
                    builder.add("$T.duration", Financials.class);
                    break;
                case UniversalLexer.EFFECT:
                    builder.add("$T.effect", Financials.class);
                    break;
                case UniversalLexer.FV:
                    builder.add("$T.fv", Financials.class);
                    break;
                case UniversalLexer.FVSCHEDULE:
                    builder.add("$T.fvschedule", Financials.class);
                    break;
                case UniversalLexer.INTRATE:
                    builder.add("$T.intrate", Financials.class);
                    break;
                case UniversalLexer.IPMT:
                    builder.add("$T.ipmt", Financials.class);
                    break;
                case UniversalLexer.IRR:
                    builder.add("$T.irr", Financials.class);
                    break;
                case UniversalLexer.ISPMT:
                    builder.add("$T.ispmt", Financials.class);
                    break;
                case UniversalLexer.MDURATION:
                    builder.add("$T.mduration", Financials.class);
                    break;
                case UniversalLexer.MIRR:
                    builder.add("$T.mirr", Financials.class);
                    break;
                case UniversalLexer.NOMINAL:
                    builder.add("$T.nominal", Financials.class);
                    break;
                case UniversalLexer.NPER:
                    builder.add("$T.nper", Financials.class);
                    break;
                case UniversalLexer.NPV:
                    builder.add("$T.npv", Financials.class);
                    break;
                case UniversalLexer.ODDFPRICE:
                    builder.add("$T.oddfprice", Financials.class);
                    break;
                case UniversalLexer.ODDFYIELD:
                    builder.add("$T.oddfyield", Financials.class);
                    break;
                case UniversalLexer.ODDLPRICE:
                    builder.add("$T.oddlprice", Financials.class);
                    break;
                case UniversalLexer.ODDLYIELD:
                    builder.add("$T.oddlyield", Financials.class);
                    break;
                case UniversalLexer.PDURATION:
                    builder.add("$T.pduration", Financials.class);
                    break;
                case UniversalLexer.PMT:
                    builder.add("$T.pmt", Financials.class);
                    break;
                case UniversalLexer.PPMT:
                    builder.add("$T.ppmt", Financials.class);
                    break;
                case UniversalLexer.PRICE:
                    builder.add("$T.price", Financials.class);
                    break;
                case UniversalLexer.PRICEDISC:
                    builder.add("$T.pricedisc", Financials.class);
                    break;
                case UniversalLexer.PRICEMAT:
                    builder.add("$T.pricemat", Financials.class);
                    break;
                case UniversalLexer.PV:
                    builder.add("$T.pv", Financials.class);
                    break;
                case UniversalLexer.RATE:
                    builder.add("$T.rate", Financials.class);
                    break;
                case UniversalLexer.RECEIVED:
                    builder.add("$T.received", Financials.class);
                    break;
                case UniversalLexer.RRI:
                    builder.add("$T.rri", Financials.class);
                    break;
                case UniversalLexer.SLN:
                    builder.add("$T.sln", Financials.class);
                    break;
                case UniversalLexer.SYD:
                    builder.add("$T.syd", Financials.class);
                    break;
                case UniversalLexer.TBILLEQ:
                    builder.add("$T.tbilleq", Financials.class);
                    break;
                case UniversalLexer.TBILLPRICE:
                    builder.add("$T.tbillprice", Financials.class);
                    break;
                case UniversalLexer.TBILLYIELD:
                    builder.add("$T.tbillyield", Financials.class);
                    break;
                case UniversalLexer.VDB:
                    builder.add("$T.vdb", Financials.class);
                    break;
                case UniversalLexer.XIRR:
                    builder.add("$T.xirr", Financials.class);
                    break;
                case UniversalLexer.XNPV:
                    builder.add("$T.xnpv", Financials.class);
                    break;
                case UniversalLexer.YIELD:
                    builder.add("$T.yield", Financials.class);
                    break;
                case UniversalLexer.YIELDDISC:
                    builder.add("$T.yielddisc", Financials.class);
                    break;
                case UniversalLexer.YIELDMAT:
                    builder.add("$T.yieldmat", Financials.class);
                    break;
                // database
                case UniversalLexer.DAVERAGE:
                    builder.add("$T.daverage", Databases.class);
                    break;
                case UniversalLexer.DCOUNT:
                    builder.add("$T.dcount", Databases.class);
                    break;
                case UniversalLexer.DCOUNTA:
                    builder.add("$T.dcounta", Databases.class);
                    break;
                case UniversalLexer.DGET:
                    builder.add("$T.dget", Databases.class);
                    break;
                case UniversalLexer.DMAX:
                    builder.add("$T.dmax", Databases.class);
                    break;
                case UniversalLexer.DMIN:
                    builder.add("$T.dmin", Databases.class);
                    break;
                case UniversalLexer.DPRODUCT:
                    builder.add("$T.dproduct", Databases.class);
                    break;
                case UniversalLexer.DSTDEV:
                    builder.add("$T.dstdev", Databases.class);
                    break;
                case UniversalLexer.DSTDEVP:
                    builder.add("$T.dstdevp", Databases.class);
                    break;
                case UniversalLexer.DSUM:
                    builder.add("$T.dsum", Databases.class);
                    break;
                case UniversalLexer.DVAR:
                    builder.add("$T.dvar", Databases.class);
                    break;
                case UniversalLexer.DVARP:
                    builder.add("$T.dvarp", Databases.class);
                    break;
                // cube
                case UniversalLexer.CUBEKPIMEMBER:
                    builder.add("$T.cubekpimember", Cubes.class);
                    break;
                case UniversalLexer.CUBEMEMBER:
                    builder.add("$T.cubemember", Cubes.class);
                    break;
                case UniversalLexer.CUBEMEMBERPROPERTY:
                    builder.add("$T.cubememberproperty", Cubes.class);
                    break;
                case UniversalLexer.CUBERANKEDMEMBER:
                    builder.add("$T.cuberankedmember", Cubes.class);
                    break;
                case UniversalLexer.CUBESET:
                    builder.add("$T.cubeset", Cubes.class);
                    break;
                case UniversalLexer.CUBESETCOUNT:
                    builder.add("$T.cubesetcount", Cubes.class);
                    break;
                case UniversalLexer.CUBEVALUE:
                    builder.add("$T.cubevalue", Cubes.class);
                    break;
                // information
                case UniversalLexer.CELL:
                    builder.add("$T.cell", Informations.class);
                    break;
                case UniversalLexer.ERROR_TYPE:
                    builder.add("$T.errorType", Informations.class);
                    break;
                case UniversalLexer.INFO:
                    builder.add("$T.info", Informations.class);
                    break;
                case UniversalLexer.ISBLANK:
                    builder.add("$T.isBlank", Informations.class);
                    break;
                case UniversalLexer.ISERR:
                    builder.add("$T.isErr", Informations.class);
                    break;
                case UniversalLexer.ISERROR:
                    builder.add("$T.isError", Informations.class);
                    break;
                case UniversalLexer.ISEVEN:
                    builder.add("$T.isEven", Informations.class);
                    break;
                case UniversalLexer.ISFORMULA:
                    builder.add("$T.isFormula", Informations.class);
                    break;
                case UniversalLexer.ISLOGICAL:
                    builder.add("$T.isLogical", Informations.class);
                    break;
                case UniversalLexer.ISNA:
                    builder.add("$T.isNa", Informations.class);
                    break;
                case UniversalLexer.ISNONTEXT:
                    builder.add("$T.isNonText", Informations.class);
                    break;
                case UniversalLexer.ISNUMBER:
                    builder.add("$T.isNumber", Informations.class);
                    break;
                case UniversalLexer.ISODD:
                    builder.add("$T.isOdd", Informations.class);
                    break;
                case UniversalLexer.ISREF:
                    builder.add("$T.isRef", Informations.class);
                    break;
                case UniversalLexer.ISTEXT:
                    builder.add("$T.isText", Informations.class);
                    break;
                case UniversalLexer.N:
                    builder.add("$T.n", Informations.class);
                    break;
                case UniversalLexer.NA:
                    builder.add("$T.na", Informations.class);
                    break;
                case UniversalLexer.SHEET:
                    builder.add("$T.sheet", Informations.class);
                    break;
                case UniversalLexer.SHEETS:
                    builder.add("$T.sheets", Informations.class);
                    break;
                case UniversalLexer.TYPE:
                    builder.add("$T.type", Informations.class);
                    break;
                // lookup & reference
                case UniversalLexer.ADDRESS:
                    builder.add("$T.address", Lookups.class);
                    break;
                case UniversalLexer.AREAS:
                    builder.add("$T.areas", Lookups.class);
                    break;
                case UniversalLexer.CHOOSE:
                    builder.add("$T.choose", Lookups.class);
                    break;
                case UniversalLexer.COLUMN:
                    builder.add("$T.column", Lookups.class);
                    break;
                case UniversalLexer.COLUMNS:
                    builder.add("$T.columns", Lookups.class);
                    break;
                case UniversalLexer.FORMULATEXT:
                    builder.add("$T.formulatext", Lookups.class);
                    break;
                case UniversalLexer.GETPIVOTDATA:
                    builder.add("$T.getpivotdata", Lookups.class);
                    break;
                case UniversalLexer.HLOOKUP:
                    builder.add("$T.hlookup", Lookups.class);
                    break;
                case UniversalLexer.HYPERLINK:
                    builder.add("$T.hyperlink", Lookups.class);
                    break;
                case UniversalLexer.INDEX:
                    builder.add("$T.index", Lookups.class);
                    break;
                case UniversalLexer.INDIRECT:
                    builder.add("$T.indirect", Lookups.class);
                    break;
                case UniversalLexer.LOOKUP:
                    builder.add("$T.lookup", Lookups.class);
                    break;
                case UniversalLexer.MATCH:
                    builder.add("$T.match", Lookups.class);
                    break;
                case UniversalLexer.OFFSET:
                    builder.add("$T.offset", Lookups.class);
                    break;
                case UniversalLexer.ROW:
                    builder.add("$T.row", Lookups.class);
                    break;
                case UniversalLexer.ROWS:
                    builder.add("$T.rows", Lookups.class);
                    break;
                case UniversalLexer.RTD:
                    builder.add("$T.rtd", Lookups.class);
                    break;
                case UniversalLexer.TRANSPOSE:
                    builder.add("$T.transpose", Lookups.class);
                    break;
                case UniversalLexer.VLOOKUP:
                    builder.add("$T.vlookup", Lookups.class);
                    break;
                // statistical
                case UniversalLexer.AVEDEV:
                    builder.add("$T.avedev", Statistics.class);
                    break;
                case UniversalLexer.AVERAGE:
                    builder.add("$T.average", Statistics.class);
                    break;
                case UniversalLexer.AVERAGEA:
                    builder.add("$T.averagea", Statistics.class);
                    break;
                case UniversalLexer.AVERAGEIF:
                    builder.add("$T.averageif", Statistics.class);
                    break;
                case UniversalLexer.AVERAGEIFS:
                    builder.add("$T.averageifs", Statistics.class);
                    break;
                case UniversalLexer.BETA_DIST:
                    builder.add("$T.beta_dist", Statistics.class);
                    break;
                case UniversalLexer.BETA_INV:
                    builder.add("$T.beta_inv", Statistics.class);
                    break;
                case UniversalLexer.BINOM_DIST:
                    builder.add("$T.binom_dist", Statistics.class);
                    break;
                case UniversalLexer.BINOM_DIST_RANGE:
                    builder.add("$T.binom_dist_range", Statistics.class);
                    break;
                case UniversalLexer.BINOM_INV:
                    builder.add("$T.binom_inv", Statistics.class);
                    break;
                case UniversalLexer.CHISQ_DIST:
                    builder.add("$T.chisq_dist", Statistics.class);
                    break;
                case UniversalLexer.CHISQ_DIST_RT:
                    builder.add("$T.chisq_dist_rt", Statistics.class);
                    break;
                case UniversalLexer.CHISQ_INV:
                    builder.add("$T.chisq_inv", Statistics.class);
                    break;
                case UniversalLexer.CHISQ_INV_RT:
                    builder.add("$T.chisq_inv_rt", Statistics.class);
                    break;
                case UniversalLexer.CHISQ_TEST:
                    builder.add("$T.chisq_test", Statistics.class);
                    break;
                case UniversalLexer.CONFIDENCE_NORM:
                    builder.add("$T.confidence_norm", Statistics.class);
                    break;
                case UniversalLexer.CONFIDENCE_T:
                    builder.add("$T.confidence_t", Statistics.class);
                    break;
                case UniversalLexer.CORREL:
                    builder.add("$T.correl", Statistics.class);
                    break;
                case UniversalLexer.COUNT:
                    builder.add("$T.count", Statistics.class);
                    break;
                case UniversalLexer.COUNTA:
                    builder.add("$T.counta", Statistics.class);
                    break;
                case UniversalLexer.COUNTBLANK:
                    builder.add("$T.countblank", Statistics.class);
                    break;
                case UniversalLexer.COUNTIF:
                    builder.add("$T.countif", Statistics.class);
                    break;
                case UniversalLexer.COUNTIFS:
                    builder.add("$T.countifs", Statistics.class);
                    break;
                case UniversalLexer.COVARIANCE_P:
                    builder.add("$T.covariance_p", Statistics.class);
                    break;
                case UniversalLexer.COVARIANCE_S:
                    builder.add("$T.covariance_s", Statistics.class);
                    break;
                case UniversalLexer.DEVSQ:
                    builder.add("$T.devsq", Statistics.class);
                    break;
                case UniversalLexer.EXPON_DIST:
                    builder.add("$T.expon_dist", Statistics.class);
                    break;
                case UniversalLexer.F_DIST:
                    builder.add("$T.f_dist", Statistics.class);
                    break;
                case UniversalLexer.F_DIST_RT:
                    builder.add("$T.f_dist_rt", Statistics.class);
                    break;
                case UniversalLexer.F_INV:
                    builder.add("$T.f_inv", Statistics.class);
                    break;
                case UniversalLexer.F_INV_RT:
                    builder.add("$T.f_inv_rt", Statistics.class);
                    break;
                case UniversalLexer.F_TEST:
                    builder.add("$T.f_test", Statistics.class);
                    break;
                case UniversalLexer.FISHER:
                    builder.add("$T.fisher", Statistics.class);
                    break;
                case UniversalLexer.FISHERINV:
                    builder.add("$T.fisherinv", Statistics.class);
                    break;
                case UniversalLexer.FORECAST:
                    builder.add("$T.forecast", Statistics.class);
                    break;
                case UniversalLexer.FORECAST_ETS:
                    builder.add("$T.forecast_ets", Statistics.class);
                    break;
                case UniversalLexer.FORECAST_ETS_CONFINT:
                    builder.add("$T.forecast_ets_confint", Statistics.class);
                    break;
                case UniversalLexer.FORECAST_ETS_SEASONALITY:
                    builder.add("$T.forecast_ets_seasonality", Statistics.class);
                    break;
                case UniversalLexer.FORECAST_ETS_STAT:
                    builder.add("$T.forecast_ets_stat", Statistics.class);
                    break;
                case UniversalLexer.FORECAST_LINEAR:
                    builder.add("$T.forecast_linear", Statistics.class);
                    break;
                case UniversalLexer.FREQUENCY:
                    builder.add("$T.frequency", Statistics.class);
                    break;
                case UniversalLexer.GAMMA:
                    builder.add("$T.gamma", Statistics.class);
                    break;
                case UniversalLexer.GAMMA_DIST:
                    builder.add("$T.gamma_dist", Statistics.class);
                    break;
                case UniversalLexer.GAMMA_INV:
                    builder.add("$T.gamma_inv", Statistics.class);
                    break;
                case UniversalLexer.GAMMALN:
                    builder.add("$T.gammaln", Statistics.class);
                    break;
                case UniversalLexer.GAMMALN_PRECISE:
                    builder.add("$T.gammaln_precise", Statistics.class);
                    break;
                case UniversalLexer.GAUSS:
                    builder.add("$T.gauss", Statistics.class);
                    break;
                case UniversalLexer.GEOMEAN:
                    builder.add("$T.geomean", Statistics.class);
                    break;
                case UniversalLexer.GROWTH:
                    builder.add("$T.growth", Statistics.class);
                    break;
                case UniversalLexer.HARMEAN:
                    builder.add("$T.harmean", Statistics.class);
                    break;
                case UniversalLexer.HYPGEOM_DIST:
                    builder.add("$T.hypgeom_dist", Statistics.class);
                    break;
                case UniversalLexer.INTERCEPT:
                    builder.add("$T.intercept", Statistics.class);
                    break;
                case UniversalLexer.KURT:
                    builder.add("$T.kurt", Statistics.class);
                    break;
                case UniversalLexer.LARGE:
                    builder.add("$T.large", Statistics.class);
                    break;
                case UniversalLexer.LINEST:
                    builder.add("$T.linest", Statistics.class);
                    break;
                case UniversalLexer.LOGEST:
                    builder.add("$T.logest", Statistics.class);
                    break;
                case UniversalLexer.LOGNORM_DIST:
                    builder.add("$T.lognorm_dist", Statistics.class);
                    break;
                case UniversalLexer.LOGNORM_INV:
                    builder.add("$T.lognorm_inv", Statistics.class);
                    break;
                case UniversalLexer.MAX:
                    builder.add("$T.max", Statistics.class);
                    break;
                case UniversalLexer.MAXA:
                    builder.add("$T.maxa", Statistics.class);
                    break;
                case UniversalLexer.MAXIFS:
                    builder.add("$T.maxifs", Statistics.class);
                    break;
                case UniversalLexer.MEDIAN:
                    builder.add("$T.median", Statistics.class);
                    break;
                case UniversalLexer.MIN:
                    builder.add("$T.min", Statistics.class);
                    break;
                case UniversalLexer.MINA:
                    builder.add("$T.mina", Statistics.class);
                    break;
                case UniversalLexer.MINIFS:
                    builder.add("$T.minifs", Statistics.class);
                    break;
                case UniversalLexer.MODE_MULT:
                    builder.add("$T.mode_mult", Statistics.class);
                    break;
                case UniversalLexer.MODE_SNGL:
                    builder.add("$T.mode_sngl", Statistics.class);
                    break;
                case UniversalLexer.NEGBINOM_DIST:
                    builder.add("$T.negbinom_dist", Statistics.class);
                    break;
                case UniversalLexer.NORM_DIST:
                    builder.add("$T.norm_dist", Statistics.class);
                    break;
                case UniversalLexer.NORM_INV:
                    builder.add("$T.norm_inv", Statistics.class);
                    break;
                case UniversalLexer.NORM_S_DIST:
                    builder.add("$T.norm_s_dist", Statistics.class);
                    break;
                case UniversalLexer.NORM_S_INV:
                    builder.add("$T.norm_s_inv", Statistics.class);
                    break;
                case UniversalLexer.PEARSON:
                    builder.add("$T.pearson", Statistics.class);
                    break;
                case UniversalLexer.PERCENTILE_EXC:
                    builder.add("$T.percentile_exc", Statistics.class);
                    break;
                case UniversalLexer.PERCENTILE_INC:
                    builder.add("$T.percentile_inc", Statistics.class);
                    break;
                case UniversalLexer.PERCENTRANK_EXC:
                    builder.add("$T.percentrank_exc", Statistics.class);
                    break;
                case UniversalLexer.PERCENTRANK_INC:
                    builder.add("$T.percentrank_inc", Statistics.class);
                    break;
                case UniversalLexer.PERMUT:
                    builder.add("$T.permut", Statistics.class);
                    break;
                case UniversalLexer.PERMUTATIONA:
                    builder.add("$T.permutationa", Statistics.class);
                    break;
                case UniversalLexer.PHI:
                    builder.add("$T.phi", Statistics.class);
                    break;
                case UniversalLexer.POISSON_DIST:
                    builder.add("$T.poisson_dist", Statistics.class);
                    break;
                case UniversalLexer.PROB:
                    builder.add("$T.prob", Statistics.class);
                    break;
                case UniversalLexer.QUARTILE_EXC:
                    builder.add("$T.quartile_exc", Statistics.class);
                    break;
                case UniversalLexer.QUARTILE_INC:
                    builder.add("$T.quartile_inc", Statistics.class);
                    break;
                case UniversalLexer.RANK_AVG:
                    builder.add("$T.rank_avg", Statistics.class);
                    break;
                case UniversalLexer.RANK_EQ:
                    builder.add("$T.rank_eq", Statistics.class);
                    break;
                case UniversalLexer.RSQ:
                    builder.add("$T.rsq", Statistics.class);
                    break;
                case UniversalLexer.SKEW:
                    builder.add("$T.skew", Statistics.class);
                    break;
                case UniversalLexer.SKEW_P:
                    builder.add("$T.skew_p", Statistics.class);
                    break;
                case UniversalLexer.SLOPE:
                    builder.add("$T.slope", Statistics.class);
                    break;
                case UniversalLexer.SMALL:
                    builder.add("$T.small", Statistics.class);
                    break;
                case UniversalLexer.STANDARDIZE:
                    builder.add("$T.standardize", Statistics.class);
                    break;
                case UniversalLexer.STDEV_P:
                    builder.add("$T.stdev_p", Statistics.class);
                    break;
                case UniversalLexer.STDEV_S:
                    builder.add("$T.stdev_s", Statistics.class);
                    break;
                case UniversalLexer.STDEVA:
                    builder.add("$T.stdeva", Statistics.class);
                    break;
                case UniversalLexer.STDEVPA:
                    builder.add("$T.stdevpa", Statistics.class);
                    break;
                case UniversalLexer.STEYX:
                    builder.add("$T.steyx", Statistics.class);
                    break;
                case UniversalLexer.T_DIST:
                    builder.add("$T.t_dist", Statistics.class);
                    break;
                case UniversalLexer.T_DIST_2T:
                    builder.add("$T.t_dist_2t", Statistics.class);
                    break;
                case UniversalLexer.T_DIST_RT:
                    builder.add("$T.t_dist_rt", Statistics.class);
                    break;
                case UniversalLexer.T_INV:
                    builder.add("$T.t_inv", Statistics.class);
                    break;
                case UniversalLexer.T_INV_2T:
                    builder.add("$T.t_inv_2t", Statistics.class);
                    break;
                case UniversalLexer.T_TEST:
                    builder.add("$T.t_test", Statistics.class);
                    break;
                case UniversalLexer.TREND:
                    builder.add("$T.trend", Statistics.class);
                    break;
                case UniversalLexer.TRIMMEAN:
                    builder.add("$T.trimmean", Statistics.class);
                    break;
                case UniversalLexer.VAR_P:
                    builder.add("$T.var_p", Statistics.class);
                    break;
                case UniversalLexer.VAR_S:
                    builder.add("$T.var_s", Statistics.class);
                    break;
                case UniversalLexer.VARA:
                    builder.add("$T.vara", Statistics.class);
                    break;
                case UniversalLexer.VARPA:
                    builder.add("$T.varpa", Statistics.class);
                    break;
                case UniversalLexer.WEIBULL_DIST:
                    builder.add("$T.weibull_dist", Statistics.class);
                    break;
                case UniversalLexer.Z_TEST:
                    builder.add("$T.z_test", Statistics.class);
                    break;
                // custom
                // code
                case UniversalLexer.CODE_ISIBAN:
                    builder.add("$T.code_isiban", Code.class);
                    break;
                case UniversalLexer.CODE_ISISIN:
                    builder.add("$T.code_isisin", Code.class);
                    break;
                case UniversalLexer.CODE_ISISSN:
                    builder.add("$T.code_isissn", Code.class);
                    break;
                case UniversalLexer.CODE_ISISBN:
                    builder.add("$T.code_isisbn", Code.class);
                    break;
                case UniversalLexer.CODE_ISISBN10:
                    builder.add("$T.code_isisbn10", Code.class);
                    break;
                case UniversalLexer.CODE_ISISBN13:
                    builder.add("$T.code_isisbn13", Code.class);
                    break;
                case UniversalLexer.CODE_ISEAN:
                    builder.add("$T.code_isean", Code.class);
                    break;
                case UniversalLexer.CODE_ISEAN8:
                    builder.add("$T.code_isean8", Code.class);
                    break;
                case UniversalLexer.CODE_ISEAN13:
                    builder.add("$T.code_isean13", Code.class);
                    break;
                case UniversalLexer.CODE_ISUPCA:
                    builder.add("$T.code_isupca", Code.class);
                    break;
                case UniversalLexer.CODE_ISUPCE:
                    builder.add("$T.code_isupce", Code.class);
                    break;
                case UniversalLexer.CODE_IBAN:
                    builder.add("$T.code_iban", Code.class);
                    break;
                case UniversalLexer.CODE_ISIN:
                    builder.add("$T.code_isin", Code.class);
                    break;
                case UniversalLexer.CODE_ISSN:
                    builder.add("$T.code_issn", Code.class);
                    break;
                case UniversalLexer.CODE_ISBN:
                    builder.add("$T.code_isbn", Code.class);
                    break;
                case UniversalLexer.CODE_ISBN10:
                    builder.add("$T.code_isbn10", Code.class);
                    break;
                case UniversalLexer.CODE_ISBN13:
                    builder.add("$T.code_isbn13", Code.class);
                    break;
                case UniversalLexer.CODE_EAN:
                    builder.add("$T.code_ean", Code.class);
                    break;
                case UniversalLexer.CODE_EAN8:
                    builder.add("$T.code_ean8", Code.class);
                    break;
                case UniversalLexer.CODE_EAN13:
                    builder.add("$T.code_ean13", Code.class);
                    break;
                case UniversalLexer.CODE_UPCA:
                    builder.add("$T.code_upca", Code.class);
                    break;
                case UniversalLexer.CODE_UPCE:
                    builder.add("$T.code_upce", Code.class);
                    break;
                case UniversalLexer.CODE_IBAN_CD:
                    builder.add("$T.code_iban_cd", Code.class);
                    break;
                case UniversalLexer.CODE_ISIN_CD:
                    builder.add("$T.code_isin_cd", Code.class);
                    break;
                case UniversalLexer.CODE_ISSN_CD:
                    builder.add("$T.code_issn_cd", Code.class);
                    break;
                case UniversalLexer.CODE_ISBN_CD:
                    builder.add("$T.code_isbn_cd", Code.class);
                    break;
                case UniversalLexer.CODE_ISBN10_CD:
                    builder.add("$T.code_isbn10_cd", Code.class);
                    break;
                case UniversalLexer.CODE_ISBN13_CD:
                    builder.add("$T.code_isbn13_cd", Code.class);
                    break;
                case UniversalLexer.CODE_EAN_CD:
                    builder.add("$T.code_ean_cd", Code.class);
                    break;
                case UniversalLexer.CODE_EAN8_CD:
                    builder.add("$T.code_ean8_cd", Code.class);
                    break;
                case UniversalLexer.CODE_EAN13_CD:
                    builder.add("$T.code_ean13_cd", Code.class);
                    break;
                case UniversalLexer.CODE_UPCA_CD:
                    builder.add("$T.code_upca_cd", Code.class);
                    break;
                case UniversalLexer.CODE_UPCE_CD:
                    builder.add("$T.code_upce_cd", Code.class);
                    break;
                case UniversalLexer.CODE_ISSAFEHTML:
                    builder.add("$T.code_issafehtml", Code.class);
                    break;
                case UniversalLexer.CODE_ISEMAIL:
                    builder.add("$T.code_isemail", Code.class);
                    break;
                case UniversalLexer.CODE_ISREGEX:
                    builder.add("$T.code_isregex", Code.class);
                    break;
                case UniversalLexer.CODE_ISIP:
                    builder.add("$T.code_isip", Code.class);
                    break;
                case UniversalLexer.CODE_ISIPV4:
                    builder.add("$T.code_isipv4", Code.class);
                    break;
                case UniversalLexer.CODE_ISIPV6:
                    builder.add("$T.code_isipv6", Code.class);
                    break;
                case UniversalLexer.CODE_ISURL:
                    builder.add("$T.code_isurl", Code.class);
                    break;
                case UniversalLexer.CODE_ISURN:
                    builder.add("$T.code_isurn", Code.class);
                    break;
                case UniversalLexer.CODE_ISDN:
                    builder.add("$T.code_isdn", Code.class);
                    break;
                case UniversalLexer.CODE_ISTLD:
                    builder.add("$T.code_istld", Code.class);
                    break;
                case UniversalLexer.CODE_ISCREDITCARD:
                    builder.add("$T.code_iscreditcard", Code.class);
                    break;
                case UniversalLexer.CODE_ISMOD10:
                    builder.add("$T.code_ismod10", Code.class);
                    break;
                case UniversalLexer.CODE_ISMOD11:
                    builder.add("$T.code_ismod11", Code.class);
                    break;
                case UniversalLexer.CODE_ISCNPJ:
                    builder.add("$T.code_iscnpj", Code.class);
                    break;
                case UniversalLexer.CODE_ISCPF:
                    builder.add("$T.code_iscpf", Code.class);
                    break;
                case UniversalLexer.CODE_ISTITULO_ELEITORAL:
                    builder.add("$T.code_istitulo_eleitoral", Code.class);
                    break;
                case UniversalLexer.CODE_ISNIP:
                    builder.add("$T.code_isnip", Code.class);
                    break;
                case UniversalLexer.CODE_ISPESEL:
                    builder.add("$T.code_ispesel", Code.class);
                    break;
                case UniversalLexer.CODE_ISREGON:
                    builder.add("$T.code_isregon", Code.class);
                    break;
                case UniversalLexer.CODE_ISJSON:
                    builder.add("$T.code_isjson", Code.class);
                    break;
                case UniversalLexer.CODE_ISXML:
                    builder.add("$T.code_isxml", Code.class);
                    break;
                case UniversalLexer.CODE_ISYAML:
                    builder.add("$T.code_isyaml", Code.class);
                    break;
                // operators
                case UniversalLexer.STRING:
                    builder.add("new $T($L)", ExpressionString.class, node.getSymbol().getText());
                    break;
                case UniversalLexer.LPAREN:
                    builder.add("(");
                    stackOfStacks.offerFirst(stack);
                    stack = new ArrayDeque<>();
                    break;
                case UniversalLexer.RPAREN:
                    builder.add(")");
                    while (stack != null && stack.peek() != null) {
                        builder.add(stack.poll().getCodeBlock());
                    }
                    stack = stackOfStacks.poll();
                    break;
                case UniversalLexer.AMPERSAND:

                    final Boolean ampersandSignedNumber = isSignedNumber(node.getParent().getText());

                    if (!ampersandSignedNumber) {
                        while (stack != null && stack.peek() != null) {
                            builder.add(stack.poll().getCodeBlock());
                        }

                        builder.add(".concat(");
                        stack.offer(new UniversalSuffix(0, CodeBlock.builder()
                                .add(")")
                                .build()));
                    } else {
                        builder.add("$T.concat(", Expressions.class);
                        stack.offer(new UniversalSuffix(0, CodeBlock.builder()
                                .add(")")
                                .build()));
                    }

                    return null;
                case UniversalLexer.PLUS:

                    final Boolean plusSignedNumber = isSignedNumber(node.getParent().getText());

                    if (!plusSignedNumber) {
                        while (stack != null && stack.peek() != null) {
                            builder.add(stack.poll().getCodeBlock());
                        }

                        builder.add(".add(");
                        stack.offer(new UniversalSuffix(0, CodeBlock.builder()
                                .add(")")
                                .build()));
                    } else {
                        builder.add("$T.plus(", Formulas.class);
                        stack.offer(new UniversalSuffix(0, CodeBlock.builder()
                                .add(")")
                                .build()));
                    }

                    return null;
                case UniversalLexer.MINUS:

                    final Boolean minusSignedNumber = isSignedNumber(node.getParent().getText());

                    if (!minusSignedNumber) {
                        while (stack != null && stack.peek() != null) {
                            builder.add(stack.poll().getCodeBlock());
                        }

                        builder.add(".subtract(");
                        stack.offer(new UniversalSuffix(0, CodeBlock.builder()
                                .add(")")
                                .build()));
                    } else {
                        builder.add("$T.minus(", Formulas.class);
                        stack.offer(new UniversalSuffix(0, CodeBlock.builder()
                                .add(")")
                                .build()));
                    }

                    return null;
                case UniversalLexer.TIMES:

                    while (stack != null && stack.peek() != null) {
                        builder.add(stack.poll().getCodeBlock());
                    }

                    builder.add(".multiply(");
                    stack.offer(new UniversalSuffix(0, CodeBlock.builder()
                            .add(")")
                            .build()));
                    return null;
                case UniversalLexer.DIV:

                    while (stack != null && stack.peek() != null) {
                        builder.add(stack.poll().getCodeBlock());
                    }

                    builder.add(".divide(");
                    stack.offer(new UniversalSuffix(0, CodeBlock.builder()
                            .add(")")
                            .build()));

                    return null;
                case UniversalLexer.GT:
                    while (stack != null && stack.peek() != null) {
                        builder.add(stack.poll().getCodeBlock());
                    }

                    builder.add(".gt(");
                    stack.offer(new UniversalSuffix(0, CodeBlock.builder()
                            .add(")")
                            .build()));
                    return null;
                case UniversalLexer.GTE:
                    while (stack != null && stack.peek() != null) {
                        builder.add(stack.poll().getCodeBlock());
                    }

                    builder.add(".gte(");
                    stack.offer(new UniversalSuffix(0, CodeBlock.builder()
                            .add(")")
                            .build()));
                    return null;
                case UniversalLexer.LT:
                    while (stack != null && stack.peek() != null) {
                        builder.add(stack.poll().getCodeBlock());
                    }

                    builder.add(".lt(");
                    stack.offer(new UniversalSuffix(0, CodeBlock.builder()
                            .add(")")
                            .build()));
                    return null;
                case UniversalLexer.LTE:
                    while (stack != null && stack.peek() != null) {
                        builder.add(stack.poll().getCodeBlock());
                    }

                    builder.add(".lte(");
                    stack.offer(new UniversalSuffix(0, CodeBlock.builder()
                            .add(")")
                            .build()));
                    return null;
                case UniversalLexer.EQ:
                    while (stack != null && stack.peek() != null) {
                        builder.add(stack.poll().getCodeBlock());
                    }

                    builder.add(".eq(");
                    stack.offer(new UniversalSuffix(0, CodeBlock.builder()
                            .add(")")
                            .build()));
                    return null;
                case UniversalLexer.NEQ:
                    while (stack != null && stack.peek() != null) {
                        builder.add(stack.poll().getCodeBlock());
                    }

                    builder.add(".neq(");
                    stack.offer(new UniversalSuffix(0, CodeBlock.builder()
                            .add(")")
                            .build()));
                    return null;
                case UniversalLexer.COMMA:
                    while (stackOfStacks.peek() != null && !stackOfStacks.peekLast().isEmpty()) {
                        builder.add(stackOfStacks.pollLast().poll().getCodeBlock());
                    }
                    builder.add(",");
                    break;
                case UniversalLexer.POINT:
                    builder.add(".");
                    break;
                case UniversalLexer.POW:
                    while (stack.peek() != null) {
                        builder.add(stack.poll().getCodeBlock());
                    }

                    builder.add(".pow(");
                    stack.offer(new UniversalSuffix(0, CodeBlock.builder()
                            .add(")")
                            .build()));
                    return null;
                case UniversalLexer.PI:
                    builder.add("new $T($T.pi($T.$L))", FormulaDecimal.class, BigDecimalMath.class, MathContext.class, "DECIMAL128");
                    break;
                case UniversalLexer.EULER:
                    builder.add("new $T($T.pi($T.$L))", FormulaDecimal.class, BigDecimalMath.class, MathContext.class, "DECIMAL128");
                    break;
                case UniversalLexer.I:
                    builder.add("$L", node.getSymbol().getText());
                    break;
                case UniversalLexer.VARIABLE:
                    final Class<?> type = propertyNameTypeMap.get(node.getSymbol().getText());
                    if (type == null) {
                        throw new UnsupportedOperationException("unknown type for " + node.getSymbol().getText());
                    } else {
                        if (CharSequence.class.isAssignableFrom(type)) {
                            builder.add("$T.valueOf(($T) $L)", ExpressionString.class, CharSequence.class, node.getSymbol().getText());
                        } else if (Character.class.isAssignableFrom(type)) {
                            if (expectedType == null || Character.class.isAssignableFrom(expectedType)) {
                                builder.add("($T) ((($T) $L == null) ? null : (($T) $L).toString())", String.class, Character.class, node.getSymbol().getText(), Character.class, node.getSymbol().getText());
                            } else {
                                builder.add("$T.valueOf(($T) ($T) ((($T) $L == null) ? null : (($T) $L).toString()))", ExpressionString.class, CharSequence.class, String.class, Character.class, node.getSymbol().getText(), Character.class, node.getSymbol().getText());
                            }
                        } else if (Number.class.isAssignableFrom(type)) {
                            builder.add("$T.valueOf(($T) $L)", Formulas.class, Number.class, node.getSymbol().getText());
                        } else if (Boolean.class.isAssignableFrom(type)) {
                            builder.add("$T.valueOf(($T) $L)", Logical.class, Boolean.class, node.getSymbol().getText());
                        } else if (Date.class.isAssignableFrom(type)) {
                            builder.add("$T.valueOf(($T) $L)", Moments.class, Date.class, node.getSymbol().getText());
                        } else if (UUID.class.isAssignableFrom(type)) {
                            builder.add("($T) ((($T) $L == null) ? null : (($T) $L).toString())", String.class, UUID.class, node.getSymbol().getText(), UUID.class, node.getSymbol().getText());
                        } else if (String[].class.isAssignableFrom(type)) {
                            builder.add("($T) $L", String[].class, node.getSymbol().getText());
                        } else if (Date[].class.isAssignableFrom(type)) {
                            builder.add("($T) $L", Date[].class, node.getSymbol().getText());
                        } else if (Boolean[].class.isAssignableFrom(type)) {
                            builder.add("($T) $L", Boolean[].class, node.getSymbol().getText());
                        } else if (BigDecimal[].class.isAssignableFrom(type)) {
                            builder.add("($T) $L", Number[].class, node.getSymbol().getText());
                        } else if (Integer[].class.isAssignableFrom(type)) {
                            builder.add("($T) $L", Number[].class, node.getSymbol().getText());
                        } else if (Long[].class.isAssignableFrom(type)) {
                            builder.add("($T) $L", Number[].class, node.getSymbol().getText());
                        } else if (Float[].class.isAssignableFrom(type)) {
                            builder.add("($T) $L", Number[].class, node.getSymbol().getText());
                        } else if (Double[].class.isAssignableFrom(type)) {
                            builder.add("($T) $L", Number[].class, node.getSymbol().getText());
                        } else {
                            throw new UnsupportedOperationException("unsupported type [" + type.getSimpleName() + "] for " + node.getSymbol().getText());
                        }
                    }
                    propertiesBuilder.add(node.getSymbol().getText());
                    break;
                case UniversalLexer.SCIENTIFIC_NUMBER:
                    builder.add("$T.valueOf($S)", Formulas.class, node.getSymbol().getText());
                    break;
                case UniversalLexer.WS:
                    break;
                default:
                    throw new NoSuchElementException(node.getText());
            }

            return super.visitTerminal(node);
        }

        protected Boolean isSignedNumber(String parentText) {

            final UniversalLexer lexer = new UniversalLexer(CharStreams.fromString(parentText));
            final CommonTokenStream tokens = new CommonTokenStream(lexer);
            final io.onsense.universal.grammar.UniversalParser parser = new io.onsense.universal.grammar.UniversalParser(tokens);
            final io.onsense.universal.grammar.UniversalParser.ExpressionContext expression = parser.expression();

            return new UniversalBaseVisitor<Boolean>() {

                int i = 0;
                int j = 0;
                int o = 0;

                @Override
                public Boolean visitScientific(io.onsense.universal.grammar.UniversalParser.ScientificContext ctx) {
                    ++i;
                    if (i >= 2) {
                        return false;
                    } else {
                        return super.visitScientific(ctx);
                    }
                }

                @Override
                public Boolean visitConstant(io.onsense.universal.grammar.UniversalParser.ConstantContext ctx) {
                    ++i;
                    if (i >= 2) {
                        return false;
                    } else {
                        return super.visitConstant(ctx);
                    }
                }

                @Override
                public Boolean visitVariable(io.onsense.universal.grammar.UniversalParser.VariableContext ctx) {
                    ++i;
                    if (i >= 2) {
                        return false;
                    } else {
                        return super.visitVariable(ctx);
                    }
                }

                @Override
                public Boolean visitFunc(io.onsense.universal.grammar.UniversalParser.FuncContext ctx) {
                    ++i;
                    if (i >= 2) {
                        return false;
                    } else {
                        return super.visitFunc(ctx);
                    }
                }

                @Override
                public Boolean visitTerminal(TerminalNode node) {
                    switch (node.getSymbol().getType()) {
                        case UniversalLexer.LPAREN:
                            j++;
                            break;
                        case UniversalLexer.RPAREN:
                            j++;
                            break;
                        case UniversalLexer.PLUS:
                            o++;
                            break;
                        case UniversalLexer.MINUS:
                            o++;
                            break;
                        case UniversalLexer.TIMES:
                            return false;
                        case UniversalLexer.DIV:
                            return false;
                        case UniversalLexer.POW:
                            return false;
                    }
                    if (o > 1) {
                        return false;
                    }
                    if (j > 2) {
                        return false;
                    }
                    return super.visitTerminal(node);
                }

                @Override
                protected Boolean defaultResult() {
                    return true;
                }
            }.visit(expression);
        }

        protected Boolean isFunction(String parentText) {

            final UniversalLexer lexer = new UniversalLexer(CharStreams.fromString(parentText));
            final CommonTokenStream tokens = new CommonTokenStream(lexer);
            final io.onsense.universal.grammar.UniversalParser parser = new io.onsense.universal.grammar.UniversalParser(tokens);
            final io.onsense.universal.grammar.UniversalParser.ExpressionContext expression = parser.expression();

            return new UniversalBaseVisitor<Boolean>() {

                @Override
                public Boolean visitFunc(io.onsense.universal.grammar.UniversalParser.FuncContext ctx) {
                    return true;
                }

                @Override
                protected Boolean defaultResult() {
                    return false;
                }
            }.visit(expression);
        }

        @Override
        protected CodeBlock defaultResult() {
            return builder.build();
        }

        @Override
        protected CodeBlock aggregateResult(CodeBlock aggregate, CodeBlock nextResult) {
            return aggregate == null || nextResult == null ? null : nextResult;
        }
    }
}
