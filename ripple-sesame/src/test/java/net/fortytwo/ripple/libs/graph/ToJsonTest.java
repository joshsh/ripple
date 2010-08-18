package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.test.RippleTestCase;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class ToJsonTest extends RippleTestCase
{
    //  <http://api.twitter.com/1/geo/id/dc3747428fa88cab.json> get >> toJson >> "place_type" >> .
    
    public void testSimple() throws Exception
    {
        String json = "\"" + StringUtils.escapeString("{\n" +
                "            \"intVal\": 42,\n" +
                "            \"jsonVal\": {\n" +
                "                \"first\": 1,\n" +
                "                \"second\": 2\n" +
                "            },\n" +
                "            \"arrayVal\": [\n" +
                "                {\n" +
                "                    \"one\":1,\n" +
                "                    \"two\":2\n" +
                "                },\n" +
                "                2,\n" +
                "                3\n" +
                "            ],\n" +
                "            \"booleanVal\": false,\n" +
                "            \"doubleVal\": 3.14,\n" +
                "            \"longVal\": 10000000000\n" +
                "         }") + "\"";

        // Simple values
        assertReducesTo(json + " toJson >> \"intVal\" >>", "42" );
        assertReducesTo(json + " toJson >> \"intVal\" >> type >>", "<http://www.w3.org/2001/XMLSchema#integer>" );
        assertReducesTo(json + " toJson >> \"booleanVal\" >>", "false" );
        assertReducesTo(json + " toJson >> \"doubleVal\" >>", "3.14" );
        assertReducesTo(json + " toJson >> \"doubleVal\" >> type >>", "<http://www.w3.org/2001/XMLSchema#double>" );
        assertReducesTo(json + " toJson >> \"longVal\" >>", "\"10000000000\"^^<http://www.w3.org/2001/XMLSchema#long>" );
        assertReducesTo(json + " toJson >> \"longVal\" >> type >>", "<http://www.w3.org/2001/XMLSchema#long>" );

        // JSON values
        assertReducesTo(json + " toJson >> \"jsonVal\" >> \"second\" >>", "2" );

        // Array values
        assertReducesTo(json + " toJson >> \"arrayVal\" >> 1 at >> \"one\" >>", "1" );
        assertReducesTo(json + " toJson >> \"arrayVal\" >> 3 at >>", "3" );
    }
}