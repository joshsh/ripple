/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.cli;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

import java.util.List;
import java.util.ArrayList;

import jline.Completor;
import jline.MultiCompletor;
import jline.ConsoleReader;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.CollectorHistory;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.cli.jline.DirectiveCompletor;
import net.fortytwo.ripple.control.Scheduler;
import net.fortytwo.ripple.control.TaskQueue;
import net.fortytwo.ripple.control.ThreadedInputStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Lexicon;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.query.PipedIOStream;

import org.apache.log4j.Logger;

/**
 * A command-line interpreter/browser which coordinates user interaction with a Ripple query engine.
 */
public class CommandLineInterface
{
	private static final Logger LOGGER
		= Logger.getLogger( CommandLineInterface.class );

	private static final byte[] EOL = { '\n' };

    private PipedIOStream writeIn;
    //private PipedInputStream  writeIn;
	//private PipedOutputStream readOut;
	private ThreadedInputStream consoleReaderInput;

	private Interpreter interpreter;

	private ConsoleReader reader;
	private int lineNumber;

	private QueryEngine queryEngine;

	private CollectorHistory<RippleList, RippleException> queryResultHistory
		= new CollectorHistory<RippleList, RippleException>( 2 );
private boolean lastQueryContinued = false;

	private TaskQueue taskQueue = new TaskQueue();

	////////////////////////////////////////////////////////////////////////////

	/**
	 * Console input:
	 *     is --> filter --> consoleReaderInput --> reader --> readOut --> writeIn --> interpreter
	 *
	 * Normal output:
	 *     [commands and queries] --> queryEngine.getPrintStream()
	 *
	 * Error output:
	 *     alert() --> queryEngine.getErrorPrintStream()
	 */
	public CommandLineInterface( final QueryEngine qe, final InputStream is )
		throws RippleException
	{
		queryEngine = qe;

		// Handling of queries
		Sink<ListAST, RippleException> querySink = new Sink<ListAST, RippleException>()
		{
			public void put( final ListAST ast ) throws RippleException
			{
				addCommand( new VisibleQueryCommand( ast, queryResultHistory, lastQueryContinued ) );
				lastQueryContinued = false;
				addCommand( new UpdateCompletorsCmd() );
				executeCommands();
			}
		};

		// Handling of "continuing" queries
		Sink<ListAST, RippleException> continuingQuerySink = new Sink<ListAST, RippleException>()
		{
			public void put( final ListAST ast ) throws RippleException
			{
				addCommand( new VisibleQueryCommand( ast, queryResultHistory, lastQueryContinued ) );
				lastQueryContinued = true;
				addCommand( new UpdateCompletorsCmd() );
				executeCommands();
			}
		};

		// Handling of commands
		Sink<Command, RippleException> commandSink = new Sink<Command, RippleException>()
		{
			public void put( final Command cmd ) throws RippleException
			{
				addCommand( cmd );
				addCommand( new UpdateCompletorsCmd() );
				executeCommands();
			}
		};

		// Handling of parser events
		Sink<RecognizerEvent, RippleException> eventSink = new Sink<RecognizerEvent, RippleException>()
		{
			public void put( final RecognizerEvent event )
				throws RippleException
			{
				switch ( event )
				{
					case NEWLINE:
						readLine();
						break;
					case ESCAPE:
						LOGGER.debug( "received escape event" );
						abortCommands();
						break;
					case QUIT:
						LOGGER.debug( "received quit event" );
						abortCommands();
						// Note: exception handling used for control
						throw new ParserQuitException();
					default:
						throw new RippleException(
							"event not yet supported: " + event );
				}
			}
		};

		RecognizerAdapter ra = new RecognizerAdapter(
			querySink, continuingQuerySink, commandSink, eventSink, qe.getErrorPrintStream() );

		Sink<Exception, RippleException> parserExceptionSink = new ParserExceptionSink(
				qe.getErrorPrintStream() );

		// Pass input through a filter to watch for special byte sequences, and
		// another draw input through it even when the interface is busy.
		InputStream filter = new InputStreamEventFilter( is, ra );
		consoleReaderInput = new ThreadedInputStream( filter );

		String jlineDebugOutput = Ripple.getProperties().getString(
                Ripple.JLINE_DEBUG_OUTPUT );

        // Create reader.
		try
		{
			reader = new ConsoleReader( consoleReaderInput,
				new OutputStreamWriter( qe.getPrintStream() ) );

			// Set up JLine logging if asked for.
			if ( null != jlineDebugOutput && 0 < jlineDebugOutput.length() )
			{
				reader.setDebug(
					new PrintWriter(
						new FileWriter( jlineDebugOutput, true ) ) );
			}
		}

		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
jline.Terminal term = reader.getTerminal();
//System.out.println( "reader.getTerminal() = " + term );

            writeIn = new PipedIOStream();
            //writeIn.write(32);
            //writeIn = new PipedInputStream();
			//readOut = new PipedOutputStream( writeIn );

		// Initialize completors.
		updateCompletors();

		// Create interpreter.
		interpreter = new Interpreter( ra, writeIn, parserExceptionSink );
	}

	public void run() throws RippleException
	{
		lineNumber = 0;
		interpreter.parse();
//System.out.println( "done parsing" );
	}

	////////////////////////////////////////////////////////////////////////////

	private void readLine()
	{
		try
		{
			++lineNumber;
			String prefix = "" + lineNumber + " >>  ";
//System.out.println( "reading a line" );
//System.out.println( "    consoleReaderInput.available() = " + consoleReaderInput.available() );
			String line = reader.readLine( prefix );
//System.out.println( "done reading the line: " + line );
	
			if ( null != line )
			{
				// Feed the line to the lexer.
				byte[] bytes = line.getBytes();
                //readOut.write( bytes, 0, bytes.length );
                writeIn.write( bytes, 0, bytes.length );

				// Add a newline character so the lexer will call readLine()
				// again when it gets there.
//                readOut.write( EOL );
                writeIn.write( EOL );

                writeIn.flush();
//                readOut.flush();
			}
		}

		catch ( java.io.IOException e )
		{
			alert( "IOException: " + e.toString() );
		}
	}

	private void alert( final String s )
	{
		queryEngine.getErrorPrintStream().println( "\n" + s + "\n" );
	}

	private void updateCompletors()
	{
		LOGGER.debug( "updating completors" );
		List<Completor> completors = new ArrayList<Completor>();

		try
		{
			Lexicon lex = queryEngine.getLexicon();

			synchronized ( lex )
			{
				completors.add( lex.getCompletor() );
			}

			ArrayList<String> directives = new ArrayList<String>();
			directives.add( "@count" );
			directives.add( "@define" );
			directives.add( "@export" );
			directives.add( "@help" );
			directives.add( "@list" );
			directives.add( "@prefix" );
			directives.add( "@quit" );
			directives.add( "@redefine" );
			directives.add( "@undefine" );

			completors.add(
				new DirectiveCompletor( directives ) );

			try
			{
				// This makes candidates from multiple completors available at once.
				Completor multiCompletor = new MultiCompletor( completors );

				reader.addCompletor( multiCompletor );
			}

			catch ( Throwable t )
			{
				throw new RippleException( t );
			}
		}

		catch ( RippleException e )
		{
			e.logError();
			LOGGER.error( "failed to update completors" );
		}
	}

	private class UpdateCompletorsCmd extends Command
	{
		public void execute( final QueryEngine qe, final ModelConnection mc )
			throws RippleException
		{
			updateCompletors();
		}

		protected void abort()
		{
		}
	}

	////////////////////////////////////////////////////////////////////////////

	private void addCommand( final Command cmd )
	{
//System.out.println( "addCommand(" + cmd + ")" );
		cmd.setQueryEngine( queryEngine );
		taskQueue.add( cmd );
	}

	private void executeCommands() throws RippleException
	{
//System.out.println( "executeCommands()" );
		Scheduler.add( taskQueue );
	
		consoleReaderInput.setEager( true );
	
		try
		{
			taskQueue.waitUntilFinished();
		}
	
		catch ( RippleException e )
		{
			consoleReaderInput.setEager( false );
			throw e;
		}

		consoleReaderInput.setEager( false );
	}

	private void abortCommands()
	{
//System.out.println( "abortCommands()" );
		taskQueue.stop();
	}
}

