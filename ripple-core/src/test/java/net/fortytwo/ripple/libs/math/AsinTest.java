package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class AsinTest extends RippleTestCase {
    @Test
    public void testSingleSolution() throws Exception {
        assertReducesTo("1 asin.", "1.5707963267948966");
        assertReducesTo("0 asin.", "0");
        assertReducesTo("-1 asin.", "-1.5707963267948966");

        assertReducesTo("0.7071067811865475e0 asin.", "0.7853981633974482");
        assertReducesTo("0e0 asin.", "0");
        assertReducesTo("-1e0 asin.", "-1.5707963267948966");

        assertReducesTo("0.7071067811865475 asin.", "0.7853981633974482");
        assertReducesTo("0.0 asin.", "0");
        assertReducesTo("-1.0 asin.", "-1.5707963267948966");
    }

    @Test
    public void testOutOfRangeArgument() throws Exception {
        assertReducesTo("2 asin.");
        assertReducesTo("2e0 asin.");
        assertReducesTo("2.0 asin.");
        assertReducesTo("-42 asin.");
    }

    @Test
    public void testSpecialValues() throws Exception {
        assertReducesTo("\"NaN\"^^xsd:double asin.");
        assertReducesTo("\"INF\"^^xsd:double asin.");
        assertReducesTo("\"-INF\"^^xsd:double asin.");
    }

    @Test
    public void testInverse() throws Exception {
        assertReducesTo("0.7853981633974484 asin~.", "0.7071067811865476");
        assertReducesTo("1.5707963267948966 asin~.", "1");
    }
}
