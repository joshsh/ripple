package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SignTest extends RippleTestCase {
    @Test
    public void testSingleSolution() throws Exception {
        assertReducesTo("1 sign.", "1");
        assertReducesTo("0.01 sign.", "1");
        assertReducesTo("0 sign.", "0");
        assertReducesTo("-42.2 sign.", "-1");
    }

    @Test
    public void testSpecialValues() throws Exception {
        // TODO: this is a bit odd
        assertReducesTo("\"NaN\"^^xsd:double sign.", "0");

        assertReducesTo("\"INF\"^^xsd:double sign.", "1");
        assertReducesTo("\"-INF\"^^xsd:double sign.", "-1");
    }
}
