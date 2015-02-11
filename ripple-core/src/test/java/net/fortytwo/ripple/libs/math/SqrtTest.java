package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SqrtTest extends RippleTestCase {
    public void testSingleSolution() throws Exception {
        assertReducesTo("1 sqrt.", "1", "-1");
        assertReducesTo("2 sqrt.", "1.4142135623730951", "-1.4142135623730951");
        assertReducesTo("0.25 sqrt.", "0.5", "-0.5");
    }

    public void testOutOfRangeArgument() throws Exception {
        assertReducesTo("-1 sqrt.");
    }

    public void testSpecialValues() throws Exception {
        assertReducesTo("\"NaN\"^^xsd:double sqrt.");
        assertReducesTo("\"INF\"^^xsd:double sqrt.", "\"-INF\"^^xsd:double", "\"INF\"^^xsd:double");
        assertReducesTo("\"-INF\"^^xsd:double sqrt.");
    }

    public void testInverse() throws Exception {
        assertReducesTo("0.5 sqrt~.", "0.25");
        assertReducesTo("-0.5 sqrt~.", "0.25");
        assertReducesTo("-1 sqrt~.", "1");
        assertReducesTo("0 sqrt~.", "0");
    }
}
