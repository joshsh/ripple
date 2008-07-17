package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class ScrapTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        assertReducesTo( "scrap >>" );
        assertReducesTo( "42 scrap >>" );
        assertReducesTo( "(1 2 3) each >> scrap >>" );
    }
}