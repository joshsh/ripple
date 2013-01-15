package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class MulTest extends RippleTestCase {
        public void testSingleSolution() throws Exception
    {
        assertReducesTo( "2 2 mul.", "4" );
        assertReducesTo( "2 -2.0 mul.", "-4" );
        assertReducesTo( "0.5 2 mul.", "1" );
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double \"NaN\"^^xsd:double mul.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"NaN\"^^xsd:double \"INF\"^^xsd:double mul.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"NaN\"^^xsd:double \"-INF\"^^xsd:double mul.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"NaN\"^^xsd:double 42 mul.", "\"NaN\"^^xsd:double" );

        assertReducesTo( "\"INF\"^^xsd:double \"NaN\"^^xsd:double mul.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double \"INF\"^^xsd:double mul.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double \"-INF\"^^xsd:double mul.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double 42 mul.", "\"INF\"^^xsd:double" );

        assertReducesTo( "\"-INF\"^^xsd:double \"NaN\"^^xsd:double mul.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double \"INF\"^^xsd:double mul.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double \"-INF\"^^xsd:double mul.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double 42 mul.", "\"-INF\"^^xsd:double" );

        assertReducesTo( "42 \"NaN\"^^xsd:double mul.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "42 \"INF\"^^xsd:double mul.", "\"INF\"^^xsd:double" );
        assertReducesTo( "42 \"-INF\"^^xsd:double mul.", "\"-INF\"^^xsd:double" );
        assertReducesTo( "0 \"INF\"^^xsd:double mul.", "\"NaN\"^^xsd:double");
    }

    public void testInverse() throws Exception
    {
        assertReducesTo( "2 5.0 mul~.", "0.4" );
        assertReducesTo( "2 2 mul~.", "1" );
    }
}
