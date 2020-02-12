package io.onsense.universal;

import com.google.common.collect.ImmutableMap;
import io.onsense.universe.Moment;
import io.onsense.universe.Moments;
import org.codehaus.commons.compiler.CompileException;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Koen Rooijmans
 */
public class UniversalAmbiguousTypeParserTest {

    @Before
    public void setUp() throws Exception {
        System.setProperty("user.timezone", "UTC");
    }

    @Test
    public void testAmbiguousMethodTyping() {
        final ImmutableMap.Builder<String, Class<?>> builder = ImmutableMap.builder();
        builder.put("project_manager_signature_date", Date.class);
        builder.put("project_manager_signature_serving_url", String.class);
        Assert.assertEquals(Date.class, UniversalTypeParser.parse(builder.build(), "IF(YEAR(project_manager_signature_date) > 0, project_manager_signature_date, IF(EXACT(project_manager_signature_serving_url, \\\"\\\"), DATE(0,0,0,0,0,0), NOW()))"));
    }

    @Test
    public void testAmbiguousDateTyping() {
        final ImmutableMap.Builder<String, Class<?>> builder = ImmutableMap.builder();
        builder.put("project_manager_signature_serving_url", String.class);
        Assert.assertEquals(Date.class, UniversalTypeParser.parse(builder.build(), "SWITCH(EXACT(project_manager_signature_serving_url, \"\"), NOW())"));
    }

    @Test
    public void testAmbiguousMethodTypingSimple() {
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("CHOOSE(1,1)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("CHOOSE(1,1,2,3)"));
        Assert.assertEquals(String.class, UniversalTypeParser.parse("CHOOSE(1,'jima')"));
        Assert.assertEquals(String.class, UniversalTypeParser.parse("CHOOSE(1,'jima','jima','jima')"));
        Assert.assertEquals(Boolean.class, UniversalTypeParser.parse("CHOOSE(1,true)"));
        Assert.assertEquals(Boolean.class, UniversalTypeParser.parse("CHOOSE(1,true,false,true)"));
    }

    @Ignore
    @Test
    public void testAmbiguousMethodTypingComplex() {
        Assert.assertEquals(Date.class, UniversalTypeParser.parse("CHOOSE(1,DATE(2011,1,1))"));
        Assert.assertEquals(Date.class, UniversalTypeParser.parse("CHOOSE(1,DATE(2011,1,1),DATE(2012,1,1),DATE(2013,1,1))"));
    }

    @Test
    public void testPartialSwitch() throws InvocationTargetException, CompileException {
        Assert.assertEquals(Date.class, UniversalTypeParser.parse("SWITCH(EXACT(sponsor_signature_serving_url, \"\"), NOW())"));
        evalDateExpressionNear(Moment.valueOf(Moments.now().year().intValue(), Moments.now().month().intValue(), Moments.now().dayOfMonth().intValue()), Universal.evaluate("SWITCH(EXACT('o-mah-gawd', \"\"), NOW())"));
    }

    protected void evalDateExpressionNear(Moment expect, Date parse) throws InvocationTargetException, CompileException {

        final Moment evaluate = new Moment(parse);
        Assert.assertEquals("year not equal", expect.year(), evaluate.year());
        Assert.assertEquals("month not equal", expect.month(), evaluate.month());
        Assert.assertEquals("day not equal", expect.day(), evaluate.day());
        Assert.assertThat("time not equal", evaluate.getTime(), Matchers.anyOf(Matchers.greaterThanOrEqualTo(expect.getTime() - 1000), Matchers.lessThanOrEqualTo(expect.getTime() + 1000)));
    }
}