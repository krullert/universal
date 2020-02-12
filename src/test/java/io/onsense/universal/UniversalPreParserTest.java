package io.onsense.universal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Koen Rooijmans
 */
public class UniversalPreParserTest {

    @Before
    public void setUp() throws Exception {
        System.setProperty("user.timezone", "UTC");
    }

    @Test
    public void testPreParse() {
        Assert.assertEquals("((1)+(2/3))", UniversalPreParser.preParse("1+2/3"));
        Assert.assertEquals("(((1)+(2)+(3)))", UniversalPreParser.preParse("(1+2+3)"));
        Assert.assertEquals("((1.001))", UniversalPreParser.preParse("000001.001"));
        Assert.assertEquals("((0.001))", UniversalPreParser.preParse("000000.001"));
        Assert.assertEquals("((0))", UniversalPreParser.preParse("000000"));
        Assert.assertEquals("((1))", UniversalPreParser.preParse("000001"));
        Assert.assertEquals("((SIN(((pi/-2)))))", UniversalPreParser.preParse("SIN(pi/-2)"));
        Assert.assertEquals("((SIN((-2))))", UniversalPreParser.preParse("SIN(-2)"));
        Assert.assertEquals("((DATE((2000),(1),(1))))", UniversalPreParser.preParse("DATE(2000,1,1)"));
        Assert.assertEquals("((DATE(((2000)+(5)),(1),(1))))", UniversalPreParser.preParse("DATE(2000+5,1,1)"));
        Assert.assertEquals("((DATE(((2000)+(5)),(+1),(-1))))", UniversalPreParser.preParse("DATE(2000+5,+1,-1)"));
        Assert.assertEquals("(((DATE(((2000)+(5)),((1*2)),(((1^2))-(3))))))", UniversalPreParser.preParse("DATE(2000+5,1*2,1^2-3)"));
    }

    @Test
    public void testDatePreParse() {
        Assert.assertEquals("((DATE(((2000)+(5)),(1),(1))))", UniversalPreParser.preParse("DATE(2000+5,1,1)"));
        Assert.assertEquals("((DATE(((2000)+(5)),(+1),(-1))))", UniversalPreParser.preParse("DATE(2000+5,+1,-1)"));
    }

    @Test
    public void testMinMaxPreParse() {
        Assert.assertEquals("((MIN((3),(4)))+(MAX((-2),(-1))))", UniversalPreParser.preParse("MIN(3,4)+MAX(-2,-1)"));
        Assert.assertEquals("(((MIN((3),(4)))+(MAX((-2),(-1)))))", UniversalPreParser.preParse("(MIN(3,4)+MAX(-2,-1))"));
        Assert.assertEquals("(((((MIN((3),(4)))))+(((MAX((-2),(-1)))))))", UniversalPreParser.preParse("((MIN(3,4))+(MAX(-2,-1)))"));
    }
}