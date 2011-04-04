/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */

package net.fortytwo.ripple;

import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.Properties;


/**
 *  Read-only configuration metadata.
 */
public final class Ripple
{
	public static final String RANDOM_URN_PREFIX = "urn:random:";

    public static final String
            BUFFER_QUERY_RESULTS                = "net.fortytwo.ripple.cli.bufferQueryResults",
            RESOURCE_VIEW_SHOW_EDGES            = "net.fortytwo.ripple.cli.resourceViewShowEdges",
            RESULT_VIEW_DEDUPLICATE_OBJECTS     = "net.fortytwo.ripple.cli.resultViewDeduplicateObjects",
            RESULT_VIEW_MAX_OBJECTS             = "net.fortytwo.ripple.cli.resultViewMaxObjects",
            RESULT_VIEW_MAX_PREDICATES          = "net.fortytwo.ripple.cli.resultViewMaxPredicates",
            JLINE_DEBUG_OUTPUT                  = "net.fortytwo.ripple.cli.jline.debugOutput",
            RESULT_VIEW_PRINT_ENTIRE_STACK      = "net.fortytwo.ripple.cli.resultViewPrintEntireStack",
            MAX_WORKER_THREADS                  = "net.fortytwo.ripple.control.maxWorkerThreads",
            ALLEGROSAIL_HOST                    = "net.fortytwo.ripple.demo.allegroSailHost",
            ALLEGROSAIL_PORT                    = "net.fortytwo.ripple.demo.allegroSailPort",
            ALLEGROSAIL_START                   = "net.fortytwo.ripple.demo.allegroSailStart",
            ALLEGROSAIL_NAME                    = "net.fortytwo.ripple.demo.allegroSailName",
            ALLEGROSAIL_DIRECTORY               = "net.fortytwo.ripple.demo.allegroSailDirectory",
            LOAD_FILE                           = "net.fortytwo.ripple.demo.loadFile",
            LOAD_FILE_FORMAT                    = "net.fortytwo.ripple.demo.loadFileFormat",
            LOAD_FILE_BASE_URI                  = "net.fortytwo.ripple.demo.loadFileBaseURI",
            LINKEDDATASAIL_BASE_SAIL            = "net.fortytwo.ripple.demo.linkedDataSailBaseSail",
            MEMORYSTORE_PERSIST_FILE            = "net.fortytwo.ripple.demo.memoryStorePersistFile",
            MEMORYSTORE_PERSIST_FILE_BASE_URI   = "net.fortytwo.ripple.demo.memoryStorePersistFileBaseURI",
            MEMORYSTORE_PERSIST_FILE_FORMAT     = "net.fortytwo.ripple.demo.memoryStorePersistFileFormat",
            NATIVESTORE_DIRECTORY               = "net.fortytwo.ripple.demo.nativeStoreDirectory",
            NATIVESTORE_INDEXES                 = "net.fortytwo.ripple.demo.nativeStoreIndexes",
            READ_ONLY                           = "net.fortytwo.ripple.demo.readOnly",
            SAIL_TYPE                           = "net.fortytwo.ripple.demo.sailType",
            ALLOW_DUPLICATE_NAMESPACES          = "net.fortytwo.ripple.io.allowDuplicateNamespaces",
            PREFER_NEWEST_NAMESPACE_DEFINITIONS = "net.fortytwo.ripple.io.preferNewestNamespaceDefinitions",
            REJECT_NONASSOCIATED_STATEMENTS     = "net.fortytwo.ripple.io.rejectNonAssociatedStatements",
            HTTPCONNECTION_COURTESY_INTERVAL    = "net.fortytwo.ripple.io.httpConnectionCourtesyInterval",
            HTTPCONNECTION_TIMEOUT              = "net.fortytwo.ripple.io.httpConnectionTimeout",
            LIST_PADDING                        = "net.fortytwo.ripple.model.listPadding",
            USE_INFERENCE                       = "net.fortytwo.ripple.model.useInference",
            PULL_ENTIRE_MODEL                   = "net.fortytwo.ripple.model.lexicon.pullEntireModel",
            USE_BLANK_NODES                     = "net.fortytwo.ripple.model.useBlankNodes",
            MEMOIZE_LISTS_FROM_RDF              = "net.fortytwo.ripple.model.memoizeListsFromRdf",
            DEFAULT_NAMESPACE                   = "net.fortytwo.ripple.query.defaultNamespace",
            EVALUATION_ORDER                    = "net.fortytwo.ripple.query.evaluationOrder",
            EVALUATION_STYLE                    = "net.fortytwo.ripple.query.evaluationStyle",
            EXPRESSION_ASSOCIATIVITY            = "net.fortytwo.ripple.query.expressionAssociativity",
            EXPRESSION_ORDER                    = "net.fortytwo.ripple.query.expressionOrder",
            // TODO: .........
            USE_ASYNCHRONOUS_QUERIES            = "";

    private static final String RIPPLE_PROPERTIES = "default.properties";
    private static final String LOG4J_PROPERTIES = "log4j.properties";

    private static boolean initialized = false;

	private static RippleProperties CONFIGURATION;

    // TODO: get rid of these
	private static boolean useAsynchronousQueries = true;

// FIXME: quiet is never used
	private static boolean quiet = false;

    ////////////////////////////////////////////////////////////////////////////

    private Ripple()
	{
	}

    /**
     * Initializes the Ripple environment.  Note: it is safe to call this method
     * more than once.  Subsequent calls simply have no effect.
     * @param configuration optional configuration properties
     * @throws RippleException  if initialization fails
     */
    public static void initialize( final Properties... configuration )
		throws RippleException
	{
		if ( initialized )
		{
			return;
		}

        PropertyConfigurator.configure(
			    Ripple.class.getResource( LOG4J_PROPERTIES ) );

		Properties props = new Properties();

		try
		{
			props.load( Ripple.class.getResourceAsStream( RIPPLE_PROPERTIES ) );
		}

		catch ( IOException e )
		{
			throw new RippleException( "unable to load properties file " + RIPPLE_PROPERTIES );
		}

        for ( Properties p : configuration )
        {
            if ( null == p )
            {
                throw new IllegalArgumentException("null Properties");
            }

            props.putAll(p);
        }

        Ripple.CONFIGURATION = new RippleProperties( props );

		initialized = true;
	}

	public static RippleProperties getConfiguration() throws RippleException
    {
        if ( !initialized )
        {
            throw new RippleException( "Environment is not ready.  Use Ripple.initialize()." );
        }

        return CONFIGURATION;
	}

	////////////////////////////////////////////////////////////////////////////

	public static String getName()
	{
		return "Ripple";
	}

	public static String getVersion()
	{
		return "0.6-alpha";
	}

	public static boolean getQuiet()
	{
		return quiet;
	}

	public static void setQuiet( final boolean q )
	{
		quiet = q;
	}

    // TODO: move this
    public static boolean useInference() throws RippleException
	{
		return CONFIGURATION.getBoolean( USE_INFERENCE );
	}

    // TODO: move these
	public static boolean asynchronousQueries()
	{
		return useAsynchronousQueries;
	}
    public static void enableAsynchronousQueries( final boolean enable )
	{
		useAsynchronousQueries = enable;
	}
}
