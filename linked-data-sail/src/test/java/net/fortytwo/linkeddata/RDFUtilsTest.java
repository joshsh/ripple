package net.fortytwo.linkeddata;

import net.fortytwo.linkeddata.util.RDFUtils;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RDFUtilsTest {
    @Test
    public void testIriToUrl() throws Exception {
        String iri;
        URL url;

        iri = "http://nl.dbpedia.org/resource/Mauritani\\u00EB";
        url = RDFUtils.iriToUrl(iri);
        // final letter rendered with a single glyph but represented with 6 characters
        assertEquals(46, iri.length());
        assertEquals("http://nl.dbpedia.org/resource/Mauritani\\u00EB", url.toString());

        iri = "http://example.org/Don'tMap-Just-Anything...";
        url = RDFUtils.iriToUrl(iri);
        assertEquals(iri, url.toString());

        /*
        iri = "http://example.org/Spaces must be mapped";
        url = RDFUtils.iriToUrl(iri);
        assertEquals("http://example.org/Spaces%20must%20be%20mapped", url.toString());
        */
    }
}
