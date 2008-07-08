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
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.query.Command;

public class RecognizerAdapter
{
	private final Sink<ListAST, RippleException> querySink, continuingQuerySink;
	private final Sink<Command, RippleException> commandSink;
	private final Sink<RecognizerEvent, RippleException> eventSink;
	private final PrintStream errorStream;

	// A helper variable for the lexer and parser.
	private String languageTag;

	public RecognizerAdapter( final Sink<ListAST, RippleException> querySink,
								final Sink<ListAST, RippleException> continuingQuerySink,
								final Sink<Command, RippleException> commandSink,
								final Sink<RecognizerEvent, RippleException> eventSink,
								final PrintStream errorStream )
	{
		this.querySink = querySink;
		this.continuingQuerySink = continuingQuerySink;
		this.commandSink = commandSink;
		this.eventSink = eventSink;
		this.errorStream = errorStream;
	}

	public void putQuery( final ListAST ast )
	{
		try
		{
			querySink.put( ast );
		}

		catch ( RippleException e )
		{
			errorStream.println( "\nQuery error: " + e + "\n" );
			e.logError();
		}
	}

	public void putContinuingQuery( final ListAST ast )
	{
		try
		{
			continuingQuerySink.put( ast );
		}

		catch ( RippleException e )
		{
			errorStream.println( "\nQuery error: " + e + "\n" );
			e.logError();
		}
	}

	public void putCommand( final Command cmd )
	{
		try
		{
			commandSink.put( cmd );
		}

		catch ( RippleException e )
		{
			errorStream.println( "\nCommand error: " + e + "\n" );
			e.logError();
		}
	}

	public void putEvent( final RecognizerEvent event )
	{
//System.out.println("putting event: " + event);
        try
		{
			eventSink.put( event );
		}

		catch ( RippleException e )
		{
			errorStream.println( "\nEvent error: " + e + "\n" );
			e.logError();
		}
	}

	public String getLanguageTag()
	{
		return languageTag;
	}

	public void setLanguageTag( final String tag )
	{
		languageTag = tag;
	}
}

