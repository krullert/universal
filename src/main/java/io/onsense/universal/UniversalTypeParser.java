package io.onsense.universal;

import io.onsense.universal.grammar.UniversalBaseVisitor;
import io.onsense.universal.grammar.UniversalLexer;
import io.onsense.universal.grammar.UniversalVisitor;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * @author Koen Rooijmans
 */
public class UniversalTypeParser {

    public static <T> Class<T> parse(String universal) {
        return parse(Collections.emptyMap(), universal);
    }

    public static <T> Class<T> parse(Map<String, Class<?>> propertyNameTypeMap, String universal) {

        final UniversalLexer lexer = new UniversalLexer(CharStreams.fromString(UniversalPreParser.preParse(universal)));
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final io.onsense.universal.grammar.UniversalParser parser = new io.onsense.universal.grammar.UniversalParser(tokens);
        final io.onsense.universal.grammar.UniversalParser.UniversalContext expression = parser.universal();
        parser.setErrorHandler(new BailErrorStrategy());

        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new IllegalArgumentException("invalid.universal");
        }

        final UniversalVisitor<Class<T>> visitor = new UniversalBaseVisitor<Class<T>>() {

            private boolean locked = false;
            private boolean locking = false;
            private boolean last = false;
            private Class<T> lock;
            private Class<T> tail;

            @Override
            public Class<T> visitEquation(io.onsense.universal.grammar.UniversalParser.EquationContext ctx) {
                return (Class<T>) Boolean.class;
            }

            @Override
            public Class<T> visitTerminal(TerminalNode node) {

                locking = true;

                switch (node.getSymbol().getType()) {
                    // math & trig
                    case UniversalLexer.ABS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ACOS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ACOSH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ACOT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ACOTH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.AGGREGATE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ARABIC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ASIN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ASINH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ATAN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ATAN2:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ATANH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BASE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CEILING:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CEILING_MATH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CEILING_PRECISE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COMBIN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COMBINA:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COSH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COTH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CSC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CSCH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DECIMAL:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DEGREES:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.EVEN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.EXP:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FACT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FACTDOUBLE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FLOOR:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FLOOR_MATH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FLOOR_PRECISE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.GCD:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.INT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ISO_CEILING:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.LCM:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.LN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.LOG:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.LOG2:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.LOG10:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MDETERM:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MINVERSE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MMULT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MOD:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MROUND:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MULTINOMIAL:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MUNIT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ODD:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.POWER:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PRODUCT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.QUOTIENT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.RADIANS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.RAND:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.RANDBETWEEN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ROMAN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ROUND:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ROUNDDOWN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ROUNDUP:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SEC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SECH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SERIESSUM:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.NUMSIGN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SIN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SINH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SQRT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SQRTPI:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SUBTOTAL:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SUM:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SUMIF:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SUMIFS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SUMPRODUCT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SUMSQ:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SUMX2MY2:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SUMX2PY2:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SUMXMY2:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.TAN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.TANH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.TRUNC:
                        return (Class<T>) BigDecimal.class;
                    // TODO category?
                    case UniversalLexer.CBRT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.HYPOT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ROOT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FACTORIAL:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BERNOULLI:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MANTISSA:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.INTEGRAL:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FRACTION:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.AVG:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MEAN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.RANGE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MODE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MULTIPLY:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DIVIDE:
                        return (Class<T>) BigDecimal.class;
                    // textual
                    case UniversalLexer.ASC:
                        return (Class<T>) String.class;
                    case UniversalLexer.BAHTTEXT:
                        return (Class<T>) String.class;
                    case UniversalLexer.CHAR:
                        return (Class<T>) String.class;
                    case UniversalLexer.CLEAN:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CONCAT:
                        return (Class<T>) String.class;
                    case UniversalLexer.CONCATENATE:
                        return (Class<T>) String.class;
                    case UniversalLexer.DBCS:
                        return (Class<T>) String.class;
                    case UniversalLexer.DOLLAR:
                        return (Class<T>) String.class;
                    case UniversalLexer.EXACT:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.FIND:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FINDB:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FIXED:
                        return (Class<T>) String.class;
                    case UniversalLexer.LEFT:
                        return (Class<T>) String.class;
                    case UniversalLexer.LEFTB:
                        return (Class<T>) String.class;
                    case UniversalLexer.LEN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.LENB:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.LOWER:
                        return (Class<T>) String.class;
                    case UniversalLexer.MID:
                        return (Class<T>) String.class;
                    case UniversalLexer.MIDB:
                        return (Class<T>) String.class;
                    case UniversalLexer.NUMBERVALUE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PHONETIC:
                        return (Class<T>) String.class;
                    case UniversalLexer.PROPER:
                        return (Class<T>) String.class;
                    case UniversalLexer.REPLACE:
                        return (Class<T>) String.class;
                    case UniversalLexer.REPLACEB:
                        return (Class<T>) String.class;
                    case UniversalLexer.REPT:
                        return (Class<T>) String.class;
                    case UniversalLexer.RIGHT:
                        return (Class<T>) String.class;
                    case UniversalLexer.RIGHTB:
                        return (Class<T>) String.class;
                    case UniversalLexer.SEARCH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SEARCHB:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SUBSTITUTE:
                        return (Class<T>) String.class;
                    case UniversalLexer.T:
                        return (Class<T>) String.class;
                    case UniversalLexer.TEXT:
                        return (Class<T>) String.class;
                    case UniversalLexer.TEXTJOIN:
                        return (Class<T>) String.class;
                    case UniversalLexer.TRIM:
                        return (Class<T>) String.class;
                    case UniversalLexer.UNICHAR:
                        return (Class<T>) String.class;
                    case UniversalLexer.UNICODE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.UPPER:
                        return (Class<T>) String.class;
                    case UniversalLexer.VALUE:
                        return (Class<T>) BigDecimal.class;
                    // additional textual
                    case UniversalLexer.FORMAT:
                        return (Class<T>) String.class;
                    case UniversalLexer.LTRIM:
                        return (Class<T>) String.class;
                    case UniversalLexer.RTRIM:
                        return (Class<T>) String.class;
                    case UniversalLexer.CHOP:
                        return (Class<T>) String.class;
                    case UniversalLexer.REPEAT:
                        return (Class<T>) String.class;
                    case UniversalLexer.PREFIX:
                        return (Class<T>) String.class;
                    case UniversalLexer.SUFFIX:
                        return (Class<T>) String.class;
                    case UniversalLexer.CAPITALIZE:
                        return (Class<T>) String.class;
                    case UniversalLexer.CAPITALIZE_FULLY:
                        return (Class<T>) String.class;
                    case UniversalLexer.DECAPITALIZE:
                        return (Class<T>) String.class;
                    case UniversalLexer.DECAPITALIZE_FULLY:
                        return (Class<T>) String.class;
                    case UniversalLexer.SUBSTRING:
                        return (Class<T>) String.class;
                    case UniversalLexer.REMOVE:
                        return (Class<T>) String.class;
                    case UniversalLexer.REMOVE_FIRST:
                        return (Class<T>) String.class;
                    case UniversalLexer.REMOVE_LAST:
                        return (Class<T>) String.class;
                    case UniversalLexer.REPLACE_FIRST:
                        return (Class<T>) String.class;
                    case UniversalLexer.REPLACE_LAST:
                        return (Class<T>) String.class;
                    case UniversalLexer.REVERSE:
                        return (Class<T>) String.class;
                    case UniversalLexer.INITIALS:
                        return (Class<T>) String.class;
                    case UniversalLexer.SWAP:
                        return (Class<T>) String.class;
                    case UniversalLexer.QUOTE:
                        return (Class<T>) String.class;
                    case UniversalLexer.UNQUOTE:
                        return (Class<T>) String.class;
                    case UniversalLexer.ENCODE:
                        return (Class<T>) String.class;
                    case UniversalLexer.DECODE:
                        return (Class<T>) String.class;
                    case UniversalLexer.RANDOM:
                        return (Class<T>) String.class;
                    case UniversalLexer.ATOB:
                        return (Class<T>) String.class;
                    case UniversalLexer.BTOA:
                        return (Class<T>) String.class;
                    case UniversalLexer.HEX:
                        return (Class<T>) String.class;
                    case UniversalLexer.MD2:
                        return (Class<T>) String.class;
                    case UniversalLexer.MD5:
                        return (Class<T>) String.class;
                    case UniversalLexer.SHA256:
                        return (Class<T>) String.class;
                    case UniversalLexer.CRC32:
                        return (Class<T>) String.class;
                    case UniversalLexer.HTML2TEXT:
                        return (Class<T>) String.class;
                    // logical
                    case UniversalLexer.AND:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.FALSE:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.IF:
                        if (!locked) {
                            last = true;
                        }
                        return (Class<T>) String.class;
                    case UniversalLexer.IFERROR:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.IFNA:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.IFS:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.NOT:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.OR:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.SWITCH:
                        if (!locked) {
                            last = true;
                        }
                        return (Class<T>) String.class;
                    case UniversalLexer.TRUE:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.XOR:
                        return (Class<T>) Boolean.class;
                    // additional logical
                    case UniversalLexer.ISNULL:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.ISNOTNULL:
                        return (Class<T>) Boolean.class;
                    // date & time
                    case UniversalLexer.DATE:
                        return (Class<T>) Date.class;
                    case UniversalLexer.DATEDIF:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DATEVALUE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DAY:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DAYS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DAYS360:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.EDATE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.EOMONTH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.HOUR:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ISOWEEKNUM:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MINUTE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MONTH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.NETWORKDAYS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.NETWORKDAYS_INTL:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.NOW:
                        return (Class<T>) Date.class;
                    case UniversalLexer.SECOND:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.TIME:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.TIMEVALUE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.TODAY:
                        return (Class<T>) Date.class;
                    case UniversalLexer.WEEKDAY:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.WEEKNUM:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.WORKDAY:
                        return (Class<T>) Date.class;
                    case UniversalLexer.WORKDAY_INTL:
                        return (Class<T>) Date.class;
                    case UniversalLexer.YEAR:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.YEARFRAC:
                        return (Class<T>) BigDecimal.class;
                    // additional date & time
                    case UniversalLexer.WEEK:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.TIMENOW:
                        return (Class<T>) Date.class;
                    case UniversalLexer.DATETIMEVALUE:
                        return (Class<T>) Date.class;
                    // engineering
                    case UniversalLexer.BESSELI:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BESSELJ:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BESSELK:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BESSELY:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BIN2DEC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BIN2HEX:
                        return (Class<T>) String.class;
                    case UniversalLexer.BITAND:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BITLSHIFT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BITOR:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BITRSHIFT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BITXOR:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COMPLEX:
                        return (Class<T>) String.class;
                    case UniversalLexer.CONVERT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DEC2BIN:
                        return (Class<T>) String.class;
                    case UniversalLexer.DEC2HEX:
                        return (Class<T>) String.class;
                    case UniversalLexer.DEC2OCT:
                        return (Class<T>) String.class;
                    case UniversalLexer.DELTA:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ERF:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ERF_PRECISE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ERFC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ERFC_PRECISE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.GESTEP:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.HEX2BIN:
                        return (Class<T>) String.class;
                    case UniversalLexer.HEX2DEC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.HEX2OCT:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMABS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.IMAGINARY:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.IMARGUMENT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.IMCONJUGATE:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMCOS:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMCOSH:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMCOT:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMCSC:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMCSCH:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMDIV:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMEXP:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMLN:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMLOG10:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMLOG2:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMPOWER:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMPRODUCT:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMREAL:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.IMSEC:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMSECH:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMSIN:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMSINH:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMSQRT:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMSUB:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMSUM:
                        return (Class<T>) String.class;
                    case UniversalLexer.IMTAN:
                        return (Class<T>) String.class;
                    case UniversalLexer.OCT2BIN:
                        return (Class<T>) String.class;
                    case UniversalLexer.OCT2DEC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.OCT2HEX:
                        return (Class<T>) String.class;
                    // additional engineering
                    case UniversalLexer.BIN2OCT:
                        return (Class<T>) String.class;
                    // financial
                    case UniversalLexer.ACCRINT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ACCRINTM:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.AMORDEGRC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.AMORLINC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COUPDAYBS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COUPDAYS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COUPDAYSNC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COUPNCD:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COUPNUM:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COUPPCD:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CUMIPMT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CUMPRINC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DB:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DDB:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DISC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DOLLARDE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DOLLARFR:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DURATION:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.EFFECT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FVSCHEDULE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.INTRATE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.IPMT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.IRR:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ISPMT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MDURATION:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MIRR:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.NOMINAL:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.NPER:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.NPV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ODDFPRICE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ODDFYIELD:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ODDLPRICE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ODDLYIELD:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PDURATION:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PMT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PPMT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PRICE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PRICEDISC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PRICEMAT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.RATE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.RECEIVED:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.RRI:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SLN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SYD:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.TBILLEQ:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.TBILLPRICE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.TBILLYIELD:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.VDB:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.XIRR:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.XNPV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.YIELD:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.YIELDDISC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.YIELDMAT:
                        return (Class<T>) BigDecimal.class;
                    // database
                    case UniversalLexer.DAVERAGE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DCOUNT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DCOUNTA:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DGET:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DMAX:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DMIN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DPRODUCT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DSTDEV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DSTDEVP:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DSUM:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DVAR:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DVARP:
                        return (Class<T>) BigDecimal.class;
                    // cube
                    case UniversalLexer.CUBEKPIMEMBER:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CUBEMEMBER:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CUBEMEMBERPROPERTY:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CUBERANKEDMEMBER:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CUBESET:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CUBESETCOUNT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CUBEVALUE:
                        return (Class<T>) BigDecimal.class;
                    // information
                    case UniversalLexer.CELL:
                        return (Class<T>) String.class;
                    case UniversalLexer.ERROR_TYPE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.INFO:
                        return (Class<T>) String.class;
                    case UniversalLexer.ISBLANK:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.ISERR:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.ISERROR:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.ISEVEN:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.ISFORMULA:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.ISLOGICAL:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.ISNA:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.ISNONTEXT:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.ISNUMBER:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.ISODD:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.ISREF:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.ISTEXT:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.N:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.NA:
                        return (Class<T>) String.class;
                    case UniversalLexer.SHEET:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SHEETS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.TYPE:
                        return (Class<T>) BigDecimal.class;
                    // lookups & refence
                    case UniversalLexer.ADDRESS:
                        return (Class<T>) String.class;
                    case UniversalLexer.AREAS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CHOOSE:
                        if (!locked) {
                            last = true;
                        }
                        return (Class<T>) String.class;
                    case UniversalLexer.COLUMN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COLUMNS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FORMULATEXT:
                        return (Class<T>) String.class;
                    case UniversalLexer.GETPIVOTDATA:
                        return (Class<T>) String.class;
                    case UniversalLexer.HLOOKUP:
                        return (Class<T>) String.class;
                    case UniversalLexer.HYPERLINK:
                        return (Class<T>) String.class;
                    case UniversalLexer.INDEX:
                        return (Class<T>) String.class;
                    case UniversalLexer.INDIRECT:
                        return (Class<T>) String.class;
                    case UniversalLexer.LOOKUP:
                        return (Class<T>) String.class;
                    case UniversalLexer.MATCH:
                        return (Class<T>) String.class;
                    case UniversalLexer.OFFSET:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.ROW:
                        return (Class<T>) String.class;
                    case UniversalLexer.ROWS:
                        return (Class<T>) String.class;
                    case UniversalLexer.RTD:
                        return (Class<T>) String.class;
                    case UniversalLexer.TRANSPOSE:
                        return (Class<T>) String.class;
                    case UniversalLexer.VLOOKUP:
                        return (Class<T>) String.class;
                    // statistical
                    case UniversalLexer.AVEDEV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.AVERAGE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.AVERAGEA:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.AVERAGEIF:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.AVERAGEIFS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BETA_DIST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BETA_INV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BINOM_DIST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BINOM_DIST_RANGE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.BINOM_INV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CHISQ_DIST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CHISQ_DIST_RT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CHISQ_INV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CHISQ_INV_RT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CHISQ_TEST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CONFIDENCE_NORM:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CONFIDENCE_T:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.CORREL:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COUNT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COUNTA:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COUNTBLANK:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COUNTIF:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COUNTIFS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COVARIANCE_P:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.COVARIANCE_S:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DEVSQ:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.EXPON_DIST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.F_DIST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.F_DIST_RT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.F_INV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.F_INV_RT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.F_TEST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FISHER:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FISHERINV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FORECAST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FORECAST_ETS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FORECAST_ETS_CONFINT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FORECAST_ETS_SEASONALITY:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FORECAST_ETS_STAT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FORECAST_LINEAR:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.FREQUENCY:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.GAMMA:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.GAMMA_DIST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.GAMMA_INV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.GAMMALN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.GAMMALN_PRECISE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.GAUSS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.GEOMEAN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.GROWTH:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.HARMEAN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.HYPGEOM_DIST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.INTERCEPT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.KURT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.LARGE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.LINEST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.LOGEST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.LOGNORM_DIST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.LOGNORM_INV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MAX:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MAXA:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MAXIFS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MEDIAN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MIN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MINA:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MINIFS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MODE_MULT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.MODE_SNGL:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.NEGBINOM_DIST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.NORM_DIST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.NORM_INV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.NORM_S_DIST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.NORM_S_INV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PEARSON:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PERCENTILE_EXC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PERCENTILE_INC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PERCENTRANK_EXC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PERCENTRANK_INC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PERMUT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PERMUTATIONA:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PHI:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.POISSON_DIST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PROB:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.QUARTILE_EXC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.QUARTILE_INC:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.RANK_AVG:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.RANK_EQ:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.RSQ:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SKEW:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SKEW_P:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SLOPE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.SMALL:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.STANDARDIZE:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.STDEV_P:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.STDEV_S:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.STDEVA:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.STDEVPA:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.STEYX:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.T_DIST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.T_DIST_2T:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.T_DIST_RT:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.T_INV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.T_INV_2T:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.T_TEST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.TREND:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.TRIMMEAN:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.VAR_P:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.VAR_S:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.VARA:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.VARPA:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.WEIBULL_DIST:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.Z_TEST:
                        return (Class<T>) BigDecimal.class;
                    // custom
                    // code
                    case UniversalLexer.CODE_ISIBAN:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISISIN:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISISSN:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISISBN:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISISBN10:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISISBN13:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISEAN:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISEAN8:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISEAN13:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISUPCA:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISUPCE:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_IBAN:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_ISIN:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_ISSN:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_ISBN:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_ISBN10:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_ISBN13:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_EAN:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_EAN8:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_EAN13:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_UPCA:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_UPCE:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_IBAN_CD:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_ISIN_CD:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_ISSN_CD:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_ISBN_CD:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_ISBN10_CD:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_ISBN13_CD:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_EAN_CD:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_EAN8_CD:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_EAN13_CD:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_UPCA_CD:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_UPCE_CD:
                        return (Class<T>) String.class;
                    case UniversalLexer.CODE_ISSAFEHTML:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISEMAIL:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISREGEX:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISIP:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISIPV4:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISIPV6:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISURL:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISURN:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISDN:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISTLD:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISCREDITCARD:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISMOD10:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISMOD11:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISCNPJ:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISCPF:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISTITULO_ELEITORAL:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISNIP:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISPESEL:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISREGON:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISJSON:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISXML:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.CODE_ISYAML:
                        return (Class<T>) Boolean.class;
                    // operators
                    case UniversalLexer.LPAREN:
                        break;
                    case UniversalLexer.RPAREN:
                        break;
                    case UniversalLexer.AMPERSAND:
                        break;
                    case UniversalLexer.PLUS:
                        break;
                    case UniversalLexer.MINUS:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.TIMES:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.DIV:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.GT:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.GTE:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.LT:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.LTE:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.EQ:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.NEQ:
                        return (Class<T>) Boolean.class;
                    case UniversalLexer.COMMA:
                        break;
                    case UniversalLexer.POINT:
                        break;
                    case UniversalLexer.POW:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.PI:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.EULER:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.I:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.VARIABLE:
                        final Class<?> typeForPropertyName = (Class<?>) propertyNameTypeMap.get(node.getSymbol().getText());
                        if (typeForPropertyName == null) {

                            locking = false;

                            return null;
                        }
                        if (Number.class.isAssignableFrom(typeForPropertyName)) {

                            locking = false;

                            return (Class<T>) BigDecimal.class;
                        } else if (Character.class.isAssignableFrom(typeForPropertyName)) {

                            locking = false;

                            return (Class<T>) Character.class;
                        } else if (CharSequence.class.isAssignableFrom(typeForPropertyName)) {
                            return (Class<T>) String.class;
                        } else if (Date.class.isAssignableFrom(typeForPropertyName)) {
                            return (Class<T>) Date.class;
                        } else if (Boolean.class.isAssignableFrom(typeForPropertyName)) {
                            return (Class<T>) Boolean.class;
                        } else {
                            return (Class<T>) typeForPropertyName;
                        }
                    case UniversalLexer.SCIENTIFIC_NUMBER:
                        return (Class<T>) BigDecimal.class;
                    case UniversalLexer.STRING:
                        return (Class<T>) String.class;
                    case UniversalLexer.WS:
                        break;
                }

                locking = false;

                return defaultResult();
            }

            @Override
            protected Class<T> aggregateResult(Class<T> aggregate, Class<T> nextResult) {
                if (last) {
                    tail = nextResult == null ? tail : nextResult;
                    return tail;
                }
                if (locked) {
                    if (lock != null) {
                        return lock;
                    }
                    return aggregate == null ? nextResult : aggregate;
                } else {
                    final Class<T> result = aggregate == null ? nextResult : ((Number.class.isAssignableFrom(aggregate)) ? (nextResult == null ? aggregate : (CharSequence.class.isAssignableFrom(nextResult) ? nextResult : aggregate)) : aggregate);
                    if (locking) {
                        if (result != null) {
                            locked = true;
                            lock = result;
                        }
                    }
                    return result;
                }
            }
        };

        final Class<T> expectedType = visitor.visit(expression);

        return expectedType;
    }
}
