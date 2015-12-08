package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SplitTest extends RippleTestCase {
    public void testSimple() throws Exception {
        assertReducesTo("\"one, two,three\" \",[ ]*\" split.", "(\"one\" \"two\" \"three\")");
        assertReducesTo("\"Mississippi\" \"i\" split.", "(\"M\" \"ss\" \"ss\" \"pp\")");
        assertReducesTo("\"Mississippi\" \"ss\" split.", "(\"Mi\" \"i\" \"ippi\")");
        assertReducesTo("\"Mississippi\" \"Miss\" split.", "(\"\" \"issippi\")");
        assertReducesTo("\"Mississippi\" \"ippi\" split.", "(\"Mississ\")");
    }

    public void testNomatch() throws Exception {
        assertReducesTo("\"Mississippi\" \"mountain\" split.", "(\"Mississippi\")");
    }

    public void testQuantifierGreediness() throws Exception {
        assertReducesTo("\"Mississippi\" \"i.*i\" split.", "(\"M\")");
    }

    public void testConflict() throws Exception {
        assertReducesTo("\"Mississippi\" \"issi\" split.", "(\"M\" \"ssippi\")");
    }

    public void testEmptyStrings() throws Exception {
        // note: different behavior with JDK 1.7 vs JDK 1.8 (expect "" vs. "M")
        //assertEquals("", "Mississippi".split("")[0]);

        assertReducesTo("\"Mississippi\" \"\" split.");
        assertReducesTo("\"\" \"\" split.");
        assertReducesTo("\"\" \"mountain\" split.", "(\"\")");
    }
}
