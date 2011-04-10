package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class FloorTest extends RippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "1 floor.", "1" );
        assertReducesTo( "1.1 floor.", "1" );
        assertReducesTo( "0.0 floor.", "0" );
        assertReducesTo( "-0.5 floor.", "-1" );
        assertReducesTo( "-42.0000001 floor.", "-43" );
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double floor.", "\"NaN\"^^xsd:double" );
        
        // TODO: these seem to yield max int values
//        assertReducesTo( "\"INF\"^^xsd:double floor.", "0" );
//        assertReducesTo( "\"-INF\"^^xsd:double floor.", "0" );
    }
}
