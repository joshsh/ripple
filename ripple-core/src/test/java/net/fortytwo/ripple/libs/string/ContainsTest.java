package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ContainsTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("\"Mississippi\" \"ippi\" contains.", "true");
        assertReducesTo("\"Mississippi\" \"ippi\" contains.", "true");
        assertReducesTo("\"Mississippi\" \"hippie\" contains.", "false");
    }

    @Test
    public void testCaseSensitivity() throws Exception {
        assertReducesTo("\"Mississippi\" \"miss\" contains.", "false");
        assertReducesTo("\"Mississippi\" \"Sissi\" contains.", "false");
    }

    @Test
    public void testEmptyStrings() throws Exception {
        assertReducesTo("\"Mississippi\" \"\" contains.", "true");
        assertReducesTo("\"\" \"ippi\" contains.", "false");
        assertReducesTo("\"\" \"\" contains.", "true");
    }
}
