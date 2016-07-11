package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class IntersectTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("((1 2) each.) (1 sqrt.) intersect.", "1");
        assertReducesTo("((1 -1) each.) (1 sqrt.) intersect.", "1", "-1");
    }

    @Test
    public void testEffectsRestOfStack() throws Exception {
        // 'intersect' consumes only two arguments, but its arguments have
        // individual effects on the rest of the stack.
        assertReducesTo("(1 2) each (each. 2 mul.) intersect.", "2");
    }
}
