package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class MembersTest extends RippleTestCase {
    @Test
    public void testSimple() throws Exception {
        reduce("@prefix ex: <http://example.org/membersTest/>\n"
                + "ex:a rdf:_1 \"first\"^^xsd:string assert."
                + "    rdf:_2 \"second\" assert."
                + "    rdf:_2 \"second (duplicate)\" assert."
                + "    rdf:_3 3 assert."
                + "    rdf:_27 \"27\"^^xsd:double assert.");
        assertReducesTo("ex:a members.", "\"first\"^^xsd:string", "\"second\"", "\"second (duplicate)\"", "3", "27");
    }
}
