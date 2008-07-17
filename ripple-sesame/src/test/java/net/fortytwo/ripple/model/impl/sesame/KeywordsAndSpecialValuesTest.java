package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.libs.stack.StackLibrary;

/**
 * Author: josh
 * Date: May 21, 2008
 * Time: 4:51:49 PM
 */
public class KeywordsAndSpecialValuesTest extends RippleTestCase
{
    public void testAliasMapsToPrimaryURI() throws Exception
    {
        assertReducesTo( "<" + StackLibrary.NS_2007_05 + "dup>", "dup" );
        assertReducesTo( "<" + StackLibrary.NS_2007_08 + "dup>", "dup" );
        assertReducesTo( "<" + StackLibrary.NS_2008_08 + "dup>", "dup" );
    }

    public void testKeywordMappingToSimultaneousValues() throws Exception
    {
        // TODO: test this feature if/when the set of core primitives makes it testable
    }

    public void testKeywordMappingToCompetingPrimaryAndAliasURIs() throws Exception
    {
        // TODO: test this feature if/when the set of core primitives makes it testable 
    }
    
    public void testKeywordMappingToAliasURIsForSameValue() throws Exception
    {
        // "times" maps to two URIs, but they're aliases for the same primary
        // URI, so we only get that URI once.
        assertReducesTo( "times", "timesApply" );
    }

    public void testObsoleteKeyword() throws Exception
    {
        assertReducesTo( "back" );
    }
}
