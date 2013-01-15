package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.RippleTestCase;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class AssertInContextTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        reduce("@prefix ex: <http://example.org/assert-in-context-test/>");
        assertReducesTo( "ex:a rdf:type." );

        assertReducesTo( "ex:a rdf:type ex:Example ex:ctx1 assert-in-context.", "ex:a" );
        assertReducesTo( "ex:a rdf:type.", "ex:Example" );
        assertReducesTo( "ex:a rdf:type ex:ctx1 inContext.", "ex:Example" );
        assertReducesTo( "ex:a rdf:type ex:ctx2 inContext." );
        assertReducesTo( "ex:Example rdf:type ex:ctx1 in-context~.", "ex:a" );
        assertReducesTo( "ex:Example rdf:type ex:ctx2 in-context~." );

        // Asserted statements are not mutually exclusive.
        assertReducesTo( "ex:a rdf:type ex:Thing ex:ctx1 assert-in-context.", "ex:a" );
        assertReducesTo( "ex:a rdf:type ex:ctx1 in-context.", "ex:Example", "ex:Thing" );
        assertReducesTo( "ex:Thing rdf:type ex:ctx1 in-context~.", "ex:a" );
        assertReducesTo( "ex:Thing rdf:type ex:ctx2 in-context~." );

        modelConnection.remove( null, null, modelConnection.list() );
        modelConnection.commit();
        assertReducesTo( "ex:b rdf:type () ex:ctx1 assert-in-context.", "ex:b" );
        assertReducesTo( "ex:b rdf:type ex:ctx1 in-context.", "rdf:nil" );
        assertReducesTo( "() rdf:type ex:ctx1 in-context~.", "ex:b" );
        assertReducesTo( "() rdf:type ex:ctx2 in-context~." );

        assertReducesTo( "ex:c rdf:type (1 2) ex:ctx1 assert-in-context.", "ex:c" );
        // The list (1 2) is not in the data store, as it was not interned.
        assertReducesTo( "ex:c rdf:type ex:ctx1 in-context. rdf:type." );
        // Equality does not imply identity in statement queries.
        assertReducesTo( "(1 2) rdf:type ex:ctx1 in-context~." );

        assertReducesTo( "@list blah: 1 2 3\n" +
                "ex:d rdf:type :blah ex:ctx1 assert-in-context.", "ex:d");
        assertReducesTo( "ex:d rdf:type ex:ctx1 in-context.", "(1 2 3)");
        // There are two results, because rdf:type matches against both the RDF resource and the native list.
        assertReducesTo( "ex:d rdf:type ex:ctx1 in-context. rdf:type.", "rdf:List", "rdf:List");

    }

    public void testLiteralObjects() throws Exception {
        reduce("@prefix ex: <http://example.org/assert-in-context-test/>");

        modelConnection.remove( null, null, modelConnection.numericValue(42) );
        modelConnection.commit();
        assertReducesTo( "ex:a ex:specialNumber ex:ctx1 in-context." );
        assertReducesTo( "ex:a ex:specialNumber 42 ex:ctx1 assert-in-context.", "ex:a" );
        assertReducesTo( "ex:a ex:specialNumber ex:ctx1 in-context.", "42" );
        assertReducesTo( "42 ex:specialNumber ex:ctx1 in-context~.", "ex:a" );
        // Equality does not imply identity in statement queries.
        assertReducesTo( "42.0 ex:specialNumber ex:ctx1 in-context~." );

        modelConnection.remove( null, null, modelConnection.typedValue("something", XMLSchema.STRING) );
        modelConnection.commit();
        assertReducesTo( "ex:a rdfs:comment ex:ctx1 in-context." );
        assertReducesTo( "ex:a rdfs:comment \"something\" ex:ctx1 assert-in-context.", "ex:a" );
        assertReducesTo( "ex:a rdfs:comment ex:ctx1 in-context.", "\"something\"" );
        assertReducesTo( "\"something\" rdfs:comment ex:ctx1 in-context~.", "ex:a" );

        modelConnection.remove( null, null, modelConnection.typedValue("something", XMLSchema.STRING) );
        modelConnection.commit();
        assertReducesTo( "ex:a rdfs:label ex:ctx1 in-context." );
        assertReducesTo( "ex:a rdfs:label \"something\"^^xsd:string ex:ctx1 assert-in-context.", "ex:a" );
        assertReducesTo( "ex:a rdfs:label ex:ctx1 in-context.", "\"something\"^^xsd:string" );
        assertReducesTo( "\"something\"^^xsd:string rdfs:label ex:ctx1 in-context~.", "ex:a" );
    }

    public void testNullContext() throws Exception {
        reduce("@prefix ex: <http://example.org/assert-in-context-test/>");

        modelConnection.remove( null, null, modelConnection.plainValue("q") );
        modelConnection.commit();
        assertReducesTo( "ex:q rdfs:label \"q\" () assert-in-context.", "ex:q" );
        assertReducesTo( "ex:q rdfs:label () in-context.", "\"q\"" );
        assertReducesTo( "ex:q rdfs:label.", "\"q\"" );
        assertReducesTo( "ex:q rdfs:label ex:wrongContext in-context." );
        assertReducesTo( "\"q\" rdfs:label () in-context~.", "ex:q" );
        assertReducesTo( "\"q\" rdfs:label~.", "ex:q" );
        assertReducesTo( "\"q\" rdfs:label ex:wrongContext in-context~." );
    }

    public void testImpossibleStatements() throws Exception {
        reduce("@prefix ex: <http://example.org/assert-in-context-test/>");

        // Bad subject.
        assertReducesTo( "42 rdfs:comment \"foo\" ex:ctx1 assert-in-context.", "42" );
        assertReducesTo( "42 rdfs:comment." );
        assertReducesTo( "\"foo\" rdfs:comment~." );

        // Bad predicate.
        assertReducesTo( "ex:a 42 \"foo\" ex:ctx1 assert-in-context.", "ex:a" );
        assertReducesTo( "ex:a 42." );
        assertReducesTo( "\"foo\" 42 ex:ctx1 in-context~." );

        // Bad context.
        assertReducesTo( "ex:b rdfs:comment \"foo\" 42 assert-in-context.", "ex:b" );
        assertReducesTo( "ex:b rdfs:comment." );
        assertReducesTo( "\"foo\" rdfs:comment~." );
    }
}