/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.query.commands;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.query.StackEvaluator;
import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;

public class RippleQueryCmd extends Command
{
	private final ListAST listAst;
	private final Sink<RippleList, RippleException> sink;
	private final Source<RippleList, RippleException> composedWith;

    private StackEvaluator evaluator;

    public RippleQueryCmd( final ListAST listAst,
							final Sink<RippleList, RippleException> sink,
							final Source<RippleList, RippleException> composedWith )
	{
		this.listAst = listAst;
		this.sink = sink;
		this.composedWith = composedWith;
	}

	public void execute( final QueryEngine qe, final ModelConnection mc )
		throws RippleException
	{
        final Collector<RippleList, RippleException> expressions = new Collector<RippleList, RippleException>();

        final Sink<RippleList, RippleException> exprSink = new Sink<RippleList, RippleException>()
        {
            public void put( final RippleList l ) throws RippleException
            {
                // Note: the first element of the list will also be a list
                final RippleList stack = ( (RippleList) l.getFirst() ).invert();

                Sink<RippleList, RippleException> composedWithSink = new Sink<RippleList, RippleException>()
                {
                    public void put( final RippleList base )
                        throws RippleException
                    {
                        expressions.put( stack.concat( base ) );
                    }
                };

                composedWith.writeTo( composedWithSink );
            }
        };

        listAst.evaluate( exprSink, qe, mc );

		evaluator = qe.getEvaluator();

		final Sink<StackContext, RippleException> resultSink = new Sink<StackContext, RippleException>() {

			public void put( final StackContext arg ) throws RippleException
			{
				sink.put( arg.getStack() );
			}
		};
		final Sink<RippleList, RippleException> evaluatorSink = new Sink<RippleList, RippleException>()
		{
			// Note: v will always be a list.
			public void put( final RippleList l ) throws RippleException
			{
				StackContext arg = new StackContext( l, mc );
				evaluator.apply( arg, resultSink );
			}
		};

//System.out.println( "evaluating: " + listAst );
		expressions.writeTo( evaluatorSink );		
	}

	protected void abort()
	{
//System.out.println( "aborting Ripple query command" );
        if ( null != evaluator )
        {
            evaluator.stop();
        }
    }
}

