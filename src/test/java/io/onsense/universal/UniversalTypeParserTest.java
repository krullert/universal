package io.onsense.universal;

import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author Koen Rooijmans
 */
public class UniversalTypeParserTest {

    @Before
    public void setUp() throws Exception {
        System.setProperty("user.timezone", "UTC");
    }

    @Test
    public void parse() {
        Assert.assertEquals(String.class, UniversalTypeParser.parse("MD5(MD5(MD5(\"a\"))) + MD5(MD5(MD5(\"a\"))) + MD5(MD5(MD5(\"a\")))"));
        Assert.assertEquals(String.class, UniversalTypeParser.parse("\"1\"+a+\"3\""));
        Assert.assertEquals(String.class, UniversalTypeParser.parse("\"1\" + LOWER(\"1\") + \"3\""));
        Assert.assertEquals(String.class, UniversalTypeParser.parse("\"1\" + REPEAT(\"1\" , 5) + \"3\""));
        Assert.assertEquals(String.class, UniversalTypeParser.parse("\"1\" + REPEAT(\"1\", 5.5) + \"3\""));
        Assert.assertEquals(String.class, UniversalTypeParser.parse("\"1\" + REPEAT(\"1\", +5) + \"3\""));
        Assert.assertEquals(String.class, UniversalTypeParser.parse("\"1\" + REPEAT(\"1\", +5.5) + \"3\""));
        Assert.assertEquals(String.class, UniversalTypeParser.parse("LOWER(\"AAaa\")"));
        Assert.assertEquals(String.class, UniversalTypeParser.parse("UPPER(\"AAaa\")"));
        Assert.assertEquals(String.class, UniversalTypeParser.parse("(MD5(MD5(MD5((\"a\" + \"a\")+ \"a\"+(\"a\"+MD5(MD5(MD5(\"a\"))))))))"));

        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("PRODUCT(1,2,3)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("(5 * (5.5 - (5.5 / 100 * 10))) + ((5 * (5.5 - (5.5 / 100 * 10))) / 100  * 21) + 4.75"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("1+2+3"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("(5 * (5.5 - (5.5 / 100 * 10)))"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("((5 * (5.5 - (5.5 / 100 * 10))) / 100  * 21) + 4.75"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("0 + (5 * (5.5 - (5.5 / 100 * 10))) + ((5 * (5.5 - (5.5 / 100 * 10))) / 100  * 21) + 4.75"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("((5 * (5.5 - (5.5 / 100 * 10))) + ((5 * (5.5 - (5.5 / 100 * 10))) / 100  * 21) + 4.75)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("(1+2+3)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("1+a+3"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("1 + COS(1) + 3"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("(1 + COS(1) + 3)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("(1 + 1 + 3)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("(SIN(1) + SIN(1) + SIN(1))"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("1 + (1 + 2 + (3 + 2 - (1)) + 2) + 3"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("1 + ((2 * 3) / 4)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("(1 + (2 * 3) / 4)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("1 + (2 * 3) / 4"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("(1 + (((2 * 3) / 4)))"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("1 + ((2 * 3) / 4) + 3.3 + ((SIN(3) / LOG(10)) * 2)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("SIN(3) / LOG(10)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("(SIN(3) + LOG(10))"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("(1 + ((2 * 3) / 4) + 3.3 + ((SIN(3) / LOG(10)) * 2))"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("1 / 2"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("(1 / 2)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("5 / 5 / 5"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("5 / 5 / 5 / 5"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("5 + 5 - 5 * 5 / 5"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("5^5"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("1/6+5/12+3/4"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("(3/8)*(2/7)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("(1/4) * (4 - (1/2))"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("(1/4) * (4 - 1/2)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("SQRT(25) - 5"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("CEIL(1.9) - 2"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("10 + 20 - 5 + SIN(10) + 2"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("SIN(3)-SIN(2)-LOG(COS(987))"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("pi+1.23e-10"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("SIN(pi+1.23e-10)+e^1.1e1"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("ATAN(0) - 0"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("00000001.001e001"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("-123.34344e-16*(-0.00001E-2)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("SIN(pi/2)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("0.9^0.8^0.7^0.6^0.5"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("((6.5 / 100 / 12) * 200000) / (1 - ((1 + (6.5 / 100 / 12)) ^ (-30 * 12)))"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("300.5 / 5.3 / 2.1 / 1.5"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("EXP(2) - e^2"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("(-7 + SQRT(50))^(1/3) + (7 - SQRT(50))^(1/3)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("MIN(-1,-2,-3)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("PRODUCT(1,2,3)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("COUNT(3,10,2,5,7,3)"));

        Assert.assertEquals(Date.class, UniversalTypeParser.parse("DATE(2000,2,3)"));
        Assert.assertEquals(Date.class, UniversalTypeParser.parse("DATE(\"2000\",\"2\",\"3\")"));
    }

    @Test
    public void parseTestWithVariables() {
        Assert.assertEquals(String.class, UniversalTypeParser.parse(ImmutableMap.of("bruv", String.class), "bruv + \"cl\""));
        Assert.assertEquals(String.class, UniversalTypeParser.parse(ImmutableMap.of("bruv", Integer.class), "bruv + \"cl\""));
        Assert.assertEquals(String.class, UniversalTypeParser.parse(ImmutableMap.of("bruv", BigDecimal.class), "bruv + \"cl\""));
    }

    @Test
    public void testAmbiguousMethodTyping() {
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("CHOOSE(1,1)"));
        Assert.assertEquals(BigDecimal.class, UniversalTypeParser.parse("CHOOSE(1,1,2,3)"));
        Assert.assertEquals(String.class, UniversalTypeParser.parse("CHOOSE(1,'jima')"));
        Assert.assertEquals(String.class, UniversalTypeParser.parse("CHOOSE(1,'jima','jima','jima')"));
        Assert.assertEquals(Boolean.class, UniversalTypeParser.parse("CHOOSE(1,true)"));
        Assert.assertEquals(Boolean.class, UniversalTypeParser.parse("CHOOSE(1,true,false,true)"));
//        Assert.assertEquals(Date.class, UniversalTypeParser.parse("CHOOSE(1,DATE(2011,1,1))"));
//        Assert.assertEquals(Date.class, UniversalTypeParser.parse("CHOOSE(1,DATE(2011,1,1),DATE(2012,1,1),DATE(2013,1,1))"));
    }

    @Test
    public void testConditionalType() {
        Assert.assertEquals(Boolean.class, UniversalTypeParser.parse("LEN('title') > 3"));
        Assert.assertEquals(Boolean.class, UniversalTypeParser.parse("LEN('title') <= 3"));
    }

    @Test
    public void testCharType() {
        final Map<String, Class<?>> propertyTypeMap = ImmutableMap.<String, Class<?>>builder()
                .put("price_currency", Character.class)
                .put("price_amount", Integer.class)
                .build();

        Assert.assertEquals(Character.class, UniversalTypeParser.parse(propertyTypeMap, "price_currency"));

        Assert.assertEquals(String.class, UniversalTypeParser.parse(propertyTypeMap, "price_currency + \\\" \\\" + FORMAT(price_amount / 100, \\\"#,##0.00\\\")"));
    }
}