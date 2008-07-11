package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.NewRippleTestCase;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.NumericValue;

import java.util.Collection;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class CompareTest extends NewRippleTestCase
{
    public void testURIs() throws Exception
    {
        assertTrue( 0.0 == compare( "rdfs:label", "rdfs:label" ) );
        assertTrue( 0.0 > compare( "rdfs:comment", "rdfs:label" ) );
        assertTrue( 0.0 < compare( "rdfs:label", "rdfs:comment" ) );
    }

    public void testNumericValues() throws Exception
    {
        assertTrue( 0.0 == compare( "42", "42" ) );
        assertTrue( 0.0 == compare( "42", "42.00" ) );
        assertTrue( 0.0 == compare( "42e0", "42" ) );
        assertTrue( 0.0 > compare( "-1", "+1" ) );
        assertTrue( 0.0 < compare( "+1", "-1" ) );

        assertTrue( 0.0 == compare( "\"42\"^^xsd:double", "42" ) );
        assertTrue( 0.0 > compare( "\"41\"^^xsd:double", "42" ) );
        assertTrue( 0.0 == compare( "42", "\"42\"^^xsd:double" ) );
        assertTrue( 0.0 > compare( "41", "\"42\"^^xsd:double" ) );

        assertTrue( 0.0 == compare( "\"42.0\"^^xsd:double", "\"42\"^^xsd:integer" ) );
        assertTrue( 0.0 < compare( "\"42.1\"^^xsd:double", "\"42\"^^xsd:integer" ) );
    }

    public void testPlainLiterals() throws Exception
    {
        assertTrue( 0.0 == compare( "\"\"", "\"\"" ) );
        assertTrue( 0.0 > compare( "\"\"", "\"foo\"" ) );
        assertTrue( 0.0 < compare( "\"foo\"", "\"\"" ) );
        assertTrue( 0.0 == compare( "\"foo\"", "\"foo\"" ) );
        assertTrue( 0.0 > compare( "\"bar\"", "\"foo\"" ) );
        assertTrue( 0.0 < compare( "\"foo\"", "\"bar\"" ) );

        assertTrue( 0.0 > compare( "\"foo\"", "\"foo\"@en" ) );
        assertTrue( 0.0 > compare( "\"foobar\"", "\"foo\"@en" ) );
        assertTrue( 0.0 < compare( "\"foo\"@en", "\"foo\"" ) );
        assertTrue( 0.0 < compare( "\"foo\"@en", "\"foobar\"" ) );
        assertTrue( 0.0 == compare( "\"foo\"@en", "\"foo\"@en" ) );
        assertTrue( 0.0 > compare( "\"foo\"@en", "\"foobar\"@en" ) );
        assertTrue( 0.0 > compare( "\"foo\"@de", "\"foo\"@en" ) );
        assertTrue( 0.0 > compare( "\"foobar\"@de", "\"foo\"@en" ) );
        assertTrue( 0.0 < compare( "\"foo\"@en", "\"foo\"@de" ) );
        assertTrue( 0.0 < compare( "\"foo\"@en", "\"foobar\"@de" ) );
    }

    public void testTypedLiterals() throws Exception
    {
        assertTrue( 0.0 == compare( "\"\"^^xsd:string", "\"\"^^xsd:string" ) );
        assertTrue( 0.0 < compare( "\"foo\"^^xsd:string", "\"\"^^xsd:string" ) );
        assertTrue( 0.0 > compare( "\"\"^^xsd:string", "\"foo\"^^xsd:string" ) );
        assertTrue( 0.0 == compare( "\"foo\"^^xsd:string", "\"foo\"^^xsd:string" ) );
        assertTrue( 0.0 > compare( "\"bar\"^^xsd:string", "\"foo\"^^xsd:string" ) );
        assertTrue( 0.0 < compare( "\"foo\"^^xsd:string", "\"bar\"^^xsd:string" ) );

        assertTrue( 0.0 > compare( "\"http://example.org\"^^xsd:anyURI", "\"http://example.org\"^^xsd:string" ) );
        assertTrue( 0.0 < compare( "\"http://example.org\"^^xsd:string", "\"http://example.org\"^^xsd:anyURI" ) );
    }

    public void testBlankNodes() throws Exception
    {
        // TODO
    }

    public void testLists() throws Exception
    {
        assertTrue( 0.0 == compare( "()", "()" ) );
        assertTrue( 0.0 < compare( "(1)", "()" ) );
        assertTrue( 0.0 > compare( "()", "(42)" ) );
        
        assertTrue( 0.0 == compare( "(1 2 3)", "(1 2 3)" ) );
        assertTrue( 0.0 > compare( "(1 2 2)", "(1 2 3)" ) );
        assertTrue( 0.0 < compare( "(1 4 3)", "(1 2 3)" ) );
        assertTrue( 0.0 > compare( "(1 2 3)", "(1 2 3 4)" ) );
        assertTrue( 0.0 == compare( "(1 (2) 3)", "(1 (2) 3)" ) );
        assertTrue( 0.0 < compare( "(1 (4) 3)", "(1 (2) 3)" ) );
        assertTrue( 0.0 > compare( "(1 (2) 3)", "(1 (37) 3)" ) );

        assertTrue( 0.0 == compare( "()", "rdf:nil" ) );
        assertTrue( 0.0 < compare( "(42)", "rdf:nil" ) );
        assertTrue( 0.0 > compare( "rdf:nil", "(1 2 3)" ) );
        reduce( "@prefix : <http://example.org/compareTest/> .\n"
                + "@define prog: 1 2 3 ." );
        assertTrue( 0.0 == compare( ":prog", "(1 2 3)" ) );
        assertTrue( 0.0 > compare( "rdf:nil", ":prog" ) );
        assertTrue( 0.0 < compare( ":prog", "rdf:nil" ) );
        assertTrue( 0.0 > compare( ":prog", "(1 2 4)" ) );
        assertTrue( 0.0 < compare( ":prog", "(1 2)" ) );
    }

    // Note: for now, boolean values are simply Literals
    public void testBooleans() throws Exception
    {
        assertTrue( 0.0 == compare( "true", "true" ) );
        assertTrue( 0.0 < compare( "true", "false" ) );
        assertTrue( 0.0 > compare( "false", "true" ) );    
        assertTrue( 0.0 == compare( "false", "false" ) );
    }

    public void testPrimitives() throws Exception
    {
        assertTrue( 0.0 == compare( "dup", "dup" ) );
        assertTrue( 0.0 > compare( "dup", "swap" ) );
        assertTrue( 0.0 < compare( "swap", "dup" ) );
    }

    public void testHeterogeneousValues() throws Exception
    {
        // Compare plain literals with typed literals.
        assertTrue( 0.0 > compare( "\"foo\"", "\"foo\"^^xsd:string" ) );
        assertTrue( 0.0 < compare( "\"foo\"^^xsd:string", "\"foo\"" ) );

        // TODO: compare objects of different types, once the rules for doing so are better defined.
    }

    private double compare(final String expr1, final String expr2) throws Exception
    {
        Collection<RippleList> results = reduce( expr1 + " " + expr2 + " compare >>" );
        assertEquals( 1, results.size() );
        RippleList l = results.iterator().next();
        assertEquals( 1, l.length() );
        RippleValue v = l.getFirst();
        assertTrue( v instanceof NumericValue);
        return ( (NumericValue) v ).doubleValue();
    }
}