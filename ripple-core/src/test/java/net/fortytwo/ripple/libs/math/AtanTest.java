package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class AtanTest extends RippleTestCase {
    @Test
    public void testSingleSolution() throws Exception {
        assertReducesTo("1 atan.", "0.7853981633974483");
        assertReducesTo("0 atan.", "0");
        assertReducesTo("-1 atan.", "-0.7853981633974483");
    }

    @Test
    public void testSpecialValues() throws Exception {
        assertReducesTo("\"NaN\"^^xsd:double atan.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"INF\"^^xsd:double atan.", "1.5707963267948966");
        assertReducesTo("\"-INF\"^^xsd:double atan.", "-1.5707963267948966");
    }

    @Test
    public void testInverse() throws Exception {
        assertReducesTo("0.7853981633974483e0 atan~.", "0.9999999999999999");
        assertReducesTo("0 atan~.", "0");
    }
}
