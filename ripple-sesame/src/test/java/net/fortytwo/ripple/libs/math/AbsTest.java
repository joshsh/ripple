package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class AbsTest extends NewRippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "2 abs >>", "2" );
        assertReducesTo( "0 abs >>", "0" );
        assertReducesTo( "-37 abs >>", "37" );

        assertReducesTo( "2e0 abs >>", "2e0" );
        assertReducesTo( "0e0 abs >>", "0e0" );
        assertReducesTo( "-37 abs >>", "37e0" );
        assertReducesTo( "-1.001e2 abs >>", "1.001e2" );

        assertReducesTo( "2.3 abs >>", "2.3" );
        assertReducesTo( "0.0 abs >>", "0.0" );
        assertReducesTo( "-37.1 abs >>", "37.1" );
        assertReducesTo( "-1.001 abs >>", "1.001" );
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double abs >>", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double abs >>", "\"INF\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double abs >>", "\"INF\"^^xsd:double" );
    }
}