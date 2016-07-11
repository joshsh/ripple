package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class InverseTest extends RippleTestCase {
    @Test
    public void testAll() throws Exception {
        // Invert primitive functions
        assertReducesTo("2 3 add inverse..", "-1");
        assertReducesTo("2 3 div inverse..", "6");

        // Invert an RDF predicate
        assertReducesTo("@relist foobar: 137 69\n"
                + "136 1 add. rdf:first inverse...", "137 69");
    }

    @Test
    public void testInverseListPredicates() throws Exception {
        reduce("@list lintilla42: 1331 137");

        // This results in three answers because both an RDF resource and a native list are matched by rdf:first,
        // and then again by rdf:rest.
        assertReducesTo("1331 rdf:first~. rdf:rest. rdf:first.", "137", "137", "137");
    }
}