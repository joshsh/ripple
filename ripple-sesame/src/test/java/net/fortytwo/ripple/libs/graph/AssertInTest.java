package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class AssertInTest extends NewRippleTestCase
{
    public void testSimple() throws Exception
    {
        reduce( "@prefix ex: <http://example.org/assertInTest/>." );
        assertReducesTo( "ex:a rdf:type >>" );

        reduce( "ex:a rdf:type ex:Example ex:ctx1 assertIn >>" );
        assertReducesTo( "ex:a rdf:type >>", "ex:Example" );
        assertReducesTo( "ex:a rdf:type ex:ctx1 inContext >> >>", "ex:Example" );
        assertReducesTo( "ex:a rdf:type ex:ctx2 inContext >> >>" );

        // Asserted statements are not mutually exclusive.
        assertReducesTo( "ex:a rdf:type ex:Thing ex:ctx1 assertIn >>", "ex:a" );
        assertReducesTo( "ex:a rdf:type ex:ctx1 inContext >> >>", "ex:Example", "ex:Thing" );

        assertReducesTo( "ex:b rdf:type () ex:ctx1 assertIn >>", "ex:b" );
        assertReducesTo( "ex:b rdf:type ex:ctx1 inContext >> >>", "rdf:nil" );

        assertReducesTo( "ex:b rdf:type (1 2) ex:ctx1 assertIn >>", "ex:b" );
        assertReducesTo( "ex:b rdf:type ex:ctx1 inContext >> >> rdf:type >>", "rdf:List" );
    }

    public void testLiteralObjects() throws Exception {
        reduce( "@prefix ex: <http://example.org/assertInTest/>." );

        assertReducesTo( "ex:a ex:specialNumer ex:ctx1 inContext >> >>" );
        assertReducesTo( "ex:a ex:specialNumer 42 ex:ctx1 assertIn >>", "ex:a" );
        assertReducesTo( "ex:a ex:specialNumer ex:ctx1 inContext >> >>", "42" );

        assertReducesTo( "ex:a rdfs:comment ex:ctx1 inContext >> >>" );
        assertReducesTo( "ex:a rdfs:comment \"something\" ex:ctx1 assertIn >>", "ex:a" );
        assertReducesTo( "ex:a rdfs:comment ex:ctx1 inContext >> >>", "\"something\"" );

        assertReducesTo( "ex:a rdfs:label ex:ctx1 inContext >> >>" );
        assertReducesTo( "ex:a rdfs:label \"something\"^^xsd:string ex:ctx1 assertIn >>", "ex:a" );
        assertReducesTo( "ex:a rdfs:label ex:ctx1 inContext >> >>", "\"something\"^^xsd:string" );
    }

    public void testImpossibleStatements() throws Exception {
        reduce( "@prefix ex: <http://example.org/assertInTest/>." );

        // Bad subject.
        assertReducesTo( "42 rdfs:comment \"foo\" ex:ctx1 assertIn >>", "42" );
        assertReducesTo( "42 rdfs:comment >>" );

        // Bad predicate.
        assertReducesTo( "ex:a 42 \"foo\" ex:ctx1 assertIn >>", "ex:a" );
        assertReducesTo( "ex:a 42 >>" );

        // Bad context.
        assertReducesTo( "ex:b rdfs:comment \"foo\" 42 assertIn >>", "ex:b" );
        assertReducesTo( "ex:b rdfs:comment >>" );
    }
}