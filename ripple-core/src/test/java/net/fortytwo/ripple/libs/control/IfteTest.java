package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class IfteTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("42 (true) (1) (2) ifte.", "42 1");
        assertReducesTo("42 (false) (1) (2) ifte.", "42 2");

        // Even though the criterion (pop. true) removes the 42 from the head
        // of the stack, this effect is undone by stack restoration.
        assertReducesTo("42 (pop. true) (\"yes\") (\"no\") ifte.", "42 \"yes\"");

        // Again, make sure the criterion has no effect on the final stack.
        assertReducesTo("42 (2 sub. 40 equal.) (2 mul.) id ifte.", "84");
    }

    @Test
    public void testNonBooleanValues() throws Exception {
        assertReducesTo("42 (108) (1) (2) ifte.", "42 2");
    }
}