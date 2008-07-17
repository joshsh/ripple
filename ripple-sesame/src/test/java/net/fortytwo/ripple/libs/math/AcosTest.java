package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class AcosTest extends RippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "1 acos >>", "0" );
        assertReducesTo( "0 acos >>", "1.5707963267948966" );
        assertReducesTo( "-1 acos >>", "3.141592653589793" );

        assertReducesTo( "0.7071067811865475e0 acos >>", "0.7853981633974484" );
        assertReducesTo( "0e0 acos >>", "1.5707963267948966" );
        assertReducesTo( "-1e0 acos >>", "3.141592653589793" );

        assertReducesTo( "0.7071067811865475 acos >>", "0.7853981633974484" );
        assertReducesTo( "0.0 acos >>", "1.5707963267948966" );
        assertReducesTo( "-1.0 acos >>", "3.141592653589793" );
    }

    public void testOutOfRangeArgument() throws Exception
    {
        assertReducesTo( "2 acos >>" );
        assertReducesTo( "2e0 acos >>" );
        assertReducesTo( "2.0 acos >>" );
        assertReducesTo( "-42 acos >>" );
    }

    public void testSpecialValues() throws Exception
    {
        assertReducesTo( "\"NaN\"^^xsd:double acos >>" );
        assertReducesTo( "\"INF\"^^xsd:double acos >>" );
        assertReducesTo( "\"-INF\"^^xsd:double acos >>" );
    }

    public void testInverse() throws Exception
    {
        assertReducesTo( "0.7853981633974484 acos <<", "0.7071067811865475" );
        assertReducesTo( "1.5707963267948966 acos << 1 add >>", "1" );
    }
}