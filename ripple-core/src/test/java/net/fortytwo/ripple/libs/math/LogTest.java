package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LogTest extends RippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "1 log.", "0" );
        assertReducesTo( "2.7182818284590455 log.", "1" );
        assertReducesTo( "0.36787944117144233 log.", "-1" );
    }

    public void testOutOfRangeArgument() throws Exception
    {
        assertReducesTo( "-1 log." );
    }

    public void testSpecialValues() throws Exception
    {
        // TODO
//        assertReducesTo( "\"NaN\"^^xsd:double log.", "\"NaN\"^^xsd:double" );
//        assertReducesTo( "\"INF\"^^xsd:double log.", "\"INF\"^^xsd:double" );
//        assertReducesTo( "0 log.", "\"-INF\"^^xsd:double" );
    }

    public void testInverse() throws Exception
    {
        assertReducesTo( "0 log~.", "1" );
        assertReducesTo( "1 log~.", "2.7182818284590455" );
        assertReducesTo( "-1 log~.", "0.36787944117144233" );
    }
}
