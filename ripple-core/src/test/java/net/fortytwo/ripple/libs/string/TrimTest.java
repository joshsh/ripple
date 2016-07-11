package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class TrimTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("\"one two\" trim.", "\"one two\"");
        assertReducesTo("\" one two\" trim.", "\"one two\"");
        assertReducesTo("\"one two \" trim.", "\"one two\"");
        assertReducesTo("\"\\n\\tone two \\t\" trim.", "\"one two\"");
    }

    @Test
    public void testEmptyStrings() throws Exception {
        assertReducesTo("\"\" trim.", "\"\"");
    }
}
