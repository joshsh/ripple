package net.fortytwo.ripple.libs.etc;

import net.fortytwo.ripple.test.NewRippleTestCase;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.model.vocabulary.RDFS;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class TimeTest extends NewRippleTestCase
{
    public void testAll() throws Exception
    {
        // FIXME: find a workaround for xsd:long literals
//        assertReducesTo( "time >>", "" + System.currentTimeMillis() );
    }
}