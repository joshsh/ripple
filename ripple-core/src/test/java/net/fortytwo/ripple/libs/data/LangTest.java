package net.fortytwo.ripple.libs.data;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * User: josh
 * Date: 4/10/11
 * Time: 3:08 AM
 */
public class LangTest extends RippleTestCase {
    public void testAll() throws Exception {
        reduce("@prefix xml: <http://www.w3.org/XML/1998/namespace#>");

        assertReducesTo("lang", "xml:lang");

        assertReducesTo("\"vierzig\"@de xml:lang.", "\"de\"");
        assertReducesTo("\"vierzig\"@de lang.", "\"de\"");
        assertReducesTo("\"vierzig\" lang.");
        assertReducesTo("() lang.");
    }
}
