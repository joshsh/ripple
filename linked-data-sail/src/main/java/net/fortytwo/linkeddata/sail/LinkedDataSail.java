package net.fortytwo.linkeddata.sail;

import net.fortytwo.linkeddata.LinkedDataCache;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.RippleProperties;
import org.openrdf.model.ValueFactory;
import org.openrdf.sail.NotifyingSail;
import org.openrdf.sail.NotifyingSailConnection;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailChangedListener;
import org.openrdf.sail.SailException;
import org.openrdf.sail.StackableSail;

import java.io.File;

/**
 * A thread-safe storage layer which treats the Semantic Web as a single global graph of linked data.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LinkedDataSail implements StackableSail, NotifyingSail {
    public static final String
            LOG_FAILED_URIS = "net.fortytwo.linkeddata.logFailedUris",
            MAX_CACHE_CAPACITY = "net.fortytwo.linkeddata.maxCacheCapacity";

    private static boolean logFailedUris;

    private final LinkedDataCache cache;

    private Sail baseSail;
    private boolean initialized = false;

    /**
     * @param baseSail   base Sail which provides a storage layer for aggregated RDF data (Note: the base Sail should be initialized before this Sail is used)
     * @param cache a custom WebClosure providing an RDF-document-level view of the Web
     * @throws RippleException if construction fails
     */
    public LinkedDataSail(final Sail baseSail,
                          final LinkedDataCache cache)
            throws RippleException {
        RippleProperties properties = Ripple.getConfiguration();
        logFailedUris = properties.getBoolean(LOG_FAILED_URIS);

        this.baseSail = baseSail;

        this.cache = cache;
    }

    /**
     * @param baseSail base Sail which provides a storage layer for aggregated RDF data (Note: the base Sail should be initialized before this Sail is used)
     * @throws net.fortytwo.ripple.RippleException if construction fails
     */
    public LinkedDataSail(final Sail baseSail)
            throws RippleException {
        this(baseSail, LinkedDataCache.createDefault(baseSail));
    }

    public void addSailChangedListener(final SailChangedListener listener) {
    }

    public synchronized NotifyingSailConnection getConnection()
            throws SailException {
        if (!initialized) {
            throw new SailException("LinkedDataSail has not been initialized");
        }

        return new LinkedDataSailConnection(baseSail, cache);
    }

    public File getDataDir() {
        return null;
    }

    public ValueFactory getValueFactory() {
        // Inherit the base Sail's ValueFactory
        return baseSail.getValueFactory();
    }

    public void initialize() throws SailException {
        initialized = true;
    }

    public boolean isWritable()
            throws SailException {
        return true;
    }

    public void removeSailChangedListener(final SailChangedListener listener) {
    }

    public void setDataDir(final File dataDir) {
    }

    public void shutDown() throws SailException {
    }

    public Sail getBaseSail() {
        return baseSail;
    }

    public void setBaseSail(final Sail baseSail) {
        this.baseSail = baseSail;
    }

    // Extended API ////////////////////////////////////////////////////////////

    public LinkedDataCache getCache() {
        return cache;
    }

    public static boolean logFailedUris() {
        return logFailedUris;
    }
}

