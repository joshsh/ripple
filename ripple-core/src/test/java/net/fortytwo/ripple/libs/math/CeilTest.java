package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class CeilTest extends RippleTestCase {
    @Test
    public void testSingleSolution() throws Exception {
        assertReducesTo("1 ceil.", "1");
        assertReducesTo("1.1 ceil.", "2");
        assertReducesTo("0.0 ceil.", "0");
        assertReducesTo("-0.5 ceil.", "0");
        assertReducesTo("-42.0000001 ceil.", "-42");
    }

    @Test
    public void testSpecialValues() throws Exception {
        assertReducesTo("\"NaN\"^^xsd:double ceil.", "0");

        // TODO: currently yield system-specific min/max values
        //assertReducesTo("\"INF\"^^xsd:double ceil.", "2147483647");
        //assertReducesTo("\"-INF\"^^xsd:double ceil.", "-2147483648");
    }
}
