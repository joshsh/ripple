package net.fortytwo.ripple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * A central location for constants and configuration settings.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public final class Ripple {
    private static final Logger logger = LoggerFactory.getLogger(Ripple.class);

    public static final String RANDOM_URN_PREFIX = "urn:uuid:";

    public static final String
            BUFFER_QUERY_RESULTS = "net.fortytwo.ripple.cli.bufferQueryResults",
            RESULT_VIEW_SHOW_EDGES = "net.fortytwo.ripple.cli.resultViewShowEdges",
            RESULT_VIEW_DEDUPLICATE_OBJECTS = "net.fortytwo.ripple.cli.resultViewDeduplicateObjects",
            RESULT_VIEW_MAX_OBJECTS = "net.fortytwo.ripple.cli.resultViewMaxObjects",
            RESULT_VIEW_MAX_PREDICATES = "net.fortytwo.ripple.cli.resultViewMaxPredicates",
            RESULT_VIEW_PRINT_ENTIRE_STACK = "net.fortytwo.ripple.cli.resultViewPrintEntireStack",
            MAX_WORKER_THREADS = "net.fortytwo.ripple.control.maxWorkerThreads",
            LINKEDDATASAIL_BASE_SAIL = "net.fortytwo.ripple.demo.linkedDataSailBaseSail",
            MEMORYSTORE_PERSIST_FILE = "net.fortytwo.ripple.demo.memoryStorePersistFile",
            NATIVESTORE_DIRECTORY = "net.fortytwo.ripple.demo.nativeStoreDirectory",
            NATIVESTORE_INDEXES = "net.fortytwo.ripple.demo.nativeStoreIndexes",
            SPARQL_ENDPOINTURL = "net.fortytwo.ripple.demo.sparqlEndpointUrl",
            READ_ONLY = "net.fortytwo.ripple.demo.readOnly",
            SAIL_LOG = "net.fortytwo.ripple.demo.sailLog",
            SAIL_TYPE = "net.fortytwo.ripple.demo.sailType",
            ALLOW_DUPLICATE_NAMESPACES = "net.fortytwo.ripple.io.allowDuplicateNamespaces",
            PREFER_NEWEST_NAMESPACE_DEFINITIONS = "net.fortytwo.ripple.io.preferNewestNamespaceDefinitions",
            HTTPCONNECTION_COURTESY_INTERVAL = "net.fortytwo.ripple.io.httpConnectionCourtesyInterval",
            HTTPCONNECTION_TIMEOUT = "net.fortytwo.ripple.io.httpConnectionTimeout",
            MEMOIZE_LISTS_FROM_RDF = "net.fortytwo.ripple.model.memoizeListsFromRdf",
            DEFAULT_NAMESPACE = "net.fortytwo.ripple.model.defaultNamespace",
            VERSION = "net.fortytwo.ripple.version";

    public static final String RIPPLE_ONTO_BASEURI = "http://fortytwo.net/2007/03/ripple/schema#";

    private static final String
            DEFAULT_PROPERTIES = "default.properties";

    private static boolean initialized = false;

    private static RippleProperties configuration;

    // TODO: move this
    private static boolean useAsynchronousQueries = true;

    private Ripple() {
    }

    /**
     * Initializes the Ripple environment.  Note: it is safe to call this method
     * more than once.  Subsequent calls simply have no effect.
     *
     * @param configuration optional configuration properties
     * @throws RippleException if initialization fails
     */
    public synchronized static void initialize(final Properties... configuration) throws RippleException {
        if (!initialized) {
            Properties props = new Properties();

            try {
                props.load(Ripple.class.getResourceAsStream(DEFAULT_PROPERTIES));
            } catch (IOException e) {
                throw new RippleException("unable to load properties file " + DEFAULT_PROPERTIES);
            }

            for (Properties p : configuration) {
                if (null == p) {
                    throw new IllegalArgumentException("null Properties");
                }

                props.putAll(p);
            }

            Ripple.configuration = new RippleProperties(props);

            for (Map.Entry e : props.entrySet()) {
                System.getProperties().setProperty((String) e.getKey(), (String) e.getValue());
            }

            initialized = true;
        }
    }

    public static RippleProperties getConfiguration() throws RippleException {
        if (!initialized) {
            // If the environment has not already been initialized (using custom properties),
            // initialize automatically (using no custom properties).
            initialize();
        }

        return configuration;
    }

    public static String getName() {
        return "Ripple";
    }

    public static String getVersion() {
        try {
            return getConfiguration().getProperty(VERSION);
        } catch (RippleException e) {
            logger.warn("could not determine Ripple version: " + e.getMessage());
            return "?";
        }
    }

    // TODO: move these
    public static boolean asynchronousQueries() {
        return useAsynchronousQueries;
    }

    public static void enableAsynchronousQueries(final boolean enable) {
        useAsynchronousQueries = enable;
    }
}
