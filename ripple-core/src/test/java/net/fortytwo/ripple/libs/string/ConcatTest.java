package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ConcatTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("\"wilde\" \"beest\" concat.", "\"wildebeest\"");
    }

    @Test
    public void testEmptyStrings() throws Exception {
        assertReducesTo("\"wilde\" \"\" concat.", "\"wilde\"");
        assertReducesTo("\"\" \"beest\" concat.", "\"beest\"");
        assertReducesTo("\"\" \"\" concat.", "\"\"");
    }
}
