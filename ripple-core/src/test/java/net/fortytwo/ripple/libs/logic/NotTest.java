package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class NotTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        assertReducesTo("true not.", "false");
        assertReducesTo("false not.", "true");
    }

    @Test
    public void testNonBooleanValues() throws Exception {
        assertReducesTo("42 not.", "true");
    }
}