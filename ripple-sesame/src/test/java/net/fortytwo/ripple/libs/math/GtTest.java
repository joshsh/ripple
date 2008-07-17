package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class GtTest extends RippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "1 1 gt >>", "false" );
        assertReducesTo( "1.1 1 gt >>", "true" );
        assertReducesTo( "1 1.1 gt >>", "false" );
        assertReducesTo( "0.0 -0.00000001 gt >>", "true" );
        assertReducesTo( "-0.5 -42 gt >>", "true" );
        assertReducesTo( "-42 -0.5 gt >>", "false" );
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double \"NaN\"^^xsd:double gt >>", "false" );
        assertReducesTo( "\"NaN\"^^xsd:double \"INF\"^^xsd:double gt >>", "false" );
        assertReducesTo( "\"NaN\"^^xsd:double \"-INF\"^^xsd:double gt >>", "false" );
        assertReducesTo( "\"NaN\"^^xsd:double 4 gt >>", "false" );

        assertReducesTo( "\"INF\"^^xsd:double \"NaN\"^^xsd:double gt >>", "false" );
        assertReducesTo( "\"INF\"^^xsd:double \"INF\"^^xsd:double gt >>", "false" );
        assertReducesTo( "\"INF\"^^xsd:double \"-INF\"^^xsd:double gt >>", "true" );
        assertReducesTo( "\"INF\"^^xsd:double 4 gt >>", "true" );

        assertReducesTo( "\"-INF\"^^xsd:double \"NaN\"^^xsd:double gt >>", "false" );
        assertReducesTo( "\"-INF\"^^xsd:double \"INF\"^^xsd:double gt >>", "false" );
        assertReducesTo( "\"-INF\"^^xsd:double \"-INF\"^^xsd:double gt >>", "false" );
        assertReducesTo( "\"-INF\"^^xsd:double 4 gt >>", "false" );

        assertReducesTo( "4 \"NaN\"^^xsd:double gt >>", "false" );
        assertReducesTo( "4 \"INF\"^^xsd:double gt >>", "false" );
        assertReducesTo( "4 \"-INF\"^^xsd:double gt >>", "true" );
        assertReducesTo( "4 4 gt >>", "false" );
    }
}