package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SinTest extends RippleTestCase {
    public void testSingleSolution() throws Exception {
        assertReducesTo("0 sin.", "0");
        assertReducesTo("1.5707963267948966 sin.", "1");
        assertReducesTo("3.141592653589793e0 sin. 10 mul. floor.", "0");
        assertReducesTo("3.141592653589793 4 div. sin.", "0.7071067811865475");
    }

    public void testSpecialValues() throws Exception {
        assertReducesTo("\"NaN\"^^xsd:double sin.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"INF\"^^xsd:double sin.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"-INF\"^^xsd:double sin.", "\"NaN\"^^xsd:double");
    }

    public void testInverse() throws Exception {
        assertReducesTo("0 sin~.", "0");
        assertReducesTo("0.7071067811865475 sin~.", "0.7853981633974482");
        assertReducesTo("1 sin~.", "1.5707963267948966");
    }
}
