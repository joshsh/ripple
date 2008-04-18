/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.cli;

import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.PipedIOStream;
import net.fortytwo.ripple.flow.Collector;
import net.fortytwo.ripple.flow.NullSink;
import net.fortytwo.ripple.RippleException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;

public class InterpreterTest extends TestCase
{
	private static final long WAIT_INTERVAL = 100l;
	private static final byte[] NEWLINE = { '\n' };

	private int lineNumber;
	private int getLineNumber() { return lineNumber; }
	private void setLineNumber( int n ) { lineNumber = n; }

	private void parse( final InputStream is, final boolean expectSuccess )
		throws Exception
	{
//		final Integer nQueries, nCommands, nEvents, lineNumber;
//		nQueries = nCommands = nEvents =
		lineNumber = 0;

		final PipedIOStream pio = new PipedIOStream();

		Collector<Exception, RippleException> exceptions = new Collector<Exception, RippleException>();
		RecognizerAdapter ra = new RecognizerAdapter(
				new NullSink<ListAST, RippleException>(),
				new NullSink<ListAST, RippleException>(),
				new NullSink<Command, RippleException>(),
				new NullSink<RecognizerEvent, RippleException>(),
				System.err );
		final Interpreter interpreter = new Interpreter( ra, pio, exceptions );

		Thread interpreterThread = new Thread( new Runnable()
			{
				public void run()
				{
					try
					{
						interpreter.parse();
					}

					catch ( Exception e )
					{
						// Throw out the error, for now.
						e.printStackTrace();
					}
				}
			 } );

		interpreterThread.start();

		final BufferedReader reader = new BufferedReader(
			new InputStreamReader( is ) );

		String line;

		do
		{
			exceptions.clear();

			lineNumber++;
System.out.println("line #" + lineNumber);
			line = reader.readLine();

			if ( null == line )
			{
				break;
			}

			pio.write( ( line.trim() + '\n' ).getBytes() );

			do
			{
	//System.out.println( "waiting " + WAIT_INTERVAL + " milliseconds" );
				synchronized ( this )
				{
					// FIXME: the first wait depends on a race condition
					try
					{
						wait( WAIT_INTERVAL );
					}

					catch ( InterruptedException e )
					{
						throw new RippleException( e );
					}
				}
			} while ( Thread.State.RUNNABLE == interpreterThread.getState() );

			if ( expectSuccess )
			{
				if ( exceptions.size() > 0 )
				{
					fail( "Success case failed on line "
							+ lineNumber + ": " + line
							+ ", with exception = " + exceptions.iterator().next() );
				}
			}

			else
			{
				if ( exceptions.size() < 1 )
				{
					fail( "Failure case succeeded on line "
							+ lineNumber + ": " + line );
				}
			}
		} while ( true );


		pio.close();
	}

/*	private void parse( final InputStream is, final boolean expectSuccess )
		throws Exception
	{
//		final Integer nQueries, nCommands, nEvents, lineNumber;
//		nQueries = nCommands = nEvents = lineNumber = 0;
		setLineNumber( 0 );

		final Collector<Exception> exceptions = new Collector<Exception>();

		Sink<ListAst> querySink = new Sink<ListAst>()
		{
			public void put( final ListAst ast )
				throws RippleException
			{
//				nQueries++;
			}
		};

		Sink<Command> commandSink = new Sink<Command>()
		{
			public void put( final Command cmd )
				throws RippleException
			{
//				nCommands++;
			}
		};

		final PipedIOStream pio = new PipedIOStream();

		final BufferedReader reader = new BufferedReader(
			new InputStreamReader( is ) );

		Sink<RecognizerEvent> eventSink = new Sink<RecognizerEvent>()
		{
			public void put( final RecognizerEvent event )
				throws RippleException
			{
//				nEvents++;
				switch ( event )
				{
					case NEWLINE:
System.out.println( "got a newline!!!!!!!!!!!!!!!!" );
						String line;

						// If lineNumber > 0, either a line has been
						// successfully matched, or an exception was thrown in
						// matching a line.
						if ( getLineNumber() > 0 )
						{
System.out.println( "(" + expectSuccess + ") exceptions.size() = " + exceptions.size() );
							if ( expectSuccess )
							{
								if ( exceptions.size() > 0 )
									fail( "Success case failed on line "
										+ getLineNumber() + ": " );// + line );
//System.out.println("    success!");
							}
		
							else
							{
								if ( exceptions.size() < 1 )
									fail( "Failure case succeeded on line "
										+ getLineNumber() + ": " );//+ line );
//System.out.println("    failure!");
							}

							exceptions.clear();
						}

						do
						{
							try
							{
								line = reader.readLine();
							}
	
							catch ( java.io.IOException e )
							{
								throw new RippleException( e );
							}

							if ( null == line )
							{
								throw new ParserQuitException();
							}

							line = line.trim();

						} while ( line.startsWith( "#" ) || line.equals( "" ) );
System.out.println( "(" + expectSuccess + ") testing line " + getLineNumber() + ": " + line );

						// If we're here, there is another line of input to match.
						try
						{
System.out.println( "pushing line to readOut: " + line );
							pio.write( line.getBytes() );
							pio.write( NEWLINE );
//readOut.flush();
//							readOut.write( ' ' );
						}

						catch ( java.io.IOException e )
						{
							throw new RippleException( e );
						}

						setLineNumber( getLineNumber() + 1 );

						break;
					case ESCAPE:
						break;
					default:
						throw new RippleException( "event not yet supported: "
							+ event );
				}
			}
		};

		final RecognizerAdapter rc = new RecognizerAdapter(
			querySink, querySink, commandSink, eventSink, System.err );

		Sink<Exception> exceptionSink = new Sink<Exception>()
		{
			public void put( final Exception e )
				throws RippleException
			{
System.out.println( "########## got an exception: " + e );
				rc.putEvent( RecognizerEvent.NEWLINE );

				exceptions.put( e );
			}
		};

		Interpreter interpreter = new Interpreter( rc, pio, exceptionSink );
		interpreter.parse();

		pio.close();
	}*/

	////////////////////////////////////////////////////////////////////////////

    public void testNothing() throws Exception
    {

    }
    
/*
    public void testSuccessCasesTest() throws Exception
    {
        InputStream is = InterpreterTest.class.getResourceAsStream( "successCases.rpl" );
        parse( is, true );
        is.close();
    }

    public void testFailureCases() throws Exception
    {
        InputStream is = InterpreterTest.class.getResourceAsStream( "failureCases.rpl" );
        parse( is, false );
        is.close();
    }
*/
}
