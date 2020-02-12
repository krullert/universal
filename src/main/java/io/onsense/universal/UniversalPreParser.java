package io.onsense.universal;

import io.onsense.universal.grammar.UniversalBaseVisitor;
import io.onsense.universal.grammar.UniversalLexer;
import io.onsense.universal.grammar.UniversalParser;
import io.onsense.universal.grammar.UniversalVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigDecimal;

/**
 * @author Koen Rooijmans
 */
public class UniversalPreParser {

    public static String preParse(String universal) {

        final UniversalLexer lexer = new UniversalLexer(CharStreams.fromString(universal));
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final io.onsense.universal.grammar.UniversalParser parser = new io.onsense.universal.grammar.UniversalParser(tokens);
        final io.onsense.universal.grammar.UniversalParser.UniversalContext expression = parser.universal();

        final PreFormattingUniversalVisitor visitor = new PreFormattingUniversalVisitor();

        visitor.visit(expression);

        final String result = visitor.builder.toString();
        return "(" + result + ")";
    }

    protected static class PreFormattingUniversalVisitor extends UniversalBaseVisitor<String> implements UniversalVisitor<String> {

        private final StringBuilder builder = new StringBuilder();
        private int l = 0;

        @Override
        public String visitEquation(UniversalParser.EquationContext ctx) {
            builder.append('(');

            final String result = super.visitEquation(ctx);

            builder.append(')');

            return result;
        }

        @Override
        public String visitExpression(UniversalParser.ExpressionContext ctx) {

            // NOTE: we only group if we are in a function and the context contains operations (+,-,/,*,^).
            final boolean shouldGroupFunctionParameters = l > 0 && hasOperations(ctx.getText());

            if (shouldGroupFunctionParameters) {
                builder.append('(');
            }

            final String result = super.visitExpression(ctx);

            if (shouldGroupFunctionParameters) {
                builder.append(')');
            }
            return result;
        }

        @Override
        public String visitMultiplyingExpression(io.onsense.universal.grammar.UniversalParser.MultiplyingExpressionContext ctx) {
            builder.append('(');

            final String result = super.visitMultiplyingExpression(ctx);

            builder.append(')');

            return result;
        }

        @Override
        public String visitPowExpression(io.onsense.universal.grammar.UniversalParser.PowExpressionContext ctx) {
            if (ctx.getText().contains("^")) {
                builder.append('(');

                final String result = super.visitPowExpression(ctx);

                builder.append(')');
                return result;
            } else {
                return super.visitPowExpression(ctx);
            }
        }

        @Override
        public String visitFunc(UniversalParser.FuncContext ctx) {

            ++l;
            final String result = super.visitFunc(ctx);
            --l;
            return result;
        }

        @Override
        public String visitTerminal(TerminalNode node) {
            final String result = super.visitTerminal(node);
            switch (node.getSymbol().getType()) {
                case UniversalLexer.SCIENTIFIC_NUMBER:
                    final BigDecimal scientificNumber = new BigDecimal(node.getText());
                    builder.append(scientificNumber.toPlainString());
                    break;
                case UniversalLexer.STRING:
                    builder.append(node.getText().replace('\'', '"'));
                    break;
                default:
                    builder.append(node.getText());
                    break;
            }
            return result;
        }

        @Override
        protected String defaultResult() {
            return builder.toString();
        }

        protected Boolean hasOperations(String parentText) {

            final UniversalLexer lexer = new UniversalLexer(CharStreams.fromString(parentText));
            final CommonTokenStream tokens = new CommonTokenStream(lexer);
            final io.onsense.universal.grammar.UniversalParser parser = new io.onsense.universal.grammar.UniversalParser(tokens);
            final io.onsense.universal.grammar.UniversalParser.ExpressionContext expression = parser.expression();

            return new UniversalBaseVisitor<Boolean>() {

                @Override
                public Boolean visitTerminal(TerminalNode node) {
                    switch (node.getSymbol().getType()) {
                        case UniversalLexer.PLUS:
                            return !isSignedNumber(node.getParent().getText());
                        case UniversalLexer.MINUS:
                            return !isSignedNumber(node.getParent().getText());
                        case UniversalLexer.TIMES:
                            return true;
                        case UniversalLexer.DIV:
                            return true;
                        case UniversalLexer.POW:
                            return true;
                    }
                    return super.visitTerminal(node);
                }

                @Override
                protected Boolean defaultResult() {
                    return false;
                }

                @Override
                protected Boolean aggregateResult(Boolean aggregate, Boolean nextResult) {
                    return aggregate == null ? nextResult : (aggregate ? true : nextResult);
                }
            }.visit(expression);
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
    }
}
