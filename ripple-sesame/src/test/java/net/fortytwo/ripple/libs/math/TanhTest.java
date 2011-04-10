package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class TanhTest extends RippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "0 tanh.", "0" );
        assertReducesTo( "-1 tanh.", "-0.7615941559557649" );
        assertReducesTo( "1 tanh.", "0.7615941559557649" );
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double tanh.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double tanh.", "1" );
        assertReducesTo( "\"-INF\"^^xsd:double tanh.", "-1" );
    }

    public void testInverse() throws Exception
    {
        // TODO: implement inverse mapping
    }
}
