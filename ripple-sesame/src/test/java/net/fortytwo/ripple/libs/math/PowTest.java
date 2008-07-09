package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class PowTest extends NewRippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "2 2 pow >>", "4" );
        assertReducesTo( "2 -2.0 pow >>", "0.25" );
        assertReducesTo( "2 0.5 pow >>", "1.4142135623730951" );
        assertReducesTo( "0.5 2 pow >>", "0.25" );
        assertReducesTo( "0 1 pow >>", "0" );
        assertReducesTo( "0 0 pow >>", "1" );
    }

    public void testOutOfRangeArgument() throws Exception
    {
        assertReducesTo( "-1 0.5 pow >>", "\"NaN\"^^xsd:double" );
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double \"NaN\"^^xsd:double pow >>", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"NaN\"^^xsd:double \"INF\"^^xsd:double pow >>", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"NaN\"^^xsd:double \"-INF\"^^xsd:double pow >>", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"NaN\"^^xsd:double 42 pow >>", "\"NaN\"^^xsd:double" );

        assertReducesTo( "\"INF\"^^xsd:double \"NaN\"^^xsd:double pow >>", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double \"INF\"^^xsd:double pow >>", "\"INF\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double \"-INF\"^^xsd:double pow >>", "0" );
        assertReducesTo( "\"INF\"^^xsd:double 42 pow >>", "\"INF\"^^xsd:double" );

        assertReducesTo( "\"-INF\"^^xsd:double \"NaN\"^^xsd:double pow >>", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double \"INF\"^^xsd:double pow >>", "\"INF\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double \"-INF\"^^xsd:double pow >>", "0" );
        assertReducesTo( "\"-INF\"^^xsd:double 42 pow >>", "\"INF\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double 41 pow >>", "\"-INF\"^^xsd:double" );

        assertReducesTo( "42 \"NaN\"^^xsd:double pow >>", "\"NaN\"^^xsd:double" );
        assertReducesTo( "42 \"INF\"^^xsd:double pow >>", "\"INF\"^^xsd:double" );
        assertReducesTo( "42 \"-INF\"^^xsd:double pow >>", "0" );
        assertReducesTo( "4 4 pow >>", "256" );
    }

    public void testInverse() throws Exception
    {
        // TODO: implement inverse mapping
    }
}