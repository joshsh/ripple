/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import net.fortytwo.ripple.cli.CommandLineInterface;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.impl.sesame.SesameModel;
import net.fortytwo.ripple.query.LazyEvaluator;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.query.StackEvaluator;
import net.fortytwo.ripple.rdf.CloseableIterationSource;
import net.fortytwo.ripple.rdf.RDFUtils;
import net.fortytwo.ripple.rdf.SailInserter;
import net.fortytwo.ripple.rdf.SesameOutputAdapter;
import net.fortytwo.linkeddata.sail.LinkedDataSail;
import net.fortytwo.ripple.flow.Source;
import org.apache.log4j.Logger;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;


/**
 * Demo application.
 */
public final class Demo
{
    private static final Logger LOGGER = Logger.getLogger( Demo.class );

	private Demo()
	{
	}
	
    private static Sail createMemoryStoreSail() throws RippleException
    {
        Sail sail = new MemoryStore();

        try {
            sail.initialize();
        } catch ( SailException e ) {
            throw new RippleException( e );
        }

        return sail;
    }

    public static RDFFormat getRDFFormat( final String name ) throws RippleException
	{
		String value = Ripple.getProperties().getString( name );

		RDFFormat format = RDFUtils.findFormat( value );

		if ( null == format )
		{
			throw new RippleException( "unknown RDF format: " + value );
		}

		return format;
	}

    public static void demo( final File store,
						 	final InputStream in,
					 		final PrintStream out,
							final PrintStream err )
		throws RippleException
	{
//net.fortytwo.ripple.tools.SitemapsUtils.test();
		// Create a Sesame triple store.
		Sail baseSail = createMemoryStoreSail();
        RDFFormat cacheFormat = getRDFFormat( Ripple.CACHE_FORMAT );

        if ( null != store )
		{
			loadFromFile( baseSail, store, cacheFormat );
		}
		
		URIMap uriMap = new URIMap();
		Sail sail = new LinkedDataSail( baseSail, uriMap );
		
		try
		{
			sail.initialize();
		}
		
		catch ( SailException e )
		{
			throw new RippleException( e );
		}

		// Attach a Ripple model to the repository.
		Model model = new SesameModel( sail, Ripple.class.getResource("libraries.txt"), uriMap );

		// Attach a query engine to the model.
        StackEvaluator evaluator = new LazyEvaluator();
//        StackEvaluator evaluator = new LazyDebugEvaluator();
		QueryEngine qe
			= new QueryEngine( model, evaluator, out, err );

		// Attach an interpreter to the query engine and let it read from
		// standard input.
		CommandLineInterface r = new CommandLineInterface( qe, in );
		r.run();

		model.shutDown();

		try
		{
			sail.shutDown();
		}
	
		catch ( SailException e )
		{
			throw new RippleException( e );
		}

		// Save back to store.
		if ( null != store )
		{
			saveToFile( baseSail, store, cacheFormat );
		}

		try
		{
			baseSail.shutDown();
		}

		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
	}

	private static void printUsage()
	{
		System.out.println( "Usage:  ripple [options] [store]" );
		System.out.println( "Options:\n"
			+ "  -c <props>   Configuration properties file\n"
			+ "  -h           Print this help and exit\n"
			+ "  -q           Suppress normal output\n"
			+ "  -v           Print version information and exit" );
		System.out.println( "For more information, please see:\n"
			+ "  <URL:http://ripple.fortytwo.net/>." );
	}

	private static void printVersion()
	{
		System.out.println( Ripple.getName() + " " + Ripple.getVersion() );

		// Would be nice: list of libraries
	}

	public static void main( final String [] args )
	{
		// Default values.
		boolean quiet = false, showVersion = false, showHelp = false;
		File configFile = null, store = null;

		// Long options are available but are not advertised.
		LongOpt [] longOptions = {
			new LongOpt( "config", LongOpt.REQUIRED_ARGUMENT, null, 'c' ),
			new LongOpt( "help", LongOpt.NO_ARGUMENT, null, 'h' ),
			new LongOpt( "quiet", LongOpt.NO_ARGUMENT, null, 'q' ),
			new LongOpt( "version", LongOpt.NO_ARGUMENT, null, 'v' ) };

		Getopt g = new Getopt( Ripple.getName(), args, "c:hqv", longOptions );
		int c;
		while ( ( c = g.getopt() ) != -1 )
		{
			switch( c )
			{
				case 'c':
				case 0:
					configFile = new File( g.getOptarg() );
					break;
				case 'h':
				case 1:
					showHelp = true;
					break;
				case 'q':
				case 2:
					quiet = true;
					break;
				case 'v':
				case 3:
					showVersion = true;
					break;
				case '?':
					 // Note: getopt() already printed an error
					printUsage();
					System.exit( 1 );
					break;
				default:
					System.err.print("getopt() returned " + c + "\n");
			}
		}

		int i = g.getOptind();
		if ( i < args.length )
		{
			// Too many non-option arguments.
			if ( args.length - i > 1 )
			{
				printUsage();
				System.exit( 1 );
			}

			store = new File( args[i] );
		}

		if ( showHelp )
		{
			printUsage();
			System.exit( 0 );
		}

		if ( showVersion )
		{
			printVersion();
			System.exit( 0 );
		}

		try
		{
			// Load Ripple configuration.
			if ( null == configFile )
			{
				Ripple.initialize();
			}
			
			else
			{
                Properties p = new Properties();

                try {
                    p.load(new FileInputStream(configFile));
                } catch (IOException e) {
                    throw new RippleException(e);
                }

                Ripple.initialize( p );
			}
		}

		catch ( RippleException e )
		{
			System.err.println( "Initialization error: " + e );
			e.logError();
			System.exit( 1 );
		}
		
		Ripple.setQuiet( quiet );
// System.out.println( "quiet = " + quiet );
// System.out.println( "showVersion = " + showVersion );
// System.out.println( "format = " + format );
// System.out.println( "store = " + store );

		try
		{
			demo( store, System.in, System.out, System.err );
		}

		catch ( RippleException e )
		{
			System.out.println( "Error: " + e );
			e.logError();
			System.exit( 1 );
		}

		// Exit despite any remaining active threads.
		System.exit( 0 );
	}
	
	private static void loadFromFile( final Sail sail, final File file, final RDFFormat format ) throws RippleException
	{
		LOGGER.info( "loading state from " + file );

		FileInputStream in;
		
		try
		{
			in = new FileInputStream( file );
		}
		
		catch ( FileNotFoundException e )
		{
			LOGGER.info( "file " + file + " does not exist. It will be created on shutdown." );
			return;
		}
		
		try
		{
			RDFParser parser = Rio.createParser( format );
			SailConnection sc = sail.getConnection();
			parser.setRDFHandler( new SailInserter( sc ) );
			parser.parse( in, "urn:nobaseuri#" );
			sc.commit();
			sc.close();
		}
		
		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
		
		try
		{
			in.close();
		}
		
		catch ( IOException e )
		{
			throw new RippleException( e );
		}
	}

	private static void saveToFile( final Sail sail, final File file, final RDFFormat format ) throws RippleException
	{
		FileOutputStream out;
		
		try
		{
			out = new FileOutputStream( file );
		}
		
		catch ( FileNotFoundException e )
		{
			throw new RippleException( e );
		}
		
		RDFHandler writer = Rio.createWriter( format, out );
		SesameOutputAdapter adapter = new SesameOutputAdapter( writer );

		try
		{
			SailConnection sc = sail.getConnection();
			adapter.startRDF();
			CloseableIterationSource<? extends Namespace, SailException> nsSource
					= new CloseableIterationSource(
				 			sc.getNamespaces() );
			( (Source<Namespace, RippleException>) nsSource ).writeTo( adapter.namespaceSink() );
			CloseableIterationSource<? extends Statement, SailException> stSource
					= new CloseableIterationSource(
							sc.getStatements( null, null, null, false )	);
			( (Source <Statement, RippleException>) stSource ).writeTo( adapter.statementSink() );
			adapter.endRDF();
			sc.close();
		}
		
		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
		
		try
		{
			out.close();
		}
		
		catch ( IOException e )
		{
			throw new RippleException( e );
		}
	}
}

