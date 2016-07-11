package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class AndTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("true true and.", "true");
        assertReducesTo("true false and.", "false");
        assertReducesTo("false true and.", "false");
        assertReducesTo("false false and.", "false");
    }

    @Test
    public void testNonBooleanValues() throws Exception {
        assertReducesTo("true 42 and.", "false");
        assertReducesTo("false 42 and.", "false");
        assertReducesTo("42 true and.", "false");
        assertReducesTo("42 false and.", "false");
        assertReducesTo("42 42 and.", "false");
    }

    @Test
    public void testInverse() throws Exception {
        assertReducesTo("true and~.", "true true");
        assertReducesTo("false and~.", "true false", "false true", "false false");
    }
}