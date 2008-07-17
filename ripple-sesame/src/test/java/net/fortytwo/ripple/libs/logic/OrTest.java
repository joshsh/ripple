package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class OrTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "true true or >>", "true" );
        assertReducesTo( "true false or >>", "true" );
        assertReducesTo( "false true or >>", "true" );
        assertReducesTo( "false false or >>", "false" );
    }

    public void testNonBooleanValues() throws Exception
    {
        assertReducesTo( "true 42 or >>", "true" );
        assertReducesTo( "false 42 or >>", "false" );
        assertReducesTo( "42 true or >>", "true" );
        assertReducesTo( "42 false or >>", "false" );
        assertReducesTo( "42 42 or >>", "false" );
    }
}