package net.fortytwo.ripple.libs.data;

import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class CompareTest extends RippleTestCase {

    @Test
    public void testURIs() throws Exception {
        assertEq("rdfs:label", "rdfs:label");
        assertLt("rdfs:comment", "rdfs:label");
        assertGt("rdfs:label", "rdfs:comment");
    }

    @Test
    public void testNumericValues() throws Exception {
        assertEq("42", "42");
        assertEq("42", "42.00");
        assertEq("42e0", "42");
        assertLt("-1", "+1");
        assertGt("+1", "-1");

        assertEq("\"42\"^^xsd:double", "42");
        assertLt("\"41\"^^xsd:double", "42");
        assertEq("42", "\"42\"^^xsd:double");
        assertLt("41", "\"42\"^^xsd:double");

        assertEq("\"42.0\"^^xsd:double", "\"42\"^^xsd:integer");
        assertGt("\"42.1\"^^xsd:double", "\"42\"^^xsd:integer");
    }

    @Test
    public void testPlainLiterals() throws Exception {
        assertEq("\"\"", "\"\"");
        assertLt("\"\"", "\"foo\"");
        assertGt("\"foo\"", "\"\"");
        assertEq("\"foo\"", "\"foo\"");
        assertLt("\"bar\"", "\"foo\"");
        assertGt("\"foo\"", "\"bar\"");

        assertGt("\"foo\"", "\"foo\"@en");
        assertGt("\"foobar\"", "\"foo\"@en");
        assertLt("\"foo\"@en", "\"foo\"");
        assertLt("\"foo\"@en", "\"foobar\"");
        assertEq("\"foo\"@en", "\"foo\"@en");
        assertLt("\"foo\"@en", "\"foobar\"@en");
        assertLt("\"foo\"@de", "\"foo\"@en");
        assertLt("\"foobar\"@de", "\"foo\"@en");
        assertGt("\"foo\"@en", "\"foo\"@de");
        assertGt("\"foo\"@en", "\"foobar\"@de");
    }

    @Test
    public void testTypedLiterals() throws Exception {
        assertEq("\"\"^^xsd:string", "\"\"^^xsd:string");
        assertGt("\"foo\"^^xsd:string", "\"\"^^xsd:string");
        assertLt("\"\"^^xsd:string", "\"foo\"^^xsd:string");
        assertEq("\"foo\"^^xsd:string", "\"foo\"^^xsd:string");
        assertLt("\"bar\"^^xsd:string", "\"foo\"^^xsd:string");
        assertGt("\"foo\"^^xsd:string", "\"bar\"^^xsd:string");

        assertLt("\"http://example.org\"^^xsd:anyURI", "\"http://example.org\"^^xsd:string");
        assertGt("\"http://example.org\"^^xsd:string", "\"http://example.org\"^^xsd:anyURI");
    }

    @Test
    public void testBlankNodes() throws Exception {
        // TODO
    }

    @Test
    public void testLists() throws Exception {
        assertEq("()", "()");
        assertGt("(1)", "()");
        assertLt("()", "(42)");

        assertEq("(1 2 3)", "(1 2 3)");
        assertLt("(1 2 2)", "(1 2 3)");
        assertGt("(1 4 3)", "(1 2 3)");
        assertLt("(1 2 3)", "(1 2 3 4)");
        assertEq("(1 (2) 3)", "(1 (2) 3)");
        assertGt("(1 (4) 3)", "(1 (2) 3)");
        assertLt("(1 (2) 3)", "(1 (37) 3)");

        assertEq("()", "rdf:nil");
        assertGt("(42)", "rdf:nil");
        assertLt("rdf:nil", "(1 2 3)");
        reduce("@prefix : <http://example.org/compareTest/>\n"
                + "@list prog: 1 2 3");
        assertEq(":prog", "(1 2 3)");
        assertLt("rdf:nil", ":prog");
        assertGt(":prog", "rdf:nil");
        assertLt(":prog", "(1 2 4)");
        assertGt(":prog", "(1 2)");
    }

    // Note: for now, boolean values are simply Literals
    @Test
    public void testBooleans() throws Exception {
        assertEq("true", "true");
        assertGt("true", "false");
        assertLt("false", "true");
        assertEq("false", "false");
    }

    @Test
    public void testPrimitives() throws Exception {
        assertEq("dup", "dup");
        assertLt("dup", "swap");
        assertGt("swap", "dup");
    }

    @Test
    public void testHeterogeneousValues() throws Exception {
        // Compare plain literals with typed literals.
        assertGt("\"foo\"", "\"foo\"^^xsd:string");
        assertLt("\"foo\"^^xsd:string", "\"foo\"");

        // TODO: compare objects of different types, once the rules for doing so are better defined.
    }
}
