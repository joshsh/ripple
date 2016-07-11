package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class BothTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("1 2 both.", "1", "2");
        assertReducesTo("1 1 both.", "1", "1");
    }
}
