package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class NotTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "true not >>", "false" );
        assertReducesTo( "false not >>", "true" );
    }

    public void testNonBooleanValues() throws Exception
    {
        assertReducesTo( "42 not >>", "true" );
    }
}