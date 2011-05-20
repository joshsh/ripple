package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class ToStringTest extends RippleTestCase
{
    public void testNumericValues() throws Exception
    {
        assertReducesTo("42 to-string.", "\"42\"^^xsd:string");
        assertReducesTo("1.4141e0 to-string.", "\"1.4141\"^^xsd:string");
    }

    // TODO
}