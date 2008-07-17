package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class AtanTest extends RippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "1 atan >>", "0.7853981633974483" );
        assertReducesTo( "0 atan >>", "0" );
        assertReducesTo( "-1 atan >>", "-0.7853981633974483" );
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double atan >>", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double atan >>", "1.5707963267948966" );
        assertReducesTo( "\"-INF\"^^xsd:double atan >>", "-1.5707963267948966" );
    }

    public void testInverse() throws Exception
    {
        assertReducesTo( "0.7853981633974483e0 atan <<", "0.9999999999999999" );
        assertReducesTo( "0 atan <<", "0" );
    }
}