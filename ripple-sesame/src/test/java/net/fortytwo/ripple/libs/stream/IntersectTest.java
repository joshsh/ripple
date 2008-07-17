package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class IntersectTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "((1 2) each >>) (1 sqrt >>) intersect >>", "1" );
        assertReducesTo( "((1 -1) each >>) (1 sqrt >>) intersect >>", "1", "-1" );
    }

    public void testEffectsRestOfStack() throws Exception
    {
        // 'intersect' consumes only two arguments, but its arguments have
        // individual effects on the rest of the stack.
        assertReducesTo( "(1 2) each (each >> 2 mul >>) intersect >>", "2" );
    }
}