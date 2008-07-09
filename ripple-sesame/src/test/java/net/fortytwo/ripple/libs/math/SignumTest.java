package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class SignumTest extends NewRippleTestCase
{
    public void testSingleSolution() throws Exception
    {
        assertReducesTo( "1 signum >>", "1" );
        assertReducesTo( "0.01 signum >>", "1" );
        assertReducesTo( "0 signum >>", "0" );
        assertReducesTo( "-42.2 signum >>", "-1" );
    }

    public void testSpecialValues() throws Exception
    {
        // TODO: this is a bit odd
        assertReducesTo( "\"NaN\"^^xsd:double signum >>", "0" );

        assertReducesTo( "\"INF\"^^xsd:double signum >>", "1" );
        assertReducesTo( "\"-INF\"^^xsd:double signum >>", "-1" );
    }
}