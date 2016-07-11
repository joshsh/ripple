package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class NegTest extends RippleTestCase {
    @Test
    public void testSingleSolution() throws Exception {
        assertReducesTo("1 neg.", "-1");
        assertReducesTo("0.01 neg.", "-0.01");
        assertReducesTo("0 neg.", "0");
        assertReducesTo("42.2 neg.", "-42.2");
    }

    @Test
    public void testSpecialValues() throws Exception {
        assertReducesTo("\"NaN\"^^xsd:double neg.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"INF\"^^xsd:double neg.", "\"-INF\"^^xsd:double");
        assertReducesTo("\"-INF\"^^xsd:double neg.", "\"INF\"^^xsd:double");
    }

    @Test
    public void testInverse() throws Exception {
        assertReducesTo("1 neg~.", "-1");
        assertReducesTo("0.01 neg~.", "-0.01");
        assertReducesTo("0 neg~.", "0");
        assertReducesTo("42.2 neg~.", "-42.2");
    }
}
