package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ToUpperCaseTest extends RippleTestCase {
    public void testSimple() throws Exception {
        assertReducesTo("\"BrainF@#$ language\" to-upper-case.", "\"BRAINF@#$ LANGUAGE\"");
    }

    public void testEmptyStrings() throws Exception {
        assertReducesTo("\"\" to-upper-case.", "\"\"");
    }
}
