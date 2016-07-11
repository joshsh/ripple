package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class TopTest extends RippleTestCase {
    @Test
    public void testAll() throws Exception {
        assertReducesTo("42 top.", "42");
        assertReducesTo("1 2 3 top.", "3");
        assertReducesTo("top.");
    }
}