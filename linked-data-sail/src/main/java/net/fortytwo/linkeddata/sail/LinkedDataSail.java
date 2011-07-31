package net.fortytwo.linkeddata.sail;

import net.fortytwo.flow.rdf.diff.RDFDiffSink;
import net.fortytwo.linkeddata.WebClosure;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.RippleProperties;
import net.fortytwo.ripple.URIMap;
import org.apache.log4j.Logger;
import org.openrdf.model.ValueFactory;
import org.openrdf.sail.NotifyingSail;
import org.openrdf.sail.NotifyingSailConnection;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailChangedListener;
import org.openrdf.sail.SailException;
import org.openrdf.sail.StackableSail;

import java.io.File;

/**
 * A thread-safe Sail which treats the Semantic Web as a single global graph of
 * linked data.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LinkedDataSail implements StackableSail, NotifyingSail {
    public static final String
            LOG_FAILED_URIS = "net.fortytwo.linkeddata.logFailedUris",
            MAX_CACHE_CAPACITY = "net.fortytwo.linkeddata.maxCacheCapacity",
            USE_COMPACT_MEMO_FORMAT = "net.fortytwo.linkeddata.useCompactMemoFormat";

    private static final Logger LOGGER = Logger.getLogger(LinkedDataSail.class);

    private static boolean logFailedUris;

    private final WebClosure webClosure;

    private Sail baseSail;
    private boolean initialized = false;

    /**
     * @param baseSail   base Sail which provides a storage layer for aggregated RDF data (Note: the base Sail should be initialized before this Sail is used)
     * @param webClosure a custom WebClosure providing an RDF-document-level view of the Web
     */
    public LinkedDataSail(final Sail baseSail,
                          final WebClosure webClosure)
            throws RippleException {
        RippleProperties properties = Ripple.getConfiguration();
        logFailedUris = properties.getBoolean(LOG_FAILED_URIS);

        this.baseSail = baseSail;

        this.webClosure = webClosure;
    }

    /**
     * @param baseSail base Sail which provides a storage layer for aggregated RDF data (Note: the base Sail should be initialized before this Sail is used)
     */
    public LinkedDataSail(final Sail baseSail)
            throws RippleException {
        this(baseSail, WebClosure.createDefault(baseSail, new URIMap()));
    }

    public void addSailChangedListener(final SailChangedListener listener) {
    }

    public synchronized NotifyingSailConnection getConnection()
            throws SailException {
        if (!initialized) {
            throw new SailException("LinkedDataSail has not been initialized");
        }

        return new LinkedDataSailConnection(baseSail, webClosure);
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

    // Extended API ////////////////////////////////////////////////////////////

    public synchronized LinkedDataSailConnection getConnection(final RDFDiffSink listenerSink)
            throws SailException {
        return new LinkedDataSailConnection(baseSail, webClosure, listenerSink);
    }

    public WebClosure getWebClosure() {
        return webClosure;
    }

    public Sail getBaseSail() {
        return baseSail;
    }

    public void setBaseSail(final Sail baseSail) {
        this.baseSail = baseSail;
    }

    ////////////////////////////////////////////////////////////////////////////

    public static boolean logFailedUris() {
        return logFailedUris;
    }

    // For testing/debugging/play only.
    public static void main(final String[] args) {
        //System.out.println(RDFFormat.TURTLE.getDefaultMIMEType());
    }
}

