package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class CountTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("1000 2000 both count.", "1000 2000 2");
        assertReducesTo("\"One, two, three stacks. Ah ah ah!\" (\",\" split. each.) count.",
                "\"One, two, three stacks. Ah ah ah!\" 3");
    }

    @Test
    public void testInsufficientArguments() throws Exception {
        assertReducesTo("count.");
    }

    @Test
    public void testIneffectualMapping() throws Exception {
        assertReducesTo("1 2 count.", "1 0");
    }
}
