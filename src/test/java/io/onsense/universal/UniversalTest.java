package io.onsense.universal;

import com.google.common.collect.ImmutableMap;
import org.codehaus.commons.compiler.CompileException;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Koen Rooijmans
 */
public class UniversalTest {

    @Test
    public void evaluate() throws InvocationTargetException, CompileException {
        assertEquals(BigDecimal.TEN, Universal.evaluate("ISOWEEKNUM(DATE(2012,3,9))"));

        assertEquals(BigDecimal.TEN, Universal.evaluate("ISOWEEKNUM(DATE(y,m,d))",
                ImmutableMap.<String, Class<?>>builder()
                        .put("y", Integer.class)
                        .put("m", Integer.class)
                        .put("d", Integer.class)
                        .build(),
                Arrays.asList(2012, 3, 9)));

        DecimalFormat df = new DecimalFormat("#,###.00000");
        assertEquals(df.format(new BigDecimal("1318.02041")), df.format(Universal.evaluate("AVEDEV(4,5,6,7,5,4,3) + 1337 - 10 * 2")));
    }
}