package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class SinhTest extends RippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "0 sinh.", "0" );
        assertReducesTo( "-1 sinh.", "-1.1752011936438014" );
        assertReducesTo( "1 sinh.", "1.1752011936438014" );
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double sinh.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double sinh.", "\"INF\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double sinh.", "\"-INF\"^^xsd:double" );
    }

    public void testInverse() throws Exception
    {
        // TODO: implement inverse mapping
    }
}
