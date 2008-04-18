/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.query;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.Closure;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import org.apache.log4j.Logger;

// Note: not thread-safe, on account of stop()
public class LazyEvaluator extends StackEvaluator
{
	private static final Logger LOGGER = Logger.getLogger( LazyEvaluator.class );

	private boolean stopped = true;

	////////////////////////////////////////////////////////////////////////////

	protected class MappingSink implements Sink<StackContext, RippleException>
	{
		private StackMapping mapping;
		private Sink<StackContext, RippleException> sink;

		public MappingSink( final StackMapping mapping, final Sink<StackContext, RippleException> sink )
		{
			this.mapping = mapping;
			this.sink = sink;
//System.out.println( this + "( " + mapping + ", " + sink + ")" );
//System.out.println( "    mapping.arity() = " + mapping.arity() );
		}

		public void put( final StackContext arg ) throws RippleException
		{
			if ( stopped )
			{
				return;
			}

			if ( mapping.arity() == 1 )
			{
				mapping.applyTo( arg, sink );
			}

			else
			{
				RippleList stack = arg.getStack();
				RippleValue first = stack.getFirst();
				RippleList rest = stack.getRest();

				Closure c = new Closure(mapping, first );

				sink.put( arg.with( rest.push( new Operator( c ) ) ) );
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////

	protected class EvaluatorSink implements Sink<StackContext, RippleException>
	{
		private Sink<StackContext, RippleException> sink;

		public EvaluatorSink( final Sink<StackContext, RippleException> sink )
		{
			this.sink = sink;
//System.out.println( this + "( " + sink + ")" );
		}

		public void put( final StackContext arg )
			throws RippleException
		{
			if ( stopped )
			{
				return;
			}

			RippleList stack = arg.getStack();
//LOGGER.info( this + ".put( " + stack + " )" );
//System.out.println( this + " -- stack = " + stack );
			RippleValue first = stack.getFirst();
//LOGGER.info( "   first = " + stack.getFirst() );
//LOGGER.info( "   first.isActive() = " + first.isActive() );
//System.out.println( "   first = " + stack.getFirst() );

			if ( first.isActive() )
			{
				RippleList rest = stack.getRest();
//LOGGER.info( "   rest = " + rest );

				StackMapping f = ( (Operator) first ).getMapping();
//LOGGER.info( "   f = " + f );
//LOGGER.info( "   f.arity() = " + f.arity() );

				// Nullary functions don't need their argument stacks reduced.
				// They shouldn't even care if the stack is empty.
				if ( f.arity() == 0 )
				{
					f.applyTo( arg.with( rest ), this );
				}

				// Functions with positive arity do require the stack to be
				// reduced, to one level per argument.
				else
				{
					// We simply ignore stacks which can't be reduced to
					// something with a passive item on top.
					if ( rest.isNil() )
					{
						return;
//						sink.put( stack );
					}

					else
					{
						( new EvaluatorSink(
							new MappingSink( f, this ) ) ).put( arg.with( rest ) );
					}
				}
			}

			else
			{
				sink.put( arg );
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////

	public void applyTo( final StackContext arg,
						final Sink<StackContext, RippleException> sink )
		throws RippleException
	{
if ( arg.getStack().isNil() )
{
	return;
}

/*Sink<StackContext, RippleException> debugSink = new Sink<StackContext, RippleException>() {
    public void put(final StackContext ctx) throws RippleException {
        System.out.println("yielding value: " + ctx.getStack());
        sink.put(ctx);
    }
};*/

        EvaluatorSink evalSink = new EvaluatorSink( sink );
//        EvaluatorSink evalSink = new EvaluatorSink( debugSink );
		stopped = false;

		try
		{
			evalSink.put( arg );
		}

		// Attempt to recover from stack overflow.
		catch ( StackOverflowError e )
		{
			throw new RippleException( e );
		}
	}

	public void stop()
	{
		synchronized ( this )
		{
			stopped = true;
		}
	}
}

// kate: tab-width 4
