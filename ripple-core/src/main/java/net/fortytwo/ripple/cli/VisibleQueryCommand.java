/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.cli;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.control.TaskSet;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RdfValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.GetStatementsQuery;
import net.fortytwo.ripple.model.StatementPatternQuery;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.query.commands.RippleQueryCmd;
import net.fortytwo.ripple.flow.Buffer;
import net.fortytwo.ripple.flow.Collector;
import net.fortytwo.ripple.flow.CollectorHistory;
import net.fortytwo.ripple.flow.NullSink;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.flow.Switch;
import net.fortytwo.ripple.flow.SynchronizedSink;
import net.fortytwo.ripple.flow.Tee;

import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;

public class VisibleQueryCommand extends Command
{
	private static RdfValue RDF_FIRST = new RdfValue( RDF.FIRST );
	
	private ListAST ast;
	private CollectorHistory<RippleList, RippleException> resultHistory;
	private boolean continued;
	private TaskSet taskSet;

	private Switch<RippleList, RippleException> results;

	public VisibleQueryCommand( final ListAST query,
							final CollectorHistory<RippleList, RippleException> history,
							final boolean continued )
	{
		ast = query;
		resultHistory = history;
		this.continued = continued;
	}

	public void execute( final QueryEngine qe, final ModelConnection mc )
		throws RippleException
	{
		boolean doBuffer = Ripple.getProperties().getBoolean(
                Ripple.BUFFER_QUERY_RESULTS );

		qe.getPrintStream().println( "" );

		// Results are first dereferenced, then placed into a buffer which
		// will be flushed into the view after the lexicon is updated.
		TurtleView view = new TurtleView(
			qe.getPrintStream(), mc );

		Sink<RippleList, RippleException> med = new SynchronizedSink<RippleList, RippleException>(
			( doBuffer
				? new Buffer<RippleList, RippleException>( view )
				: view ) );

		results = new Switch<RippleList, RippleException>(
			new Tee<RippleList, RippleException>( med, resultHistory ),
			new NullSink<RippleList, RippleException>() );

		Sink<RippleList, RippleException> derefSink = new Sink<RippleList, RippleException>()
		{
			public void put( final RippleList list) throws RippleException
			{
				dereference( list.getFirst(), mc );
				results.put( list );
			}
		};

Collector<RippleList, RippleException> nilSource = new Collector<RippleList, RippleException>();
nilSource.put( mc.list() );
		Command cmd = new RippleQueryCmd( ast, derefSink,
			( continued
				? resultHistory.getSource( 1 )
				: nilSource ) );

		// Execute the inner command and wait until it is finished.
		cmd.setQueryEngine( qe );
		taskSet = new TaskSet();
		taskSet.add( cmd );
		taskSet.waitUntilEmpty();

		// Flush results to the view.
		if ( doBuffer )
		{
			( (Buffer<RippleList, RippleException>) med ).flush();
		}

		if ( view.size() > 0 )
		{
			qe.getPrintStream().println( "" );
		}

		resultHistory.advance();
	}

	protected void abort()
	{
		// Late arrivals should not show up in the view.
		results.flip();

		taskSet.stopWaiting();
	}

	private static void dereference( final RippleValue v, final ModelConnection mc )
		throws RippleException
	{
		try
		{
            StatementPatternQuery query = new StatementPatternQuery( v, RDF_FIRST, null, false );
            mc.query( query, new NullSink<RippleValue, RippleException>() );
		}

		catch ( RippleException e )
		{
			// (soft fail... don't even log the error)
			return;
		}
	}
}
