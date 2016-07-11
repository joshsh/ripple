package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class BranchTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("1 true id neg branch.", "1");
        assertReducesTo("1 false id neg branch.", "-1");
    }

    @Test
    public void testNonBooleanValues() throws Exception {
        assertReducesTo("1 42 id neg branch.", "-1");
    }
}