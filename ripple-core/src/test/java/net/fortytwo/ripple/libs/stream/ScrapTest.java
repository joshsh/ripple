package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ScrapTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("scrap.");
        assertReducesTo("42 scrap.");
        assertReducesTo("(1 2 3) each. scrap.");
    }
}
