package net.fortytwo.linkeddata;

import org.restlet.representation.Representation;

/**
 * An object which fetches documents based on URI (for example, Web documents via HTTP).
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface Dereferencer {
    /**
     * @param uri a URI from which to fetch a document
     * @return the retrieved HTTP representation, or null if dereferencing the URI is found not to be necessary
     * (for example, if the URI redirects to a document which has already been retrieved)
     */
    // TODO: this method throws an exception, while Rdfizer.handle does not
    Representation dereference(String uri);
}
