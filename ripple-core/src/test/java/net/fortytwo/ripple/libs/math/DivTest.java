package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class DivTest extends RippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "2 2 div.", "1" );
        assertReducesTo( "2 -2.0 div.", "-1" );
        assertReducesTo( "0.5 2 div.", "0.25" );
    }

    public void testOutOfRangeArgument() throws Exception
    {
        assertReducesTo( "2 0 div." );
        assertReducesTo( "0 0 div." );        
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double \"NaN\"^^xsd:double div.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"NaN\"^^xsd:double \"INF\"^^xsd:double div.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"NaN\"^^xsd:double \"-INF\"^^xsd:double div.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"NaN\"^^xsd:double 42 div.", "\"NaN\"^^xsd:double" );

        assertReducesTo( "\"INF\"^^xsd:double \"NaN\"^^xsd:double div.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double \"INF\"^^xsd:double div.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double \"-INF\"^^xsd:double div.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double 42 div.", "\"INF\"^^xsd:double" );

        assertReducesTo( "\"-INF\"^^xsd:double \"NaN\"^^xsd:double div.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double \"INF\"^^xsd:double div.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double \"-INF\"^^xsd:double div.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double 42 div.", "\"-INF\"^^xsd:double" );

        assertReducesTo( "42 \"NaN\"^^xsd:double div.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "42 \"INF\"^^xsd:double div.", "0" );
        assertReducesTo( "42 \"-INF\"^^xsd:double div.", "0" );
        assertReducesTo( "42 42 div.", "1" );
    }

    public void testInverse() throws Exception
    {
        assertReducesTo( "2 5.0 div~.", "10" );
        assertReducesTo( "2 0 div~.", "0" );
    }
}