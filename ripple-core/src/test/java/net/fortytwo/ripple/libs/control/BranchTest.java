package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
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