package net.fortytwo.linkeddata;

import net.fortytwo.ripple.RippleException;
import org.restlet.representation.Representation;

/**
 * An object which fetches documents based on URI (for example, Web documents via HTTP).
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface Dereferencer {
    /**
     * @param uri a URI from which to fetch a document
     * @return the retrieved HTTP representation
     * @throws RippleException if dereferencing the URI fails for any reason
     */
    // TODO: this method throws an exception, while Rdfizer.handle does not
    Representation dereference(String uri) throws RippleException;
}

