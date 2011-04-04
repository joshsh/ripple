/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.query;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Buffer;
import net.fortytwo.flow.Collector;
import net.fortytwo.flow.CollectorHistory;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;
import net.fortytwo.ripple.cli.Interpreter;
import net.fortytwo.ripple.cli.ParserExceptionSink;
import net.fortytwo.ripple.cli.RecognizerAdapter;
import net.fortytwo.ripple.cli.RecognizerEvent;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.commands.RippleQueryCmd;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * A simple query pipeline.  Each submitted String must be a complete, valid
 * expression or sequence of expressions.
 */
public class QueryPipe implements Sink<String, RippleException>
{
	private final RecognizerAdapter recognizerAdapter;
    private final Sink<Exception, RippleException> parserExceptionSink;
    private final Buffer<RippleList, RippleException> resultBuffer;
	private final CollectorHistory<RippleList, RippleException> queryResultHistory
		= new CollectorHistory<RippleList, RippleException>( 2 );

    private boolean lastQueryContinued = false;

	public QueryPipe( final QueryEngine queryEngine, final Sink<RippleList, RippleException> resultSink ) throws RippleException
	{
		resultBuffer = new Buffer<RippleList, RippleException>( resultSink );
		final Object mutex = resultBuffer;

		final Collector<RippleList, RippleException> nilSource = new Collector<RippleList, RippleException>();
		// FIXME: this is stupid
		ModelConnection mc = queryEngine.getConnection();
		nilSource.put( mc.list() );
                mc.close();

		// Handling of queries
		Sink<ListAST, RippleException> querySink = new Sink<ListAST, RippleException>()
		{
			public void put( final ListAST ast ) throws RippleException
			{
//System.out.println( "### received: " + ast );
				synchronized ( mutex )
				{
					Source<RippleList, RippleException> composedWith = lastQueryContinued
							? queryResultHistory.getSource( 1 ) : nilSource;

					ModelConnection mc = queryEngine.getConnection();
                    try {
                        new RippleQueryCmd( ast, resultBuffer, composedWith ).execute( queryEngine, mc );
                    } finally {
                        mc.close();
                    }

                    lastQueryContinued = false;
					queryResultHistory.advance();
				}
			}
		};

		// Handling of "continuing" queries
		Sink<ListAST, RippleException> continuingQuerySink = new Sink<ListAST, RippleException>()
		{
			public void put( final ListAST ast ) throws RippleException
			{
				synchronized ( mutex )
				{
					Source<RippleList, RippleException> composedWith = lastQueryContinued
							? queryResultHistory.getSource( 1 ) : nilSource;

					ModelConnection mc = queryEngine.getConnection();
                    try {
                        new RippleQueryCmd( ast, resultBuffer, composedWith ).execute( queryEngine, mc );
                    } finally {
                        mc.close();
                    }

                    lastQueryContinued = true;
					queryResultHistory.advance();
				}
			}
		};

		// Handling of commands
		Sink<Command, RippleException> commandSink = new Sink<Command, RippleException>()
		{
			public void put( final Command cmd ) throws RippleException
			{
				ModelConnection mc = queryEngine.getConnection();
                try {
                    cmd.execute( queryEngine, mc );
                } finally {
                    mc.close();
                }
            }
		};

		// Handling of parser events
		Sink<RecognizerEvent, RippleException> eventSink = new Sink<RecognizerEvent, RippleException>()
		{
			public void put( final RecognizerEvent event )
				throws RippleException
			{
				/*if ( RecognizerEvent.QUIT == event )
				{
					throw new ParserQuitException();
				}*/
				
				// (ignore other events)
			}
		};
		
		recognizerAdapter = new RecognizerAdapter(
				querySink, continuingQuerySink, commandSink, eventSink, queryEngine.getErrorPrintStream() );

		parserExceptionSink = new ParserExceptionSink(
				queryEngine.getErrorPrintStream() );
	}
	
	public void close() throws RippleException
	{
	}
	
    public void put( final InputStream input ) throws RippleException
    {
        // TODO: creating a new Interpreter for each unit of input is not very efficient
        Interpreter interpreter = new Interpreter( recognizerAdapter, input, parserExceptionSink );
        interpreter.parse();

        try {
            input.close();
        } catch ( IOException e ) {
            throw new RippleException( e );
        }

		resultBuffer.flush();
    }

    public void put( final String expr ) throws RippleException
	{
//System.out.println("interpreting query: " + expr);
        InputStream input = new ByteArrayInputStream( expr.getBytes() );

        try {
            put( input );
        } finally {
            try {
                input.close();
            } catch ( IOException e ) {
                throw new RippleException( e );
            }
        }
	}
}
