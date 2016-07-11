package net.fortytwo.linkeddata.util;

import net.fortytwo.linkeddata.sail.LinkedDataSail;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Logger;


/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public final class RDFUtils {

    private RDFUtils() {
    }

    /**
     * Strips the fragment identifier of a (usually) HTTP URI.
     *
     * @param uri a URI possibly containing a fragment identifier, e.g. http://example.org/foo#bar
     * @return the URI without a fragment identifier, e.g. http://example.org/foo
     */
    public static String removeFragmentIdentifier(final String uri) {
        int i = uri.lastIndexOf('#');
        return 0 <= i ? uri.substring(0, i) : uri;
    }

    /**
     * Creates a hashed version of a URI.
     * This is useful for storing metadata about a URI in a graph without also linking the metadata
     * to the original URI; it is accessible only through hashing.
     */
    public static String hashedUri(final String originalUri) {
        return LinkedDataSail.RANDOM_URN_PREFIX
                + UUID.nameUUIDFromBytes(originalUri.getBytes());
    }

    public static String findGraphUri(final String uri) {
        String docUri = removeFragmentIdentifier(uri);
        return hashedUri(docUri);
    }

    public static URL iriToUrl(final String iriStr) throws MalformedURLException, UnsupportedEncodingException {
        return new URL(iriStr);
    }
}
