package io.onsense.universal;

import io.onsense.universe.FormulaDecimal;
import io.onsense.universe.Formulas;
import io.onsense.universe.Moment;
import io.onsense.universe.Moments;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ExpressionEvaluator;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * @author Koen Rooijmans
 */
public class UniversalCompatibilityTest {

    @Before
    public void setUp() throws Exception {
        System.setProperty("user.timezone", "UTC");
    }

    @Ignore
    @Test
    public void testWeekdayCompatibility() throws InvocationTargetException, CompileException {
        eval("5", Universal.evaluate("WEEKDAY(DATE(2018,2,14))"));
        eval("4", Universal.evaluate("WEEKDAY(DATE(2018,2,14),2)"));
        eval("3", Universal.evaluate("WEEKDAY(DATE(2018,2,14),3)"));
    }

    @Test
    public void testDatedifCompatibility() throws InvocationTargetException, CompileException {
        eval("2", Universal.evaluate("DATEDIF(\"1/1/2001\",\"1/1/2003\",\"Y\")"));
        eval("440", Universal.evaluate("DATEDIF(\"6/1/2001\",\"8/15/2002\",\"D\")"));
        eval("75", Universal.evaluate("DATEDIF(\"6/1/2001\",\"8/15/2002\",\"YD\")"));
    }

    @Test
    public void testIsoweeknumCompatibility() throws InvocationTargetException, CompileException {
        eval("10", Universal.evaluate("ISOWEEKNUM(DATE(2012,3,9))"));
    }

    @Test
    public void testNetworkdaysCompatibility() throws InvocationTargetException, CompileException {
        eval("110", Universal.evaluate("NETWORKDAYS(DATE(2012,10,1),DATE(2013,3,1))"));
        eval("109", Universal.evaluate("NETWORKDAYS(DATE(2012,10,1),DATE(2013,3,1),DATE(2012,11,22))"));
    }

    @Test
    public void testHourCompatibility() throws InvocationTargetException, CompileException {
        eval("18", Universal.evaluate("HOUR(0.75)"));
        eval("7", Universal.evaluate("HOUR(DATE(2011,7,18,7,45,0))"));
        eval("0", Universal.evaluate("HOUR(DATE(2012,4,21))"));
    }

    @Ignore
    @Test
    public void testDateValueCompatibilityExtended() throws InvocationTargetException, CompileException {
        evalDateExpression(Moment.valueOf(new Moment().year().intValue(), 6, 5), Universal.evaluate("DATEVALUE(\"5-JUL\")"));
    }

    @Test
    public void testTimeCompatibility() throws InvocationTargetException, CompileException {
        eval("0.5", Universal.evaluate("TIME(12,0,0)"));
        eval("0.7001157", Universal.evaluate("TIME(16,48,10)"));
    }

    @Test
    public void testTimevalueCompatibility() throws InvocationTargetException, CompileException {
        eval("0.1", Universal.evaluate("TIMEVALUE(\"2:24 AM\")"));
        eval("0.27431", Universal.evaluate("TIMEVALUE(\"22-Aug-2011 6:35 AM\")"));
    }

    @Test
    public void testWorkdayCompatibility() throws InvocationTargetException, CompileException {
        evalDateExpression(Moment.valueOf(2009, 4, 30), Universal.evaluate("WORKDAY(DATE(2008,10,1), 151)"));
        evalDateExpression(Moment.valueOf(2009, 5, 5), Universal.evaluate("WORKDAY(DATE(2008,10,1), 151, DATE(2008,11,26), DATE(2008,12,4), DATE(2009,1,21))"));
    }

    @Test
    public void testEngineeringCompatibility() throws InvocationTargetException, CompileException {
        eval("0.329926", Universal.evaluate("BESSELJ(1.9, 2)"));
        eval("0.277388", Universal.evaluate("BESSELK(1.5, 1)"));
        eval("0.145918", Universal.evaluate("BESSELY(2.5, 1)"));
        eval("0.981666", Universal.evaluate("BESSELI(1.5, 1)"));
        eval("0.981666", Universal.evaluate("BESSELI(1.5,1)"));
        eval("0.329926", Universal.evaluate("BESSELJ(1.9,2)"));
        eval("0.277388", Universal.evaluate("BESSELK(1.5,1)"));
        eval("0.145918", Universal.evaluate("BESSELY(2.5,1)"));
        eval("0.3378346183356807306736249150034441509298976277361517", Universal.evaluate("BESSELI(1.5, 2)"));
        eval("0.3378346183356807306736249150034441509298976277361517", Universal.evaluate("BESSELI(1.5,2)"));
        eval("0.8860919793963105", Universal.evaluate("BESSELI(1.4,1)"));
        eval("0.5419477138848564", Universal.evaluate("BESSELJ(1.4, 1)"));
        eval("0.32083590550458985", Universal.evaluate("BESSELK(1.4, 1)"));
        eval("-0.47914697411134044", Universal.evaluate("BESSELY(1.4, 1)"));
        eval("100", Universal.evaluate("BIN2DEC('1100100')"));
        eval("100", Universal.evaluate("BIN2DEC(1100100)"));
        eval("42", Universal.evaluate("BIN2DEC('101010')"));
        eval("-512", Universal.evaluate("BIN2DEC(1000000000)"));
        evalExpression("00fb", Universal.evaluate("BIN2HEX(11111011,4)"));
        evalExpression("e", Universal.evaluate("BIN2HEX(1110)"));
        evalExpression("2a", Universal.evaluate("BIN2HEX('101010')"));
        evalExpression("1ff", Universal.evaluate("BIN2HEX(111111111)"));
        evalExpression("ffffffffff", Universal.evaluate("BIN2HEX(1111111111)"));
        evalExpression("fffffffe00", Universal.evaluate("BIN2HEX(1000000000)"));
        evalExpression("011", Universal.evaluate("BIN2OCT(1001,3)"));
        evalExpression("144", Universal.evaluate("BIN2OCT(1100100)"));
        evalExpression("7777777777", Universal.evaluate("BIN2OCT(1111111111)"));
        evalExpression("52", Universal.evaluate("BIN2OCT('101010')"));
        evalExpression("0052", Universal.evaluate("BIN2OCT(101010,4.5)"));
        eval("1", Universal.evaluate("BITAND(1,5)"));
        eval("9", Universal.evaluate("BITAND(13,25)"));
        eval("16", Universal.evaluate("BITLSHIFT(4,2)"));
        eval("31", Universal.evaluate("BITOR(23,10)"));
        eval("3", Universal.evaluate("BITRSHIFT(13,2)"));
        eval("0", Universal.evaluate("BITLSHIFT(0,0)"));
        eval("6", Universal.evaluate("BITXOR(5,3)"));
        evalExpression("0064", Universal.evaluate("DEC2HEX(100,4)"));
        evalExpression("1c", Universal.evaluate("DEC2HEX(28)"));
        evalExpression("ffffffffca", Universal.evaluate("DEC2HEX(-54)"));
        evalExpression("1001", Universal.evaluate("DEC2BIN(9)"));
        evalExpression("1001", Universal.evaluate("DEC2BIN(9,4)"));
        evalExpression("72", Universal.evaluate("DEC2OCT(58)"));
        evalExpression("072", Universal.evaluate("DEC2OCT(58,3)"));
        evalExpression("7777777634", Universal.evaluate("DEC2OCT(-100)"));
        evalExpression("1110011100", Universal.evaluate("DEC2BIN(-100)"));
        evalExpression("00001111", Universal.evaluate("DEC2BIN(15,8)"));
        eval("0", Universal.evaluate("DELTA(5,4)"));
        eval("1", Universal.evaluate("DELTA(5,5)"));
        eval("0", Universal.evaluate("DELTA(0.5,0)"));
        eval("165", Universal.evaluate("HEX2DEC('A5')"));
        eval("-165", Universal.evaluate("HEX2DEC('FFFFFFFF5B')"));
        eval("1034160313", Universal.evaluate("HEX2DEC('3DA408B9')"));
        evalExpression("00001111", Universal.evaluate("HEX2BIN('F',8)"));
        evalExpression("10110111", Universal.evaluate("HEX2BIN('B7')"));
        evalExpression("1111111111", Universal.evaluate("HEX2BIN('FFFFFFFFFF')"));
        evalExpression("017", Universal.evaluate("HEX2OCT('F',3)"));
        evalExpression("35516", Universal.evaluate("HEX2OCT('3B4E')"));
        evalExpression("7777777400", Universal.evaluate("HEX2OCT('FFFFFFFF00')"));
        evalExpression("11", Universal.evaluate("OCT2BIN('3')"));
        evalExpression("011", Universal.evaluate("OCT2BIN('3', 3)"));
        evalExpression("1000000000", Universal.evaluate("OCT2BIN('7777777000')"));
        eval("44", Universal.evaluate("OCT2DEC('54')"));
        eval("-165", Universal.evaluate("OCT2DEC('7777777533')"));
        evalExpression("40", Universal.evaluate("OCT2HEX('100')"));
        evalExpression("0040", Universal.evaluate("OCT2HEX('100', 4)"));
        evalExpression("ffffffff5b", Universal.evaluate("OCT2HEX('7777777533', 3)"));
        evalExpression("ffe0000000", Universal.evaluate("OCT2HEX('4000000000')"));
        eval("0.7079289200957377", Universal.evaluate("ERF(0.745)"));
        eval("0.8427007929497149", Universal.evaluate("ERF(1)"));
        eval("0.1572992070502851", Universal.evaluate("ERFC(1)"));
        eval("1", Universal.evaluate("GESTEP(5,4)"));
        eval("1", Universal.evaluate("GESTEP(5,5)"));
        eval("1", Universal.evaluate("GESTEP(-4,-5)"));
        eval("0", Universal.evaluate("GESTEP(-1)"));
        eval("13", Universal.evaluate("IMABS('5+12i')"));
        eval("4", Universal.evaluate("IMAGINARY('3+4i')"));
        eval("1", Universal.evaluate("IMAGINARY('i')"));
        eval("+1", Universal.evaluate("IMAGINARY('+i')"));
        eval("-1", Universal.evaluate("IMAGINARY('-j')"));
        eval("-1", Universal.evaluate("IMAGINARY('0-j')"));
        eval("0", Universal.evaluate("IMAGINARY('4')"));
        eval("0", Universal.evaluate("IMAGINARY(0)"));
        eval("0.9272952180016122", Universal.evaluate("IMARGUMENT('3+4i')"));
        eval("0", Universal.evaluate("IMARGUMENT('2')"));
        eval("2.0344439357957027", Universal.evaluate("IMARGUMENT('-1+2i')"));
        eval("-2.0344439357957027", Universal.evaluate("IMARGUMENT('-1-2i')"));
        evalExpression("3-4i", Universal.evaluate("IMCONJUGATE('3+4i')"));
        evalExpression("5+12i", Universal.evaluate("IMDIV('-238+240i', '10+24i')"));
        evalExpression("j", Universal.evaluate("IMDIV('j', '1')"));
        evalExpression("-1j", Universal.evaluate("IMDIV('1', 'j')"));
        evalExpression("27+11i", Universal.evaluate("IMPRODUCT('3+4i', '5-3i')"));
        evalExpression("30+60i", Universal.evaluate("IMPRODUCT('1+2i', '30+0i')"));
        eval("6", Universal.evaluate("IMREAL('6-9i')"));
        eval("0", Universal.evaluate("IMREAL('i')"));
        eval("0", Universal.evaluate("IMREAL('+i')"));
        eval("0", Universal.evaluate("IMREAL('-j')"));
        eval("0", Universal.evaluate("IMREAL('0-j')"));
        eval("4", Universal.evaluate("IMREAL('4')"));
        eval("0", Universal.evaluate("IMREAL(0)"));
        eval("1", Universal.evaluate("IMREAL('+1+j')"));
        eval("-1", Universal.evaluate("IMREAL('-1+j')"));
        eval("0", Universal.evaluate("IMREAL('4j')"));
        evalExpression("8+j", Universal.evaluate("IMSUB('13+4j', '5+3j')"));
        evalExpression("8-3j", Universal.evaluate("IMSUB('13', '5+3j')"));
        evalExpression("8+i", Universal.evaluate("IMSUM('3+4i', '5-3i')"));
        evalExpression("3+4i", Universal.evaluate("COMPLEX(3,4)"));
        evalExpression("3+4j", Universal.evaluate("COMPLEX(3,4,'j')"));
        evalExpression("i", Universal.evaluate("COMPLEX(0,1)"));
        evalExpression("1", Universal.evaluate("COMPLEX(1,0)"));
        evalExpression("0", Universal.evaluate("COMPLEX(0,0)"));
        evalImaginaryExpression("0.6215180171704283-0.3039310016284264i", Universal.evaluate("IMCSC('1+i')"));

        eval("13", Universal.evaluate("IMABS(\"5+12i\")"));
        eval("4", Universal.evaluate("IMAGINARY(\"3+4i\")"));
        eval("-1", Universal.evaluate("IMAGINARY(\"0-j\")"));
        eval("0", Universal.evaluate("IMAGINARY(4)"));
        eval("0.92729522", Universal.evaluate("IMARGUMENT(\"3+4i\")"));
        evalImaginaryExpression("3-4i", Universal.evaluate("IMCONJUGATE(\"3+4i\")"));
        evalImaginaryExpression("0.833730025131149-0.988897705762865i", Universal.evaluate("IMCOS(\"1+i\")"));
        evalImaginaryExpression("-27.0349456030742+3.85115333481178i", Universal.evaluate("IMCOSH(\"4+3i\")"));
        evalImaginaryExpression("0.00490118239430447-0.999266927805902i", Universal.evaluate("IMCOT(\"4+3i\")"));
        evalImaginaryExpression("-0.0754898329158637+0.0648774713706355i", Universal.evaluate("IMCSC(\"4+3i\")"));
        evalImaginaryExpression("-0.036275889628626-0.0051744731840194i", Universal.evaluate("IMCSCH(\"4+3i\")"));
        evalImaginaryExpression("5+12i", Universal.evaluate("IMDIV(\"-238+240i\",\"10+24i\")"));
        evalImaginaryExpression("1.46869393991589+2.28735528717884i", Universal.evaluate("IMEXP(\"1+i\")"));
        evalImaginaryExpression("1.6094379124341+0.927295218001612i", Universal.evaluate("IMLN(\"3+4i\")"));
        evalImaginaryExpression("0.698970004336019+0.402719196273373i", Universal.evaluate("IMLOG10(\"3+4i\")"));
        evalImaginaryExpression("-46+9.00000000000001i", Universal.evaluate("IMPOWER(\"2+3i\", 3)"));
        evalImaginaryExpression("27+11i", Universal.evaluate("IMPRODUCT(\"3+4i\",\"5-3i\")"));
        evalImaginaryExpression("30+60i", Universal.evaluate("IMPRODUCT(\"1+2i\",30)"));
        eval("6", Universal.evaluate("IMREAL(\"6-9i\")"));
        evalImaginaryExpression("-0.0652940278579471-0.0752249603027732i", Universal.evaluate("IMSEC(\"4+3i\")"));
        evalImaginaryExpression("-0.0362534969158689-0.00516434460775318i", Universal.evaluate("IMSECH(\"4+3i\")"));
        evalImaginaryExpression("-7.61923172032141-6.548120040911i", Universal.evaluate("IMSIN(\"4+3i\")"));
        evalImaginaryExpression("1.09868411346781+0.455089860562227i", Universal.evaluate("IMSQRT(\"1+i\")"));
        evalImaginaryExpression("8+i", Universal.evaluate("IMSUB(\"13+4i\",\"5+3i\")"));
        evalImaginaryExpression("8+i", Universal.evaluate("IMSUM(\"3+4i\",\"5-3i\")"));
        evalImaginaryExpression("3+4i", Universal.evaluate("COMPLEX(3,4)"));
        evalImaginaryExpression("3+4j", Universal.evaluate("COMPLEX(3,4,\"j\")"));
        evalExpression("i", Universal.evaluate("COMPLEX(0,1)"));
        evalExpression("1", Universal.evaluate("COMPLEX(1,0)"));
        evalImaginaryExpression("-27.0168132580039+3.85373803791938i", Universal.evaluate("IMSINH(\"4+3i\")"));
        evalImaginaryExpression("0.00490825806749606+1.00070953606723i", Universal.evaluate("COMPLEX(0.00490825806749606, 1.00070953606723)"));
        evalImaginaryExpression("0.00490825806749606+1.00070953606723i", Universal.evaluate("IMTAN(\"4+3i\")"));
        evalImaginaryExpression("2.32192809488736+1.33780421245098i", Universal.evaluate("IMLOG2(\"3+4i\")"));
        evalImaginaryExpression("0.3039310016284264-0.6215180171704283i", Universal.evaluate("IMCSCH('1+i')"));
    }

    @Test
    public void testEngineeringConvertCompatibility() throws InvocationTargetException, CompileException {

        eval("1000", Universal.evaluate("CONVERT(1,'Yg','Zg')"));
        eval("1000", Universal.evaluate("CONVERT(1,'Zg','Eg')"));
        eval("1000", Universal.evaluate("CONVERT(1,'Eg','Pg')"));
        eval("1000", Universal.evaluate("CONVERT(1,'Pg','Tg')"));
        eval("1000", Universal.evaluate("CONVERT(1,'Tg','Gg')"));
        eval("1000", Universal.evaluate("CONVERT(1,'Gg','Mg')"));
        eval("1000", Universal.evaluate("CONVERT(1,'Mg','kg')"));
        eval("10", Universal.evaluate("CONVERT(1,'kg','hg')"));
        eval("100", Universal.evaluate("CONVERT(1,'eg','dg')"));
        eval("10", Universal.evaluate("CONVERT(1,'dg','cg')"));
        eval("10", Universal.evaluate("CONVERT(1,'cg','mg')"));
        eval("1000", Universal.evaluate("CONVERT(1,'mg','ug')"));
        eval("1000", Universal.evaluate("CONVERT(1,'ug','ng')"));
        eval("1000", Universal.evaluate("CONVERT(1,'ng','pg')"));
        eval("1000", Universal.evaluate("CONVERT(1,'pg','fg')"));
        eval("1000", Universal.evaluate("CONVERT(1,'fg','ag')"));
        eval("1000", Universal.evaluate("CONVERT(1,'ag','zg')"));
        eval("1000", Universal.evaluate("CONVERT(1,'zg','yg')"));

        eval("0.45359237", Universal.evaluate("CONVERT(1,'lbm','kg')"));
        eval("30.48000", Universal.evaluate("CONVERT(100,'ft','m')"));
        eval("0.000621371", Universal.evaluate("CONVERT(1,'m','mi')"));
        eval("1.09361", Universal.evaluate("CONVERT(1,'m','yd')"));
        eval("3520", Universal.evaluate("CONVERT(2,'mi','yd')"));
        eval("0.000002", Universal.evaluate("CONVERT(2,'nm','mm')"));
        eval("4.409245243697551", Universal.evaluate("CONVERT(2,'kg','lbm')"));
        eval("0.004409245243697552", Universal.evaluate("CONVERT(2,'g','lbm')"));
        eval("0.000004409245243697551", Universal.evaluate("CONVERT(2,'mg','lbm')"));

        eval("0.4535924", Universal.evaluate("CONVERT(1, \"lbm\", \"kg\")"));
        eval("20", Universal.evaluate("CONVERT(68, \"°F\", \"°C\")"));
        eval("20", Universal.evaluate("CONVERT(68, \"fah\", \"cel\")"));
        eval("9.290304", Universal.evaluate("CONVERT(CONVERT(100,\"ft\",\"m\"),\"ft\",\"m\")"));

        eval("0.00220462", Universal.evaluate("CONVERT(1,'g','lbm')")); // 1 gram to pound mass
        eval("602214179421676400000000.00000", Universal.evaluate("CONVERT(1,'g','u')")); // 1 gram to atomic mass unit
        eval("0.035274", Universal.evaluate("CONVERT(1,'g','ozm')")); // 1 gram to ounce mass
        eval("15.43236", Universal.evaluate("CONVERT(1,'g','grain')")); // 1 gram to grain
        eval("0.0000196841", Universal.evaluate("CONVERT(1,'g','cwt')")); // 1 gram to U.S. (short) hundredweight
        eval("0.0000196841", Universal.evaluate("CONVERT(1,'g','shweight')")); // 1 gram to U.S. (short) hundredweight
        eval("0.0000196841", Universal.evaluate("CONVERT(1,'g','uk_cwt')")); // 1 gram to Imperial hundredweight
        eval("0.0000196841", Universal.evaluate("CONVERT(1,'g','lcwt')")); // 1 gram to Imperial hundredweight
        eval("0.0000196841", Universal.evaluate("CONVERT(1,'g','hweight')")); // 1 gram to Imperial hundredweight
        eval("0.000157473", Universal.evaluate("CONVERT(1,'g','stone')")); // 1 gram to Stone
        eval("0.00000110231", Universal.evaluate("CONVERT(1,'g','ton')")); // 1 gram to Ton
        eval("0.000000984207", Universal.evaluate("CONVERT(1,'g','uk_ton')")); // 1 gram to Imperial ton
        eval("0.000000984207", Universal.evaluate("CONVERT(1,'g','LTON')")); // 1 gram to Imperial ton
        eval("0.000000984207", Universal.evaluate("CONVERT(1,'g','brton')")); // 1 gram to Imperial ton
        eval("0.0685218", Universal.evaluate("CONVERT(1,'kg','sg')")); // 1 gram to slug

        eval("14593.90294", Universal.evaluate("CONVERT(1,'sg', 'g')")); // 1 slug to gram
        eval("453.59237", Universal.evaluate("CONVERT(1,'lbm', 'g')")); // 1 pound mass to gram
        eval("0.00000000000000000000000166054", Universal.evaluate("CONVERT(1,'u', 'g')")); // 1 atomic mass unit to gram
        eval("28.34952", Universal.evaluate("CONVERT(1,'ozm', 'g')")); // 1 ounce mass to gram
        eval("0.0647989", Universal.evaluate("CONVERT(1,'grain', 'g')")); // 1 grain to gram
        eval("45359.23700", Universal.evaluate("CONVERT(1,'cwt', 'g')")); // 1 U.S. (short) hundredweight to gram
        eval("45359.23700", Universal.evaluate("CONVERT(1,'shweight', 'g')")); // 1 U.S. (short) hundredweight to gram
        eval("50802.34544", Universal.evaluate("CONVERT(1,'uk_cwt', 'g')")); // 1 Imperial hundredweight to gram
        eval("50802.34544", Universal.evaluate("CONVERT(1,'lcwt', 'g')")); // 1 Imperial hundredweight to gram
        eval("50802.34544", Universal.evaluate("CONVERT(1,'hweight', 'g')")); // 1 Imperial hundredweight to gram
        eval("6350.29318", Universal.evaluate("CONVERT(1,'stone', 'g')")); // 1 Stone to gram
        eval("907184.74000", Universal.evaluate("CONVERT(1,'ton', 'g')")); // 1 Ton to gram
        eval("1016046.90880", Universal.evaluate("CONVERT(1,'uk_ton', 'g')")); // 1 Imperial ton to gram
        eval("1016046.90880", Universal.evaluate("CONVERT(1,'LTON', 'g')")); // 1 Imperial ton to gram
        eval("1016046.90880", Universal.evaluate("CONVERT(1,'brton', 'g')")); // 1 Imperial ton to gram
    }

    @Ignore
    @Test
    public void testConvertByteCompat() throws InvocationTargetException, CompileException {
        eval("3.583", Universal.evaluate("CONVERT(3583,'byte','kbyte')"));
        eval("28664", Universal.evaluate("CONVERT(3583,'byte','bit')"));
        eval("524288", Universal.evaluate("CONVERT(64,'kibyte','bit')"));
    }

    @Ignore
    @Test
    public void testBin2DecCompatibility() throws InvocationTargetException, CompileException {
        eval("-1", Universal.evaluate("BIN2DEC('1111111111')"));
        eval("-1", Universal.evaluate("BIN2DEC(1111111111)"));
    }

    @Test
    public void testMathTrigCompatibility() throws InvocationTargetException, CompileException {
        eval("1", Universal.evaluate("ABS(-1)"));
        eval("1.0000", Universal.evaluate("ACOS(1) + 1"));
        eval("0", Universal.evaluate("ACOSH(1)"));
        eval("0.7853981633974483", Universal.evaluate("ACOT(1)"));
        eval("0.5235987755982989", Universal.evaluate("ASIN(0.5)"));
        eval("0.48121182505960347", Universal.evaluate("ASINH(0.5)"));
        eval("0.7853981633974483", Universal.evaluate("ATAN(1)"));
        eval("0.7853981633974483", Universal.evaluate("ATAN2(1,1)"));
        eval("0.42365", Universal.evaluate("ATANH(0.4)"));
        eval("5", Universal.evaluate("CEILING(4.1)"));
        eval("5", Universal.evaluate("CEILING(4.9)"));
        eval("-4", Universal.evaluate("CEILING(-4.1)"));
        eval("-4", Universal.evaluate("CEILING(-4.9)"));
        eval("0", Universal.evaluate("CEILING(4.1,0)"));
        eval("5", Universal.evaluate("CEILING(4.1,1)"));
        eval("3", Universal.evaluate("CEILING(2.5, 1)"));
        eval("-4", Universal.evaluate("CEILING(-2.5, -2)"));
        eval("-2", Universal.evaluate("CEILING(-2.5, 2)"));
        eval("1.5", Universal.evaluate("CEILING(1.5, 0.1)"));
        eval("0.24", Universal.evaluate("CEILING(0.234, 0.01)"));
        eval("22.3", Universal.evaluate("CEILING(22.25, 0.1)"));
        eval("22.5", Universal.evaluate("CEILING(22.25, 0.5)"));
        eval("23", Universal.evaluate("CEILING(22.25, 1)"));
        eval("30", Universal.evaluate("CEILING(22.25, 10)"));
        eval("40", Universal.evaluate("CEILING(22.25, 20)"));
        eval("-11.09183", Universal.evaluate("CEILING(-11.12333, 0.03499)"));
        eval("-4", Universal.evaluate("CEILING(-4.1,2)"));
        eval("1.3", Universal.evaluate("CEILING(1.234,0.1)"));
        eval("-1.2", Universal.evaluate("CEILING(-1.234,0.1)"));
        eval("-1.234", Universal.evaluate("CEILING(-1.234,-0.001)"));
        eval("28", Universal.evaluate("COMBIN(8,2)"));
        eval("1", Universal.evaluate("COMBIN(0,0)"));
        eval("1", Universal.evaluate("COMBIN(1,0)"));
        eval("1", Universal.evaluate("COMBIN(1,1)"));
        eval("2", Universal.evaluate("COMBIN(2,1)"));
        eval("1", Universal.evaluate("COMBIN(2,2)"));
        eval("3", Universal.evaluate("COMBIN(3,1)"));
        eval("3", Universal.evaluate("COMBIN(3,2)"));
        eval("1", Universal.evaluate("COMBIN(3,3)"));
        eval("120", Universal.evaluate("COMBIN(10,3)"));
        eval("20", Universal.evaluate("COMBINA(4,3)"));
        eval("220", Universal.evaluate("COMBINA(10,3)"));
        eval("1", Universal.evaluate("COMBINA(0,0)"));
        eval("1", Universal.evaluate("COMBINA(1,0)"));
        eval("1", Universal.evaluate("COMBINA(1,1)"));
        eval("2", Universal.evaluate("COMBINA(2,1)"));
        eval("3", Universal.evaluate("COMBINA(2,2)"));
        eval("3", Universal.evaluate("COMBINA(3,1)"));
        eval("6", Universal.evaluate("COMBINA(3,2)"));
        eval("10", Universal.evaluate("COMBINA(3,3)"));
        eval("220", Universal.evaluate("COMBINA(10,3)"));
        eval("1", Universal.evaluate("COS(0)"));
        eval("0.54030", Universal.evaluate("COS(1)"));
        eval("1", Universal.evaluate("COSH(0)"));
        eval("1.54308", Universal.evaluate("COSH(1)"));
        eval("0.6420926159343306", Universal.evaluate("COT(1)"));
        eval("1.3130352854993312", Universal.evaluate("COTH(1)"));
        eval("1.00251", Universal.evaluate("CSC(1.5)"));
        eval("1.18840", Universal.evaluate("CSC(1)"));
        eval("0.46964", Universal.evaluate("CSCH(1.5)"));
        eval("0.85092", Universal.evaluate("CSCH(1)"));
        eval("10", Universal.evaluate("DECIMAL(10.5)"));
        eval("0", Universal.evaluate("DECIMAL('0',2)"));
        eval("1", Universal.evaluate("DECIMAL('1',2)"));
        eval("2", Universal.evaluate("DECIMAL('10',2)"));
        eval("10", Universal.evaluate("DECIMAL('10',10)"));
        eval("255", Universal.evaluate("DECIMAL('FF',16)"));
        eval("1295", Universal.evaluate("DECIMAL('ZZ',36)"));
        eval("180", Universal.evaluate("DEGREES(PI)"));
        eval("4", Universal.evaluate("EVEN(3)"));
        eval("720", Universal.evaluate("FACT(6)"));
        eval("120", Universal.evaluate("FACT(5)"));
        eval("1", Universal.evaluate("FACT(1.9)"));
        eval("1", Universal.evaluate("FACT(0)"));
        eval("1", Universal.evaluate("FACT(1)"));
        eval("48", Universal.evaluate("FACTDOUBLE(6)"));
        eval("105", Universal.evaluate("FACTDOUBLE(7)"));
        eval("3840", Universal.evaluate("FACTDOUBLE(10)"));
        eval("2", Universal.evaluate("FLOOR(3.7,2)"));
        eval("-2", Universal.evaluate("FLOOR(-2.5,-2)"));
        eval("1.5", Universal.evaluate("FLOOR(1.58,0.1)"));
        eval("0.23", Universal.evaluate("FLOOR(0.234,0.01)"));
        eval("0", Universal.evaluate("FLOOR(0.234,0)"));
        eval("2", Universal.evaluate("FLOOR(3.7,2)"));
        eval("-2", Universal.evaluate("FLOOR(-2.5,-2)"));
        eval("1.5", Universal.evaluate("FLOOR(1.58,0.1)"));
        eval("0.23", Universal.evaluate("FLOOR(0.234,0.01)"));
        eval("-2", Universal.evaluate("FLOOR(-2.5,-2)"));
        eval("0.23", Universal.evaluate("FLOOR(0.234,0.01)"));
        eval("1", Universal.evaluate("GCD(5,2)"));
        eval("12", Universal.evaluate("GCD(24,36)"));
        eval("1", Universal.evaluate("GCD(7,1)"));
        eval("5", Universal.evaluate("GCD(5,0)"));
        eval("5", Universal.evaluate("INT(5.5)"));
        eval("8", Universal.evaluate("INT(8.9)"));
        eval("-9", Universal.evaluate("INT(-8.9)"));
        eval("0.5", Universal.evaluate("19.5-INT(19.5)"));
        eval("10", Universal.evaluate("LCM(5,2)"));
        eval("72", Universal.evaluate("LCM(24,36)"));
        eval("1", Universal.evaluate("LOG(10,10)"));
        eval("3", Universal.evaluate("LOG(8,2)"));
        eval("2.095903274289384604296567522021401250607518006797930116923", Universal.evaluate("LOG(10,3)"));
        eval("1.660964047443681173935159714744695087932415696512290306027", Universal.evaluate("LOG(10,4)"));
        eval("1.430676558073393050670106568763965632069791932079760449321", Universal.evaluate("LOG(10,5)"));
        eval("1.285097208938468759943479096554289548715733213281751227870", Universal.evaluate("LOG(10,6)"));
        eval("4.4543473", Universal.evaluate("LOG(86, 2.7182818)"));
        eval("1", Universal.evaluate("LOG10(10)"));
        eval("-3", Universal.evaluate("MDETERM([[3,6],[1,1]])"));
        eval("1", Universal.evaluate("MDETERM([[3,6,1],[1,1,0],[3,10,2]])"));
        eval("88", Universal.evaluate("MDETERM([[1,3,8,5],[1,3,6,1],[1,1,1,0]],[7,3,10,2]])"));
        eval("-2", Universal.evaluate("MDETERM([[1,2],[3,4]])"));
        eval("1", Universal.evaluate("MOD(3,2)"));
        eval("1", Universal.evaluate("MOD(-3,2)"));
        eval("-1", Universal.evaluate("MOD(3,-2)"));
        eval("-1", Universal.evaluate("MOD(-3,-2)"));
        eval("9", Universal.evaluate("MROUND(10,3)"));
        eval("-9", Universal.evaluate("MROUND(-10,-3)"));
        eval("1.4000000000000001", Universal.evaluate("MROUND(1.3,0.2)"));
        eval("1260", Universal.evaluate("MULTINOMIAL(2,3,4)"));
        eval("1260", Universal.evaluate("MULTINOMIAL([2,3,4])"));
        eval("3", Universal.evaluate("ODD(3)"));
        eval("3", Universal.evaluate("ODD(2)"));
        eval("3", Universal.evaluate("ODD(1.4)"));
        eval("3", Universal.evaluate("ODD(2.9)"));
        eval("3", Universal.evaluate("ODD(1.9)"));
        eval("3", Universal.evaluate("ODD(1.6)"));
        eval("3", Universal.evaluate("ODD(1.5)"));
        eval("-1", Universal.evaluate("ODD(-1)"));
        eval("-3", Universal.evaluate("ODD(-2)"));
        eval("-1", Universal.evaluate("ODD(-1.6)"));
        eval("-1", Universal.evaluate("ODD(-1.5)"));
        eval("-1", Universal.evaluate("ODD(-1.4)"));
        eval("1", Universal.evaluate("ODD(0)"));
        eval(String.valueOf(Math.PI), Universal.evaluate("PI"));
        eval(String.valueOf(Math.PI), Universal.evaluate("pi"));
        eval("25", Universal.evaluate("POWER(5,2)"));
        eval("2401077.2220695773", Universal.evaluate("POWER(98.6,3.2)"));
        eval("5.656854249492381", Universal.evaluate("POWER(4,5/4)"));
        eval("25", Universal.evaluate("POW(5,2)"));
        eval("2401077.2220695773", Universal.evaluate("POW(98.6,3.2)"));
        eval("5.656854249492381", Universal.evaluate("POW(4,5/4)"));
        eval("2250", Universal.evaluate("PRODUCT([5,15,30])"));
        eval("2", Universal.evaluate("QUOTIENT(5,2)"));
        eval("1", Universal.evaluate("QUOTIENT(4.5,3.1)"));
        eval("-3", Universal.evaluate("QUOTIENT(-10,3)"));
        eval(String.valueOf(Math.PI), Universal.evaluate("RADIANS(180)"));
        eval("2.2", Universal.evaluate("ROUND(2.15,1)"));
        eval("2.1", Universal.evaluate("ROUND(2.149,1)"));
        eval("20", Universal.evaluate("ROUND(21.5,-1)"));
        eval("1000", Universal.evaluate("ROUND(626.3,-3)"));
        eval("0", Universal.evaluate("ROUND(1.98,-1)"));
        eval("-100", Universal.evaluate("ROUND(-50.55,-2)"));
        eval("3", Universal.evaluate("ROUNDDOWN(3.2,0)"));
        eval("76", Universal.evaluate("ROUNDDOWN(76.9,0)"));
        eval("3.141", Universal.evaluate("ROUNDDOWN(3.14159,3)"));
        eval("-3.1", Universal.evaluate("ROUNDDOWN(-3.14159,1)"));
        eval("31400", Universal.evaluate("ROUNDDOWN(31415.92654,-2)"));
        eval("4", Universal.evaluate("ROUNDUP(3.2,0)"));
        eval("77", Universal.evaluate("ROUNDUP(76.9,0)"));
        eval("3.142", Universal.evaluate("ROUNDUP(3.14159,3)"));
        eval("-3.2", Universal.evaluate("ROUNDUP(-3.14159,1)"));
        eval("31500", Universal.evaluate("ROUNDUP(31415.92654,-2)"));
        eval("1.9035944074044246", Universal.evaluate("SEC(45)"));
        eval("6.482921234962678", Universal.evaluate("SEC(30)"));
        eval("5.725037161098787e-20", Universal.evaluate("SECH(45)"));
        eval("1.8715245937680347e-13", Universal.evaluate("SECH(30)"));
        eval("0", Universal.evaluate("SIGN(0)"));
        eval("-1", Universal.evaluate("SIGN(-5)"));
        eval("1", Universal.evaluate("SIGN(5)"));
        eval("1", Universal.evaluate("SIGN(10)"));
        eval("0", Universal.evaluate("SIGN(4-4)"));
        eval("-1", Universal.evaluate("SIGN(-0.00001)"));
        eval("1", Universal.evaluate("SIN(PI/2)"));
        eval("1.1752011936438014", Universal.evaluate("SINH(1)"));
        eval("2", Universal.evaluate("SQRT(4)"));
        eval("3.0699801238394655", Universal.evaluate("SQRTPI(3)"));
        eval("6", Universal.evaluate("SUM(1,2,3)"));
        eval("6", Universal.evaluate("SUM([1,2,3])"));
        eval("9", Universal.evaluate("SUM([1,2,3],1,2)"));
        eval("9", Universal.evaluate("SUM([1,2,3],[1,2])"));
        eval("12", Universal.evaluate("SUM([[1,1],[2,2],[3,3]])"));
        eval("15", Universal.evaluate("SUM([[1,1],[2,2],[3,3]],1,2)"));
        eval("15", Universal.evaluate("SUM([[1,1],[2,2],[3,3]],1,2)"));
        eval("24", Universal.evaluate("SUM([[1,1],[2,2],[3,3]],[[1,1],[2,2],[3,3]])"));
        eval("1", Universal.evaluate("TAN(RADIANS(45))"));
        eval("0.46211715726000974", Universal.evaluate("TANH(0.5)"));
        eval("8", Universal.evaluate("TRUNC(8.9)"));
        eval("-8", Universal.evaluate("TRUNC(-8.9)"));
        eval("0", Universal.evaluate("TRUNC(0.45)"));
        eval("2", Universal.evaluate("SUBTOTAL(1,[1,2,3])"));
        eval("3", Universal.evaluate("SUBTOTAL(4,[1,2,3])"));
        eval("1", Universal.evaluate("SUBTOTAL(5,[1,2,3])"));
        eval("6", Universal.evaluate("SUBTOTAL(6,[1,2,3])"));
        eval("6", Universal.evaluate("SUBTOTAL(9,[1,2,3])"));
        eval("2", Universal.evaluate("SUBTOTAL(101,[1,2,3])"));
        eval("3", Universal.evaluate("SUBTOTAL(104,[1,2,3])"));
        eval("1", Universal.evaluate("SUBTOTAL(105,[1,2,3])"));
        eval("6", Universal.evaluate("SUBTOTAL(106,[1,2,3])"));
        eval("6", Universal.evaluate("SUBTOTAL(109,[1,2,3])"));
        eval("3", Universal.evaluate("SUMIF([1,2,3],'>2')"));
        eval("6", Universal.evaluate("SUMIF([[1,1],[2,2],[3,3]],'>2')"));
        eval("156", Universal.evaluate("SUMPRODUCT([[3,4],[8,6],[1,9]],[[2,7],[6,7],[5,3]])"));
        eval("2.75", Universal.evaluate("SUMPRODUCT([[1],[4],[10]],[[0.55],[0.3],[0.1]])"));
        eval("2.75", Universal.evaluate("SUMPRODUCT([1,4,10],[0.55,0.3,0.1])"));
        eval("14", Universal.evaluate("SUMSQ(1,2,3)"));
        eval("14", Universal.evaluate("SUMSQ([1,2,3])"));
        eval("25", Universal.evaluate("SUMSQ(3,4)"));
        eval("-55", Universal.evaluate("SUMX2MY2([2, 3, 9, 1, 8, 7, 5], [6, 5, 11, 7, 5, 4, 4])"));
        eval("-63", Universal.evaluate("SUMX2MY2([1,2,3],[4,5,6])"));
        eval("521", Universal.evaluate("SUMX2PY2([2,3,9,1,8,7,5], [6,5,11,7,5,4,4])"));
        eval("91", Universal.evaluate("SUMX2PY2([1,2,3],[4,5,6])"));
        eval("79", Universal.evaluate("SUMXMY2([2, 3, 9, 1, 8, 7, 5], [6, 5, 11, 7, 5, 4, 4])"));
        eval("27", Universal.evaluate("SUMXMY2([1,2,3],[4,5,6])"));
        eval("2", Universal.evaluate("AGGREGATE(1,4,[1,2,3])"));
        eval("3", Universal.evaluate("AGGREGATE(4,4,[1,2,3])"));
        eval("1", Universal.evaluate("AGGREGATE(5,4,[1,2,3])"));
        eval("6", Universal.evaluate("AGGREGATE(6,4,[1,2,3])"));
        eval("6", Universal.evaluate("AGGREGATE(9,4,[1,2,3])"));
        eval("2", Universal.evaluate("AGGREGATE(14,4,[1,2,3],2)"));
        eval("2", Universal.evaluate("AGGREGATE(15,4,[1,2,3],2)"));
    }

    @Ignore
    @Test
    public void testMathTrigCompatibilityExtended() throws InvocationTargetException, CompileException {
        eval("1", Universal.evaluate("SUBTOTAL(7,[1,2,3])"));
        eval("0.816496580927726", Universal.evaluate("SUBTOTAL(8,[1,2,3])"));
        eval("1", Universal.evaluate("SUBTOTAL(10,[1,2,3])"));
        eval("0.6666666666666666", Universal.evaluate("SUBTOTAL(11,[1,2,3])"));
        eval("1", Universal.evaluate("SUBTOTAL(107,[1,2,3])"));
        eval("0.816496580927726", Universal.evaluate("SUBTOTAL(108,[1,2,3])"));
        eval("1", Universal.evaluate("SUBTOTAL(110,[1,2,3])"));
        eval("0.6666666666666666", Universal.evaluate("SUBTOTAL(111,[1,2,3])"));
        eval("3", Universal.evaluate("SUBTOTAL(2,[1,2,3,'doesnotcount'])"));
        eval("4", Universal.evaluate("SUBTOTAL(3,[1,2,3,'counts'])"));
        eval("3", Universal.evaluate("SUBTOTAL(102,[1,2,3,'doesnotcount'])"));
        eval("4", Universal.evaluate("SUBTOTAL(103,[1,2,3,'counts'])"));
        eval("3", Universal.evaluate("AGGREGATE(2,4,[1,2,3,'doesnotcount'])"));
        eval("4", Universal.evaluate("AGGREGATE(3,4,[1,2,3,'counts'])"));
        eval("1", Universal.evaluate("AGGREGATE(7,4,[1,2,3])"));
        eval("0.816496580927726", Universal.evaluate("AGGREGATE(8,4,[1,2,3])"));
        eval("1", Universal.evaluate("AGGREGATE(10,4,[1,2,3])"));
        eval("0.6666666666666666", Universal.evaluate("AGGREGATE(11,4,[1,2,3])"));
        eval("1", Universal.evaluate("AGGREGATE(13,4,[1,2,3])"));
        eval("1.8", Universal.evaluate("AGGREGATE(16,4,[1,2,3],0.4)"));
        eval("2", Universal.evaluate("AGGREGATE(17,4,[1,2,3],2)"));
        eval("1.6", Universal.evaluate("AGGREGATE(18,4,[1,2,3],0.4)"));
        eval("2", Universal.evaluate("AGGREGATE(19,4,[1,2,3],2)"));
        eval("2", Universal.evaluate("AGGREGATE(12,4,[1,2,3])"));
        eval("-4", Universal.evaluate("CEILING(-4.1,2,0)"));
        eval("-6", Universal.evaluate("CEILING(-4.1,2,-1)"));
        eval("-4", Universal.evaluate("CEILING(-4.1,-2,0)"));
        eval("-6", Universal.evaluate("CEILING(-4.1,-2,-1)"));
        eval("25", Universal.evaluate("CEILING.MATH(24.3,5)"));
        eval("7", Universal.evaluate("CEILING.MATH(6.7)"));
        eval("-8", Universal.evaluate("CEILING.MATH(-8.1,2)"));
        eval("-6", Universal.evaluate("CEILING.MATH(-5.5,2,-1)"));
        eval("5", Universal.evaluate("CEILING.PRECISE(4.3)"));
        eval("-4", Universal.evaluate("CEILING.PRECISE(-4.3)"));
        eval("6", Universal.evaluate("CEILING.PRECISE(4.3,2)"));
        eval("6", Universal.evaluate("CEILING.PRECISE(4.3,-2)"));
        eval("-4", Universal.evaluate("CEILING.PRECISE(-4.3,2)"));
        eval("-4", Universal.evaluate("CEILING.PRECISE(-4.3,-2)"));
        eval("-22.3", Universal.evaluate("CEILING(22.25, -0.1)"));
        eval("-23", Universal.evaluate("CEILING(22.25, -1)"));
        eval("-25", Universal.evaluate("CEILING(22.25, -5)"));
        eval("-22", Universal.evaluate("CEILING(-22.25, -5)"));
        eval("-23", Universal.evaluate("CEILING(-22.25, -5)"));
        eval("-4", Universal.evaluate("CEILING(-4.1,-2)"));
        eval("-1.23", Universal.evaluate("CEILING(-1.234,-0.01)"));
        eval("-1.2", Universal.evaluate("CEILING(-1.234,-0.1)"));
        eval("2.5", Universal.evaluate("DIVIDE(10,4)"));
        eval("-2", Universal.evaluate("DIVIDE(12,-6)"));
        eval("0", Universal.evaluate("DIVIDE(0,1)"));
        eval("true", Universal.evaluate("EQ(10,10)"));
        eval("true", Universal.evaluate("EQ(1.2,1.2)"));
        eval("false", Universal.evaluate("EQ('hello','jim')"));
        eval("true", Universal.evaluate("EQ('hello','hello')"));
        eval("false", Universal.evaluate("EQ(123,'hello')"));
        eval("true", Universal.evaluate("EQ(true,true)"));
        eval("true", Universal.evaluate("EQ(false,false)"));
        eval("false", Universal.evaluate("EQ(false,0)"));
        eval("false", Universal.evaluate("EQ(1,'string')"));
        eval("-2", Universal.evaluate("FLOOR(2.5,-2)"));
        eval("2", Universal.evaluate("FLOOR(3.7,2.1)"));
        eval("0.1", Universal.evaluate("FLOOR(1.58,0.1)"));
        eval("2014.4", Universal.evaluate("FLOOR.PRECISE(2014.6,0.2)"));
        eval("-4", Universal.evaluate("FLOOR.PRECISE(-3.2,-1)"));
        eval("3", Universal.evaluate("FLOOR.PRECISE(3.2,1)"));
        eval("-4", Universal.evaluate("FLOOR.PRECISE(-3.2,1)"));
        eval("3", Universal.evaluate("FLOOR.PRECISE(3.2,-1)"));
        eval("3", Universal.evaluate("FLOOR.PRECISE(3.2)"));
        eval("20", Universal.evaluate("FLOOR.MATH(24.3,5)"));
        eval("6", Universal.evaluate("FLOOR.MATH(6.7)"));
        eval("-10", Universal.evaluate("FLOOR.MATH(-8.1,2)"));
        eval("0", Universal.evaluate("FLOOR.MATH(-8.1,0)"));
        eval("-4", Universal.evaluate("FLOOR.MATH(-5.5,2,-1)"));
        eval("-4", Universal.evaluate("FLOOR.MATH(-3.2,-1)"));
        eval("3", Universal.evaluate("FLOOR.MATH(3.2,1)"));
        eval("-4", Universal.evaluate("FLOOR.MATH(-3.2,1)"));
        eval("3", Universal.evaluate("FLOOR.MATH(3.2,-1)"));
        eval("3", Universal.evaluate("FLOOR.MATH(3.2)"));
        eval("0", Universal.evaluate("FLOOR.MATH(3.2,0)"));
        eval("true", Universal.evaluate("GTE(10,4)"));
        eval("true", Universal.evaluate("GTE(10,10)"));
        eval("false", Universal.evaluate("GTE(10,12)"));
        eval("5", Universal.evaluate("ISO.CEILING(4.3)"));
        eval("-4", Universal.evaluate("ISO.CEILING(-4.3)"));
        eval("6", Universal.evaluate("ISO.CEILING(4.3,2)"));
        eval("6", Universal.evaluate("ISO.CEILING(4.3,-2)"));
        eval("-4", Universal.evaluate("ISO.CEILING(-4.3,2)"));
        eval("-4", Universal.evaluate("ISO.CEILING(-4.3,-2)"));
        eval("false", Universal.evaluate("LT(10,4)"));
        eval("false", Universal.evaluate("LT(10,10)"));
        eval("true", Universal.evaluate("LT(10,12)"));
        eval("false", Universal.evaluate("LTE(10,4)"));
        eval("true", Universal.evaluate("LTE(10,10)"));
        eval("true", Universal.evaluate("LTE(10,12)"));
        eval("40", Universal.evaluate("MULTIPLY(10,4)"));
        eval("-72", Universal.evaluate("MULTIPLY(12,-6)"));
        eval("0", Universal.evaluate("MULTIPLY(0,0)"));
        eval("0", Universal.evaluate("MULTIPLY(1,0)"));
        eval("0", Universal.evaluate("MULTIPLY(0,1)"));
        eval("false", Universal.evaluate("NE(10,10)"));
        eval("false", Universal.evaluate("NE(1.2,1.2)"));
        eval("true", Universal.evaluate("NE('hello','jim')"));
        eval("false", Universal.evaluate("NE('hello','hello')"));
        eval("true", Universal.evaluate("NE(123,'hello')"));
        eval("false", Universal.evaluate("NE(true,true)"));
        eval("false", Universal.evaluate("NE(false,false)"));
        eval("true", Universal.evaluate("NE(false,0)"));
        eval("true", Universal.evaluate("NE(1,'string')"));
        eval("-2.8", Universal.evaluate("MINUS(1.2,4)"));
        eval("6", Universal.evaluate("MINUS(10,4)"));
        eval("14", Universal.evaluate("ADD(10,4)"));
        eval("5.2", Universal.evaluate("ADD(1.2,4)"));
        eval("10", Universal.evaluate("ARABIC('X')"));
        eval("111", Universal.evaluate("BASE(7,2)"));
        eval("0000000400", Universal.evaluate("BASE(400,10,10)"));
        eval("X", Universal.evaluate("ROMAN(10)"));
        eval("CIII", Universal.evaluate("ROMAN(103)"));
        eval("2", Universal.evaluate("SUMIFS([1,2,3],'>1','<3')"));
        eval("4", Universal.evaluate("SUMIFS([[1,1],[2,2],[3,3]],'>1','<3')"));
    }

    @Ignore
    @Test
    public void testMathTrigPiFunctionCompatibility() throws InvocationTargetException, CompileException {
        eval(String.valueOf(Math.PI), Universal.evaluate("PI()"));
    }

    @Ignore
    @Test
    public void testMiscellaneousCompatibility() throws InvocationTargetException, CompileException {
        eval("10,000.0000", Universal.evaluate("NUMERAL(10000, '0,0.0000')"));
        eval("10,000", Universal.evaluate("NUMERAL(10000.23, '0,0')"));
        eval("$1,000.23", Universal.evaluate("NUMERAL(1000.234, '$0,0.00')"));
        eval("100B", Universal.evaluate("NUMERAL(100, '0b')"));
        eval("97.488%", Universal.evaluate("NUMERAL(0.974878234, '0.000%')"));
        eval("[1,2,3,4,5,6]", Universal.evaluate("UNIQUE(1, 2, 3, 4, 5, 6, 6, 3)"));
        eval("['jima','jimb','jimc']", Universal.evaluate("UNIQUE('jima', 'jimb', 'jima', 'jimc')"));
        eval("[]", Universal.evaluate("UNIQUE()"));
        eval("[[]]", Universal.evaluate("UNIQUE([])"));
        eval("[1,2,3,4]", Universal.evaluate("ARGS2ARRAY(1, 2, 3, 4)"));
        eval("['jim',2,3.14]", Universal.evaluate("ARGS2ARRAY('jim', 2, 3.14)"));
        eval("[1,2,3,4,5]", Universal.evaluate("FLATTEN([1, [2, 3, [4, 5]]])"));
        eval("[]", Universal.evaluate("FLATTEN([])"));
        eval("1,2,3,4,5", Universal.evaluate("JOIN([1, [2, 3, [4, 5]]])"));
        eval("jimalateras", Universal.evaluate("JOIN(['jim', 'alateras'], ' ')"));
        eval("true", Universal.evaluate("NUMBERS([1, [2, 3, [4, 5]]])"));
        eval("false", Universal.evaluate("NUMBERS(['jim', 'alateras'], ' ')"));
    }

    @Test
    public void testTextCompatibility() throws InvocationTargetException, CompileException {
        evalExpression("A", Universal.evaluate("CHAR(65)"));
        evalExpression("ÿ", Universal.evaluate("CHAR(255 )"));
        evalExpression("Ϩ", Universal.evaluate("CHAR(1000)"));
        evalExpression("", Universal.evaluate("CLEAN('' )"));
        evalExpression("Monthly Report", Universal.evaluate("CLEAN('" + String.valueOf((char) 9) + "Monthly Report" + String.valueOf((char) 9) + "' )"));
        eval("65", Universal.evaluate("CODE('A' )"));
        eval("1000", Universal.evaluate("CODE(\"Ϩ\" )"));
        evalExpression("hello world", Universal.evaluate("CONCATENATE('hello', ' ', 'world')"));
        evalExpression("hello my world", Universal.evaluate("CONCATENATE(['hello', ' my ', 'world'] )"));
        evalExpression("1one", Universal.evaluate("CONCATENATE(1, 'one' )"));
        evalExpression("TRUEyes", Universal.evaluate("CONCATENATE(true, 'yes')"));
        evalExpression("FALSEno", Universal.evaluate("CONCATENATE(false, 'no')"));
        evalExpression("$1,234.57", Universal.evaluate("DOLLAR(1234.567)"));
        evalExpression("$1,200", Universal.evaluate("DOLLAR(1234.567, -2)"));
        evalExpression("$-1,200", Universal.evaluate("DOLLAR(-1234.567, -2 )"));
        evalExpression("$-0.1230", Universal.evaluate("DOLLAR(-0.123, 4 )"));
        evalExpression("$-99.89", Universal.evaluate("DOLLAR(-99.888 )"));
        evalLogical(true, Universal.evaluate("EXACT('yes', 'yes' )"));
        eval("1", Universal.evaluate("FIND('M', 'Miriam McGovern')"));
        eval("6", Universal.evaluate("FIND('m', 'Miriam McGovern')"));
        eval("8", Universal.evaluate("FIND('M', 'Miriam McGovern', 3 )"));
        evalExpression("1,234.6", Universal.evaluate("FIXED(1234.567, 1)"));
        evalExpression("1,230", Universal.evaluate("FIXED(1234.567, -1 )"));
        evalExpression("-1230", Universal.evaluate("FIXED(-1234.567, -1, true)"));
        evalExpression("44.33", Universal.evaluate("FIXED(44.332 )"));
        evalExpression("Sale", Universal.evaluate("LEFT('Sale Price', 4 )"));
        evalExpression("S", Universal.evaluate("LEFT('Sweeden' )"));
        eval("4", Universal.evaluate("LEN('four' )"));
        eval("5", Universal.evaluate("LEN([1, 2, 3, 4, 5])"));
        evalExpression("abcd", Universal.evaluate("LOWER('abcd' )"));
        evalExpression("abcd", Universal.evaluate("LOWER('ABcd' )"));
        evalExpression("abcd", Universal.evaluate("LOWER('ABCD' )"));
        evalExpression("", Universal.evaluate("LOWER('' )"));
        evalExpression("Fluid", Universal.evaluate("MID('Fluid Flow', 1, 5 )"));
        evalExpression("Flow", Universal.evaluate("MID('Fluid Flow', 7, 20)"));
        evalExpression("", Universal.evaluate("MID('Fluid Flow', 20, 50 )"));
        evalExpression("A Title Case", Universal.evaluate("PROPER('a title case')"));
        evalExpression("True", Universal.evaluate("PROPER(true)"));
        evalExpression("False", Universal.evaluate("PROPER(false )"));
        evalExpression("90", Universal.evaluate("PROPER(90)"));
        evalExpression("abcde*k", Universal.evaluate("REPLACE('abcdefghijk', 6, 5, '*' )"));
        evalExpression("2010", Universal.evaluate("REPLACE('2009', 3, 2, '10' )"));
        evalExpression("@456", Universal.evaluate("REPLACE('123456', 1, 3, '@')"));
        evalExpression("multiple multiple multiple ", Universal.evaluate("REPT('multiple ', 3)"));
        evalExpression("Price", Universal.evaluate("RIGHT('Sale Price', 5)"));
        evalExpression("r", Universal.evaluate("RIGHT('Stock Number' )"));
        eval("7", Universal.evaluate("SEARCH('e', 'Statements', 6)"));
        eval("8", Universal.evaluate("SEARCH('margin', 'Profit Margin' )"));
        eval("1", Universal.evaluate("SEARCH(\"ba\", \"bar\" )"));
        evalExpression("James Alateras", Universal.evaluate("SUBSTITUTE('Jim Alateras', 'im', 'ames')"));
        evalExpression("Jim Alateras", Universal.evaluate("SUBSTITUTE('Jim Alateras', '', 'ames')"));
        evalExpression("", Universal.evaluate("SUBSTITUTE('', 'im', 'ames')"));
        evalExpression("Quarter 2, 2008", Universal.evaluate("SUBSTITUTE('Quarter 1, 2008', '1', '2', 1)"));
        evalExpression("Rainfall", Universal.evaluate("T('Rainfall' )"));
        evalExpression("1234.6", Universal.evaluate("TEXT('1234.59', '####.#' )"));
        evalExpression("1234.5", Universal.evaluate("TEXT('1234.52', '####.#' )"));
        evalExpression("1234.56", Universal.evaluate("TEXT('1234.56', '####.##')"));
        evalExpression("more spaces", Universal.evaluate("TRIM(' more spaces ')"));
        evalExpression("A", Universal.evaluate("UNICHAR(65 )"));
        evalExpression("ÿ", Universal.evaluate("UNICHAR(255)"));
        evalExpression("Ϩ", Universal.evaluate("UNICHAR(1000 )"));
        eval("65", Universal.evaluate("UNICODE('A')"));
        eval("1000", Universal.evaluate("UNICODE(\"Ϩ\")"));
        evalExpression("TO UPPER CASE PLEASE", Universal.evaluate("UPPER('to upper case please' )"));
        eval("1000", Universal.evaluate("VALUE('$1,000' )"));
        eval("250", Universal.evaluate("NUMBERVALUE(\"250\",\",\",\".\")"));
        eval("2500.27", Universal.evaluate("NUMBERVALUE(\"2.500,27\",\",\",\".\" )"));
    }

    @Test
    public void testNumberValueFunc() throws InvocationTargetException, CompileException {
        eval("2500.27", Universal.evaluate("NUMBERVALUE(\"2.500,27\",\",\",\".\" )"));
        eval("250", Universal.evaluate("NUMBERVALUE(\"250\",\",\",\".\")"));
        eval("0.035", Universal.evaluate("NUMBERVALUE(\"3.5%\")"));
    }

    @Ignore
    @Test
    public void testValueCompatibilityExtended() throws InvocationTargetException, CompileException {
        eval("60480", Universal.evaluate("VALUE('16:48:00' )"));
        eval("0", Universal.evaluate("VALUE('foo Bar' )"));
    }

    @Ignore
    @Test
    public void testTextCompat() throws InvocationTargetException, CompileException {
        eval("0.035", Universal.evaluate("NUMBERVALUE(\"3.5%\")"));
        evalExpression("EXCEL", Universal.evaluate("DBCS(\"EXCEL\")"));
        evalExpression("EXCEL", Universal.evaluate("ASC(\"EXCEL\")"));
        evalExpression("หนึ่งพันสองร้อยสามสิบสี่บาทถ้วน", Universal.evaluate("BAHTTEXT(1234)"));
        evalExpression("A", Universal.evaluate("CHAR(65)"));
        evalExpression("!", Universal.evaluate("CHAR(33)"));
    }

    @Test
    public void testTextCompatibilityExtended() throws InvocationTargetException, CompileException {
        evalExpression("28.5%", Universal.evaluate("TEXT(0.285,\"0.0%\")"));
        evalExpression("$1,234.57", Universal.evaluate("TEXT(1234.567,\"$#,##0.00\")"));
        evalExpression("4 1/3", Universal.evaluate("TEXT(4.34 ,\"# ?/?\")"));
        evalExpression("1/3", Universal.evaluate("TRIM(TEXT(0.34,\"# ?/?\"))"));
        evalExpression("(123) 456-7898", Universal.evaluate("TEXT(1234567898,\"[<=9999999]###-####;(###) ###-####\")"));
        evalExpression("0001234", Universal.evaluate("TEXT(1234,\"0000000\")"));
        evalExpression("1.22E+07", Universal.evaluate("TEXT(12200000,\"0.00E+00\")"));
        evalExpression("1:03 AM", Universal.evaluate("TEXT(DATE(2012,03,14, 1,29),\"H:MM a\")"));
    }

    @Ignore
    @Test
    public void testSearchCompatibility() throws InvocationTargetException, CompileException {
        evalExpression("error.value", Universal.evaluate("SEARCH(true, 'bool')"));
        evalExpression("error.value", Universal.evaluate("SEARCH(\"foo\", \"bar\")"));
    }

    @Ignore
    @Test
    public void testHtml2TextCompatibility() throws InvocationTargetException, CompileException {
        evalExpression("", Universal.evaluate("HTML2TEXT( )"));
        evalExpression("", Universal.evaluate("HTML2TEXT('' )"));
        evalExpression("Hello", Universal.evaluate("HTML2TEXT('<i>Hello</i>' )"));
        evalExpression("Hello\nJim", Universal.evaluate("HTML2TEXT(['<i>Hello</i>', '<b>Jim</b>'] )"));
    }

    @Ignore
    @Test
    public void testRegexCompatibility() throws InvocationTargetException, CompileException {
        evalExpression("ets', '(([A-Za-z]+", Universal.evaluate("REGEXEXTRACT('(Content )"));
        evalExpression("826.25", Universal.evaluate("REGEXEXTRACT('The price today is $826.25', '[0-9]+.[0-9]+[0-9]+' )"));
        evalExpression("101", Universal.evaluate("REGEXEXTRACT('Google Doc 101', '[0-9]+')"));
        evalExpression("ets', '(([A-Za-z]+", Universal.evaluate("REGEXREPLACE('(Content )"));
        evalExpression("ets', '(([A-Za-z]+", Universal.evaluate("REGEXMATCH('(Content )"));
    }

    @Test
    public void testDateTimeCompatibility() throws InvocationTargetException, CompileException {
        evalDateExpression(Moment.valueOf(1900, 1, 1), Universal.evaluate("DATE(1900, 1, 1)"));
        eval("36921", Universal.evaluate("DATEVALUE(DATE(2001,1,30))"));
        eval("36921", Universal.evaluate("DATEVALUE('2001/1/30')"));
        eval("2", Universal.evaluate("DATEDIF(DATE(2001,1,1),DATE(2003,1,1), 'Y')"));
        eval("440", Universal.evaluate("DATEDIF(DATE(2001,6,1),DATE(2002,8,15), 'D')"));
        eval("75", Universal.evaluate("DATEDIF(DATE(2001,6,1),DATE(2002,8,15), 'YD')"));
        eval("2", Universal.evaluate("DATEDIF('2001/1/1','2003/1/1', 'Y')"));
        eval("440", Universal.evaluate("DATEDIF('2001/6/1','2002/8/15', 'D')"));
        eval("75", Universal.evaluate("DATEDIF('2001/6/1','2002/8/15', 'YD')"));
        eval("2", Universal.evaluate("DATEDIF(DATEVALUE('2001/1/1'),DATEVALUE('2003/1/1'), 'Y')"));
        eval("440", Universal.evaluate("DATEDIF(DATEVALUE('2001/6/1'),DATEVALUE('2002/8/15'), 'D')"));
        eval("75", Universal.evaluate("DATEDIF(DATEVALUE('2001/6/1'),DATEVALUE('2002/8/15'), 'YD')"));
        eval("2", Universal.evaluate("DATEDIF(36891,DATE(2003,1,1), 'Y')"));
        eval("2", Universal.evaluate("DATEDIF(36891,DATEVALUE('2003/1/1'), 'Y')"));
        eval("40597", Universal.evaluate("DATEVALUE(\"2011/02/23\")"));
        eval("40685", Universal.evaluate("DATEVALUE(\"22-MAY-2011\")"));
        eval("40777", Universal.evaluate("DATEVALUE(\"8/22/2011\")"));
        eval("40850", Universal.evaluate("DATEVALUE(\"11/3/2011\")"));
        eval("39448", Universal.evaluate("DATEVALUE(\"1/1/2008\")"));
        eval("39634", Universal.evaluate("DATEVALUE(\"7/5/2008\")"));
        eval("39634", Universal.evaluate("DATEVALUE(\"5-JUL-2008\")"));
        eval(String.valueOf(Moments.datevalue(Moment.valueOf(new Moment().year().intValue(), 7, 5))), Universal.evaluate("DATEVALUE(\"5-JUL\")"));
        eval("1", Universal.evaluate("DATEVALUE('1/1/1900')"));
        eval("2958465", Universal.evaluate("DATEVALUE('12/31/9999')"));
        eval("15", Universal.evaluate("DAY('15-Apr-11')"));
        eval("5", Universal.evaluate("DAY(\"5-JUL-2008\")"));
        eval("5", Universal.evaluate("DAY(DATEVALUE(\"5-JUL-2008\"))"));
        eval("23", Universal.evaluate("DAY(\"2011/02/23\")"));
        eval("23", Universal.evaluate("DAY(DATEVALUE(\"2011/02/23\"))"));
        eval("31", Universal.evaluate("DAY(2958465)"));
        eval("1", Universal.evaluate("DAY(1)"));
        eval("1", Universal.evaluate("DAY('1')"));
        eval("1", Universal.evaluate("DAY('1/1/1900')"));
        eval("1", Universal.evaluate("DAY(DATE(1900,0,1))"));
        eval("42", Universal.evaluate("DAYS(\"3/15/11\",\"2/1/11\")"));
        eval("364", Universal.evaluate("DAYS(\"12/31/2011\",\"1/1/2011\")"));
        eval("1", Universal.evaluate("DAYS(2,1)"));
        eval("1", Universal.evaluate("DAYS('1/2/1900','1/1/1900')"));
        eval("1", Universal.evaluate("DAYS(DATE(1900,1,2),DATE(1900,1,1))"));
        eval("1", Universal.evaluate("DAYS360('30-Jan-11','1-Feb-11')"));
        eval("30", Universal.evaluate("DAYS360('1-Jan-11','1-Feb-11')"));
        eval("360", Universal.evaluate("DAYS360('1-Jan-11','1-Jan-12')"));
        eval("180", Universal.evaluate("DAYS360('1-JUN-13','31-NOV-13')"));
        eval("120", Universal.evaluate("DAYS360('1-JUN-13','31-SEP-13')"));
        eval("360", Universal.evaluate("DAYS360('1-Jan-11','31-Dec-11')"));
        eval("359", Universal.evaluate("DAYS360('1/1/1901','12/31/1901',true)"));
        eval("360", Universal.evaluate("DAYS360('1/1/1901','1/1/1902',true)"));
        eval("30", Universal.evaluate("DAYS360('1/1/2008','2/1/2008',true)"));
        eval("1", Universal.evaluate("DAYS360('1/1/2008','1/2/2008',true)"));
        eval("1", Universal.evaluate("DAYS360('1/1/1901','1/2/1901',true)"));
        eval("30", Universal.evaluate("DAYS360('1/1/1901','2/1/1901',true)"));
        eval("1", Universal.evaluate("DAYS360('1/1/1901','1/2/1901',false)"));
        eval("360", Universal.evaluate("DAYS360('1/1/1901','12/31/1901',false)"));
        eval("360", Universal.evaluate("DAYS360('1/1/1901','1/1/1902',false)"));
        eval("30", Universal.evaluate("DAYS360('1/1/1901','2/1/1901',false)"));
        eval("330", Universal.evaluate("DAYS360('1/30/1901','12/31/1901',false)"));
        eval(String.valueOf(Moments.datevalue(Moment.valueOf(2011, 2, 15))), Universal.evaluate("EDATE('15-Jan-11',1)"));
        eval(String.valueOf(Moments.datevalue(Moment.valueOf(2010, 12, 15))), Universal.evaluate("EDATE('15-Jan-11',-1)"));
        eval(String.valueOf(Moments.datevalue(Moment.valueOf(2011, 3, 15))), Universal.evaluate("EDATE('15-Jan-11',2)"));
        eval("1", Universal.evaluate("EDATE('1/1/1900',0)"));
        eval("367", Universal.evaluate("EDATE('1/1/1900',12)"));
        eval(String.valueOf(Moments.datevalue(Moment.valueOf(2011, 2, 28))), Universal.evaluate("EOMONTH('1-Jan-11',1)"));
        eval(String.valueOf(Moments.datevalue(Moment.valueOf(2010, 10, 31))), Universal.evaluate("EOMONTH('1-Jan-11',-3)"));
        eval("397", Universal.evaluate("EOMONTH('1/1/1900',12)"));
        eval("18", Universal.evaluate("HOUR(0.75)"));
        eval("7", Universal.evaluate("HOUR('7/18/2011 7:45')"));
        eval("0", Universal.evaluate("HOUR('4/21/2012')"));
        eval("0", Universal.evaluate("HOUR('1/1/1900')"));
        eval("10", Universal.evaluate("ISOWEEKNUM('3/9/2012')"));
        eval("1", Universal.evaluate("ISOWEEKNUM('1/1/1901')"));
        eval("2", Universal.evaluate("ISOWEEKNUM('1/8/1901')"));
        eval("52", Universal.evaluate("ISOWEEKNUM('12/29/1901')"));
        eval("23", Universal.evaluate("ISOWEEKNUM('6/6/1902')"));
        eval("45", Universal.evaluate("MINUTE('12:45:00 PM')"));
        eval("45", Universal.evaluate("MINUTE('6:45:00 PM')"));
        eval("45", Universal.evaluate("MINUTE('6:45 PM')"));
        eval("0", Universal.evaluate("MINUTE('1/1/1901')"));
        eval("4", Universal.evaluate("MONTH('15-Apr-11')"));
        eval("1", Universal.evaluate("MONTH('1/1/1900')"));
        eval("12", Universal.evaluate("MONTH('12/1/1900')"));
        eval("110", Universal.evaluate("NETWORKDAYS('10/1/2012','3/1/2013')"));
        eval("109", Universal.evaluate("NETWORKDAYS('10/1/2012','3/1/2013', '11/22/2012')"));
        eval("107", Universal.evaluate("NETWORKDAYS('10/1/2012','3/1/2013', '11/22/2012', '12/4/2012', '1/21/2013')"));
        eval("1", Universal.evaluate("NETWORKDAYS('2013/12/04','2013/12/04')"));
        eval("2", Universal.evaluate("NETWORKDAYS('2013/12/04','2013/12/05')"));
        eval("3", Universal.evaluate("NETWORKDAYS('2013/12/04','2013/12/06')"));
        eval("4", Universal.evaluate("NETWORKDAYS('2013/12/04','2013/12/09')"));
        eval("1", Universal.evaluate("NETWORKDAYS('12/4/2013','12/4/2013')"));
        eval("22", Universal.evaluate("NETWORKDAYS.INTL(DATE(2006,1,1),DATE(2006,1,31))"));
        eval("22", Universal.evaluate("NETWORKDAYS.INTL(DATE(2006,1,1),DATE(2006,1,31),\"0000011\")"));
        eval("-21", Universal.evaluate("NETWORKDAYS.INTL(DATE(2006,2,28),DATE(2006,1,31))"));
        eval("-21", Universal.evaluate("NETWORKDAYS.INTL(DATE(2006,2,28),DATE(2006,1,31),\"0000011\")"));
        eval("22", Universal.evaluate("NETWORKDAYS.INTL(DATE(2006,1,1),DATE(2006,2,1),7,[\"2006/1/2\",\"2006/1/16\"])"));
        eval("24", Universal.evaluate("NETWORKDAYS.INTL(DATE(2006,1,1),DATE(2006,2,1),7)"));
        eval("22", Universal.evaluate("NETWORKDAYS.INTL(DATE(2006,1,1),DATE(2006,2,1),\"0000110\",[\"2006/1/2\",\"2006/1/16\"])"));
        eval("24", Universal.evaluate("NETWORKDAYS.INTL(DATE(2006,1,1),DATE(2006,2,1),\"0000110\")"));
        eval("23", Universal.evaluate("NETWORKDAYS.INTL(DATE(2006,1,1),DATE(2006,2,1),\"0010001\")"));
        eval("21", Universal.evaluate("NETWORKDAYS.INTL(DATE(2006,1,1),DATE(2006,2,1),\"0010001\",[\"2006/1/2\",\"2006/1/16\"])"));
        eval("2", Universal.evaluate("NETWORKDAYS.INTL('12/4/2013','12/5/2013')"));
        evalDateExpressionNear(Moment.valueOf(new Moment().year().intValue(), new Moment().month().intValue(), new Moment().day().intValue()), Universal.evaluate("NOW()"));
        evalDateExpressionNear(Moment.valueOf(new Moment().year().intValue(), new Moment().month().intValue(), new Moment().day().intValue(), new Moment().hour().intValue() - 12, new Moment().minute().intValue()), Universal.evaluate("NOW()-0.5"));
        evalDateExpressionNear(Moment.valueOf(new Moment().year().intValue(), new Moment().month().intValue(), new Moment().day().intValue() + 7), Universal.evaluate("NOW()+7"));
        evalDateExpressionNear(Moment.valueOf(new Moment().year().intValue(), new Moment().month().intValue(), new Moment().day().intValue() - 2, new Moment().hour().intValue() - 6, new Moment().minute().intValue()), Universal.evaluate("NOW()-2.25"));
        eval("18", Universal.evaluate("SECOND('4:48:18 PM')"));
        eval("0", Universal.evaluate("SECOND('4:48 PM')"));
        eval("0", Universal.evaluate("SECOND('1/1/1900')"));
        eval("0.5", Universal.evaluate("TIME(12,0,0)"));
        eval("0.7001157", Universal.evaluate("TIME(16,48,10)"));
        eval("0", Universal.evaluate("TIME(0,0,0)"));
        eval("0.04237268518518519", Universal.evaluate("TIME(1,1,1)"));
        eval("0.10", Universal.evaluate("TIMEVALUE(\"2:24 AM\")"));
        eval("0.27431", Universal.evaluate("TIMEVALUE(\"22-Aug-2011 6:35 AM\")"));
        eval("0", Universal.evaluate("TIMEVALUE('1/1/1900 00:00:00')"));
        eval("0.5", Universal.evaluate("TIMEVALUE('1/1/1900 12:00:00')"));
        evalDateExpressionNear(Moment.valueOf(new Moment().year().intValue(), new Moment().month().intValue(), new Moment().day().intValue()), Universal.evaluate("TODAY()"));
        evalDateExpressionNear(Moment.valueOf(new Moment().year().intValue(), new Moment().month().intValue(), new Moment().day().intValue() + 5), Universal.evaluate("TODAY()+5"));
        eval(Moment.valueOf(new Moment().year().intValue(), new Moment().month().intValue(), new Moment().day().intValue()).day().toString(), Universal.evaluate("DAY(TODAY())"));
        eval(Moment.valueOf(new Moment().year().intValue(), new Moment().month().intValue(), new Moment().day().intValue()).month().toString(), Universal.evaluate("MONTH(TODAY())"));
        eval("5", Universal.evaluate("WEEKDAY(DATE(2008, 2, 14))"));
        eval("4", Universal.evaluate("WEEKDAY(DATE(2008, 2, 14), 2)"));
        eval("3", Universal.evaluate("WEEKDAY(DATE(2008, 2, 14), 3)"));
        eval("5", Universal.evaluate("WEEKDAY('2/14/2008')"));
        eval("4", Universal.evaluate("WEEKDAY('2/14/2008', 2)"));
        eval("3", Universal.evaluate("WEEKDAY('2/14/2008', 3)"));
        eval("3", Universal.evaluate("WEEKDAY('1/1/1901')"));
        eval("2", Universal.evaluate("WEEKDAY('1/1/1901',2)"));
        eval("10", Universal.evaluate("WEEKNUM('3/9/2012')"));
        eval("11", Universal.evaluate("WEEKNUM('3/9/2012', 2)"));
        eval("1", Universal.evaluate("WEEKNUM('1/1/1900')"));
        eval("5", Universal.evaluate("WEEKNUM('2/1/1900')"));
        eval("6", Universal.evaluate("WEEKNUM('2/1/1909',2)"));
        eval("1", Universal.evaluate("WEEKNUM('1/1/1901',21)"));
        eval("2008", Universal.evaluate("YEAR('7/5/2008')"));
        eval("2010", Universal.evaluate("YEAR('7/5/2010')"));
        eval("1900", Universal.evaluate("YEAR('1/1/1900')"));
        eval("0.58055556", Universal.evaluate("YEARFRAC('1/1/2012','7/30/2012')"));
        eval("0.57808219", Universal.evaluate("YEARFRAC('1/1/2012','7/30/2012',3)"));
        eval("0.57650273", Universal.evaluate("YEARFRAC('1/1/2012','7/30/2012',1)"));
        eval("0.002777777777777778", Universal.evaluate("YEARFRAC('1/1/1900','1/2/1900')"));
        eval("0.16666666666666666", Universal.evaluate("YEARFRAC('1/31/1900','3/31/1900',0)"));
        eval("0.002777777777777778", Universal.evaluate("YEARFRAC('1/31/1900','2/1/1900',0)"));
        eval("0.16666666666666666", Universal.evaluate("YEARFRAC('1/30/1900','3/31/1900',0)"));
        eval("0.002777777777777778", Universal.evaluate("YEARFRAC('1/1/1900','1/2/1900',4)"));
        evalDateExpression(Moment.valueOf(2009, 4, 30), Universal.evaluate("WORKDAY('10/1/2008',151)"));
        evalDateExpression(Moment.valueOf(2009, 5, 5), Universal.evaluate("WORKDAY('10/1/2008',151, '11/26/2008','12/4/2008','1/21/2009')"));
        eval("2", Universal.evaluate("DAY(WORKDAY('1/1/1900',1))"));
        eval("4", Universal.evaluate("DAY(WORKDAY('1/1/1900',2,'1/2/1900'))"));
        eval("10", Universal.evaluate("DAY(WORKDAY('1/1/1900',7))"));
        eval("41013", Universal.evaluate("DATEVALUE(WORKDAY.INTL(DATE(2012,1,1),90,11))"));
        eval("40944", Universal.evaluate("DATEVALUE(WORKDAY.INTL(DATE(2012,1,1),30,17))"));
        eval("2", Universal.evaluate("DAY(WORKDAY.INTL('1/1/1900',1))"));
    }

    @Test
    public void testYearFrac() throws InvocationTargetException, CompileException {
        eval("0.58055556", Universal.evaluate("YEARFRAC('1/1/2012','7/30/2012')"));
        eval("0.57808219", Universal.evaluate("YEARFRAC('1/1/2012','7/30/2012',3)"));
        eval("0.57650273", Universal.evaluate("YEARFRAC('1/1/2012','7/30/2012',1)"));
        eval("0.002777777777777778", Universal.evaluate("YEARFRAC('1/1/1900','1/2/1900')"));
        eval("0.16666666666666666", Universal.evaluate("YEARFRAC('1/31/1900','3/31/1900',0)"));
        eval("0.002777777777777778", Universal.evaluate("YEARFRAC('1/31/1900','2/1/1900',0)"));
        eval("0.16666666666666666", Universal.evaluate("YEARFRAC('1/30/1900','3/31/1900',0)"));
        eval("0.002777777777777778", Universal.evaluate("YEARFRAC('1/1/1900','1/2/1900',4)"));
    }

    @Test
    public void testStatisticalCompatibility() throws InvocationTargetException, CompileException {
        eval("1.020408", Universal.evaluate("AVEDEV(4,5,6,7,5,4,3)"));
        eval("2.5", Universal.evaluate("AVEDEV(1,2,3,4,5,6,7,8,9,10)"));
        eval("0", Universal.evaluate("AVEDEV(1,1,1,1,1,1,1,1,1,1)"));
        eval("0", Universal.evaluate("AVEDEV(0,0,0,0,0,0,0,0,0,0)"));
        eval("0.5", Universal.evaluate("AVEDEV(1,2,1,2,1,2,1,2,1,2)"));
        eval("36.42176053333", Universal.evaluate("AVEDEV(123.12,33.3333,2/3,5.37828,0.999)"));
        eval("2.5", Universal.evaluate("AVEDEV(-1,-2,-3,-4,-5,-6,-7,-8,-9,-10)"));
        eval("4.5", Universal.evaluate("AVEDEV(2,4,8,16)"));
        eval("4.5", Universal.evaluate("AVEDEV([2,4,8,16])"));
        eval("4.5", Universal.evaluate("AVEDEV([2,4],[8,16])"));
        eval("4.5", Universal.evaluate("AVEDEV([[2,4],[8,16]])"));
        eval("7.5", Universal.evaluate("AVERAGE(2,4,8,16)"));
        eval("7.5", Universal.evaluate("AVERAGE([2,4,8,16])"));
        eval("7.5", Universal.evaluate("AVERAGE([2,4],[8,16])"));
        eval("7.5", Universal.evaluate("AVERAGE([[2,4],[8,16]])"));
        eval("7.5", Universal.evaluate("AVERAGE([[2,4],[8,16],[true,false]])"));
        eval("7.5", Universal.evaluate("AVERAGEA(2,4,8,16)"));
        eval("7.5", Universal.evaluate("AVERAGEA([2,4,8,16])"));
        eval("7.5", Universal.evaluate("AVERAGEA([2,4],[8,16])"));
        eval("3.5", Universal.evaluate("AVERAGEA([2,4],[6,8],[true,false])"));
        eval("14000", Universal.evaluate("AVERAGEIF(7000,14000,21000,28000,'<23000')"));
        eval("150000", Universal.evaluate("AVERAGEIF(100000,200000,300000,400000,'<250000')"));
        eval("24500", Universal.evaluate("AVERAGEIF(100000,200000,300000,400000,'>250000',7000,14000,21000,28000)"));
        eval("16733.5", Universal.evaluate("AVERAGEIF('East','West','North','South (New Office)', 'MidWest','=*West',45678,23789,-4789,0,9678)"));
        eval("18589", Universal.evaluate("AVERAGEIF('East','West','North','South (New Office)', 'MidWest','<>*(New Office)',45678,23789,-4789,0,9678)"));
        eval("12", Universal.evaluate("AVERAGEIF([2,4,8,16],'>5')"));
        eval("3.5", Universal.evaluate("AVERAGEIF([2,4,8,16],'>5',[1,2,3,4])"));
        eval("3.5", Universal.evaluate("AVERAGEIF([[2,4],[8,16]],'>5',[[1,2],[3,4]])"));
        eval("9.966606842186748", Universal.evaluate("BETA.DIST(1/52,0.4,9.6,false)"));
        eval("0.5406016379941343", Universal.evaluate("BETA.DIST(1/52,0.4,9.6,true)"));
        eval("2", Universal.evaluate("BETA.INV(0.685470581,8,10,1,3)"));
        eval("1.9999999999999998", Universal.evaluate("BETA.INV(0.6854705810117458,8,10,1,3)"));
        eval("0.205078125", Universal.evaluate("BINOM.DIST(6,10,0.5,false)"));
        eval("0.08397496742904752", Universal.evaluate("BINOM.DIST.RANGE(60,0.75,48)"));
        eval("0.5236297934718873", Universal.evaluate("BINOM.DIST.RANGE(60,0.75,45,50)"));
        eval("4", Universal.evaluate("BINOM.INV(6,0.5,0.75)"));
        eval("0.5204998778130242", Universal.evaluate("CHISQ.DIST(0.5,1,true)"));
        eval("0.20755375", Universal.evaluate("CHISQ.DIST(2,3,false)"));
        eval("0.5578254", Universal.evaluate("CHISQ.DIST.RT(3,4)"));
        eval("3.283020286473263", Universal.evaluate("CHISQ.INV(0.93,1)"));
        eval("1.83258146374831", Universal.evaluate("CHISQ.INV(0.6,2)"));
        eval("6.210757195", Universal.evaluate("CHISQ.INV.RT(0.4,6)"));
        eval("0.006376", Universal.evaluate("CHISQ.TEST([58,11,10,35,25,23],[45.35,17.56,16.09,47.65,18.44,16.91])"));
        eval("0.9970544855015815", Universal.evaluate("CORREL([3,2,4,5,6],[9,7,12,15,17])"));
        eval("0", Universal.evaluate("COUNT()"));
        eval("4", Universal.evaluate("COUNT(1,2,3,4)"));
        eval("4", Universal.evaluate("COUNT([1,2,3,4])"));
        eval("4", Universal.evaluate("COUNT([1,2],[3,4])"));
        eval("4", Universal.evaluate("COUNT([[1,2],[3,4]])"));
        eval("0", Universal.evaluate("COUNTA()"));
        eval("4", Universal.evaluate("COUNTA(1,3,'a','','c')"));
        eval("4", Universal.evaluate("COUNTA([1,3,'a','','c'])"));
        eval("4", Universal.evaluate("COUNTA([1,3],['a','','c'])"));
        eval("4", Universal.evaluate("COUNTA([[1,3],['a','','c']])"));
        eval("0", Universal.evaluate("COUNTBLANK()"));
        eval("1", Universal.evaluate("COUNTBLANK(1,3,'a','','c')"));
        eval("1", Universal.evaluate("COUNTBLANK([1,3,'a','','c'])"));
        eval("1", Universal.evaluate("COUNTBLANK([1,3],['a','','c'])"));
        eval("1", Universal.evaluate("COUNTBLANK([[1,3],['a','','c']])"));
        eval("5.2", Universal.evaluate("COVARIANCE.P([3,2,4,5,6],[9,7,12,15,17])"));
        eval("9.666666666666668", Universal.evaluate("COVARIANCE.S([2,4,8],[5,11,12])"));
        eval("48", Universal.evaluate("DEVSQ([4,5,8,7,11,4,3])"));
        eval("0.8646647167633873", Universal.evaluate("EXPON.DIST(0.2,10,true)"));
        eval("1.353352832366127", Universal.evaluate("EXPON.DIST(0.2,10,false)"));
        eval("0.9899999999985833", Universal.evaluate("F.DIST(15.20686486,6,4,true)"));
        eval("0.0012237995987608916", Universal.evaluate("F.DIST(15.20686486,6,4,false)"));
        eval("0.0100", Universal.evaluate("F.DIST.RT(15.20686486,6,4)"));
        eval("0.10930991412457851", Universal.evaluate("F.INV(0.01,6,4)"));
        eval("15.20686486", Universal.evaluate("F.INV.RT(0.01,6,4)"));
        eval("0.1497", Universal.evaluate("F.TEST([4,2,5,1,3],[8,3,9,0,1])"));
        eval("0.9729550745276566", Universal.evaluate("FISHER(0.75)"));
        eval("0.75", Universal.evaluate("FISHERINV(0.9729550745276566)"));
        eval("1.3293403919101043", Universal.evaluate("GAMMA(2.5)"));
        eval("0.26786611734776916", Universal.evaluate("GAMMA(-3.75)"));
        eval("0.068094", Universal.evaluate("GAMMA.DIST(10.00001131,9,2,true)"));
        eval("0.03263913", Universal.evaluate("GAMMA.DIST(10.00001131,9,2,false)"));
        eval("10.000011", Universal.evaluate("GAMMA.INV(0.068094,9,2)"));
        eval("1.7917594692280547", Universal.evaluate("GAMMALN(4)"));
        eval("1.7917594692280547", Universal.evaluate("GAMMALN.PRECISE(4)"));
        eval("2.453736571", Universal.evaluate("GAMMALN.PRECISE(4.5)"));
        eval("0.4772498680518208", Universal.evaluate("GAUSS(2)"));
        eval("5.476986969656962", Universal.evaluate("GEOMEAN([4,5,8,7,11,4,3])"));
        eval("5.028375962061728", Universal.evaluate("HARMEAN([4,5,8,7,11,4,3])"));
        eval("0.3632610939112487", Universal.evaluate("HYPGEOM.DIST(1,4,8,20,false)"));
        eval("0.46542827657378744", Universal.evaluate("HYPGEOM.DIST(1,4,8,20,true)"));
        eval("-0.15179963720841627", Universal.evaluate("KURT([3,4,5,2,3,4,5,6,4,7])"));
        eval("4", Universal.evaluate("LARGE([3,5,3,5,4],3)"));
        eval("0.0390835557068005", Universal.evaluate("LOGNORM.DIST(4,3.5,1.2,true)"));
        eval("0.01761759668181924", Universal.evaluate("LOGNORM.DIST(4,3.5,1.2,false)"));
        eval("4.000000000000001", Universal.evaluate("LOGNORM.INV(0.0390835557068005,3.5,1.2)"));
        eval("0", Universal.evaluate("MAX()"));
        eval("0.8", Universal.evaluate("MAX([0.1,0.2],[0.4,0.8],[true,false])"));
        eval("1", Universal.evaluate("MAX([[0,0.1,0.2],[0.4,0.8,1],[true,false]])"));
        eval("0", Universal.evaluate("MAXA()"));
        eval("1", Universal.evaluate("MAXA([0.1,0.2],[0.4,0.8],[true,false])"));
        eval("1", Universal.evaluate("MAXA([[0.1,0.2],[0.4,0.8],[true,false]])"));
        eval("3", Universal.evaluate("MEDIAN(1,2,3,4,5)"));
        eval("3.5", Universal.evaluate("MEDIAN(1,2,3,4,5,6)"));
        eval("0", Universal.evaluate("MIN()"));
        eval("0.1", Universal.evaluate("MIN([0.1,0.2],[0.4,0.8],[true,false])"));
        eval("0", Universal.evaluate("MIN([0,0.1,0.2],[0.4,0.8,1],[true,false])"));
        eval("0", Universal.evaluate("MIN([[10,0],[0.1,0.2]],[[10,0.4],[0.8,1]],[[10,10],[true,false]])"));
        eval("0", Universal.evaluate("MINA()"));
        eval("0", Universal.evaluate("MINA([0.1,0.2],[0.4,0.8],[true,false])"));
        eval("0", Universal.evaluate("MINA([10,0],[0.1,0.2]],[[10,0.4],[0.8,1]],[[10,10],[true,false]])"));
        eval("0.3135141", Universal.evaluate("NEGBINOM.DIST(10,5,0.25,true)"));
        eval("0.9087888", Universal.evaluate("NORM.DIST(42,40,1.5,true)"));
        eval("0.10934", Universal.evaluate("NORM.DIST(42,40,1.5,false)"));
        eval("42.000002", Universal.evaluate("NORM.INV(0.908789,40,1.5)"));
        eval("0.908788726", Universal.evaluate("NORM.S.DIST(1.333333,TRUE)"));
        eval("0.164010148", Universal.evaluate("NORM.S.DIST(1.333333,FALSE)"));
        eval("1.3333347", Universal.evaluate("NORM.S.INV(0.908789)"));
        eval("0.699379", Universal.evaluate("PEARSON([9,7,5,3,1],[10,6,1,5,3])"));
        eval("2.5", Universal.evaluate("PERCENTILE.EXC([1,2,3,6,6,6,7,8,9],0.25)"));
        eval("1.9", Universal.evaluate("PERCENTILE.INC([1,2,3,4],0.3)"));
        eval("0.7", Universal.evaluate("PERCENTRANK.EXC([1,2,3,6,6,6,7,8,9],7)"));
        eval("0.333", Universal.evaluate("PERCENTRANK.INC([13,12,11,8,4,3,2,1,1,1],2)"));
        eval("0.555", Universal.evaluate("PERCENTRANK.INC([13,12,11,8,4,3,2,1,1,1],4)"));
        eval("0.666", Universal.evaluate("PERCENTRANK.INC([13,12,11,8,4,3,2,1,1,1],8)"));
        eval("970200", Universal.evaluate("PERMUT(100,3)"));
        eval("6", Universal.evaluate("PERMUT(3,2)"));
        eval("9", Universal.evaluate("PERMUTATIONA(3,2)"));
        eval("4", Universal.evaluate("PERMUTATIONA(2,2)"));
        eval("0.30113743215480443", Universal.evaluate("PHI(0.75)"));
        eval("0.12465201948308113", Universal.evaluate("POISSON.DIST(2,5,true)"));
        eval("0.08422433748856833", Universal.evaluate("POISSON.DIST(2,5,false)"));
        eval("0.1", Universal.evaluate("PROB([0,1,2,3],[0.2,0.3,0.1,0.4],2)"));
        eval("0.8", Universal.evaluate("PROB([0,1,2,3],[0.2,0.3,0.1,0.4],1,3)"));
        eval("0", Universal.evaluate("PROB([0,1,2,3],[0.2,0.3,0.1,0.4])"));
        eval("40", Universal.evaluate("QUARTILE.EXC([6,7,15,36,39,40,41,42,43,47,49],2)"));
        eval("7.5", Universal.evaluate("QUARTILE.INC([1,2,4,7,8,9,10,12],2)"));
        eval("9.25", Universal.evaluate("QUARTILE.INC([1,2,4,7,8,9,10,12],3)"));
        eval("0.05795019157088122", Universal.evaluate("RSQ([2,3,9,1,8,7,5],[6,5,11,7,5,4,4])"));
        eval("0.3595430714067974", Universal.evaluate("SKEW([3,4,5,2,3,4,5,6,4,7])"));
        eval("0.3055555555555556", Universal.evaluate("SLOPE([2,3,9,1,8,7,5],[6,5,11,7,5,4,4])"));
        eval("4", Universal.evaluate("SMALL([3,4,5,2,3,4,6,4,7],4)"));
        eval("1.3333333333333333", Universal.evaluate("STANDARDIZE(42,40,1.5)"));
        eval("0", Universal.evaluate("STANDARDIZE(10,10,10)"));
        eval("26.054558142482477", Universal.evaluate("STDEV.P([1345,1301,1368,1322,1310,1370,1318,1350,1303,1299])"));
        eval("27.46391571984349", Universal.evaluate("STDEVA([1345,1301,1368,1322,1310,1370,1318,1350,1303,1299])"));
        eval("26.054558142482477", Universal.evaluate("STDEVPA([1345,1301,1368,1322,1310,1370,1318,1350,1303,1299])"));
        eval("0.9946953263673741", Universal.evaluate("T.DIST(60,1,true)"));
        eval("0.0007369065188787021", Universal.evaluate("T.DIST(8,3,false)"));
        eval("0.092426312", Universal.evaluate("T.DIST.2T(2,6)"));
        eval("0.002490664", Universal.evaluate("T.DIST.2T(20,2)"));
        eval("0.025016522", Universal.evaluate("T.DIST.RT(2,60)"));
        eval("0.046213156", Universal.evaluate("T.DIST.RT(2,6)"));
        eval("1.2958210933417948", Universal.evaluate("T.INV(0.9,60)"));
        eval("0.126194364", Universal.evaluate("T.INV.2T(0.9,60)"));
        eval("3.7777777777777777", Universal.evaluate("TRIMMEAN([4,5,6,7,2,3,4,5,1,2,3],0.2)"));
        eval("13.333333333333334", Universal.evaluate("VAR.P(1,2,3,4,10,10)"));
        eval("678.84", Universal.evaluate("VAR.P(1345,1301,1368,1322,1310,1370,1318,1350,1303,1299)"));
        eval("754.26666666", Universal.evaluate("VAR.S(1345,1301,1368,1322,1310,1370,1318,1350,1303,1299)"));
        eval("754.26667", Universal.evaluate("VARA(1345,1301,1368,1322,1310,1370,1318,1350,1303,1299)"));
        eval("678.84", Universal.evaluate("VARPA(1345,1301,1368,1322,1310,1370,1318,1350,1303,1299)"));
        eval("0.9295813900692769", Universal.evaluate("WEIBULL.DIST(105,20,100,true)"));
        eval("0.03558886402450435", Universal.evaluate("WEIBULL.DIST(105,20,100,false)"));
        eval("0.09057419685136381", Universal.evaluate("Z.TEST([3,6,7,8,6,5,4,2,1,9],4)"));
        eval("0.863043", Universal.evaluate("Z.TEST([3,6,7,8,6,5,4,2,1,9],6)"));
        eval("0.196016", Universal.evaluate("T.TEST([3,4,5,8,9,1,2,4,5],[6,19,3,2,14,4,5,17,1],2,1)"));
    }

    @Ignore
    @Test
    public void testStatisticalCompatibilityExtended() throws InvocationTargetException, CompileException {
        eval("4", Universal.evaluate("RANK.AVG(94,[89,88,92,101,94,97,95])"));
        eval("1", Universal.evaluate("RANK.AVG(88,[89,88,92,101,94,97,95],1)"));
        eval("4", Universal.evaluate("RANK.EQ(2,[7,3.5,3.5,1,2])"));
        eval("5", Universal.evaluate("RANK.EQ(7,[7,3.5,3.5,1,2],1)"));
        eval("3", Universal.evaluate("RANK.EQ(3.5,[7,3.5,3.5,1,2],1)"));
        eval("[1,2]", Universal.evaluate("ROW([[1,2],[2,3],[2,4]],0)"));
        eval("[2,4]", Universal.evaluate("ROW([[1,2],[2,3],[2,4]],2)"));
        eval("0", Universal.evaluate("ROWS([])"));
        eval("3", Universal.evaluate("ROWS([[1,2],[2,3],[2,4]])"));
        eval("1", Universal.evaluate("ROWS([[1,2]])"));
        eval("2", Universal.evaluate("ROWS([1,2])"));
        eval("0.303193339354144", Universal.evaluate("SKEW.P([3,4,5,2,3,4,5,6,4,7])"));
        eval("15", Universal.evaluate("QUARTILE.EXC([6,7,15,36,39,40,41,42,43,47,49],1)"));
        eval("40", Universal.evaluate("QUARTILE.EXC([6,7,15,36,39,40,41,42,43,47,49],2)"));
        eval("43", Universal.evaluate("QUARTILE.EXC([6,7,15,36,39,40,41,42,43,47,49],3)"));
        eval("3.5", Universal.evaluate("QUARTILE.INC([1,2,4,7,8,9,10,12],1)"));
        eval("7.5", Universal.evaluate("QUARTILE.INC([1,2,4,7,8,9,10,12],2)"));
        eval("9.25", Universal.evaluate("QUARTILE.INC([1,2,4,7,8,9,10,12],3)"));
        eval("0.0550487", Universal.evaluate("NEGBINOM.DIST(10,5,0.25,false)"));
        eval("10.607253086419755", Universal.evaluate("FORECAST(30,[6,7,9,15,21],[20,28,31,38,40])"));
        eval("0.04838709677419217", Universal.evaluate("INTERCEPT([2,3,9,1,8],[6,5,11,7,5])"));
        eval("1", Universal.evaluate("COUNTIF([1,3,'a',''],'>1')"));
        eval("0", Universal.evaluate("COUNTIF([1,'c','a',''],'>1')"));
        eval("2", Universal.evaluate("COUNTIF([[1,3],['a',4,'c']],'>1')"));
        eval("2", Universal.evaluate("COUNTIF([[1,'a'],['a',4,'c']],'a')"));
        eval("1", Universal.evaluate("COUNTIFS([1,3,'a',''],'>1')"));
        eval("0", Universal.evaluate("COUNTIFS([1,'c','a',''],'>1')"));
        eval("2", Universal.evaluate("COUNTIFS([[1,3],['a',4,'c']],'>1')"));
        eval("2", Universal.evaluate("COUNTIFS([[1,'a'],['a',4,'c']],'a')"));
        eval("1", Universal.evaluate("COUNTIFS([1],'1',[2],'2')"));
        eval("0", Universal.evaluate("COUNTIFS([1],'1',[2],'2')"));
        eval("1", Universal.evaluate("COUNTIFS([[1]],'1',[[2],[1]],'2')"));
        eval("0.6929519121748391", Universal.evaluate("CONFIDENCE.NORM(0.05,2.5,50)"));
        eval("0.28419685015290463", Universal.evaluate("CONFIDENCE.T(0.05,1,50)"));
        eval("12", Universal.evaluate("AVERAGEIFS([2,4,8,16],[1,2,3,4],'>2')"));
        eval("12", Universal.evaluate("AVERAGEIFS([2,4,8,16],[1,2,3,4],'>2',[1,2,3,4],'>2')"));
        eval("0", Universal.evaluate("AVERAGEIFS([2,4,8,16],[1,2,3,4],'>2',[1,1,1,1],'>2')"));
        eval("3", Universal.evaluate("MODE.MULT([1,2,3,4,3,2,1,2,3,5,6,1]).length"));
        eval("1", Universal.evaluate("MODE.MULT([1,2,3,4,3,2,1,2,3,5,6,1])"));
        eval("1", Universal.evaluate("MODE.MULT([1,2,3,4,3,2,1,2,3,5,6,1])"));
        eval("2", Universal.evaluate("MODE.MULT([1,2,3,4,3,2,1,2,3,5,6,1])"));
        eval("3", Universal.evaluate("MODE.SNGL([5.6,4,4,3,2,4])"));
        eval("3.305718950210041", Universal.evaluate("STEYX([2,3,9,1,8,7,5],[6,5,11,7,5,4,4])"));
        eval("0.381", Universal.evaluate("PERCENTRANK.EXC([1,2,3,6,6,6,7,8,9],10,5.43)"));
        eval("0.3", Universal.evaluate("PERCENTRANK.EXC([1,2,3,6,6,6,7,8,9],5.43,1)"));
        eval("0.583", Universal.evaluate("PERCENTRANK.INC([13,12,11,8,4,3,2,1,1,1],5)"));
        eval("15", Universal.evaluate("QUARTILE.EXC([6,7,15,36,39,40,41,42,43,47,49],1)"));
        eval("40", Universal.evaluate("QUARTILE.EXC([6,7,15,36,39,40,41,42,43,47,49],2)"));
        eval("43", Universal.evaluate("QUARTILE.EXC([6,7,15,36,39,40,41,42,43,47,49],3)"));
        eval("3.5", Universal.evaluate("QUARTILE.INC([1,2,4,7,8,9,10,12],1)"));
        eval("7.5", Universal.evaluate("QUARTILE.INC([1,2,4,7,8,9,10,12],2)"));
        eval("9.25", Universal.evaluate("QUARTILE.INC([1,2,4,7,8,9,10,12],3)"));
        eval("1.282", Universal.evaluate("F.TEST([1,3,5,7,9],[5,9,3,8,3])"));
        eval("0.64831785", Universal.evaluate("F.TEST([6,7,9,15,21],[20,28,31,38,40])"));
        eval("0.6854706", Universal.evaluate("BETA.DIST(2,8,10,true,1,3)"));
        eval("1.4837646", Universal.evaluate("BETA.DIST(2,8,10,false,1,3)"));
        eval("0.6854705810117458", Universal.evaluate("BETA.DIST(2,8,10,true,1,3)"));
        eval("0.000308", Universal.evaluate("CHISQ.TEST([[58,35],[11,25],[10,23]],[[45.35,47.65],[17.56,18.44],[16.09,16.91]])"));
        eval("[[1],[2],[2]]", Universal.evaluate("COLUMN([[1,2],[2,3],[2,4]],0)"));
        eval("[[2],[3],[4]]", Universal.evaluate("COLUMN([[1,2],[2,3],[2,4]],1)"));
        eval("0", Universal.evaluate("COLUMNS([])"));
        eval("2", Universal.evaluate("COLUMNS([[1,2],[2,3],[2,4]])"));
        eval("2", Universal.evaluate("COLUMNS([[1,2]])"));
        eval("1", Universal.evaluate("COLUMNS([1,2])"));
        eval("4", Universal.evaluate("COUNT([[1,2],[3,2],[null,null]])"));
        eval("2", Universal.evaluate("COUNT([[1,2],['a','b'],[null,null]])"));
        eval("4", Universal.evaluate("COUNTA(1,null,3,'a','','c')"));
        eval("4", Universal.evaluate("COUNTA([1,null,3,'a','','c'])"));
        eval("4", Universal.evaluate("COUNTA([1,null,3],['a','','c'])"));
        eval("4", Universal.evaluate("COUNTA([[1,null,3],['a','','c']])"));
        eval("2", Universal.evaluate("COUNTBLANK(1,null,3,'a','','c')"));
        eval("2", Universal.evaluate("COUNTBLANK([1,null,3,'a','','c'])"));
        eval("2", Universal.evaluate("COUNTBLANK([1,null,3],['a','','c'])"));
        eval("2", Universal.evaluate("COUNTBLANK([[1,null,3],['a','','c']])"));
        eval("1", Universal.evaluate("COUNTIF([1,null,3,'a',''],'>1')"));
        eval("0", Universal.evaluate("COUNTIF([1,null,'c','a',''],'>1')"));
        eval("2", Universal.evaluate("COUNTIF([[1,null,3],['a',4,'c']],'>1')"));
        eval("2", Universal.evaluate("COUNTIF([[1,null,'a'],['a',4,'c']],'a')"));
        eval("1", Universal.evaluate("COUNTIFS([1,null,3,'a',''],'>1')"));
        eval("0", Universal.evaluate("COUNTIFS([1,null,'c','a',''],'>1')"));
        eval("2", Universal.evaluate("COUNTIFS([[1,null,3],['a',4,'c']],'>1')"));
        eval("2", Universal.evaluate("COUNTIFS([[1,null,'a'],['a',4,'c']],'a')"));
        eval("1", Universal.evaluate("COUNTIFS([1,null],'1',[2,null],'2')"));
        eval("0", Universal.evaluate("COUNTIFS([1,null],'1',[null,2],'2')"));
        eval("1", Universal.evaluate("COUNTIFS([[1],[null]],'1',[[2],[1]],'2')"));
        eval("0", Universal.evaluate("COUNTUNIQUE()"));
        eval("3", Universal.evaluate("COUNTUNIQUE(1,1,2,2,3,3)"));
        eval("3", Universal.evaluate("COUNTUNIQUE([1,1,2,2,3,3])"));
        eval("3", Universal.evaluate("COUNTUNIQUE([1,1,2],[2,3,3])"));
        eval("5", Universal.evaluate("COUNTUNIQUE([[1,1],[2,5]],[[2,3],[3,4]])"));
        eval("2", Universal.evaluate("COUNTIN([1,1,2,2,2],1)"));
        eval("3", Universal.evaluate("COUNTIN([1,1,2,2,2],2)"));
        eval("3", Universal.evaluate("COUNTIN([[1],1,2,[2],2],2)"));
        eval("3", Universal.evaluate("SLOPE([DATE(1900,1,2),DATE(1900,1,3),DATE(1900,1,9),DATE(1900,1,1),DATE(1900,1,8),DATE(1900,1,7),DATE(1900,1,5)],[6,5,11,7,5,4,4])"));
        eval("27.46391571984349", Universal.evaluate("STDEV.S([1345,1301,1368,1322,1310,1370,1318,1350,1303,1299,true,false,'nope'])"));
        eval("???TODO???", Universal.evaluate("STDEVA([2,1,true,false,'nope'])"));
        eval("???TODO???", Universal.evaluate("STDEVPA([2,1,true,false,'nope'])"));
    }

    @Test
    public void testFinancialCompatibility() throws InvocationTargetException, CompileException {
        eval("16.666667", Universal.evaluate("ACCRINT(39508,39691,39569,0.1,1000,2,0)"));
        eval("15.555556", Universal.evaluate("ACCRINT(DATE(2008,3,5),39691,39569,0.1,1000,2,0, false)"));
        eval("7.2222222", Universal.evaluate("ACCRINT(DATE(2008, 4, 5),39691,39569,0.1,1000,2,0, true)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2012,3,30),DATE(2013,12,4),0.1,1000,1,0,true)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2012,3,30),DATE(2013,12,4),0.1,1000,1,0,true)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2012,3,30),DATE(2013,12,4),0.1,1000,2,0,true)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2012,3,30),DATE(2013,12,4),0.1,1000,4,0,true)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2012,3,30),DATE(2013,12,4),0.1,1000,1,4,true)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2012,3,30),DATE(2013,12,4),0.1,1000,2,4,true)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2012,3,30),DATE(2013,12,4),0.1,1000,4,4,true)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2012,3,30),DATE(2013,12,4),0.1,1000,1,0,false)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2012,3,30),DATE(2013,12,4),0.1,1000,2,0,false)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2012,3,30),DATE(2013,12,4),0.1,1000,4,0,false)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2012,3,30),DATE(2013,12,4),0.1,1000,1,4,false)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2012,3,30),DATE(2013,12,4),0.1,1000,2,4,false)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2012,3,30),DATE(2013,12,4),0.1,1000,4,4,false)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,1,0,true)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,2,0,true)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,4,0,true)"));
        eval("15.573770491803279", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,1,1,true)"));
        eval("15.573770491803279", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,2,1,true)"));
        eval("15.573770491803279", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,4,1,true)"));
        eval("15.833333333333332", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,1,2,true)"));
        eval("15.833333333333332", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,2,2,true)"));
        eval("15.833333333333332", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,4,2,true)"));
        eval("15.616438356164384", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,1,3,true)"));
        eval("15.616438356164384", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,2,3,true)"));
        eval("15.616438356164384", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,4,3,true)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,1,4,true)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,2,4,true)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,4,4,true)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,1,0,false)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,2,0,false)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,4,0,false)"));
        eval("15.573770491803279", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,1,1,false)"));
        eval("15.573770491803279", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,2,1,false)"));
        eval("15.573770491803279", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,4,1,false)"));
        eval("15.833333333333332", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,1,2,false)"));
        eval("15.833333333333332", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,2,2,false)"));
        eval("15.833333333333332", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,4,2,false)"));
        eval("15.616438356164384", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,1,3,false)"));
        eval("15.616438356164384", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,2,3,false)"));
        eval("15.616438356164384", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,4,3,false)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,1,4,false)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,2,4,false)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT(DATE(2012,2,2),DATE(2013,12,4),DATE(2012,3,30),0.1,1000,4,4,false)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT('2/2/2012','3/30/2012','12/4/2013',0.1,1000,1,0,true)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT('2/2/2012','3/30/2012','12/4/2013',0.1,1000,1,0,true)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT('2/2/2012','3/30/2012','12/4/2013',0.1,1000,2,0,true)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT('2/2/2012','3/30/2012','12/4/2013',0.1,1000,4,0,true)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT('2/2/2012','3/30/2012','12/4/2013',0.1,1000,1,4,true)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT('2/2/2012','3/30/2012','12/4/2013',0.1,1000,2,4,true)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT('2/2/2012','3/30/2012','12/4/2013',0.1,1000,4,4,true)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT('2/2/2012','3/30/2012','12/4/2013',0.1,1000,1,0,false)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT('2/2/2012','3/30/2012','12/4/2013',0.1,1000,2,0,false)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT('2/2/2012','3/30/2012','12/4/2013',0.1,1000,4,0,false)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT('2/2/2012','3/30/2012','12/4/2013',0.1,1000,1,4,false)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT('2/2/2012','3/30/2012','12/4/2013',0.1,1000,2,4,false)"));
        eval("183.88888888888889", Universal.evaluate("ACCRINT('2/2/2012','3/30/2012','12/4/2013',0.1,1000,4,4,false)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,1,0,true)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,2,0,true)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,4,0,true)"));
        eval("15.573770491803279", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,1,1,true)"));
        eval("15.573770491803279", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,2,1,true)"));
        eval("15.573770491803279", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,4,1,true)"));
        eval("15.833333333333332", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,1,2,true)"));
        eval("15.833333333333332", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,2,2,true)"));
        eval("15.833333333333332", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,4,2,true)"));
        eval("15.616438356164384", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,1,3,true)"));
        eval("15.616438356164384", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,2,3,true)"));
        eval("15.616438356164384", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,4,3,true)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,1,4,true)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,2,4,true)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,4,4,true)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,1,0,false)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,2,0,false)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,4,0,false)"));
        eval("15.573770491803279", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,1,1,false)"));
        eval("15.573770491803279", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,2,1,false)"));
        eval("15.573770491803279", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,4,1,false)"));
        eval("15.833333333333332", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,1,2,false)"));
        eval("15.833333333333332", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,2,2,false)"));
        eval("15.833333333333332", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,4,2,false)"));
        eval("15.616438356164384", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,1,3,false)"));
        eval("15.616438356164384", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,2,3,false)"));
        eval("15.616438356164384", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,4,3,false)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,1,4,false)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,2,4,false)"));
        eval("16.11111111111111", Universal.evaluate("ACCRINT('2/2/2012','12/4/2013','3/30/2012',0.1,1000,4,4,false)"));
        eval("2581.40337", Universal.evaluate("FV(0.06/12,10,-200,-500,1)"));
        eval("12682.50301", Universal.evaluate("FV(0.12/12,12,-1000)"));
        eval("2301.40183", Universal.evaluate("FV(0.06/12,12,-100,-1000,1)"));
        eval("82846.24637", Universal.evaluate("FV(0.11/12,35,-2000,0,1)"));
        eval("1037.03209", Universal.evaluate("ABS(PMT(0.08/12, 10, 10000,0,0))"));
        eval("1037.03209", Universal.evaluate("ABS(PMT(0.08/12, 10, 10000,0))"));
        eval("1037.03209", Universal.evaluate("ABS(PMT(0.08/12, 10, 10000))"));
        eval("1030.16433", Universal.evaluate("ABS(PMT(0.08/12, 10, 10000, 0, 1))"));
        eval("129.08116", Universal.evaluate("ABS(PMT(0.06/12, 18*12, 0, 50000))"));
        eval("129.08116", Universal.evaluate("ABS(PMT(0.06/12, 18*12, 0, 50000, 0))"));
        eval("-11135.23213", Universal.evaluate("CUMIPMT(0.09/12,30*12,125000,13,24,0)"));
        eval("-937.5", Universal.evaluate("CUMIPMT(0.09/12,30*12,125000,1,1,0)"));
        eval("-9916.77251395708", Universal.evaluate("CUMIPMT(0.1/12,30*12,100000,13,24,0)"));
        eval("-9834.815716321069", Universal.evaluate("CUMIPMT(0.1/12,30*12,100000,13,24,1)"));
        eval("-19891.752778759568", Universal.evaluate("CUMIPMT(0.1/12,30*12,100000,1,24,0)"));
        eval("-614.0863271085149", Universal.evaluate("CUMPRINC(0.1/12,30*12,100000,13,24,0)"));
        eval("-609.0112334960476", Universal.evaluate("CUMPRINC(0.1/12,30*12,100000,13,24,1)"));
        eval("186083.33333", Universal.evaluate("DB(1000000,100000,6,1,7)"));
        eval("259639.41667", Universal.evaluate("DB(1000000,100000,6,2,7)"));
        eval("176814.44275", Universal.evaluate("DB(1000000,100000,6,3,7)"));
        eval("120410.63551", Universal.evaluate("DB(1000000,100000,6,4,7)"));
        eval("81999.64278", Universal.evaluate("DB(1000000,100000,6,5,7)"));
        eval("55841.75674", Universal.evaluate("DB(1000000,100000,6,6,7)"));
        eval("319000", Universal.evaluate("DB(1000000,100000,6,1)"));
        eval("217239", Universal.evaluate("DB(1000000,100000,6,2)"));
        eval("147939.759", Universal.evaluate("DB(1000000,100000,6,3)"));
        eval("100746.97587900002", Universal.evaluate("DB(1000000,100000,6,4)"));
        eval("68608.690573599", Universal.evaluate("DB(1000000,100000,6,5)"));
        eval("46722.518280620934", Universal.evaluate("DB(1000000,100000,6,6)"));
        eval("159500", Universal.evaluate("DB(1000000,100000,6,1,6)"));
        eval("268119.5", Universal.evaluate("DB(1000000,100000,6,2,6)"));
        eval("182589.3795", Universal.evaluate("DB(1000000,100000,6,3,6)"));
        eval("124343.36743949998", Universal.evaluate("DB(1000000,100000,6,4,6)"));
        eval("84677.83322629951", Universal.evaluate("DB(1000000,100000,6,5,6)"));
        eval("57665.60442710997", Universal.evaluate("DB(1000000,100000,6,6,6)"));
        eval("239250", Universal.evaluate("DB(1000000,100000,6,1,9)"));
        eval("242679.25", Universal.evaluate("DB(1000000,100000,6,2,9)"));
        eval("165264.56925", Universal.evaluate("DB(1000000,100000,6,3,9)"));
        eval("112545.17165925002", Universal.evaluate("DB(1000000,100000,6,4,9)"));
        eval("76643.26189994926", Universal.evaluate("DB(1000000,100000,6,5,9)"));
        eval("52194.061353865436", Universal.evaluate("DB(1000000,100000,6,6,9)"));
        eval("0", Universal.evaluate("DB(1000000,1000000,6,1,6)"));
        eval("0", Universal.evaluate("DB(100000,1000000,6,1,6)"));
        eval("1.31507", Universal.evaluate("DDB(2400,300,10*365,1)"));
        eval("1.31507", Universal.evaluate("DDB(2400,300,10*365,1,2)"));
        eval("40.00", Universal.evaluate("DDB(2400,300,10*12,1,2)"));
        eval("40.00", Universal.evaluate("DDB(2400,300,10*12,1)"));
        eval("480.00", Universal.evaluate("DDB(2400,300,10,1,2)"));
        eval("480.00", Universal.evaluate("DDB(2400,300,10,1)"));
        eval("306.00", Universal.evaluate("DDB(2400,300,10,2,1.5)"));
        eval("333333.3333333333", Universal.evaluate("DDB(1000000,100000,6,1)"));
        eval("222222.22222222225", Universal.evaluate("DDB(1000000,100000,6,2)"));
        eval("148148.14814814815", Universal.evaluate("DDB(1000000,100000,6,3)"));
        eval("98765.43209876546", Universal.evaluate("DDB(1000000,100000,6,4)"));
        eval("250000", Universal.evaluate("DDB(1000000,100000,6,1,1.5)"));
        eval("187500", Universal.evaluate("DDB(1000000,100000,6,2,1.5)"));
        eval("140625", Universal.evaluate("DDB(1000000,100000,6,3,1.5)"));
        eval("105468.75", Universal.evaluate("DDB(1000000,100000,6,4,1.5)"));
        eval("79101.5625", Universal.evaluate("DDB(1000000,100000,6,5,1.5)"));
        eval("59326.171875", Universal.evaluate("DDB(1000000,100000,6,6,1.5)"));
        eval("0", Universal.evaluate("DDB(1000000,1000000,6,1,1.5)"));
        eval("0", Universal.evaluate("DDB(100000,1000000,6,1,1.5)"));
        eval("1.125", Universal.evaluate("DOLLARDE(1.02,16)"));
        eval("1.3125", Universal.evaluate("DOLLARDE(1.1,32)"));
        eval("1.1", Universal.evaluate("DOLLARDE(1.1,1)"));
        eval("1.5", Universal.evaluate("DOLLARDE(1.1,2)"));
        eval("1.25", Universal.evaluate("DOLLARDE(1.1,4)"));
        eval("1.125", Universal.evaluate("DOLLARDE(1.1,8)"));
        eval("1.625", Universal.evaluate("DOLLARDE(1.1,16)"));
        eval("1.3125", Universal.evaluate("DOLLARDE(1.1,32)"));
        eval("-1.1", Universal.evaluate("DOLLARDE(-1.1,1)"));
        eval("-1.5", Universal.evaluate("DOLLARDE(-1.1,2)"));
        eval("-1.25", Universal.evaluate("DOLLARDE(-1.1,4)"));
        eval("-1.125", Universal.evaluate("DOLLARDE(-1.1,8)"));
        eval("-1.625", Universal.evaluate("DOLLARDE(-1.1,16)"));
        eval("-1.3125", Universal.evaluate("DOLLARDE(-1.1,32)"));
        eval("1.02", Universal.evaluate("DOLLARFR(1.125,16)"));
        eval("1.04", Universal.evaluate("DOLLARFR(1.125,32)"));
        eval("1.1", Universal.evaluate("DOLLARFR(1.1,1)"));
        eval("1.1", Universal.evaluate("DOLLARFR(1.5,2)"));
        eval("1.1", Universal.evaluate("DOLLARFR(1.25,4)"));
        eval("1.1", Universal.evaluate("DOLLARFR(1.125,8)"));
        eval("1.1", Universal.evaluate("DOLLARFR(1.625,16)"));
        eval("1.1", Universal.evaluate("DOLLARFR(1.3125,32)"));
        eval("-1.1", Universal.evaluate("DOLLARFR(-1.1,1)"));
        eval("-1.1", Universal.evaluate("DOLLARFR(-1.5,2)"));
        eval("-1.1", Universal.evaluate("DOLLARFR(-1.25,4)"));
        eval("-1.1", Universal.evaluate("DOLLARFR(-1.125,8)"));
        eval("-1.1", Universal.evaluate("DOLLARFR(-1.625,16)"));
        eval("-1.1", Universal.evaluate("DOLLARFR(-1.3125,32)"));
        eval("0.0535427", Universal.evaluate("EFFECT(0.0525,4)"));
        eval("0.10381289062499977", Universal.evaluate("EFFECT(0.1,4)"));
        eval("0.10381289062499977", Universal.evaluate("EFFECT(0.1,4.5)"));
        eval("2581.4033740601185", Universal.evaluate("FV(0.06/12,10,-200,-500,1)"));
        eval("12682.503013196976", Universal.evaluate("FV(0.12/12,12,-1000)"));
        eval("2301.4018303408993", Universal.evaluate("FV(0.06/12,12,-100,-1000,1)"));
        eval("2200", Universal.evaluate("FV(0,12,-100,-1000,1)"));
        eval("1.33089", Universal.evaluate("FVSCHEDULE(1,{0.09,0.11,0.1})"));
        eval("133.08900000000003", Universal.evaluate("FVSCHEDULE(100,[0.09,0.1,0.11])"));
        eval("66.66667", Universal.evaluate("ABS(IPMT(0.1/12,1,3*12,8000))"));
        eval("292.44713", Universal.evaluate("ABS(IPMT(0.1,3,3,8000))"));
        eval("928.8235718400465", Universal.evaluate("IPMT(0.1/12,6,2*12,100000,1000000,0)"));
        eval("921.1473439736042", Universal.evaluate("IPMT(0.1/12,6,2*12,100000,1000000,1)"));
        eval("0", Universal.evaluate("IPMT(0.1/12,1,2*12,100000,1000000,1)"));
        eval("-833.3333333333334", Universal.evaluate("IPMT(0.1/12,1,2*12,100000,1000000,0)"));
        eval("-64814.81481", Universal.evaluate("ISPMT(0.1/12,1,3*12,8000000)"));
        eval("-533333.33333", Universal.evaluate("ISPMT(0.1,1,3,8000000)"));
        eval("-625", Universal.evaluate("ISPMT(0.1/12,6,2*12,100000)"));
        eval("1188.44341", Universal.evaluate("NPV(0.1,-10000,3000,4200,6800)"));
        eval("1922.06155", Universal.evaluate("NPV(0.08,[8000,9200,10000,12000,14500]) + -40000"));
        eval("3749.46509", Universal.evaluate("ABS(NPV(0.08,[8000,9200,10000,12000,14500], -9000) + -40000)"));
        eval("0.12609", Universal.evaluate("MIRR([-120000,39000,30000,21000,37000,46000],0.1,0.12)"));
        eval("-0.04804", Universal.evaluate("MIRR([-120000,39000,30000,21000],0.1,0.12)"));
        eval("0.13476", Universal.evaluate("MIRR([-120000,39000,30000,21000,37000,46000],0.1,0.14)"));
        eval("0.07971710360838036", Universal.evaluate("MIRR([-75000,12000,15000,18000,21000,24000],0.1,0.12)"));
        eval("0.05250032", Universal.evaluate("NOMINAL(0.053543,4)"));
        eval("0.09645475633778045", Universal.evaluate("NOMINAL(0.1,4)"));
        eval("0.09645475633778045", Universal.evaluate("NOMINAL(0.1,4.5)"));
        eval("59.6738657", Universal.evaluate("NPER(0.12/12,-100,-1000,10000,1)"));
        eval("60.0821229", Universal.evaluate("NPER(0.12/12,-100,-1000,10000)"));
        eval("-9.57859404", Universal.evaluate("NPER(0.12/12,-100,-1000)"));
        eval("63.39385422740764", Universal.evaluate("NPER(0.1/12,-100,-1000,10000,0)"));
        eval("63.016966422019685", Universal.evaluate("NPER(0.1/12,-100,-1000,10000,1)"));
        eval("63.39385422740764", Universal.evaluate("NPER(0.1/12,-100,-1000,10000)"));
        eval("-9.645090919837394", Universal.evaluate("NPER(0.1/12,-100,-1000)"));
        eval("1031.3503176012546", Universal.evaluate("NPV(0.1,-10000,2000,4000,8000)"));
        eval("1031.3503176012546", Universal.evaluate("NPV(0.1,[-10000,2000,4000,8000])"));
        eval("-68181.81818181818", Universal.evaluate("NPV(0.1,[-75000])"));
        eval("62448.362521940246", Universal.evaluate("NPV(0.12,[12000,15000,18000,21000,24000])"));
        eval("3.85987", Universal.evaluate("PDURATION(0.025,2000,2200)"));
        eval("87.60548", Universal.evaluate("PDURATION(0.025/12,1000,1200)"));
        eval("7.272540897341714", Universal.evaluate("PDURATION(0.1,1000,2000)"));
        eval("-129.0811608679973", Universal.evaluate("PMT(0.06/12,18*12,0,50000)"));
        eval("-42075.45683100995", Universal.evaluate("PMT(0.1/12,2*12,100000,1000000,1)"));
        eval("-42426.08563793503", Universal.evaluate("PMT(0.1/12,2*12,100000,1000000)"));
        eval("-37811.59300418336", Universal.evaluate("PMT(0.1/12,2*12,0,1000000)"));
        eval("-4614.49263375167", Universal.evaluate("PMT(0.1/12,2*12,100000)"));
        eval("-4166.666666666667", Universal.evaluate("PMT(0,2*12,100000)"));
        eval("-75.62319", Universal.evaluate("PPMT(0.1/12,1,2*12,2000)"));
        eval("-27598.05346", Universal.evaluate("PPMT(0.08,10, 10,200000)"));
        eval("-75.62318600836673", Universal.evaluate("PPMT(0.1/12,1,2*12,2000)"));
        eval("-27598.05346242135", Universal.evaluate("PPMT(0.08,10,10,200000)"));
        eval("-43354.909209775076", Universal.evaluate("PPMT(0.1/12,6,2*12,100000,1000000,0)"));
        eval("-42996.60417498356", Universal.evaluate("PPMT(0.1/12,6,2*12,100000,1000000,1)"));
        eval("-3941.355382706826", Universal.evaluate("PPMT(0.1/12,6,2*12,100000)"));
        eval("-59777145.85119", Universal.evaluate("PV(0.08/12,12*20,500000,0,0)"));
        eval("-59777145.85119", Universal.evaluate("PV(0.08/12,12*20,500000,0)"));
        eval("-59777145.85119", Universal.evaluate("PV(0.08/12,12*20,500000)"));
        eval("-29864.950264779152", Universal.evaluate("PV(0.1/12,2*12,1000,10000,0)"));
        eval("-30045.54072173169", Universal.evaluate("PV(0.1/12,2*12,1000,10000,1)"));
        eval("0.00770", Universal.evaluate("RATE(4*12,-200,8000)"));
        eval("0.09243", Universal.evaluate("RATE(4*12,-200,8000)*12"));
        eval("0.06517891177181546", Universal.evaluate("RATE(2*12,-1000,-10000,100000)"));
        eval("0.06517891177181533", Universal.evaluate("RATE(2*12,-1000,-10000,100000,0,0.1)"));
        eval("0.0651789117718154", Universal.evaluate("RATE(2*12,-1000,-10000,100000,0,0.75)"));
        eval("0.06517891177181524", Universal.evaluate("RATE(2*12,-1000,-10000,100000,0,0.065)"));
        eval("0.0632395800018064", Universal.evaluate("RATE(2*12,-1000,-10000,100000,1,0.1)"));
        eval("0.0009933", Universal.evaluate("RRI(96,10000,11000)"));
        eval("0.011985024140399592", Universal.evaluate("RRI(8,10000,11000)"));
        eval("2250", Universal.evaluate("SLN(30000,7500,10)"));
        eval("4090.90909", Universal.evaluate("SYD(30000,7500,10,1)"));
        eval("409.09091", Universal.evaluate("SYD(30000,7500,10,10)"));
        eval("4.181818181818182", Universal.evaluate("SYD(30,7,10,1)"));
        eval("0.09415", Universal.evaluate("TBILLEQ('03/31/2008','06/01/2008',0.0914)"));
        eval("98.45", Universal.evaluate("TBILLPRICE('03/31/2008','06/01/2008',0.09)"));
        eval("0.09142", Universal.evaluate("TBILLYIELD('03/31/2008','06/01/2008',98.45)"));
        eval("0.373362535", Universal.evaluate("XIRR([-10000,2750,4250,3250,2750],['01/01/2008','03/01/2008','10/30/2008','02/15/2009','04/01/2009'],0.1)"));
        eval("2086.64760", Universal.evaluate("XNPV(0.09,[-10000,2750,4250,3250,2750],['01/01/2008','03/01/2008','10/30/2008','02/15/2009','04/01/2009'])"));
        eval("-0.0212448482734", Universal.evaluate("IRR([-70000,12000,15000,18000,21000])"));
        eval("0.0866309480365", Universal.evaluate("IRR([-70000,12000,15000,18000,21000,26000])"));
        eval("-0.4435069413347", Universal.evaluate("IRR([-70000,12000,15000], -0.1)"));
        eval("0.05715142887178467", Universal.evaluate("IRR([-75000,12000,15000,18000,21000,24000])"));
        eval("0.05715142887178467", Universal.evaluate("IRR([[-75000,12000],[15000,18000],[21000,24000]])"));
        eval("0.05715142887178467", Universal.evaluate("IRR([-75000,12000,15000,18000,21000,24000],0.1)"));
        eval("0.05715142887178447", Universal.evaluate("IRR([-75000,12000,15000,18000,21000,24000],0.075)"));
        eval("0.05715142887178453", Universal.evaluate("IRR([-75000,12000,15000,18000,21000,24000],0.05)"));
    }

    @Ignore
    @Test
    public void testFinancialCompatibilityExt() throws InvocationTargetException, CompileException {
        eval("20.54794521", Universal.evaluate("ACCRINTM(39539,39614,0.1,1000,3)"));
        eval("776", Universal.evaluate("AMORDEGRC(2400,39679,39813,300,1,0.15,1)"));
        eval("360", Universal.evaluate("AMORLINC(2400,39679,39813,300,1,0.15,1)"));
        eval("0.0006864088", Universal.evaluate("DISC('07/01/2018', '01/01/2048', 97.975, 100, 1)"));
        eval("0.05768", Universal.evaluate("INTRATE('02/15/2008', '05/15/2008', 1000000, 1014420, 2)"));
        eval("99.9844988756", Universal.evaluate("PRICEMAT('2/15/2008','4/13/2008','11/11/2007',0.0610,0.0610,0)"));
        eval("1014584.65441", Universal.evaluate("RECEIVED('15-Feb-08','15-May-08',1000000,0.0575,2)"));
        eval("71", Universal.evaluate("COUPDAYBS('25-jan-11', '15-nov-11', 2,1)"));
        eval("181", Universal.evaluate("COUPDAYS('25-jan-11', '15-nov-11', 2,1)"));
        eval("110", Universal.evaluate("COUPDAYSNC('25-jan-11', '15-nov-11', 2,1)"));
        eval(Moments.datevalue(Moments.valueOf("15-May-11")).toString(), Universal.evaluate("COUPNCD('25-jan-11', '15-nov-11', 2,1)"));
        eval(Moments.datevalue(Moments.valueOf("15-Nov-10")).toString(), Universal.evaluate("COUPPCD('25-jan-11', '15-nov-11', 2,1)"));
        eval("99.87829", Universal.evaluate("ODDLPRICE('February 7, 2008','June 15, 2008','October 15, 2007',0.0375,0.0405,100,2,0)"));
        eval("0.04519", Universal.evaluate("ODDLYIELD('4/20/2008','6/15/2008','12/24/2007',0.0375,99.875,100,2,0)"));
        eval("94.6343616213", Universal.evaluate("PRICE('2/15/2008','11/15/2017',0.0575,0.0650,100,2,0)"));
        eval("0.0650000069", Universal.evaluate("YIELD('15-Feb-08','15-Nov-16',0.0575,95.04287,100,2,0)"));
        eval("0.052822572", Universal.evaluate("YIELDDISC('16-Feb-08','1-Mar-08',99.795,100,2)"));
        eval("0.0609543337", Universal.evaluate("YIELDMAT('15-Mar-08','3-Nov-08','8-Nov-07',0.0625,100.0123,0)"));
        eval("99.80", Universal.evaluate("PRICEDISC('2/16/2008','3/1/2008',0.0525,100,2)"));
        eval("10.9191453", Universal.evaluate("DURATION('07/01/2018', '01/01/2048', 0.08,0.09,2,1)"));
        eval("5.736", Universal.evaluate("MDURATION('01/01/2008', '01/01/2016', 0.08,0.09,2,1)"));
        eval("4", Universal.evaluate("COUPNUM('25-jan-07', '15-nov-08', 2,1)"));
        eval("1.32", Universal.evaluate("VDB(2400,300,10*365,0,1)"));
        eval("40.00", Universal.evaluate("VDB(2400,300,10*12,0,1)"));
        eval("480.00", Universal.evaluate("VDB(2400,300,10,0,1)"));
        eval("396.31", Universal.evaluate("VDB(2400,300,10*12,6,18)"));
        eval("311.81", Universal.evaluate("VDB(2400,300,10*12,6,18,1.5)"));
        eval("315.00", Universal.evaluate("VDB(2400,300,10,0,0.875,1.6)"));
        eval("113.60", Universal.evaluate("ODDFPRICE('11/11/2008','3/1/2021','10/15/2008','3/1/2009',0.0785,0.0625,100,2,1)"));
        eval("0.0772", Universal.evaluate("ODDFYIELD('November 11, 2008','March 1, 2021','October 15, 2008','March 1, 2009',0.0575,84.50,100,2,0)"));
    }

    @Ignore
    @Test
    public void testFinancialCompatibilityExtended() throws InvocationTargetException, CompileException {
        eval("0.3734", Universal.evaluate("XIRR([-10000,2750,4250,3250,2750],['01/jan/08','01/mar/08','30/oct/08','15/feb/09','01/apr/09'],0.1)"));
        eval("2086.65", Universal.evaluate("XNPV(0.09,[-10000,2750,4250,3250,2750],['01/01/2008','03/01/2008','10/30/2008','02/15/2009','04/01/2009'])"));
    }

    @Test
    public void testInformationCompatibility() throws InvocationTargetException, CompileException {
        evalLogical(false, Universal.evaluate("ISBLANK(1)"));
        evalLogical(false, Universal.evaluate("ISERR(1)"));
        evalLogical(false, Universal.evaluate("ISERROR(1)"));
        evalLogical(false, Universal.evaluate("ISEVEN(-1)"));
        evalLogical(false, Universal.evaluate("ISEVEN(2.5)"));
        evalLogical(false, Universal.evaluate("ISEVEN(5)"));
        evalLogical(true, Universal.evaluate("ISEVEN(0)"));
        evalLogical(true, Universal.evaluate("ISLOGICAL(true)"));
        evalLogical(true, Universal.evaluate("ISLOGICAL(false)"));
        evalLogical(false, Universal.evaluate("ISLOGICAL(1)"));
        evalLogical(false, Universal.evaluate("ISLOGICAL('true')"));
        evalLogical(false, Universal.evaluate("ISNA(1)"));
        evalLogical(true, Universal.evaluate("ISNONTEXT(1)"));
        evalLogical(true, Universal.evaluate("ISNONTEXT(true)"));
        evalLogical(false, Universal.evaluate("ISNONTEXT('a')"));
        evalLogical(true, Universal.evaluate("ISNUMBER(1)"));
        evalLogical(false, Universal.evaluate("ISNUMBER('1')"));
        evalLogical(true, Universal.evaluate("ISODD(-1)"));
        evalLogical(true, Universal.evaluate("ISODD(5)"));
        evalLogical(false, Universal.evaluate("ISODD(2.5)"));
        evalLogical(true, Universal.evaluate("ISTEXT('a')"));
        evalLogical(false, Universal.evaluate("ISTEXT(1)"));
        evalLogical(false, Universal.evaluate("ISTEXT(true)"));
        eval("1", Universal.evaluate("N(1)"));
        eval("1", Universal.evaluate("N(true)"));
        eval("0", Universal.evaluate("N(false)"));
        eval("0", Universal.evaluate("N('a')"));
        eval("1337", Universal.evaluate("N('1337')"));
        eval("43145", Universal.evaluate("N(DATE(2018,2,14))"));
        eval("1", Universal.evaluate("TYPE(1)"));
        eval("2", Universal.evaluate("TYPE('a')"));
        eval("4", Universal.evaluate("TYPE(true)"));
    }

    @Ignore
    @Test
    public void testInformationCompatibilityExtended() throws InvocationTargetException, CompileException {
        eval("1", Universal.evaluate("ERROR.TYPE(error.nil)"));
        eval("2", Universal.evaluate("ERROR.TYPE(error.div0)"));
        eval("3", Universal.evaluate("ERROR.TYPE(error.value)"));
        eval("4", Universal.evaluate("ERROR.TYPE(error.ref)"));
        eval("5", Universal.evaluate("ERROR.TYPE(error.name)"));
        eval("6", Universal.evaluate("ERROR.TYPE(error.num)"));
        eval("7", Universal.evaluate("ERROR.TYPE(error.na)"));
        eval("8", Universal.evaluate("ERROR.TYPE(error.data)"));
        eval("error.na", Universal.evaluate("ERROR.TYPE(1)"));
        eval("true", Universal.evaluate("ISBLANK(null)"));
        eval("false", Universal.evaluate("ISERR(error.na)"));
        eval("true", Universal.evaluate("ISERR(error.value)"));
        eval("true", Universal.evaluate("ISERR(NaN)"));
        eval("true", Universal.evaluate("ISERR(1/0)"));
        eval("true", Universal.evaluate("ISERROR(error.na)"));
        eval("true", Universal.evaluate("ISERROR(error.value)"));
        eval("true", Universal.evaluate("ISNA(error.na)"));
        eval("false", Universal.evaluate("ISNUMBER(1/0)"));
        eval("16", Universal.evaluate("TYPE(error.na)"));
        eval("64", Universal.evaluate("TYPE([1])"));
    }

    @Test
    public void testLogicalCompatibility() throws InvocationTargetException, CompileException {
        eval("1", Universal.evaluate("IF(true,1,2)"));
        eval("2", Universal.evaluate("IF(false,1,2)"));
        evalExpression("1", Universal.evaluate("IF(true,'1','2')"));
        evalExpression("2", Universal.evaluate("IF(false,'1','2')"));
        evalLogical(true, Universal.evaluate("AND(50>1,50<100)"));
        evalExpression("50", Universal.evaluate("IF(AND(50<100,50<100),50,\"The value is out of range\")"));
        evalExpression("The value is out of range", Universal.evaluate("IF(AND(100>1,100<100),100,\"The value is out of range\")"));
        evalLogical(true, Universal.evaluate("AND(true,true)"));
        evalLogical(false, Universal.evaluate("AND(true,false)"));
        evalLogical(false, Universal.evaluate("FALSE()"));
        evalLogical(false, Universal.evaluate("NOT(true)"));
        evalLogical(true, Universal.evaluate("NOT(false)"));
        evalLogical(true, Universal.evaluate("OR(true)"));
        evalLogical(false, Universal.evaluate("OR(false)"));
        evalLogical(true, Universal.evaluate("OR(true,false)"));
        evalLogical(true, Universal.evaluate("TRUE()"));
        evalLogical(false, Universal.evaluate("XOR(false,false)"));
        evalLogical(true, Universal.evaluate("XOR(false,true)"));
        evalLogical(true, Universal.evaluate("XOR(true,false)"));
        evalLogical(false, Universal.evaluate("XOR(true,true)"));
        evalExpression("Default Expression", Universal.evaluate("SWITCH(7,\"Default Expression\")"));
        evalExpression("Seven", Universal.evaluate("SWITCH(7,9,\"Nine\",7,\"Seven\")"));
        evalExpression("Eight", Universal.evaluate("SWITCH(8,9,\"Nine\",7,\"Seven\",\"Eight\")"));
    }

    @Test
    public void testChooseCompatibility() throws InvocationTargetException, CompileException {
        evalExpression("jima", Universal.evaluate("CHOOSE(1,'jima')"));
        evalExpression("jimc", Universal.evaluate("CHOOSE(3,'jima','jimb','jimc')"));
    }

    @Ignore
    @Test
    public void testLookupReferenceCompatibility() throws InvocationTargetException, CompileException {
        eval("2", Universal.evaluate("MATCH(1,[0,1,2,3,4,100,7])"));
        eval("5", Universal.evaluate("MATCH(4,[0,1,2,3,4,100,7],1)"));
        eval("5", Universal.evaluate("MATCH(4,[0,1,2,3,4,100,7],0)"));
        eval("5", Universal.evaluate("MATCH(4,[0,1,2,3,4,100,7],-1)"));
        eval("5", Universal.evaluate("MATCH(5,[0,1,2,3,4,100,7],1)"));
//        eval("error.na", Universal.evaluate("MATCH(5,[0,1,2,3,4,100,7],0)"));
        eval("7", Universal.evaluate("MATCH(5,[0,1,2,3,4,100,7],-1)"));
//        eval("error.na", Universal.evaluate("MATCH(4,[0,1,2,3,4,100,7],2)"));
//        eval("error.na", Universal.evaluate("MATCH(4,[0,1,2,3,4,100,7],-2)"));
        eval("1", Universal.evaluate("MATCH('jima',['jima','jimb','jimc','bernie'],0)"));
        eval("2", Universal.evaluate("MATCH('j*b',['jima','jimb','jimc','bernie'],0)"));
//        eval("error.na", Universal.evaluate("MATCH('j?b',['jima','jimb','jimc','bernie'],0)"));
        eval("2", Universal.evaluate("MATCH('j??b',['jima','jimb','jimc','bernie'],0)"));
//        eval("error.na", Universal.evaluate("MATCH('j???b',['jima','jimb','jimc','bernie'],0)"));
        eval("1", Universal.evaluate("MATCH('j???',['jima','jimb','jimc','bernie'],0)"));
        eval("3", Universal.evaluate("MATCH('jimc',['jima','jimb','jimc','bernie'],0)"));
        eval("3", Universal.evaluate("MATCH('jimc',['jima','jimb','jimd','bernie'],-1)"));
        eval("2", Universal.evaluate("MATCH('jimc',['jima','jimb','jimd','bernie'],1)"));
//        eval("error.na", Universal.evaluate("VLOOKUP()"));
//        eval("error.na", Universal.evaluate("VLOOKUP(1)"));
//        eval("error.na", Universal.evaluate("VLOOKUP(1,[[1,2]])"));
        eval("2", Universal.evaluate("VLOOKUP(1,[[1,2]],2)"));
        eval("2", Universal.evaluate("VLOOKUP(1,[[1,2]],2,false)"));
        eval("2", Universal.evaluate("VLOOKUP(1,[[1,2]],2,true)"));
        eval("4", Universal.evaluate("VLOOKUP(3,[[1,2],[3,4]],2,true)"));
//        eval("error.na", Universal.evaluate("VLOOKUP(5,[[1,2],[3,4]],2,false)"));
//        eval("error.na", Universal.evaluate("VLOOKUP(5,[[1,2],[3,4]],2,true)"));
//        eval("error.na", Universal.evaluate("VLOOKUP('ji',[['jim',2],['jam',4]],2,false)"));
        eval("2", Universal.evaluate("VLOOKUP('ji',[['jim',2],['jam',4]],2,true)"));
//        eval("error.na", Universal.evaluate("VLOOKUP('li',[['jim',2],['jam',4]],2,true)"));
//        eval("error.ref", Universal.evaluate("VLOOKUP('ji',[['jim',2],['jam',4]],3,true)"));
//        eval("error.na", Universal.evaluate("VLOOKUP('ji',[['jim',2],['jam',4]],3,false)"));
//        eval("error.na", Universal.evaluate("HLOOKUP()"));
//        eval("error.na", Universal.evaluate("HLOOKUP(1)"));
//        eval("error.na", Universal.evaluate("HLOOKUP(1,[[1,2]])"));
        eval("2", Universal.evaluate("HLOOKUP(1,[[1],[2]],2)"));
//        eval("error.ref", Universal.evaluate("HLOOKUP(1,[[1],[2]],3)"));
        eval("3", Universal.evaluate("HLOOKUP(1,[[1,2],[3,4]],2)"));
        eval("4", Universal.evaluate("HLOOKUP(2,[[1,2],[3,4]],2)"));
        eval("2", Universal.evaluate("HLOOKUP(1,[[1],[2]],2,true)"));
//        eval("error.ref", Universal.evaluate("HLOOKUP(1,[[1],[2]],3,true)"));
        eval("3", Universal.evaluate("HLOOKUP(1,[[1,2],[3,4]],2,true)"));
        eval("4", Universal.evaluate("HLOOKUP(2,[[1,2],[3,4]],2,true)"));
//        eval("error.na", Universal.evaluate("HLOOKUP('ji',[['jim','jam'],[1,4]],2,false)"));
        eval("1", Universal.evaluate("HLOOKUP('ji',[['jim','jam'],[1,4]],2,true)"));
//        eval("error.na", Universal.evaluate("HLOOKUP('li',[['jim','jam'],[1,4]],2,true)"));
//        eval("error.ref", Universal.evaluate("HLOOKUP('ji',[['jim','jam'],[1,4]],3,true)"));
//        eval("error.na", Universal.evaluate("HLOOKUP('ji',[['jim','jam'],[1,4]],3,false)"));
        eval("[]", Universal.evaluate("TRANSPOSE([])"));
        eval("[[1],[2],[3]]", Universal.evaluate("TRANSPOSE([1,2,3])"));
        eval("[[1,3,5], [2,4,6]]", Universal.evaluate("TRANSPOSE([[1,2],[3,4],[5,6]])"));
        eval("[[1,4],[2,5],[3,6]]", Universal.evaluate("TRANSPOSE([[1,2,3],[4,5,6]])"));
    }

    @Ignore
    @Test
    public void testMiscellaneous() throws InvocationTargetException, CompileException {
        eval("[1, 2, 3, 4, 5, 6]", Universal.evaluate("UNIQUE(1,2,3,4,5,6,6,3)"));
        eval("['jima', 'jimb', 'jimc']", Universal.evaluate("UNIQUE('jima','jimb','jima','jimc')"));
        eval("[]", Universal.evaluate("UNIQUE()"));
        eval("[[]]", Universal.evaluate("UNIQUE([])"));
        eval("[1, 2, 3, 4]", Universal.evaluate("ARGS2ARRAY(1,2,3,4)"));
        eval("['jim', 2, 3.14]", Universal.evaluate("ARGS2ARRAY('jim',2,3.14)"));
        eval("[1, 2, 3, 4, 5]", Universal.evaluate("FLATTEN([1,[2,3,[4,5]]])"));
        eval("[]", Universal.evaluate("FLATTEN([])"));
        eval("'1,2,3,4,5'", Universal.evaluate("JOIN([1,[2,3,[4,5]]])"));
        eval("'jim alateras'", Universal.evaluate("JOIN(['jim','alateras'],'')"));
        eval("true", Universal.evaluate("NUMBERS([1,[2,3,[4,5]]])"));
        eval("false", Universal.evaluate("NUMBERS(['jim','alateras'],'')"));
        eval("'Jim'", Universal.evaluate("REFERENCE(\"{name:{firstName:'Jim',lastName:'Alateras',nickNames:['jforce','jimmya','jima'],address:{number:'5',street:'Kalulu',type:'Rd',mobile:'0422344861'}}}\",'name.firstName')"));
        eval("'number', '5'", Universal.evaluate("REFERENCE(\"{name:{firstName:'Jim',lastName:'Alateras',nickNames:['jforce','jimmya','jima'],address:{number:'5',street:'Kalulu',type:'Rd',mobile:'0422344861'}}}\",'name.address')"));
        eval("'0422344861'", Universal.evaluate("REFERENCE(\"{name:{firstName:'Jim',lastName:'Alateras',nickNames:['jforce','jimmya','jima'],address:{number:'5',street:'Kalulu',type:'Rd',mobile:'0422344861'}}}\",'name.address.mobile')"));
        eval("'jforce'", Universal.evaluate("REFERENCE(\"{name:{firstName:'Jim',lastName:'Alateras',nickNames:['jforce','jimmya','jima'],address:{number:'5',street:'Kalulu',type:'Rd',mobile:'0422344861'}}}\",'name.nickNames[0]')"));
    }

    protected void eval(String expect, BigDecimal parse) throws InvocationTargetException, CompileException {

        DecimalFormat df = new DecimalFormat("#,###.00000");

        Assert.assertEquals(df.format(new BigDecimal(expect)), df.format(parse));
    }

    protected void evalExpression(String expect, String parse) throws InvocationTargetException, CompileException {

        Assert.assertEquals(expect, parse);
    }

    protected void evalImaginaryExpression(String expect, String parse) throws InvocationTargetException, CompileException {

        final ExpressionEvaluator eval = new ExpressionEvaluator();
        final Object evaluate = parse;
        final FormulaDecimal actualReal = Formulas.imreal(evaluate.toString());
        final FormulaDecimal actualImaginary = Formulas.imaginary(evaluate.toString());

        final FormulaDecimal expectedReal = Formulas.imreal(expect.toString());
        final FormulaDecimal expectedImaginary = Formulas.imaginary(expect.toString());

        DecimalFormat df = new DecimalFormat("#,###.00000");

        Assert.assertEquals(df.format(expectedReal.asBigDecimal()), df.format(((Number) actualReal)));
        Assert.assertEquals(df.format(expectedImaginary.asBigDecimal()), df.format(((Number) actualImaginary)));
    }

    protected void evalDateExpression(Moment expect, Date parse) throws InvocationTargetException, CompileException {

        final ExpressionEvaluator eval = new ExpressionEvaluator();
        Assert.assertEquals("year not equal", expect.year(), Moment.valueOf(parse).year());
        Assert.assertEquals("month not equal", expect.month(), Moment.valueOf(parse).month());
        Assert.assertEquals("day not equal", expect.day(), Moment.valueOf(parse).day());
        Assert.assertEquals("time not equal", expect.getTime(), parse.getTime());
    }


    protected void evalLogical(boolean expect, Boolean parse) throws InvocationTargetException, CompileException {

        Assert.assertEquals(expect, parse.booleanValue());
    }

    protected void evalDateExpressionNear(Moment expect, Date parse) throws InvocationTargetException, CompileException {

        Assert.assertEquals("year not equal", expect.year(), Moment.valueOf(parse).year());
        Assert.assertEquals("month not equal", expect.month(), Moment.valueOf(parse).month());
        Assert.assertEquals("day not equal", expect.day(), Moment.valueOf(parse).day());
        Assert.assertThat("time not equal", parse.getTime(), Matchers.anyOf(Matchers.greaterThanOrEqualTo(expect.getTime() - 1000), Matchers.lessThanOrEqualTo(expect.getTime() + 1000)));
    }
}