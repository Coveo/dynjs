package org.dynjs.runtime.builtins;

import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.*;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.Types;
import org.junit.Test;

public class MathTest extends AbstractDynJSTestSupport {

    @Test
    public void testMathPrototypeIsUndefined() {
        assertThat(eval("Math.prototype")).isEqualTo(Types.UNDEFINED);
    }
    
    @Test
    public void testMathAtan2BuiltinProperties() {
        eval("var desc = Object.getOwnPropertyDescriptor(Math, 'atan2');");
        assertThat(eval("desc.value == Math.atan2")).isEqualTo(true);
        assertThat(eval("desc.writable")).isEqualTo(true);
        assertThat(eval("desc.configurable")).isEqualTo(true);
        assertThat(eval("desc.enumerable")).isEqualTo(false);
    }

    @Test
    public void testMathE() {
        assertThat(eval("Math.E")).isEqualTo(java.lang.Math.E);
    }

    @Test
    public void testLn10() {
        assertThat(eval("Math.LN10")).isEqualTo(java.lang.Math.log(10));
    }

    @Test
    public void testLn2() {
        assertThat(eval("Math.LN2")).isEqualTo(java.lang.Math.log(2));
    }

    @Test
    public void testLog2e() {
        assertThat(eval("Math.LOG2E")).isEqualTo(java.lang.Math.log(java.lang.Math.E) / java.lang.Math.log(2));
    }

    @Test
    public void testLog10e() {
        assertThat(eval("Math.LOG10E")).isEqualTo(java.lang.Math.log10(java.lang.Math.E));
    }

    @Test
    public void testLog10eDoesNotEnum() {
        check("var result = false; for (x in Math) { if (x === 'LOG10E') { result = true; } }", false);
    }

    @Test
    public void testMathPi() {
        assertThat(eval("Math.PI")).isEqualTo(java.lang.Math.PI);
    }

    @Test
    public void testMathSqrt1_2() {
        assertThat(eval("Math.SQRT1_2")).isEqualTo(java.lang.Math.sqrt(0.5f));
    }

    @Test
    public void testMathSqrt2() {
        assertThat(eval("Math.SQRT2")).isEqualTo(java.lang.Math.sqrt(2.0f));
    }

    @Test
    public void testMathAbs() {
        assertThat(eval("Math.abs(-2)")).isEqualTo(2L);
    }

    @Test
    public void testMathAbsNaN() {
        assertThat(eval("Math.abs(new Number('asdf'))")).isEqualTo(Double.NaN);
    }

    @Test
    public void testMathAbsNegativeInfinity() {
        assertThat(eval("Math.abs(-Infinity)")).isEqualTo(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testMathAbsNumberNegativeInfinity() {
        assertThat(eval("Math.abs(Number.NEGATIVE_INFINITY)")).isEqualTo(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testMathAbsNegativeZero() {
        assertThat(eval("Math.abs(-0)")).isEqualTo(0L);
    }

    @Test
    public void testMathAcos() {
        assertThat(eval("Math.acos(0.5)")).isEqualTo(java.lang.Math.acos(0.5));
    }

    @Test
    public void testMathAcosNaN() {
        assertThat(eval("Math.acos(new Number('asdf'))")).isEqualTo(Double.NaN);
    }

    @Test
    public void testMathAcosGreaterThanOne() {
        assertThat(eval("Math.acos(1.5)")).isEqualTo(Double.NaN);
    }

    @Test
    public void testMathAcosLessThanNegativeOne() {
        assertThat(eval("Math.acos(-1.5)")).isEqualTo(Double.NaN);
    }

    @Test
    public void testMathAcosExactlyOne() {
        assertThat(eval("Math.acos(1)")).isEqualTo(0L);
    }

    @Test
    public void testMathAsin() {
        assertThat(eval("Math.asin(0.5)")).isEqualTo(java.lang.Math.asin(0.5));
    }

    @Test
    public void testMathAsinNaN() {
        assertThat(eval("Math.asin(new Number('qw4'))")).isEqualTo(Double.NaN);
    }

    @Test
    public void testMathAsinGreaterThanOne() {
        assertThat(eval("Math.asin(1.5)")).isEqualTo(Double.NaN);
    }

    @Test
    public void testMathAsinLessThanNegativeOne() {
        assertThat(eval("Math.asin(-1.5)")).isEqualTo(Double.NaN);
    }

    @Test
    public void testMathAsinNegativeZero() {
        assertThat(eval("Math.asin(-0)")).isEqualTo(0L);
    }

    @Test
    public void testMathAsinZero() {
        assertThat(eval("Math.asin(0)")).isEqualTo(0L);
    }

    @Test
    public void testMathAsinFloatyZero() {
        assertThat(eval("Math.asin(0.0)")).isEqualTo(0L);
    }

    @Test
    public void testMathAtan() {
        assertThat(eval("Math.atan(0.5)")).isEqualTo(java.lang.Math.atan(0.5));
    }

    @Test
    public void testAtanNaN() {
        assertThat(eval("Math.atan(new Number('asdf'))")).isEqualTo(Double.NaN);
    }

    @Test
    public void testMathAtanPositiveZero() {
        assertThat(eval("Math.atan(0)")).isEqualTo(0L);
    }

    @Test
    public void testMathAtanNegativeZero() {
        assertThat(eval("Math.atan(-0)")).isEqualTo(0L);
    }

    @Test
    public void testMathAtanNegativeInfinity() {
        assertThat(eval("Math.atan(-Infinity)")).isEqualTo(-java.lang.Math.PI / 2);
    }

    @Test
    public void testMathAtanPositiveInfinity() {
        assertThat(eval("Math.atan(Infinity)")).isEqualTo(java.lang.Math.PI / 2);
    }

    @Test
    public void testMathAtan2Length() {
        assertThat(eval("Math.atan2.length")).isEqualTo(2L);
    }

    @Test
    public void testMathAtan2WithOnlyOneArg() {
        assertThat(eval("Math.atan2(0.4)")).isEqualTo(Double.NaN);
    }

    @Test
    public void testMathAtan2() {
        assertThat(eval("Math.atan2(0.5, 0.5)")).isEqualTo(java.lang.Math.atan2(0.5, 0.5));
    }

    @Test
    public void testMathAtan2WithIntegers() {
        assertThat(eval("Math.atan2(1, 1)")).isEqualTo(java.lang.Math.atan2(1, 1));
    }

    @Test
    public void testMathAtan2WithXInteger() {
        assertThat(eval("Math.atan2(1.0, 1)")).isEqualTo(java.lang.Math.atan2(1.0, 1));
    }

    @Test
    public void testMathAtan2YNaN() {
        assertEval("Math.atan2(new Number('asdf'), 0.3)", Double.NaN);
    }

    @Test
    public void testMathAtan2XNaN() {
        assertEval("Math.atan2(0.3, new Number('asdf'))", Double.NaN);
    }

    @Test
    public void testMathAtan2XGreaterThanZero() {
        assertEval("Math.atan2(1, 0)", java.lang.Math.PI / 2);
    }

    @Test
    public void testMathAtan2NegativeInfinity() {
        assertEval("Math.atan2(-Infinity, Number.NEGATIVE_INFINITY)", -3 * java.lang.Math.PI / 4);
    }

    @Test
    public void testMathAtan2NegativeZero() {
        assertEval("Math.atan2(-0, -0)", -java.lang.Math.PI);
    }

    @Test
    public void testMathAtan2PositiveZero() {
        assertEval("Math.atan2(0, 0)", 0.0);
    }

    @Test
    public void testMathCeil() {
        assertEval("Math.ceil(12)", 12L);
    }

    @Test
    public void testMathCeilFloaty() {
        assertEval("Math.ceil(12.12)", 13L);
    }

    @Test
    public void testMathCeilNaN() {
        assertEval("Math.ceil(new Number('asdf'))", Double.NaN);
    }

    @Test
    public void testMathCeilPositiveInfinity() {
        assertEval("Math.ceil(Number.POSITIVE_INFINITY)", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testMathCeilNegativeInfinity() {
        assertEval("Math.ceil(Number.NEGATIVE_INFINITY)", Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testMathCeilNegativeLessThanNegativeOne() {
        assertEval("Math.ceil(-0.2)", 0L);
    }

    @Test
    public void testMathCos() {
        assertEval("Math.cos(3)", java.lang.Math.cos(3));
    }

    @Test
    public void testMathCosZero() {
        assertEval("Math.cos(0)", 1L);
    }

    @Test
    public void testMathCosStrictEquality() {
        assertEval("Math.cos(+0) === 1", true);
    }

    @Test
    public void testMathCosFloaty() {
        assertEval("Math.cos(3.5)", java.lang.Math.cos(3.5));
    }

    @Test
    public void testMathCosInfinite() {
        assertEval("Math.cos(Infinity)", Double.NaN);
    }

    @Test
    public void testMathCosNegativeInfinite() {
        assertEval("Math.cos(-Infinity)", Double.NaN);
    }

    @Test
    public void testMathExp() {
        assertEval("Math.exp(123.456)", java.lang.Math.exp(123.456));
    }

    @Test
    public void testMathExpInt() {
        assertEval("Math.exp(123)", java.lang.Math.exp(123));
    }

    @Test
    public void testMathExpZero() {
        assertEval("Math.exp(0)", 1);
    }

    @Test
    public void testMathExpInfinite() {
        assertEval("Math.exp(Infinity)", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testMathExpNegativeInfinite() {
        assertEval("Math.exp(-Infinity)", 0);
    }

    @Test
    public void testMathFloor() {
        assertEval("Math.floor(1.6)", 1L);
    }

    @Test
    public void testMathFloorInfinite() {
        assertEval("Math.floor(Infinity)", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testMathFloorNegativeInfinite() {
        assertEval("Math.floor(-Infinity)", Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testMathFloorFootnote() {
        assertEval("Math.floor(1.234) == -Math.ceil(-1.234)", true);
    }

    @Test
    public void testMathFloorGreaterThanZeroButLessThanOne() {
        assertEval("Math.floor(0.99999999)", 0L);
    }

    @Test
    public void testMathLog() {
        assertEval("Math.log(2)", java.lang.Math.log(2));
    }

    @Test
    public void testMathLogNaN() {
        assertEval("Math.log(new Number('asdf'))", Double.NaN);
    }

    @Test
    public void testMathLogLessThanZero() {
        assertEval("Math.log(-1)", Double.NaN);
    }

    @Test
    public void testMathLogZero() {
        assertEval("Math.log(0)", Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testMathLogExactlyOne() {
        assertEval("Math.log(1)", 0L);
    }

    @Test
    public void testMathLogInfinity() {
        assertEval("Math.log(Infinity)", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testMathMaxLength() {
        assertEval("Math.max.length", 2L);
    }

    @Test
    public void testMathMax() {
        assertEval("Math.max(0.5, 2)", 2L);
    }

    @Test
    public void strictZeroEquality() {
        assertEval("0.0 === -0", true);
    }

    @Test
    public void strictZeroInequality() {
        assertEval("Number(0) !== -0", false);
    }

    @Test
    public void testMathMaxNegativeZero() {
        assertEval("Math.max(-0, +0) !== -0", false);
    }

    @Test
    public void testMathMaxNegativeZeroReverse() {
        assertEval("Math.max(+0, -0) !== -0", false);
    }

    @Test
    public void testMathMaxNoArgs() {
        assertEval("Math.max()", Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testMathMaxOneArg() {
        assertEval("Math.max(2)", 2L);
    }

    @Test
    public void testMathMaxThreeArgs() {
        assertEval("Math.max(1,4,6)", 6L);
    }

    @Test
    public void testMathMaxVeryManyArgs() {
        assertEval("Math.max(1,4,6,12,4,987,0,12.34,98765.45)", 98765.45);
    }

    @Test
    public void testMathMaxVeryManyArgsAndOneNaNStuckInTheMiddle() {
        assertEval("Math.max(1,4,6,12,4,987,Number.NaN,12.34,98765.45)", Double.NaN);
    }

    @Test
    public void testMathMaxNaN() {
        assertEval("Math.max(0.0, -0.0, NaN)", Double.NaN);
    }

    @Test
    public void testMathMinLength() {
        assertEval("Math.min.length", 2L);
    }

    @Test
    public void testMathMin() {
        assertEval("Math.min(0.5, 2)", 0.5);
    }

    @Test
    public void testMathMinNoArgs() {
        assertEval("Math.min()", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testMathMinOneArg() {
        assertEval("Math.min(2)", 2L);
    }

    @Test
    public void testMathMinThreeArgs() {
        assertEval("Math.min(1,4,0.5)", 0.5);
    }

    @Test
    public void testMathMinVeryManyArgs() {
        assertEval("Math.min(1,4,6,12,4,987,0,12.34,98765.45)", 0L);
    }

    @Test
    public void testMathMinVeryManyArgsAndOneNaNStuckInTheMiddle() {
        assertEval("Math.min(1,4,6,12,4,987,Number.NaN,12.34,98765.45)", Double.NaN);
    }

    @Test
    public void testMathPow() {
        assertEval("Math.pow(12,12.123)", java.lang.Math.pow(12, 12.123));
    }

    @Test
    public void testMathPowWithYNaN() {
        assertEval("Math.pow(12, NaN)", Double.NaN);
    }

    @Test
    public void testMathPowXNaNYZero() {
        assertEval("Math.pow(NaN, 0)", 1L);
    }

    @Test
    public void testMathRandom() {
        assertEval("Math.random() > 0", true);
    }

    @Test
    public void testMathRandomLessThanOne() {
        assertEval("Math.random() < 1", true);
    }

    @Test
    public void testMathRound() {
        assertEval("Math.round(3.5)", 4L);
    }

    @Test
    public void testMathRoundNaN() {
        assertEval("Math.round(NaN)", Double.NaN);
    }

    @Test
    public void testMathRoundInfinity() {
        assertEval("Math.round(Infinity)", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testMathRoundNegativeInfinity() {
        assertEval("Math.round(-Infinity)", Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testMathRoundDown() {
        assertEval("Math.round(3.4)", 3L);
    }

    @Test
    public void testMathRoundNegative() {
        assertEval("Math.round(-3.5)", -3L);
    }

    @Test
    public void testMathFunction() {
        try {
            eval("Math()");
            fail("The Math function should raise a TypeError");
        } catch (ThrowException e) {
            // expected
        }
    }
    
    @Test
    public void testMathTanInfinityIsNaN() {
        assertEval("isNaN(Math.tan(+Infinity))", true);
    }

    @Test
    public void testMathTanNegativeInfinityIsNaN() {
        assertEval("isNaN(Math.tan(-Infinity))", true);
    }

    @Test
    public void testMathTanNaNIsNaN() {
        assertEval("isNaN(Math.tan(NaN))", true);
    }
    
    @Test
    public void testModulo() {
        assertEval( "13%2", 1L );
    }

    private void assertEval(String javascript, Object expected) {
        assertThat(eval(javascript)).isEqualTo(expected);
    }

}
