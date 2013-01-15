package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class CosTest extends RippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "0 cos.", "1" );
        assertReducesTo( "1.5707963267948966 cos. 1 add.", "1" );
        assertReducesTo( "3.141592653589793 cos.", "-1" );
//        assertReducesTo( "3.141592653589793 4 div. cos.", "1 2 sqrt. abs. distinct. div." );
        assertReducesTo( "3.141592653589793 4 div. cos.", "0.7071067811865476" );
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double cos.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"INF\"^^xsd:double cos.", "\"NaN\"^^xsd:double" );
        assertReducesTo( "\"-INF\"^^xsd:double cos.", "\"NaN\"^^xsd:double" );
    }

    public void testInverse() throws Exception
    {
        assertReducesTo( "0.7071067811865475 cos~.", "0.7853981633974484" );
        assertReducesTo( "0 cos~.", "1.5707963267948966" );
    }
}
