package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.NewRippleTestCase;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class DenyInTest extends NewRippleTestCase
{
    public void testSimple() throws Exception
    {
        reduce( "@prefix ex: <http://example.org/denyInTest/> ." );

        assertReducesTo( "ex:a rdf:type ex:ClassA ex:ClassB both >> ex:ctx1 assertIn >>", "ex:a", "ex:a" );
        assertReducesTo( "ex:a rdf:type >>", "ex:ClassA", "ex:ClassB" );
        assertReducesTo( "ex:a rdf:type ex:ctx1 inContext >> >>", "ex:ClassA", "ex:ClassB" );
        assertReducesTo( "ex:ClassA rdf:type <<", "ex:a" );
        assertReducesTo( "ex:ClassA rdf:type ex:ctx1 inContext >> <<", "ex:a" );
        assertReducesTo( "ex:ClassB rdf:type <<", "ex:a" );
        assertReducesTo( "ex:ClassB rdf:type ex:ctx1 inContext >> <<", "ex:a" );
        assertReducesTo( "ex:a rdf:type ex:ClassC ex:ctx2 denyIn >>", "ex:a" );
        assertReducesTo( "ex:a rdf:type >>", "ex:ClassA", "ex:ClassB" );
        assertReducesTo( "ex:a rdf:type ex:ctx1 inContext >> >>", "ex:ClassA", "ex:ClassB" );
        assertReducesTo( "ex:ClassA rdf:type <<", "ex:a" );
        assertReducesTo( "ex:ClassA rdf:type ex:ctx1 inContext >> <<", "ex:a" );
        assertReducesTo( "ex:ClassB rdf:type <<", "ex:a" );
        assertReducesTo( "ex:ClassB rdf:type ex:ctx1 inContext >> <<", "ex:a" );
        assertReducesTo( "ex:a rdf:type ex:ClassA ex:ctx1 denyIn >>", "ex:a" );
        assertReducesTo( "ex:a rdf:type >>", "ex:ClassB" );
        assertReducesTo( "ex:a rdf:type ex:ctx1 inContext >> >>", "ex:ClassB" );
        assertReducesTo( "ex:ClassA rdf:type <<" );
        assertReducesTo( "ex:ClassA rdf:type ex:ctx1 inContext >> <<" );
        assertReducesTo( "ex:ClassB rdf:type <<", "ex:a" );
        assertReducesTo( "ex:ClassB rdf:type ex:ctx1 inContext >> <<", "ex:a" );

        modelConnection.remove( null, null, modelConnection.list() );
        modelConnection.commit();
        assertReducesTo( "ex:b rdf:type () ex:ctx1 assertIn >>", "ex:b" );
        assertReducesTo( "ex:b rdf:type >>", "()" );
        assertReducesTo( "ex:b rdf:type ex:ctx1 inContext >> >>", "()" );
        assertReducesTo( "() rdf:type <<", "ex:b" );
        assertReducesTo( "() rdf:type ex:ctx1 inContext >> <<", "ex:b" );
        assertReducesTo( "ex:b rdf:type () ex:ctx1 denyIn >>", "ex:b" );
        assertReducesTo( "ex:b rdf:type >>" );
        assertReducesTo( "ex:b rdf:type ex:ctx1 inContext >> >>" );
        assertReducesTo( "() rdf:type <<" );
        assertReducesTo( "() rdf:type ex:ctx1 inContext >> <<" );

        assertReducesTo( "ex:c rdf:type (1 2) ex:ctx1 assertIn >>", "ex:c" );
        assertReducesTo( "ex:c rdf:type >>", "(1 2)" );
        assertReducesTo( "ex:c rdf:type ex:ctx1 inContext >> >>", "(1 2)" );
        // Equality does not imply identity in statement queries.
        assertReducesTo( "ex:c rdf:type (1 2) ex:ctx1 denyIn >>", "ex:c" );
        assertReducesTo( "ex:c rdf:type >>", "(1 2)" );
        assertReducesTo( "ex:c rdf:type ex:ctx1 inContext >> >>", "(1 2)" );
    }

    public void testLiteralObjects() throws Exception {
        reduce( "@prefix ex: <http://example.org/denyInTest/>." );

        modelConnection.remove( null, null, modelConnection.value( 42 ) );
        modelConnection.commit();
        assertReducesTo( "ex:a ex:specialNumer 42 ex:ctx1 assertIn >>", "ex:a" );
        assertReducesTo( "ex:a ex:specialNumer >>", "42" );
        assertReducesTo( "ex:a ex:specialNumer ex:ctx1 inContext >> >>", "42" );
        assertReducesTo( "42 ex:specialNumer <<", "ex:a" );
        assertReducesTo( "42 ex:specialNumer ex:ctx1 inContext >> <<", "ex:a" );
        assertReducesTo( "ex:a ex:specialNumer 42 ex:ctx1 denyIn >>", "ex:a" );
        assertReducesTo( "ex:a ex:specialNumer >>" );
        assertReducesTo( "ex:a ex:specialNumer ex:ctx1 inContext >> >>" );
        assertReducesTo( "42 ex:specialNumer <<" );
        assertReducesTo( "42 ex:specialNumer ex:ctx1 inContext >> <<" );
        // Equality does not imply identity in statement queries.
        assertReducesTo( "ex:b ex:specialNumer 42 ex:ctx1 assertIn >>", "ex:b" );
        assertReducesTo( "ex:b ex:specialNumer 42.0 ex:ctx1 denyIn >>", "ex:b" );
        assertReducesTo( "ex:b ex:specialNumer >>", "42" );
        assertReducesTo( "42 ex:specialNumer <<", "ex:b" );

        modelConnection.remove( null, null, modelConnection.value( "something" ) );
        modelConnection.commit();
        assertReducesTo( "ex:a rdfs:comment \"something\" ex:ctx1 assertIn >>", "ex:a" );
        assertReducesTo( "ex:a rdfs:comment >>", "\"something\"" );
        assertReducesTo( "ex:a rdfs:comment ex:ctx1 inContext >> >>", "\"something\"" );
        assertReducesTo( "\"something\" rdfs:comment <<", "ex:a" );
        assertReducesTo( "\"something\" rdfs:comment ex:ctx1 inContext >> <<", "ex:a" );
        assertReducesTo( "ex:a rdfs:comment \"something\" ex:ctx1 denyIn >>", "ex:a" );
        assertReducesTo( "ex:a rdfs:comment >>" );
        assertReducesTo( "ex:a rdfs:comment ex:ctx1 inContext >> >>" );
        assertReducesTo( "\"something\" rdfs:comment <<" );
        assertReducesTo( "\"something\" rdfs:comment ex:ctx1 inContext >> <<" );

        modelConnection.remove( null, null, modelConnection.value( "something", XMLSchema.STRING ) );
        modelConnection.commit();
        assertReducesTo( "ex:a rdfs:label \"something\"^^xsd:string ex:ctx1 assertIn >>", "ex:a" );
        assertReducesTo( "ex:a rdfs:label >>", "\"something\"^^xsd:string" );
        assertReducesTo( "ex:a rdfs:label ex:ctx1 inContext >> >>", "\"something\"^^xsd:string" );
        assertReducesTo( "\"something\"^^xsd:string rdfs:label <<", "ex:a" );
        assertReducesTo( "\"something\"^^xsd:string rdfs:label ex:ctx1 inContext >> <<", "ex:a" );
        assertReducesTo( "ex:a rdfs:label \"something\" ex:ctx1 denyIn >>", "ex:a" );
        assertReducesTo( "ex:a rdfs:label >>", "\"something\"^^xsd:string" );
        assertReducesTo( "ex:a rdfs:label ex:ctx1 inContext >> >>", "\"something\"^^xsd:string" );
        assertReducesTo( "\"something\"^^xsd:string rdfs:label <<", "ex:a" );
        assertReducesTo( "\"something\"^^xsd:string rdfs:label ex:ctx1 inContext >> <<", "ex:a" );
        assertReducesTo( "ex:a rdfs:label \"something\"^^xsd:string ex:ctx1 denyIn >>", "ex:a" );
        assertReducesTo( "ex:a rdfs:comment >>" );
        assertReducesTo( "ex:a rdfs:comment ex:ctx1 inContext >> >>" );
        assertReducesTo( "\"something\"^^xsd:string rdfs:label <<" );
        assertReducesTo( "\"something\"^^xsd:string rdfs:label ex:ctx1 inContext >> <<" );
    }

    public void testNullContext() throws Exception {
        reduce( "@prefix ex: <http://example.org/assertInTest/>." );

        modelConnection.remove( null, null, modelConnection.value( "q" ) );
        modelConnection.commit();
        assertReducesTo( "ex:q rdfs:label \"q\" () assertIn >>", "ex:q" );
        assertReducesTo( "ex:q rdfs:label () inContext >> >>", "\"q\"" );
        assertReducesTo( "ex:q rdfs:label >>", "\"q\"" );
        assertReducesTo( "ex:q rdfs:label \"q\" () denyIn >>", "ex:q" );
        assertReducesTo( "ex:q rdfs:label () inContext >> >>" );
        assertReducesTo( "ex:q rdfs:label >>" );
    }
}