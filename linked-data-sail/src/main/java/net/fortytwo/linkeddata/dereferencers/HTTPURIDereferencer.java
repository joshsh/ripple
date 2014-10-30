package net.fortytwo.linkeddata.dereferencers;

import net.fortytwo.linkeddata.Dereferencer;
import net.fortytwo.linkeddata.LinkedDataCache;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import org.apache.log4j.Logger;
import org.restlet.representation.Representation;

import java.util.HashSet;
import java.util.Set;

// Note: throughout this implementation, the caching context of a URI and
//       is the same as its success or failure 'memo'.
// The web location may be different, depending on IRI --> URI mapping.

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class HTTPURIDereferencer implements Dereferencer {
    private static final Logger LOGGER = Logger.getLogger(HTTPURIDereferencer.class);

    // FIXME: temporary
    private final LinkedDataCache linkedDataCache;

    private final Set<String> badExtensions;

    public HTTPURIDereferencer(final LinkedDataCache linkedDataCache) {
        this.linkedDataCache = linkedDataCache;

        badExtensions = new HashSet<String>();

    }

    public Representation dereference(final String uri) throws RippleException {
        // Don't dereference a URI which appears to point to a file which is not
        // an RDF document.
        int l = uri.lastIndexOf('.');
        if (l >= 0 && badExtensions.contains(uri.substring(l + 1))) {
            throw new RippleException("URI <" + StringUtils.escapeURIString(uri) + "> has blacklisted extension");
            // TODO: we can throw exceptions or return nulls to indicate an error, but we shouldn't do both
        }

        return new HTTPRepresentation(uri, linkedDataCache.getAcceptHeader());
    }

    public void blackListExtension(final String ext) {
        badExtensions.add(ext);
    }

    public void whitelistExtension(final String ext) {
        badExtensions.remove(ext);
    }

    @Override
    public String toString() {
        return "HTTP URI dereferencer";
    }
}

