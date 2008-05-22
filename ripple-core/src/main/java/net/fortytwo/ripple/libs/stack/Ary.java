/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.NullStackMapping;

/**
 * A primitive which consumes a numeric "arity" and produces an active identity
 * filter with the given arity.  This forces the remainder of the stack to be
 * reduced to the corresponding depth.
 */
public class Ary extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_06 + "ary",
            StackLibrary.NS_2007_08 + "ary",
            StackLibrary.NS_2007_05 + "ary"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Ary()
		throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	private class NaryId implements StackMapping
	{
		private int n;

		public NaryId( final int arity )
		{
			n = arity;
		}

		public int arity()
		{
			return n;
		}

		public void applyTo( final StackContext arg,
							 final Sink<StackContext, RippleException> sink
		)
			throws RippleException
		{
			sink.put( arg );
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

        public String toString()
        {
            return "NaryId(" + n + ")";
        }
    }

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> sink
	)
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		int n;

		n = mc.toNumericValue( stack.getFirst() ).intValue();
		stack = stack.getRest();

		sink.put( arg.with(
			stack.push( new Operator( new NaryId( n ) ) ) ) );
	}
}

