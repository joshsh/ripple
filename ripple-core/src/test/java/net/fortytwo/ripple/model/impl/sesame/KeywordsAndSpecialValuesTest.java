package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.libs.string.StringLibrary;
import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class KeywordsAndSpecialValuesTest extends RippleTestCase {
    /*
    public void testTemp() throws Exception {
        QueryEngine qe = getTestQueryEngine();
        Sink<RippleList, RippleException> results = new NullSink<RippleList, RippleException>();

        QueryPipe pipe = new QueryPipe( qe, results );

        long before = new Date().getTime();

        int iterations = 100000;
        for (int i = 0; i < iterations; i++) {
            pipe.put("links >> pop >> links >> .");
        }

        long after = new Date().getTime();
        long duration = (after - before);
        System.out.println("duration: " + duration + "ms (" + (duration / (1.0 * iterations)) + "ms / expr)");
    }   */

    public void testAliasMapsToPrimaryURI() throws Exception {
        assertReducesTo("<" + StackLibrary.NS_2007_05 + "dup>", "dup");
        assertReducesTo("<" + StackLibrary.NS_2007_08 + "dup>", "dup");
        assertReducesTo("<" + StackLibrary.NS_2008_08 + "dup>", "dup");
    }

    public void testKeywordMapsToSimultaneousPrimaryURIs() throws Exception {
        // TODO: test this feature if/when the set of core primitives makes it testable
    }

    public void testKeywordMapsToSimultaneousAliasURIs() throws Exception {
        // TODO: test this feature if/when the set of core primitives makes it testable
    }

    public void testKeywordMapsToCompetingPrimaryAndAliasURIs() throws Exception {
        // The keyword "contains" does NOT also map to analysis:members, even
        // though that primitive has "contains" as an alias.
        assertReducesTo("contains", "<" + StringLibrary.NS_2008_08 + "contains>");
    }

    public void testKeywordMapsToAliasURIsForSameValue() throws Exception {
        // "times" maps to two URIs, but they're aliases for the same primary
        // URI, so we only get that URI once.
        assertReducesTo("times", "timesApply");
    }

    public void testObsoleteKeyword() throws Exception {
        assertReducesTo("back");
    }
}
