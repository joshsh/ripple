package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class CbrtTest extends RippleTestCase {
    @Test
    public void testSingleSolution() throws Exception {
        assertReducesTo("1 cbrt.", "1");
        assertReducesTo("-1 cbrt.", "-1");
        assertReducesTo("0.125 cbrt.", "0.5");
    }

    @Test
    public void testSpecialValues() throws Exception {
        assertReducesTo("\"NaN\"^^xsd:double cbrt.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"INF\"^^xsd:double cbrt.", "\"INF\"^^xsd:double");
        assertReducesTo("\"-INF\"^^xsd:double cbrt.", "\"-INF\"^^xsd:double");
    }

    @Test
    public void testInverse() throws Exception {
        assertReducesTo("0.5 cbrt~.", "0.125");
        assertReducesTo("-1 cbrt~.", "-1");
    }
}
