package io.onsense.universal;

import com.squareup.javapoet.CodeBlock;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ExpressionEvaluator;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author Koen Rooijmans
 */
public class Universal {

    public static <T> T evaluate(String expression) throws CompileException, InvocationTargetException {
        return evaluate(expression, null, null);
    }

    public static <T> T evaluate(String expression, Map<String, Class<?>> propertyNameTypeMap, List<Object> propertyValues) throws CompileException, InvocationTargetException {
        final CodeBlock parsedExpression;
        if (propertyNameTypeMap == null) {
            parsedExpression = UniversalParser.parse(expression);
        } else {
            parsedExpression = UniversalParser.parse(propertyNameTypeMap, expression);
        }

        if (propertyValues == null) {
            final ExpressionEvaluator eval = new ExpressionEvaluator();
            eval.cook(parsedExpression.toString());
            return (T) eval.evaluate(null);
        } else {
            final ExpressionEvaluator eval = new ExpressionEvaluator();
            eval.setParameters(propertyNameTypeMap.keySet().toArray(new String[0]), propertyNameTypeMap.values().toArray(new Class[0]));
            eval.cook(parsedExpression.toString());
            return (T) eval.evaluate(propertyValues.toArray());
        }
    }
}
