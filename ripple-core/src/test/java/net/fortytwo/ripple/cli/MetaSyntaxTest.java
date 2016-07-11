package net.fortytwo.ripple.cli;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * User: josh
 * Date: 4/8/11
 * Time: 11:59 PM
 */
public class MetaSyntaxTest extends RippleTestCase {
    @Test
    public void testLineContinuation() throws Exception {
        assertReducesTo("2 3 add.", "5");
        assertReducesTo("2 \\\n 3 add.", "5");
        assertReducesTo("2 \\ \n  \t\\\n 3     \\\nadd.", "5");

        assertReducesTo("2 3\\\nadd.\n2\\\n4\\\n\n", "5", "2 4");
    }

    @Test
    public void testFalseLineContinuation() throws Exception {
        assertReducesTo("2 \\\n\n 3 add.", "2");
        assertReducesTo("2 \\ \n \n 3", "2", "3");
    }
}
