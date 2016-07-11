package net.fortytwo.linkeddata;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.linkeddata.util.RDFUtils;
import org.openrdf.model.IRI;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.SimpleValueFactory;
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
    private static final ValueFactory valueFactory = SimpleValueFactory.getInstance();
    
    private final SailConnection connection;

    public RedirectManager(SailConnection connection) {
        this.connection = connection;
    }

    /**
     * Finds whether any URI has already been dereferenced which redirects to the given document URI
     * @param documentUri the URI of a possible Linked Data document
     * @return whether any URI has already been dereferenced which redirects to the given document URI
     */
    public boolean existsRedirectTo(final String documentUri) throws SailException {
        IRI hashedDocumentUri = valueFactory.createIRI(RDFUtils.hashedUri(documentUri));

        CloseableIteration<? extends Statement, SailException> iter
                = connection.getStatements(null, LinkedDataCache.CACHE_REDIRECTSTO, hashedDocumentUri, false);
        try {
            return iter.hasNext();
        } finally {
            iter.close();
        }
    }

    /**
     * Persistently stores the fact that the given thing URI has been found to redirect to the given document URI
     * @param thingUri the URI of a non-information resource
     * @param documentUri the URI to which the original URI has been redirected
     */
    public void persistRedirect(final String thingUri, final String documentUri) throws SailException {
        IRI hashedThingUri = valueFactory.createIRI(RDFUtils.hashedUri(thingUri));
        IRI hashedDocumentUri = valueFactory.createIRI(RDFUtils.hashedUri(documentUri));

        connection.removeStatements(hashedThingUri, LinkedDataCache.CACHE_REDIRECTSTO, null);
        connection.addStatement(hashedThingUri, LinkedDataCache.CACHE_REDIRECTSTO, hashedDocumentUri);

        // note: a LinkedDataSail cache miss is currently not an atomic operation w.r.t. the underlying triple store
        connection.commit();
        connection.begin();
    }
}
