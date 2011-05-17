package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.test.RippleTestCase;

import java.util.Collection;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class IdTest extends RippleTestCase
{
    public void testAll() throws Exception
    {
        assertReducesTo( "42 id.", "42" );
    }

    public void testNilStack() throws Exception
    {
        Collection<RippleList> s = reduce("id.");
        assertEquals(1, s.size());
        assertTrue(s.iterator().next().isNil());
    }
}