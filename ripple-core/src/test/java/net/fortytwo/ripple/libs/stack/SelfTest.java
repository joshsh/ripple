package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SelfTest extends RippleTestCase {
    @Test
    public void testAll() throws Exception {
        assertReducesTo("42 self.", "42");
    }

    @Test
    public void testNilStack() throws Exception {
        Collection<RippleList> s = reduce("self.");
        assertEquals(1, s.size());
        assertTrue(s.iterator().next().isNil());
    }
}