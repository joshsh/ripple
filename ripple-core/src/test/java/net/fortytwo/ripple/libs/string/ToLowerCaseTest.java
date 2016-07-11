package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ToLowerCaseTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("\"BrainF@#$ language\" to-lower-case.", "\"brainf@#$ language\"");
    }

    @Test
    public void testEmptyStrings() throws Exception {
        assertReducesTo("\"\" to-lower-case.", "\"\"");
    }
}
