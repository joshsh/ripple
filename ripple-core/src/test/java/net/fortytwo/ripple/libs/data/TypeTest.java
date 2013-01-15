package net.fortytwo.ripple.libs.data;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class TypeTest extends RippleTestCase {
    public void testAll() throws Exception {
        assertReducesTo("type", "xsd:type");
        assertReducesTo("42 type.", "xsd:integer");
        assertReducesTo("42 xsd:type.", "xsd:integer");
        assertReducesTo("\"foo\" toString. type.", "xsd:string");
        assertReducesTo("() type.");
    }
}
