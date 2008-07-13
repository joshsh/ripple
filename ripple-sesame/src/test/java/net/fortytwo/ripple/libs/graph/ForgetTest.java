package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.NewRippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class ForgetTest extends NewRippleTestCase
{
    public void testSimple() throws Exception
    {
        /* TODO: restore this test case if/when forget has an implementation once again.
        reduce( "@prefix ex: <http://example.org/forgetTest/> ." );

        assertReducesTo( "ex:a\n"
                + "rdf:type ex:ClassA ex:ClassB both >> assert >>\n"
                + "rdfs:comment \"test resource\" ex:ctx1 assertIn >>", "ex:a", "ex:a" );
        assertReducesTo( "ex:a rdf:type >>", "ex:ClassA", "ex:ClassB" );
        assertReducesTo( "ex:a rdfs:comment >>", "\"test resource\"" );
        assertReducesTo( "ex:a forget >>", "ex:a" );
        assertReducesTo( "ex:a rdf:type >>" );
        assertReducesTo( "ex:a rdfs:comment >>" );
        */
    }

    public void testLists() throws Exception
    {
        /* TODO: restore this test case if/when the corresponding feature is added.
        assertReducesTo( "rdf:nil forget >>", "rdf:nil" );
        assertReducesTo( "rdf:nil rdf:type >>", "rdf:List" );
        */
    }
}