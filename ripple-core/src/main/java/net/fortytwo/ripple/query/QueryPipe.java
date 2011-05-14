/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.query;

import net.fortytwo.flow.HistorySink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Buffer;
import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;
import net.fortytwo.ripple.cli.Interpreter;
import net.fortytwo.ripple.cli.ParserExceptionSink;
import net.fortytwo.ripple.cli.RecognizerAdapter;
import net.fortytwo.ripple.cli.RecognizerEvent;
import net.fortytwo.ripple.cli.ast.KeywordAST;
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
	private final HistorySink<RippleList, RippleException> queryResultHistory
		= new HistorySink<RippleList, RippleException>( 2 );

	public QueryPipe( final QueryEngine queryEngine, final Sink<RippleList, RippleException> resultSink ) throws RippleException
	{
		resultBuffer = new Buffer<RippleList, RippleException>( resultSink );
		final Object mutex = "";

		recognizerAdapter = new RecognizerAdapter(queryEngine.getErrorPrintStream() ){
            @Override
            protected void handleQuery(ListAST ast) throws RippleException {
				synchronized ( mutex )
				{
                    queryResultHistory.advance();

                    ModelConnection mc = queryEngine.getConnection();
                    try {
                        new RippleQueryCmd( ast, resultBuffer ).execute( queryEngine, mc );
                    } finally {
                        mc.close();
                    }
				}
            }

            @Override
            protected void handleCommand(Command command) throws RippleException {
				ModelConnection mc = queryEngine.getConnection();
                try {
                    command.execute( queryEngine, mc );
                } finally {
                    mc.close();
                }
            }

            @Override
            protected void handleEvent(RecognizerEvent event) throws RippleException {
                // TODO
            }

            @Override
            protected void handleAssignment(KeywordAST name) throws RippleException {
                // TODO
            }
        };

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
        InputStream input = new ByteArrayInputStream( (expr + "\n").getBytes() );

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
