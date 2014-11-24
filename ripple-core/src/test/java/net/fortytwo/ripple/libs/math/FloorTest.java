package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class FloorTest extends RippleTestCase {
    public void testSingleSolution() throws Exception {
        assertReducesTo("1 floor.", "1");
        assertReducesTo("1.1 floor.", "1");
        assertReducesTo("0.0 floor.", "0");
        assertReducesTo("-0.5 floor.", "-1");
        assertReducesTo("-42.0000001 floor.", "-43");
    }

    public void testSpecialValues() throws Exception {
        assertReducesTo("\"NaN\"^^xsd:double floor.", "0");

        // TODO: currently yield system-specific min/max values
        //assertReducesTo( "\"INF\"^^xsd:double floor.", "2147483647" );
        //assertReducesTo( "\"-INF\"^^xsd:double floor.", "-2147483648" );
    }
}
