package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class MembersTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        reduce("@prefix ex: <http://example.org/membersTest/> .\n"
                + "ex:a rdf:_1 \"first\"^^xsd:string assert >>\n"
                + "    rdf:_2 \"second\" assert >>\n"
                + "    rdf:_2 \"second (duplicate)\" assert >>\n"
                + "    rdf:_3 3 assert >>\n"
                + "    rdf:_27 \"27\"^^xsd:double assert >> .");
        assertReducesTo( "ex:a members >>", "\"first\"^^xsd:string", "\"second\"", "\"second (duplicate)\"", "3", "27" );
    }
}