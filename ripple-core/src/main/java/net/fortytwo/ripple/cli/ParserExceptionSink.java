/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.cli;

import java.io.PrintStream;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import antlr.RecognitionException;
import antlr.TokenStreamException;

public class ParserExceptionSink implements Sink<Exception, RippleException>
{
	private final PrintStream errorPrintStream;
	
	public ParserExceptionSink( final PrintStream ps )
	{
		errorPrintStream = ps;
	}
	
	public void put( final Exception e )
	{
		// This happens, for instance, when the parser receives a value
		// which is too large for the target data type.  Non-fatal.
		if ( e instanceof NumberFormatException )
		{
			alert( e.toString() );
		}

        // Report lexer errors to user, but don't log them.
		else if ( e instanceof TokenStreamException)
		{
			alert( "Lexer error: " + e.toString() );
		}

        // Report parser errors to user, but don't log them.
		else if ( e instanceof RecognitionException)
		{
			alert( "Parser error: " + e.toString() );
		}

        else
		{
			alert( "Error: " + e.toString() );
			( new RippleException( e ) ).logError();
		}
	}
	
	private void alert( final String s )
	{
		errorPrintStream.println( "\n" + s + "\n" );
	}
};
