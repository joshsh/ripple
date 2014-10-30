package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.test.RippleTestCase;

import java.util.Collection;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SelfTest extends RippleTestCase {
    public void testAll() throws Exception {
        assertReducesTo("42 self.", "42");
    }

    public void testNilStack() throws Exception {
        Collection<RippleList> s = reduce("self.");
        assertEquals(1, s.size());
        assertTrue(s.iterator().next().isNil());
    }
}