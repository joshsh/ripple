/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.ListMemoizer;
import net.fortytwo.ripple.flow.Sink;


/**
 * A filter which drops any stack which has already been transmitted and behaves
 * like the identity filter otherwise, making a stream of stacks into a set of
 * stacks.
 */
public class Intersect extends PrimitiveStackMapping
{
	private static final int ARITY = 2;
	private static final String MEMO = "memo";

	public Intersect() throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> sink
	)
		throws RippleException
	{
		RippleList stack = arg.getStack();

		RippleValue rtrue = stack.getFirst();
		stack = stack.getRest();
		RippleValue rfalse = stack.getFirst();
		stack = stack.getRest();

		Operator inner = new Operator( new IntersectInner() );
		sink.put( arg.with(
				stack.push( rtrue ).push( Operator.OP ).push( StackLibrary.getTrueValue() ).push( inner ) ) );
		sink.put( arg.with(
				stack.push( rfalse ).push( Operator.OP ).push( StackLibrary.getFalseValue() ).push( inner ) ) );
	}

	////////////////////////////////////////////////////////////////////////////

	protected class IntersectInner implements StackMapping
	{
		private ListMemoizer<RippleValue, String> trueMemoizer = null;
		private ListMemoizer<RippleValue, String> falseMemoizer = null;

		public int arity()
		{
			// Require that the remainder of the stack (below the marker) is
			// in normal form.  This is somewhat arbitrary.
			return 2;
		}

		public void applyTo( final StackContext arg,
							 final Sink<StackContext, RippleException> sink
		)
			throws RippleException
		{
			RippleList stack = arg.getStack();
			RippleValue marker = stack.getFirst();

			if ( marker.equals( StackLibrary.getTrueValue() ) )
			{
				applyTrue( arg, sink );
			}

			if ( marker.equals( StackLibrary.getFalseValue() ) )
			{
				applyFalse( arg, sink );
			}
		}

		private void applyTrue( final StackContext arg,
								final Sink<StackContext, RippleException> sink ) throws RippleException
		{
			RippleList stack = arg.getStack().getRest();

			if ( null == trueMemoizer )
			{
				trueMemoizer = new ListMemoizer<RippleValue, String>( stack, MEMO );
			}

			else
			{
				trueMemoizer.put( stack, MEMO );
			}

			if ( null != falseMemoizer && null != falseMemoizer.get( stack ) )
			{
				sink.put( arg.with( stack ) );
			}
		}

		private void applyFalse( final StackContext arg,
								final Sink<StackContext, RippleException> sink ) throws RippleException
		{
			RippleList stack = arg.getStack().getRest();

			if ( null == falseMemoizer )
			{
				falseMemoizer = new ListMemoizer<RippleValue, String>( stack, MEMO );
			}

			else
			{
				falseMemoizer.put( stack, MEMO );
			}

			if ( null != trueMemoizer && null != trueMemoizer.get( stack ) )
			{
				sink.put( arg.with( stack ) );
			}
		}

		public boolean isTransparent()
		{
			return true;
		}

        // TODO
        public StackMapping inverse() throws RippleException
        {
            return new NullStackMapping();
        }
    }
}
