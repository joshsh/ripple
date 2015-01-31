package net.fortytwo.linkeddata;

import info.aduna.iteration.CloseableIteration;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

/**
 * An object which keeps track of the 3xx redirects which have been followed in the course of dereferencing
 * Linked Data URIs.
 * Persisting and looking up these redirects saves time, space, and bandwidth when multiple redirects
 * to the same document are encountered.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RedirectManager {
    private final SailConnection connection;

    public RedirectManager(SailConnection connection) {
        this.connection = connection;
    }

    public boolean isExistingRedirectTo(final String documentUri) throws SailException {
        URI hashedDocumentUri = new URIImpl(RDFUtils.hashedUri(documentUri));

        CloseableIteration<? extends Statement, SailException> iter
                = connection.getStatements(null, LinkedDataCache.CACHE_REDIRECTSTO, hashedDocumentUri, false);
        try {
            return iter.hasNext();
        } finally {
            iter.close();
        }
    }

    public void persistRedirect(final String thingUri, final String documentUri) throws SailException {
        URI hashedThingUri = new URIImpl(RDFUtils.hashedUri(thingUri));
        URI hashedDocumentUri = new URIImpl(RDFUtils.hashedUri(documentUri));

        connection.removeStatements(hashedThingUri, LinkedDataCache.CACHE_REDIRECTSTO, null);
        connection.addStatement(hashedThingUri, LinkedDataCache.CACHE_REDIRECTSTO, hashedDocumentUri);

        // note: a LinkedDataSail cache miss is currently not an atomic operation w.r.t. the underlying triple store
        connection.commit();
        connection.begin();
    }
}
