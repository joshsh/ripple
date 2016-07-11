package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Atan2Test extends RippleTestCase {

    @Test
    public void testSingleSolution() throws Exception {
        assertReducesTo("1 1 atan2.", "0.7853981633974483");
        assertReducesTo("-1 1 atan2.", "2.356194490192345");
        assertReducesTo("1 -1 atan2.", "-0.7853981633974483");
        assertReducesTo("-1 -1 atan2.", "-2.356194490192345");
        assertReducesTo("1 0 atan2.", "0");
        assertReducesTo("-1 0 atan2.", "3.141592653589793");
        assertReducesTo("0 1 atan2.", "1.5707963267948966");
        assertReducesTo("0 -1 atan2.", "-1.5707963267948966");
    }

    @Test
    public void testUndefined() throws Exception {
        assertReducesTo("0 0 atan2.");
    }

    @Test
    public void testSpecialValues() throws Exception {
        assertReducesTo("\"NaN\"^^xsd:double 0 atan2.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"INF\"^^xsd:double 0 atan2.", "0");
        assertReducesTo("\"-INF\"^^xsd:double 0 atan2.", "3.141592653589793");
        // ...
    }
}
