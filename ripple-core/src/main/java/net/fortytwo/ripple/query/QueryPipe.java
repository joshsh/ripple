/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.query;

import net.fortytwo.flow.HistorySink;
import net.fortytwo.flow.Tee;
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
import net.fortytwo.ripple.model.ListGenerator;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.commands.DefineKeywordCmd;
import net.fortytwo.ripple.query.commands.RippleQueryCmd;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * A pipeline for evaluating expressions in Ripple's text-based syntax.
 * Each submitted String must be a complete, valid expression consisting of a sequence of programs and/or commands.
 * Expressions are pushed into the QueryPipe using the put method,
 * and the results flow from the other end of the pipe into the specified sink.
 * Results arrive at any time and in any order, depending on the intermediate components of the pipe.
 */
public class QueryPipe implements Sink<String> {
    private final RecognizerAdapter recognizerAdapter;
    private final Sink<Exception> parserExceptionSink;
    private final Buffer<RippleList> resultBuffer;
    private final HistorySink<RippleList> queryResultHistory
            = new HistorySink<RippleList>(1);
    private final ModelConnection connection;

    public QueryPipe(final QueryEngine queryEngine,
                     final Sink<RippleList> resultSink) throws RippleException {
        connection = queryEngine.createConnection();

        resultBuffer = new Buffer<RippleList>(resultSink);
        final Object mutex = "";

        final Sink<RippleList> resultTee = new Tee<RippleList>
                (resultBuffer, queryResultHistory);

        recognizerAdapter = new RecognizerAdapter(queryEngine.getErrorPrintStream()) {
            protected void handleQuery(ListAST ast) throws RippleException {
                synchronized (mutex) {
                    queryResultHistory.advance();

                    new RippleQueryCmd(ast, resultTee).execute(queryEngine, connection);
                    connection.commit();
                }
            }

            protected void handleCommand(Command command) throws RippleException {
                command.execute(queryEngine, connection);
                connection.commit();
            }

            protected void handleEvent(RecognizerEvent event) throws RippleException {
                // TODO
            }

            protected void handleAssignment(KeywordAST name) throws RippleException {
                Source<RippleList> source = queryResultHistory.get(0);
                if (null == source) {
                    source = new Collector<RippleList>();
                }

                new DefineKeywordCmd(name, new ListGenerator(source)).execute(queryEngine, connection);
                connection.commit();
            }
        };

        parserExceptionSink = new ParserExceptionSink(
                queryEngine.getErrorPrintStream());
    }

    public ModelConnection getConnection() {
        return connection;
    }

    public void close() throws RippleException {
        connection.close();
    }

    public void put(final InputStream input) throws RippleException {
        // TODO: creating a new Interpreter for each unit of input is not very efficient
        Interpreter interpreter = new Interpreter(recognizerAdapter, input, parserExceptionSink);
        interpreter.parse();

        try {
            input.close();
        } catch (IOException e) {
            throw new RippleException(e);
        }

        connection.finish();
        resultBuffer.flush();
    }

    public void put(final String expr) throws RippleException {
//        System.out.println("interpreting query: " + expr);
//System.exit(1);
        InputStream input = new ByteArrayInputStream((expr + "\n").getBytes());

        try {
            try {
                put(input);
            } finally {
                input.close();
            }
        } catch (IOException e) {
            throw new RippleException(e);
        }
    }
}
