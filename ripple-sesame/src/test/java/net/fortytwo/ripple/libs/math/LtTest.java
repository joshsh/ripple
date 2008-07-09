package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class LtTest extends NewRippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "1 1 lt >>", "false" );
        assertReducesTo( "1.1 1 lt >>", "false" );
        assertReducesTo( "1 1.1 lt >>", "true" );
        assertReducesTo( "0.0 -0.00000001 lt >>", "false" );
        assertReducesTo( "-0.5 -42 lt >>", "false" );
        assertReducesTo( "-42 -0.5 lt >>", "true" );
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double \"NaN\"^^xsd:double lt >>", "false" );
        assertReducesTo( "\"NaN\"^^xsd:double \"INF\"^^xsd:double lt >>", "false" );
        assertReducesTo( "\"NaN\"^^xsd:double \"-INF\"^^xsd:double lt >>", "false" );
        assertReducesTo( "\"NaN\"^^xsd:double 4 lt >>", "false" );

        assertReducesTo( "\"INF\"^^xsd:double \"NaN\"^^xsd:double lt >>", "false" );
        assertReducesTo( "\"INF\"^^xsd:double \"INF\"^^xsd:double lt >>", "false" );
        assertReducesTo( "\"INF\"^^xsd:double \"-INF\"^^xsd:double lt >>", "false" );
        assertReducesTo( "\"INF\"^^xsd:double 4 lt >>", "false" );

        assertReducesTo( "\"-INF\"^^xsd:double \"NaN\"^^xsd:double lt >>", "false" );
        assertReducesTo( "\"-INF\"^^xsd:double \"INF\"^^xsd:double lt >>", "true" );
        assertReducesTo( "\"-INF\"^^xsd:double \"-INF\"^^xsd:double lt >>", "false" );
        assertReducesTo( "\"-INF\"^^xsd:double 4 lt >>", "true" );

        assertReducesTo( "4 \"NaN\"^^xsd:double lt >>", "false" );
        assertReducesTo( "4 \"INF\"^^xsd:double lt >>", "true" );
        assertReducesTo( "4 \"-INF\"^^xsd:double lt >>", "false" );
        assertReducesTo( "4 4 lt >>", "false" );
    }
}