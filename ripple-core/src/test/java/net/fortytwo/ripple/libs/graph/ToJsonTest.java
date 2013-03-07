package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ToJsonTest extends RippleTestCase
{
    //  <http://api.twitter.com/1/geo/id/dc3747428fa88cab.json> get. toJson. "place_type". .
    
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
        assertReducesTo(json + " to-json. \"intVal\".", "42" );
        assertReducesTo(json + " to-json. \"intVal\". type.", "<http://www.w3.org/2001/XMLSchema#integer>" );
        assertReducesTo(json + " to-json. \"booleanVal\".", "false" );
        assertReducesTo(json + " to-json. \"doubleVal\".", "3.14" );
        assertReducesTo(json + " to-json. \"doubleVal\". type.", "<http://www.w3.org/2001/XMLSchema#double>" );
        assertReducesTo(json + " to-json. \"longVal\".", "\"10000000000\"^^<http://www.w3.org/2001/XMLSchema#long>" );
        assertReducesTo(json + " to-json. \"longVal\". type.", "<http://www.w3.org/2001/XMLSchema#long>" );

        // JSON values
        assertReducesTo(json + " to-json. \"jsonVal\". \"second\".", "2" );

        // Array values
        assertReducesTo(json + " to-json. \"arrayVal\". 1 at. \"one\".", "1" );
        assertReducesTo(json + " to-json. \"arrayVal\". 3 at.", "3" );
    }
}
