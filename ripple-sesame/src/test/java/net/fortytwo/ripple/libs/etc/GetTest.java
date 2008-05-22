package net.fortytwo.ripple.libs.etc;

import net.fortytwo.ripple.test.NewRippleTestCase;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.model.vocabulary.RDFS;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class GetTest extends NewRippleTestCase
{
    public void testAll() throws Exception
    {
        getTestUriMap().put( "http://example.org/getTest.txt", getClass().getResource( "getTest.txt" ).toString() );

        // FIXME: 'file' protocol is not supported by etc:get
//        assertReducesTo( "<http://example.org/getTest.txt> get >>", "\"testing, one two three...\"" );
    }
}