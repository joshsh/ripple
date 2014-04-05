package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.RippleTestCase;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class DenyInContextTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        reduce("@prefix ex: <http://example.org/denyInTest/> .");

        assertReducesTo( "ex:a rdf:type ex:ClassA ex:ClassB both. ex:ctx1 assert-in-context.", "ex:a", "ex:a" );
        assertReducesTo( "ex:a rdf:type.", "ex:ClassA", "ex:ClassB" );
        assertReducesTo( "ex:a rdf:type ex:ctx1 in-context.", "ex:ClassA", "ex:ClassB" );
        assertReducesTo( "ex:ClassA rdf:type~.", "ex:a" );
        assertReducesTo( "ex:ClassA rdf:type ex:ctx1 in-context~.", "ex:a" );
        assertReducesTo( "ex:ClassB rdf:type~.", "ex:a" );
        assertReducesTo( "ex:ClassB rdf:type ex:ctx1 in-context~.", "ex:a" );
        assertReducesTo( "ex:a rdf:type ex:ClassC ex:ctx2 deny-in-context.", "ex:a" );
        assertReducesTo( "ex:a rdf:type.", "ex:ClassA", "ex:ClassB" );
        assertReducesTo( "ex:a rdf:type ex:ctx1 in-context.", "ex:ClassA", "ex:ClassB" );
        assertReducesTo( "ex:ClassA rdf:type~.", "ex:a" );
        assertReducesTo( "ex:ClassA rdf:type ex:ctx1 in-context~.", "ex:a" );
        assertReducesTo( "ex:ClassB rdf:type~.", "ex:a" );
        assertReducesTo( "ex:ClassB rdf:type ex:ctx1 in-context~.", "ex:a" );
        assertReducesTo( "ex:a rdf:type ex:ClassA ex:ctx1 deny-in-context.", "ex:a" );
        assertReducesTo( "ex:a rdf:type.", "ex:ClassB" );
        assertReducesTo( "ex:a rdf:type ex:ctx1 in-context.", "ex:ClassB" );
        assertReducesTo( "ex:ClassA rdf:type~." );
        assertReducesTo( "ex:ClassA rdf:type ex:ctx1 in-context~." );
        assertReducesTo( "ex:ClassB rdf:type~.", "ex:a" );
        assertReducesTo( "ex:ClassB rdf:type ex:ctx1 in-context~.", "ex:a" );

        modelConnection.remove( null, null, modelConnection.list() );
        modelConnection.commit();
        assertReducesTo( "ex:b rdf:type () ex:ctx1 assert-in-context.", "ex:b" );
        assertReducesTo( "ex:b rdf:type.", "()" );
        assertReducesTo( "ex:b rdf:type ex:ctx1 in-context.", "()" );
        assertReducesTo( "() rdf:type~.", "ex:b" );
        assertReducesTo( "() rdf:type ex:ctx1 in-context~.", "ex:b" );
        assertReducesTo( "ex:b rdf:type () ex:ctx1 deny-in-context.", "ex:b" );
        assertReducesTo( "ex:b rdf:type." );
        assertReducesTo( "ex:b rdf:type ex:ctx1 in-context." );
        assertReducesTo( "() rdf:type~." );
        assertReducesTo( "() rdf:type ex:ctx1 in-context~." );
    }

    public void testLiteralObjects() throws Exception {
        reduce("@prefix ex: <http://example.org/deny-in-contextTest/>.");

        modelConnection.remove( null, null, modelConnection.valueOf(42) );
        modelConnection.commit();
        assertReducesTo( "ex:a ex:specialNumer 42 ex:ctx1 assert-in-context.", "ex:a" );
        assertReducesTo( "ex:a ex:specialNumer.", "42" );
        assertReducesTo( "ex:a ex:specialNumer ex:ctx1 in-context.", "42" );
        assertReducesTo( "42 ex:specialNumer~.", "ex:a" );
        assertReducesTo( "42 ex:specialNumer ex:ctx1 in-context~.", "ex:a" );
        assertReducesTo( "ex:a ex:specialNumer 42 ex:ctx1 deny-in-context.", "ex:a" );
        assertReducesTo( "ex:a ex:specialNumer." );
        assertReducesTo( "ex:a ex:specialNumer ex:ctx1 in-context." );
        assertReducesTo( "42 ex:specialNumer~." );
        assertReducesTo( "42 ex:specialNumer ex:ctx1 in-context~." );
        // Equality does not imply identity in statement queries.
        assertReducesTo( "ex:b ex:specialNumer 42 ex:ctx1 assert-in-context.", "ex:b" );
        assertReducesTo( "ex:b ex:specialNumer 42.0 ex:ctx1 deny-in-context.", "ex:b" );
        assertReducesTo( "ex:b ex:specialNumer.", "42" );
        assertReducesTo( "42 ex:specialNumer~.", "ex:b" );

        modelConnection.remove( null, null, modelConnection.valueOf("something") );
        modelConnection.commit();
        assertReducesTo( "ex:a rdfs:comment \"something\" ex:ctx1 assert-in-context.", "ex:a" );
        assertReducesTo( "ex:a rdfs:comment.", "\"something\"" );
        assertReducesTo( "ex:a rdfs:comment ex:ctx1 in-context.", "\"something\"" );
        assertReducesTo( "\"something\" rdfs:comment~.", "ex:a" );
        assertReducesTo( "\"something\" rdfs:comment ex:ctx1 in-context~.", "ex:a" );
        assertReducesTo( "ex:a rdfs:comment \"something\" ex:ctx1 deny-in-context.", "ex:a" );
        assertReducesTo( "ex:a rdfs:comment." );
        assertReducesTo( "ex:a rdfs:comment ex:ctx1 in-context." );
        assertReducesTo( "\"something\" rdfs:comment~." );
        assertReducesTo( "\"something\" rdfs:comment ex:ctx1 in-context~." );

        modelConnection.remove( null, null, modelConnection.valueOf("something", XMLSchema.STRING) );
        modelConnection.commit();
        assertReducesTo( "ex:a rdfs:label \"something\"^^xsd:string ex:ctx1 assert-in-context.", "ex:a" );
        assertReducesTo( "ex:a rdfs:label.", "\"something\"^^xsd:string" );
        assertReducesTo( "ex:a rdfs:label ex:ctx1 in-context.", "\"something\"^^xsd:string" );
        assertReducesTo( "\"something\"^^xsd:string rdfs:label~.", "ex:a" );
        assertReducesTo( "\"something\"^^xsd:string rdfs:label ex:ctx1 in-context~.", "ex:a" );
        assertReducesTo( "ex:a rdfs:label \"something\" ex:ctx1 deny-in-context.", "ex:a" );
        assertReducesTo( "ex:a rdfs:label.", "\"something\"^^xsd:string" );
        assertReducesTo( "ex:a rdfs:label ex:ctx1 in-context.", "\"something\"^^xsd:string" );
        assertReducesTo( "\"something\"^^xsd:string rdfs:label~.", "ex:a" );
        assertReducesTo( "\"something\"^^xsd:string rdfs:label ex:ctx1 in-context~.", "ex:a" );
        assertReducesTo( "ex:a rdfs:label \"something\"^^xsd:string ex:ctx1 deny-in-context.", "ex:a" );
        assertReducesTo( "ex:a rdfs:comment." );
        assertReducesTo( "ex:a rdfs:comment ex:ctx1 in-context." );
        assertReducesTo( "\"something\"^^xsd:string rdfs:label~." );
        assertReducesTo( "\"something\"^^xsd:string rdfs:label ex:ctx1 in-context~." );
    }

    public void testNullContext() throws Exception {
        reduce("@prefix ex: <http://example.org/assert-in-contextTest/>.");

        modelConnection.remove( null, null, modelConnection.valueOf("q") );
        modelConnection.commit();
        assertReducesTo( "ex:q rdfs:label \"q\" () assert-in-context.", "ex:q" );
        assertReducesTo( "ex:q rdfs:label () in-context.", "\"q\"" );
        assertReducesTo( "ex:q rdfs:label.", "\"q\"" );
        assertReducesTo( "ex:q rdfs:label \"q\" () deny-in-context.", "ex:q" );
        assertReducesTo( "ex:q rdfs:label () in-context." );
        assertReducesTo( "ex:q rdfs:label." );
    }
}
