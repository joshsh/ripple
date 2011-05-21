package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class CeilTest extends RippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "1 ceil.", "1" );
        assertReducesTo( "1.1 ceil.", "2" );
        assertReducesTo( "0.0 ceil.", "0" );
        assertReducesTo( "-0.5 ceil.", "0" );
        assertReducesTo( "-42.0000001 ceil.", "-42" );
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double ceil.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double ceil.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double ceil.", "\"NaN\"^^xsd:double" );
    }
}
