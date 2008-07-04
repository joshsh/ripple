/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-core/src/main/java/net/fortytwo/ripple/query/LazyEvaluator.java $
 * $Revision: 6 $
 * $Author: parcour $
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

import java.util.List;
import java.util.LinkedList;

// Note: not thread-safe, on account of stop()
public class LazyDebugEvaluator extends StackEvaluator
{
	private static final Logger LOGGER = Logger.getLogger( LazyEvaluator.class );

	private boolean stopped = true;

    private class DebugStack
    {
        public final int depth;
        public final StackContext stackContext;
        public final DebugStack parent;
        public final List<DebugStack> children = new LinkedList<DebugStack>();

        public DebugStack(final StackContext stackContext, final DebugStack parent)
        {
            this.parent = parent;
            depth = ( null == parent )
                    ? 0
                    : 1 + parent.depth;
            this.stackContext = stackContext;
            if ( null != parent )
            {
                parent.children.add( this );
            }
        }

        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            for ( int i = 0; i < depth; i++ )
            {
                sb.append( "   " );
            }

            sb.append( stackContext.getStack() );

            for ( DebugStack child : children )
            {
                sb.append( "\n" )
                        .append( child );
            }

            return sb.toString();
        }
    }

    protected class EvaluatorSink implements Sink<DebugStack, RippleException>
	{
		private Sink<DebugStack, RippleException> sink;

		public EvaluatorSink( final Sink<DebugStack, RippleException> sink )
		{
			this.sink = sink;
//System.out.println( this + "( " + sink + ")" );
		}

		public void put( final DebugStack d )
			throws RippleException
		{
            final Sink<DebugStack, RippleException> thisEval = this;

			if ( stopped )
			{
				return;
			}

            StackContext arg = d.stackContext;
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

				final StackMapping f = ( (Operator) first ).getMapping();
//LOGGER.info( "   f = " + f );
//LOGGER.info( "   f.arity() = " + f.arity() );

                // Nullary functions don't need their argument stacks reduced.
				// They shouldn't even care if the stack is empty.
				if ( f.arity() == 0 )
				{
                    Sink<StackContext, RippleException> resultSink = new Sink<StackContext, RippleException>()
                    {
                        public void put( final StackContext arg ) throws RippleException
                        {
                            DebugStack sib = new DebugStack( arg, d.parent );
                            thisEval.put( sib );
                        }
                    };

                    f.applyTo( arg.with( rest ), resultSink );
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
                        Sink<DebugStack, RippleException> argSink = new Sink<DebugStack, RippleException>() {
                            public void put(final DebugStack d2) throws RippleException {
                                StackContext arg = d2.stackContext;
                                RippleList stack = arg.getStack();
                                Closure c = new Closure(f, stack.getFirst());
                                DebugStack child = new DebugStack( arg.with(stack.getRest().push(new Operator(c))), d.parent );
                                new EvaluatorSink(thisEval).put( child );
                            }
                        };

                        // Reduce the argument portion of the stack.
                        DebugStack child = new DebugStack( arg.with(stack.getRest()), d );
                        new EvaluatorSink( argSink ).put( child );
                    }
                }
			}

			else
			{
				sink.put( d );
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////

	public void applyTo( final StackContext arg,
						final Sink<StackContext, RippleException> solutions)
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

        Sink<DebugStack, RippleException> resultSink = new Sink<DebugStack, RippleException>()
        {
            public void put( final DebugStack d ) throws RippleException
            {
                solutions.put( d.stackContext );
            }
        };

        EvaluatorSink evalSink = new EvaluatorSink( resultSink );
//        EvaluatorSink evalSink = new EvaluatorSink( debugSink );
		stopped = false;

        DebugStack root = new DebugStack(arg.with( arg.getModelConnection().list() ), null);
        DebugStack child = new DebugStack( arg, root );

        try
		{
			evalSink.put( child );
		}

		// Attempt to recover from stack overflow.
		catch ( StackOverflowError e )
		{
			throw new RippleException( e );
		}

        finally
        {
            System.out.println( root.toString() );
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