package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class EachTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("(1 2 3) each.", "1", "2", "3");
        assertReducesTo("() each.");
    }

    @Test
    public void testListEquivalence() throws Exception {
        assertReducesTo("rdf:nil each.");
        assertReducesTo("2 each.", "2");
        assertReducesTo("dup each.", "dup");
    }

    @Test
    public void testTransparency() throws Exception {
        // Lists are currently not transparent to 'each'
        assertReducesTo("(2 dup.) each.", "2", "dup");
    }
}
