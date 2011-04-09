package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class BranchTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "1 true id neg branch.", "1" );
        assertReducesTo( "1 false id neg branch.", "-1" );
    }

    public void testNonBooleanValues() throws Exception
    {
        assertReducesTo( "1 42 id neg branch.", "-1" );
    }
}