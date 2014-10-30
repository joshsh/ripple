package net.fortytwo.ripple;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;


/**
 * Read-only configuration metadata.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public final class Ripple {
    private static final Logger LOGGER = Logger.getLogger(Ripple.class.getName());

    public static final String RANDOM_URN_PREFIX = "urn:uuid:";

    public static final String
            BUFFER_QUERY_RESULTS = "net.fortytwo.ripple.cli.bufferQueryResults",
            RESULT_VIEW_SHOW_EDGES = "net.fortytwo.ripple.cli.resultViewShowEdges",
            RESULT_VIEW_DEDUPLICATE_OBJECTS = "net.fortytwo.ripple.cli.resultViewDeduplicateObjects",
            RESULT_VIEW_MAX_OBJECTS = "net.fortytwo.ripple.cli.resultViewMaxObjects",
            RESULT_VIEW_MAX_PREDICATES = "net.fortytwo.ripple.cli.resultViewMaxPredicates",
            RESULT_VIEW_PRINT_ENTIRE_STACK = "net.fortytwo.ripple.cli.resultViewPrintEntireStack",
            MAX_WORKER_THREADS = "net.fortytwo.ripple.control.maxWorkerThreads",
            ALLEGROSAIL_HOST = "net.fortytwo.ripple.demo.allegroSailHost",
            ALLEGROSAIL_PORT = "net.fortytwo.ripple.demo.allegroSailPort",
            ALLEGROSAIL_START = "net.fortytwo.ripple.demo.allegroSailStart",
            ALLEGROSAIL_NAME = "net.fortytwo.ripple.demo.allegroSailName",
            ALLEGROSAIL_DIRECTORY = "net.fortytwo.ripple.demo.allegroSailDirectory",
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
            USE_BLANK_NODES = "net.fortytwo.ripple.model.useBlankNodes",
            MEMOIZE_LISTS_FROM_RDF = "net.fortytwo.ripple.model.memoizeListsFromRdf",
            DEFAULT_NAMESPACE = "net.fortytwo.ripple.model.defaultNamespace",
            VERSION = "net.fortytwo.ripple.version",
    // TODO: .........
    USE_ASYNCHRONOUS_QUERIES = "";

    public static final String RIPPLE_ONTO_BASEURI = "http://fortytwo.net/2007/03/ripple/schema#";

    private static final String RIPPLE_PROPERTIES = "default.properties";
    private static final String LOG4J_PROPERTIES = "log4j.properties";

    private static boolean initialized = false;

    private static RippleProperties CONFIGURATION;

    // TODO: get rid of these
    private static boolean useAsynchronousQueries = true;

    // FIXME: quiet is never used
    private static boolean quiet = false;

    private Ripple() {
    }

    /**
     * Initializes the Ripple environment.  Note: it is safe to call this method
     * more than once.  Subsequent calls simply have no effect.
     *
     * @param configuration optional configuration properties
     * @throws RippleException if initialization fails
     */
    public static void initialize(final Properties... configuration)
            throws RippleException {
        if (!initialized) {
            Properties props = new Properties();

            try {
                props.load(Ripple.class.getResourceAsStream(RIPPLE_PROPERTIES));
            } catch (IOException e) {
                throw new RippleException("unable to load properties file " + RIPPLE_PROPERTIES);
            }

            for (Properties p : configuration) {
                if (null == p) {
                    throw new IllegalArgumentException("null Properties");
                }

                props.putAll(p);
            }

            Ripple.CONFIGURATION = new RippleProperties(props);

            initialized = true;
        }
    }

    public static RippleProperties getConfiguration() throws RippleException {
        if (!initialized) {
            // If the environment has not already been initialized (using custom properties),
            // initialize automatically (using no custom properties).
            initialize();
            //throw new RippleException( "Environment is not ready.  Use Ripple.initialize()." );
        }

        return CONFIGURATION;
    }

    public static String getName() {
        return "Ripple";
    }

    public static String getVersion() {
        try {
            return getConfiguration().getProperty(VERSION);
        } catch (RippleException e) {
            LOGGER.warning("could not determine Ripple version: " + e.getMessage());
            return "?";
        }
    }

    public static boolean getQuiet() {
        return quiet;
    }

    public static void setQuiet(final boolean q) {
        quiet = q;
    }

    // TODO: move these
    public static boolean asynchronousQueries() {
        return useAsynchronousQueries;
    }

    public static void enableAsynchronousQueries(final boolean enable) {
        useAsynchronousQueries = enable;
    }
}
