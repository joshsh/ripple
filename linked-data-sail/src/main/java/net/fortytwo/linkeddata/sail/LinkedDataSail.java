package net.fortytwo.linkeddata.sail;

import net.fortytwo.linkeddata.LinkedDataCache;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.ValueFactory;
import org.openrdf.sail.NotifyingSail;
import org.openrdf.sail.NotifyingSailConnection;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailChangedListener;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.StackableSail;
import org.openrdf.sail.helpers.SailBase;

import java.io.File;

/**
 * A dynamic storage layer which treats the Semantic Web as a single global graph of linked data.
 * LinkedDataSail is layered on top of another Sail which serves as a database for cached Semantic Web documents.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LinkedDataSail extends SailBase implements StackableSail, NotifyingSail {
    public static final String
            CACHE_LIFETIME = "net.fortytwo.linkeddata.cacheLifetime",
            DATATYPE_HANDLING_POLICY = "net.fortytwo.linkeddata.datatypeHandlingPolicy",
            MEMORY_CACHE_CAPACITY = "net.fortytwo.linkeddata.memoryCacheCapacity";

    private final LinkedDataCache cache;

    private Sail baseSail;

    /**
     * @param baseSail base Sail which provides a storage layer for aggregated RDF data.
     *                 Note: the base Sail should be initialized before this Sail is used.
     * @param cache    a custom WebClosure providing an RDF-document-level view of the Web
     * @throws RippleException if there is a configuration error
     */
    public LinkedDataSail(final Sail baseSail,
                          final LinkedDataCache cache)
            throws RippleException {
        this.baseSail = baseSail;

        this.cache = cache;
    }

    /**
     * @param baseSail base Sail which provides a storage layer for aggregated RDF data.
     *                 Note: the base Sail should be initialized before this Sail is used.
     * @throws RippleException if there is a configuration error
     */
    public LinkedDataSail(final Sail baseSail)
            throws RippleException {
        this(baseSail, LinkedDataCache.createDefault(baseSail));
    }

    public void addSailChangedListener(final SailChangedListener listener) {
        if (baseSail instanceof NotifyingSail) {
            ((NotifyingSail) baseSail).addSailChangedListener(listener);
        }
    }

    @Override
    public NotifyingSailConnection getConnection() throws SailException {
        SailConnection sc = getConnectionInternal();
        return (NotifyingSailConnection) sc;
    }

    protected synchronized SailConnection getConnectionInternal() throws SailException {
        return new LinkedDataSailConnection(this, baseSail, cache);
    }

    public File getDataDir() {
        throw new UnsupportedOperationException();
    }

    public ValueFactory getValueFactory() {
        // Inherit the base Sail's ValueFactory
        return baseSail.getValueFactory();
    }

    public void initialize() throws SailException {
        // Do not initialize the base Sail; it is initialized independently.
    }

    public boolean isWritable() throws SailException {
        // You may write to LinkedDataSail, but be sure not to interfere with caching metadata.
        return true;
    }

    public void removeSailChangedListener(final SailChangedListener listener) {
        if (baseSail instanceof NotifyingSail) {
            ((NotifyingSail) baseSail).removeSailChangedListener(listener);
        }
    }

    public void setDataDir(final File dataDir) {
        throw new UnsupportedOperationException();
    }

    protected void shutDownInternal() throws SailException {
        // Do not shut down the base Sail.
    }

    public Sail getBaseSail() {
        return baseSail;
    }

    public void setBaseSail(final Sail baseSail) {
        this.baseSail = baseSail;
    }

    /**
     * @return this LinkedDataSail's cache manager
     */
    public LinkedDataCache getCache() {
        return cache;
    }
}

