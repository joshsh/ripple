package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.model.Lexicon;
import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LexiconTest extends RippleTestCase {
    @Test
    public void testIsValidPrefix() throws Exception {
        // TODO: make this into a more thorough collection of test cases
        String[] validPrefixes = {"", "a", "a_", "a-", "foo-bar", "rdf", "ns108"};
        String[] inValidPrefixes = {"_a", "-a", "a b", "5", "107a", "a.b"};

        Lexicon l = new Lexicon(getTestModel());

        for (String prefix : validPrefixes) {
            assertTrue("\"" + prefix + "\" should be accepted as a valid prefix", l.isValidPrefix(prefix));
        }

        for (String prefix : inValidPrefixes) {
            assertFalse("\"" + prefix + "\" should not be accepted as a valid prefix", l.isValidPrefix(prefix));
        }
    }

    @Test
    public void testisValidLocalName() throws Exception {
        // TODO: make this into a more thorough collection of test cases
        String[] validLocalNames = {"", "a", "foo", "foo1331", "_a", "__a"};
        String[] inValidLocalNames = {".a", "a.b", "23", "2a"};

        Lexicon l = new Lexicon(getTestModel());

        for (String localName : validLocalNames) {
            assertTrue("\"" + localName + "\" should be accepted as a valid local name",
                    l.isValidLocalName(localName));
        }

        for (String localName : inValidLocalNames) {
            assertFalse("\"" + localName + "\" should not be accepted as a valid local name",
                    l.isValidLocalName(localName));
        }
    }
}