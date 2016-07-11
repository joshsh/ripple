package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ToUpperCaseTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("\"BrainF@#$ language\" to-upper-case.", "\"BRAINF@#$ LANGUAGE\"");
    }

    @Test
    public void testEmptyStrings() throws Exception {
        assertReducesTo("\"\" to-upper-case.", "\"\"");
    }
}
