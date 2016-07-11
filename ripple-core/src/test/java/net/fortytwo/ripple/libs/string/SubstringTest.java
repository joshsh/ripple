package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SubstringTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("\"Mississippi\" 0 11 substring.", "\"Mississippi\"");
        assertReducesTo("\"Mississippi\" 1 8 substring.", "\"ississi\"");
    }

    @Test
    public void testOutOfRangeIndices() throws Exception {
        assertReducesTo("\"Mississippi\" -1 11 substring.");
        assertReducesTo("\"Mississippi\" 0 12 substring.");
    }

    @Test
    public void testEqualIndices() throws Exception {
        assertReducesTo("\"Mississippi\" 2 2 substring.", "\"\"");
    }

    @Test
    public void testOutOfOrderIndices() throws Exception {
        assertReducesTo("\"Mississippi\" 5 2 substring.");
    }

    @Test
    public void testEmptyStrings() throws Exception {
        assertReducesTo("\"\" 0 0 substring.", "\"\"");
    }
}
