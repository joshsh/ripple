package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.libs.string.StringLibrary;
import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class KeywordsAndSpecialValuesTest extends RippleTestCase {

    @Test
    public void testAliasMapsToPrimaryURI() throws Exception {
        assertReducesTo("<" + StackLibrary.NS_2007_05 + "dup>", "dup");
        assertReducesTo("<" + StackLibrary.NS_2007_08 + "dup>", "dup");
        assertReducesTo("<" + StackLibrary.NS_2008_08 + "dup>", "dup");
    }

    @Ignore
    @Test
    public void testKeywordMapsToSimultaneousPrimaryURIs() throws Exception {
        // TODO: test this feature if/when the set of core primitives makes it testable
    }

    @Ignore
    @Test
    public void testKeywordMapsToSimultaneousAliasURIs() throws Exception {
        // TODO: test this feature if/when the set of core primitives makes it testable
    }

    @Test
    public void testKeywordMapsToCompetingPrimaryAndAliasURIs() throws Exception {
        // The keyword "contains" does NOT also map to analysis:members, even
        // though that primitive has "contains" as an alias.
        assertReducesTo("contains", "<" + StringLibrary.NS_2008_08 + "contains>");
    }

    @Test
    public void testKeywordMapsToAliasURIsForSameValue() throws Exception {
        // "times" maps to two URIs, but they're aliases for the same primary
        // URI, so we only get that URI once.
        assertReducesTo("times", "timesApply");
    }

    @Test
    public void testObsoleteKeyword() throws Exception {
        assertReducesTo("back");
    }
}
