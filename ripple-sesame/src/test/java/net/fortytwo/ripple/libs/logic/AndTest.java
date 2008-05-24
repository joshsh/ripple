package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class AndTest extends NewRippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "true true and >>", "true" );
        assertReducesTo( "true false and >>", "false" );
        assertReducesTo( "false true and >>", "false" );
        assertReducesTo( "false false and >>", "false" );
    }

    public void testNonBooleanValues() throws Exception
    {
        assertReducesTo( "true 42 and >>", "false" );
        assertReducesTo( "false 42 and >>", "false" );
        assertReducesTo( "42 true and >>", "false" );
        assertReducesTo( "42 false and >>", "false" );
        assertReducesTo( "42 42 and >>", "false" );
    }
}