/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.linkeddata.sail;

import net.fortytwo.flow.rdf.diff.RDFDiffSink;
import net.fortytwo.linkeddata.ContextMemo;
import net.fortytwo.linkeddata.Rdfizer;
import net.fortytwo.linkeddata.WebCache;
import net.fortytwo.linkeddata.WebClosure;
import net.fortytwo.linkeddata.dereferencers.FileURIDereferencer;
import net.fortytwo.linkeddata.dereferencers.HTTPURIDereferencer;
import net.fortytwo.linkeddata.dereferencers.JarURIDereferencer;
import net.fortytwo.linkeddata.rdfizers.ImageRdfizer;
import net.fortytwo.linkeddata.rdfizers.VerbatimRdfizer;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.RippleProperties;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.util.RDFUtils;
import org.apache.log4j.Logger;
import org.openrdf.model.ValueFactory;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.NotifyingSail;
import org.openrdf.sail.NotifyingSailConnection;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailChangedListener;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.StackableSail;
import org.restlet.data.MediaType;

import java.io.File;

/**
 * A thread-safe Sail which treats the Semantic Web as a single global graph of
 * linked data.
 */
public class LinkedDataSail implements StackableSail, NotifyingSail {
    public static final String
            LOG_FAILED_URIS = "net.fortytwo.linkeddata.logFailedUris",
            MAX_CACHE_CAPACITY = "net.fortytwo.linkeddata.maxCacheCapacity",
            USE_COMPACT_MEMO_FORMAT = "net.fortytwo.linkeddata.useCompactMemoFormat";

    private static final Logger LOGGER = Logger.getLogger(LinkedDataSail.class);

    // TODO: move this
    private static final String[] BADEXT = {
            "123", "3dm", "3dmf", "3gp", "8bi", "aac", "ai", "aif", "app", "asf",
            "asp", "asx", "avi", "bat", "bin", "bmp", "c", "cab", "cfg", "cgi",
            "com", "cpl", "cpp", "css", "csv", "dat", "db", "dll", "dmg", "dmp",
            "doc", "drv", "drw", "dxf", "eps", "exe", "fnt", "fon", "gif", "gz",
            "h", "hqx", "htm", "html", "iff", "indd", "ini", "iso", "java", /*"jpeg",*/
            /*"jpg",*/ "js", "jsp", "key", "log", "m3u", "mdb", "mid", "midi", "mim",
            "mng", "mov", "mp3", "mp4", "mpa", "mpg", "msg", "msi", "otf", "pct",
            "pdf", "php", "pif", "pkg", "pl", "plugin", "png", "pps", "ppt", "ps",
            "psd", "psp", "qt", "qxd", "qxp", "ra", "ram", "rar", "reg", "rm",
            "rtf", "sea", "sit", "sitx", "sql", "svg", "swf", "sys", "tar", "tif",
            "ttf", "uue", "vb", "vcd", "wav", "wks", "wma", "wmv", "wpd", "wps",
            "ws", "xhtml", "xll", "xls", "yps", "zip"};

    private static boolean logFailedUris;

    private final WebClosure webClosure;
    private final URIMap URIMap;

    private Sail baseSail;
    private boolean initialized = false;

    /**
     * @param baseSail base Sail which provides a storage layer for aggregated RDF data (Note: the base Sail should be initialized before this Sail is used)
     * @param uriMap mapping for virtual URI spaces
     * @param webClosure a custom WebClosure providing an RDF-document-level view of the Web
     */
    public LinkedDataSail(final Sail baseSail,
                          final URIMap uriMap,
                          final WebClosure webClosure)
            throws RippleException {
        RippleProperties properties = Ripple.getProperties();
        logFailedUris = properties.getBoolean(LOG_FAILED_URIS);

        this.baseSail = baseSail;
        this.URIMap = uriMap;

        this.webClosure = webClosure;
    }

    /**
     * @param baseSail base Sail which provides a storage layer for aggregated RDF data (Note: the base Sail should be initialized before this Sail is used)
     * @param uriMap mapping for virtual URI spaces
     */
    public LinkedDataSail(final Sail baseSail,
                          final URIMap uriMap)
            throws RippleException {
        this(baseSail, uriMap, createDefaultWebClosure(baseSail, uriMap));
    }

    /**
     * @param baseSail base Sail which provides a storage layer for aggregated RDF data (Note: the base Sail should be initialized before this Sail is used)
     */
    public LinkedDataSail(final Sail baseSail)
            throws RippleException {
        this(baseSail, new URIMap());
    }

    public void addSailChangedListener(final SailChangedListener listener) {
    }

    public synchronized NotifyingSailConnection getConnection()
            throws SailException {
        if (!initialized) {
            throw new SailException("LinkedDataSail has not been initialized");
        }

        return new LinkedDataSailConnection(baseSail, webClosure, URIMap);
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
        return new LinkedDataSailConnection(baseSail, webClosure, URIMap, listenerSink);
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

    private static WebClosure createDefaultWebClosure(final Sail baseSail,
                                                      final URIMap uriMap) throws RippleException {
        try {
            SailConnection sc = baseSail.getConnection();
            try {
                int maxCacheCapacity = Ripple.getProperties().getInt(MAX_CACHE_CAPACITY);

                WebCache cache = new WebCache(maxCacheCapacity, baseSail.getValueFactory());
                WebClosure wc = new WebClosure(cache, uriMap, baseSail.getValueFactory());

                // Add URI dereferencers.
                HTTPURIDereferencer hdref = new HTTPURIDereferencer(wc);
                for (String aBADEXT : BADEXT) {
                    hdref.blackListExtension(aBADEXT);
                }
                wc.addDereferencer("http", hdref);
                wc.addDereferencer("jar", new JarURIDereferencer());
                wc.addDereferencer("file", new FileURIDereferencer());

                // Add rdfizers.
                wc.addRdfizer(RDFUtils.findMediaType(RDFFormat.RDFXML), new VerbatimRdfizer(RDFFormat.RDFXML));
                wc.addRdfizer(RDFUtils.findMediaType(RDFFormat.TURTLE), new VerbatimRdfizer(RDFFormat.TURTLE));
                wc.addRdfizer(RDFUtils.findMediaType(RDFFormat.N3), new VerbatimRdfizer(RDFFormat.N3), 0.9);
                wc.addRdfizer(RDFUtils.findMediaType(RDFFormat.TRIG), new VerbatimRdfizer(RDFFormat.TRIG), 0.8);
                wc.addRdfizer(RDFUtils.findMediaType(RDFFormat.TRIX), new VerbatimRdfizer(RDFFormat.TRIX), 0.8);
                wc.addRdfizer(RDFUtils.findMediaType(RDFFormat.NTRIPLES), new VerbatimRdfizer(RDFFormat.NTRIPLES), 0.5);
                Rdfizer imageRdfizer = new ImageRdfizer();
                // Mainstream EXIF-compatible image types: JPEG, TIFF
                wc.addRdfizer(MediaType.IMAGE_JPEG, imageRdfizer, 0.4);
                wc.addRdfizer(new MediaType("image/tiff"), imageRdfizer, 0.4);
                wc.addRdfizer(new MediaType("image/tiff-fx"), imageRdfizer, 0.4);
                // TODO: add an EXIF-based Rdfizer for RIFF WAV audio files

                // Don't bother trying to dereference terms in these common namespaces.
                wc.addMemo("http://www.w3.org/XML/1998/namespace#", new ContextMemo(ContextMemo.Status.Ignored), sc);
                wc.addMemo("http://www.w3.org/2001/XMLSchema", new ContextMemo(ContextMemo.Status.Ignored), sc);
                wc.addMemo("http://www.w3.org/2001/XMLSchema#", new ContextMemo(ContextMemo.Status.Ignored), sc);

                // Don't try to dereference the cache index.
                wc.addMemo("http://fortytwo.net/2007/08/ripple/cache#", new ContextMemo(ContextMemo.Status.Ignored), sc);

                return wc;
            } finally {
                sc.close();
            }
        } catch (SailException e) {
            throw new RippleException(e);
        }
    }

    public static boolean logFailedUris() {
        return logFailedUris;
    }

    public static void main(final String[] args) {
        System.out.println(RDFFormat.TURTLE.getDefaultMIMEType());
    }
}

