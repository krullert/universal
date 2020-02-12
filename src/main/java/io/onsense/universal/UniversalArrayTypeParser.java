package io.onsense.universal;

import io.onsense.universal.grammar.UniversalBaseVisitor;
import io.onsense.universal.grammar.UniversalLexer;
import io.onsense.universal.grammar.UniversalVisitor;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Collections;
import java.util.Map;

/**
 * @author Koen Rooijmans
 */
public class UniversalArrayTypeParser {

    public static <T> boolean parse(String universal) {
        return parse(Collections.emptyMap(), universal);
    }

    public static <T> boolean parse(Map<String, Class<?>> propertyNameTypeMap, String universal) {

        final UniversalLexer lexer = new UniversalLexer(CharStreams.fromString(UniversalPreParser.preParse(universal)));
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final UniversalParser parser = new UniversalParser(tokens);
        final UniversalParser.UniversalContext expression = parser.universal();
        parser.setErrorHandler(new BailErrorStrategy());

        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new IllegalArgumentException("invalid.universal");
        }

        final UniversalVisitor<Boolean> visitor = new UniversalBaseVisitor<Boolean>() {

            @Override
            public Boolean visitTerminal(TerminalNode node) {

                switch (node.getSymbol().getType()) {
                    // no-op
                }
                return defaultResult();
            }

            @Override
            protected Boolean aggregateResult(Boolean aggregate, Boolean nextResult) {
                if (aggregate == null && nextResult == null) {
                    return null;
                }
                if (aggregate == null) {
                    return nextResult;
                }
                if (nextResult == null) {
                    return aggregate;
                }
                if (nextResult || aggregate) {
                    return true;
                }
                return null;
            }
        };

        final Boolean expectingArray = visitor.visit(expression);
        if (expectingArray == null) {
            return false;
        } else {
            return expectingArray;
        }
    }
}
