package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ExpTest extends RippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "0 exp.", "1" );
        assertReducesTo( "1 exp.", "2.718281828459045" );
        assertReducesTo( "-1 exp.", "0.36787944117144233" );
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double exp.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double exp.", "\"INF\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double exp.", "0" );
    }

    public void testInverse() throws Exception
    {
        assertReducesTo( "1 exp~.", "0" );
        assertReducesTo( "2.7182818284590455 exp~.", "1" );
        assertReducesTo( "0.36787944117144233 exp~.", "-1" );
    }
}
