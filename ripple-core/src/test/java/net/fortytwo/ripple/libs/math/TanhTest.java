package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class TanhTest extends RippleTestCase {
    @Test
    public void testSingleSolution() throws Exception {
        assertReducesTo("0 tanh.", "0");
        assertReducesTo("-1 tanh.", "-0.7615941559557649");
        assertReducesTo("1 tanh.", "0.7615941559557649");
    }

    @Test
    public void testSpecialValues() throws Exception {
        assertReducesTo("\"NaN\"^^xsd:double tanh.", "\"NaN\"^^xsd:double");
        assertReducesTo("\"INF\"^^xsd:double tanh.", "1");
        assertReducesTo("\"-INF\"^^xsd:double tanh.", "-1");
    }

    @Ignore
    @Test
    public void testInverse() throws Exception {
        // TODO: implement inverse mapping
    }
}
