package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LengthTest extends RippleTestCase {
    public void testAll() throws Exception {
        assertReducesTo("\"one\" length.", "3");
        assertReducesTo("\"\" length.", "0");
    }

    // FIXME: string functions are currently applicable to all literals,
    // whereas they should really be limited to xsd:string values.
    public void testNonStrings() throws Exception {
        assertReducesTo("42 length.", "2");
    }
}
