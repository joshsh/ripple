package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LengthTest extends RippleTestCase {
    @Test
    public void testAll() throws Exception {
        assertReducesTo("\"one\" length.", "3");
        assertReducesTo("\"\" length.", "0");
    }

    // FIXME: string functions are currently applicable to all literals,
    // whereas they should really be limited to xsd:string values.
    @Test
    public void testNonStrings() throws Exception {
        assertReducesTo("42 length.", "2");
    }
}
