package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ReplaceAllTest extends RippleTestCase {
    @Test
    public void testReplacementText() throws Exception {
        assertReducesTo("\"Mississippi\" \"ss\" \".\" replaceAll.", "\"Mi.i.ippi\"");
        assertReducesTo("\"Mississippi\" \"ss\" \"th\" replaceAll.", "\"Mithithippi\"");
    }

    @Test
    public void testQuantifierGreediness() throws Exception {
        assertReducesTo("\"Mississippi\" \"s\" \".\" replaceAll.", "\"Mi..i..ippi\"");
        assertReducesTo("\"Mississippi\" \"s*\" \".\" replaceAll.", "\".M.i..i..i.p.p.i.\"");

        assertReducesTo("\"Mississippi\" \"i.*s\" \".\" replaceAll.", "\"M.ippi\"");
    }

    @Test
    public void testOrderOfReplacement() throws Exception {
        // Potential infinite loop avoided by skipping past replacement text
        assertReducesTo("\"Mississippi\" \"Miss\" \"aMiss\" replaceAll.", "\"aMississippi\"");

        // Apparent conflict avoided by skipping past replacement text
        assertReducesTo("\"Mississippi\" \"issi\" \".\" replaceAll.", "\"M.ssippi\"");
    }

    @Test
    public void testEmptyStrings() throws Exception {
        assertReducesTo("\"\" \"one\" \"two\" replaceAll.", "\"\"");
        assertReducesTo("\"Mississippi\" \"s\" \"\" replaceAll.", "\"Miiippi\"");
        assertReducesTo("\"Mississippi\" \"\" \".\" replaceAll.", "\".M.i.s.s.i.s.s.i.p.p.i.\"");
    }
}
