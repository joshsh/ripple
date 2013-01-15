package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ModTest extends RippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "2 2 mod.", "0" );
        assertReducesTo( "47 7 mod.", "5" );
        assertReducesTo( "2 -2.0 mod.", "0" );
        assertReducesTo( "0.5 2 mod.", "0.5" );
        assertReducesTo( "42.2 21.01 mod.", "0.18" );
    }

    public void testOutOfRangeArgument() throws Exception
    {
        assertReducesTo( "2 0 mod." );
        assertReducesTo( "0 0 mod." );
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double \"NaN\"^^xsd:double mod.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"NaN\"^^xsd:double \"INF\"^^xsd:double mod.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"NaN\"^^xsd:double \"-INF\"^^xsd:double mod.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"NaN\"^^xsd:double 4 mod.", "\"NaN\"^^xsd:double" );

        assertReducesTo( "\"INF\"^^xsd:double \"NaN\"^^xsd:double mod.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double \"INF\"^^xsd:double mod.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double \"-INF\"^^xsd:double mod.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double 4 mod.", "\"NaN\"^^xsd:double" );

        assertReducesTo( "\"-INF\"^^xsd:double \"NaN\"^^xsd:double mod.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double \"INF\"^^xsd:double mod.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double \"-INF\"^^xsd:double mod.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double 4 mod.", "\"NaN\"^^xsd:double" );

        assertReducesTo( "4 \"NaN\"^^xsd:double mod.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "4 \"INF\"^^xsd:double mod.", "4" );
        assertReducesTo( "4 \"-INF\"^^xsd:double mod.", "4" );
        assertReducesTo( "4 4 mod.", "0" );
    }
}
