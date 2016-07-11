package net.fortytwo.linkeddata.sail;

import net.fortytwo.linkeddata.LinkedDataCache;
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
            MEMORY_CACHE_CAPACITY = "net.fortytwo.linkeddata.memoryCacheCapacity",
            USE_BLANK_NODES = "net.fortytwo.linkeddata.useBlankNodes",
            HTTPCONNECTION_COURTESY_INTERVAL = "net.fortytwo.linkeddata.httpConnectionCourtesyInterval",
            HTTPCONNECTION_TIMEOUT = "net.fortytwo.linkeddata.httpConnectionTimeout";

    public static final String RANDOM_URN_PREFIX = "urn:uuid:";

    private final LinkedDataCache cache;

    private Sail baseSail;

    public static String getProperty(String name, String defaultValue) {
        String value = System.getProperty(name);
        if (null == value) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * @param baseSail base Sail which provides a storage layer for aggregated RDF data.
     *                 Note: the base Sail should be initialized before this Sail is used.
     * @param cache    a custom WebClosure providing an RDF-document-level view of the Web
     */
    public LinkedDataSail(final Sail baseSail,
                          final LinkedDataCache cache) {
        this.baseSail = baseSail;

        this.cache = cache;
    }

    /**
     * @param baseSail base Sail which provides a storage layer for aggregated RDF data.
     *                 Note: the base Sail should be initialized before this Sail is used.
     */
    public LinkedDataSail(final Sail baseSail) {
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
        cache.close();

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

