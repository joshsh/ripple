package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class ChoiceTest extends NewRippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "true 1 2 choice >>", "1" );
        assertReducesTo( "false 1 2 choice >>", "2" );
        assertReducesTo( "42 1 2 choice >>", "2" );
    }

    public void testNonBooleanValues() throws Exception
    {
        assertReducesTo( "42 1 2 choice >>", "2" );
    }
}