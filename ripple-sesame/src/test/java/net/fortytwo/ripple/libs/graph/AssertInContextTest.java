package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.RippleTestCase;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class AssertInContextTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        reduce( "@prefix ex: <http://example.org/assertInTest/>." );
        assertReducesTo( "ex:a rdf:type >>" );

        assertReducesTo( "ex:a rdf:type ex:Example ex:ctx1 assertInContext >>", "ex:a" );
        assertReducesTo( "ex:a rdf:type >>", "ex:Example" );
        assertReducesTo( "ex:a rdf:type ex:ctx1 inContext >>", "ex:Example" );
        assertReducesTo( "ex:a rdf:type ex:ctx2 inContext >>" );
        assertReducesTo( "ex:Example rdf:type ex:ctx1 inContext <<", "ex:a" );
        assertReducesTo( "ex:Example rdf:type ex:ctx2 inContext <<" );

        // Asserted statements are not mutually exclusive.
        assertReducesTo( "ex:a rdf:type ex:Thing ex:ctx1 assertInContext >>", "ex:a" );
        assertReducesTo( "ex:a rdf:type ex:ctx1 inContext >>", "ex:Example", "ex:Thing" );
        assertReducesTo( "ex:Thing rdf:type ex:ctx1 inContext <<", "ex:a" );
        assertReducesTo( "ex:Thing rdf:type ex:ctx2 inContext <<" );

        modelConnection.remove( null, null, modelConnection.list() );
        modelConnection.commit();
        assertReducesTo( "ex:b rdf:type () ex:ctx1 assertInContext >>", "ex:b" );
        assertReducesTo( "ex:b rdf:type ex:ctx1 inContext >>", "rdf:nil" );
        assertReducesTo( "() rdf:type ex:ctx1 inContext <<", "ex:b" );
        assertReducesTo( "() rdf:type ex:ctx2 inContext <<" );

        assertReducesTo( "ex:c rdf:type (1 2) ex:ctx1 assertInContext >>", "ex:c" );
        assertReducesTo( "ex:c rdf:type ex:ctx1 inContext >> rdf:type >>", "rdf:List" );
        assertReducesTo( "ex:c rdf:type ex:ctx1 inContext >>", "(1 2)" );
        // Equality does not imply identity in statement queries.
        assertReducesTo( "(1 2) rdf:type ex:ctx1 inContext <<" );
    }

    public void testLiteralObjects() throws Exception {
        reduce( "@prefix ex: <http://example.org/assertInContextTest/>." );

        modelConnection.remove( null, null, modelConnection.value( 42 ) );
        modelConnection.commit();
        assertReducesTo( "ex:a ex:specialNumer ex:ctx1 inContext >>" );
        assertReducesTo( "ex:a ex:specialNumer 42 ex:ctx1 assertInContext >>", "ex:a" );
        assertReducesTo( "ex:a ex:specialNumer ex:ctx1 inContext >>", "42" );
        assertReducesTo( "42 ex:specialNumer ex:ctx1 inContext <<", "ex:a" );
        // Equality does not imply identity in statement queries.
        assertReducesTo( "42.0 ex:specialNumer ex:ctx1 inContext <<" );

        modelConnection.remove( null, null, modelConnection.value( "something", XMLSchema.STRING ) );
        modelConnection.commit();
        assertReducesTo( "ex:a rdfs:comment ex:ctx1 inContext >>" );
        assertReducesTo( "ex:a rdfs:comment \"something\" ex:ctx1 assertInContext >>", "ex:a" );
        assertReducesTo( "ex:a rdfs:comment ex:ctx1 inContext >>", "\"something\"" );
        assertReducesTo( "\"something\" rdfs:comment ex:ctx1 inContext <<", "ex:a" );

        modelConnection.remove( null, null, modelConnection.value( "something", XMLSchema.STRING ) );
        modelConnection.commit();
        assertReducesTo( "ex:a rdfs:label ex:ctx1 inContext >>" );
        assertReducesTo( "ex:a rdfs:label \"something\"^^xsd:string ex:ctx1 assertInContext >>", "ex:a" );
        assertReducesTo( "ex:a rdfs:label ex:ctx1 inContext >>", "\"something\"^^xsd:string" );
        assertReducesTo( "\"something\"^^xsd:string rdfs:label ex:ctx1 inContext <<", "ex:a" );
    }

    public void testNullContext() throws Exception {
        reduce( "@prefix ex: <http://example.org/assertInContextTest/>." );

        modelConnection.remove( null, null, modelConnection.value( "q" ) );
        modelConnection.commit();
        assertReducesTo( "ex:q rdfs:label \"q\" () assertInContext >>", "ex:q" );
        assertReducesTo( "ex:q rdfs:label () inContext >>", "\"q\"" );
        assertReducesTo( "ex:q rdfs:label >>", "\"q\"" );
        assertReducesTo( "ex:q rdfs:label ex:wrongContext inContext >>" );
        assertReducesTo( "\"q\" rdfs:label () inContext <<", "ex:q" );
        assertReducesTo( "\"q\" rdfs:label <<", "ex:q" );
        assertReducesTo( "\"q\" rdfs:label ex:wrongContext inContext <<" );
    }

    public void testImpossibleStatements() throws Exception {
        reduce( "@prefix ex: <http://example.org/assertInContextTest/>." );

        // Bad subject.
        assertReducesTo( "42 rdfs:comment \"foo\" ex:ctx1 assertInContext >>", "42" );
        assertReducesTo( "42 rdfs:comment >>" );
        assertReducesTo( "\"foo\" rdfs:comment <<" );

        // Bad predicate.
        assertReducesTo( "ex:a 42 \"foo\" ex:ctx1 assertInContext >>", "ex:a" );
        assertReducesTo( "ex:a 42 >>" );
        assertReducesTo( "\"foo\" 42 ex:ctx1 inContext <<" );

        // Bad context.
        assertReducesTo( "ex:b rdfs:comment \"foo\" 42 assertInContext >>", "ex:b" );
        assertReducesTo( "ex:b rdfs:comment >>" );
        assertReducesTo( "\"foo\" rdfs:comment <<" );
    }
}