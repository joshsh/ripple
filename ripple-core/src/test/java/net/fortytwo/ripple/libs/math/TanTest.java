package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class TanTest extends RippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "0.7853981633974483e0 tan.", "0.9999999999999999" );
        assertReducesTo( "0 tan.", "0" );
        assertReducesTo( "3.141592653589793 4 div. tan.", "0.9999999999999999" );
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double tan.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double tan.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double tan.", "\"NaN\"^^xsd:double" );
    }

    public void testInverse() throws Exception
    {
        assertReducesTo( "1 tan~.", "0.7853981633974483" );
        assertReducesTo( "0 tan~.", "0" );
        assertReducesTo( "-1 tan~.", "-0.7853981633974483" );
    }
}
