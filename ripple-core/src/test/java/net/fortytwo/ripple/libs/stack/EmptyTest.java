package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class EmptyTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("(1 2 3) empty.", "false");
        assertReducesTo("() empty.", "true");
    }

    @Test
    public void testRDFLists() throws Exception {
        assertReducesTo("rdf:nil empty.", "true");

        reduce("@define list42: 1 2 3 .");
        assertReducesTo(":list42 empty.", "false");
    }

    @Test
    public void testImplicitLists() throws Exception {
        assertReducesTo("42 empty.", "false");
        assertReducesTo("42 dup cat. empty.", "false");
    }
}
