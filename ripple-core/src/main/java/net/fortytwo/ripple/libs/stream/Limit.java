/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stream;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a number n and produces a filter which transmits
 * at most n stacks.
 */
public class Limit extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StreamLibrary.NS_2011_08 + "limit",
            StreamLibrary.NS_2008_08 + "limit",
            StreamLibrary.NS_2007_08 + "limit",
            StreamLibrary.NS_2007_05 + "limit"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Limit()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "lim", "a non-negative integer", true )};
    }

    public String getComment()
    {
        return "transmits at most lim stacks";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

		int lim;

		lim = mc.toNumericValue( stack.getFirst() ).intValue();
		stack = stack.getRest();

		solutions.put(
			stack.push(
				new Operator(
					new LimitInner( (long) lim ) ) ) );
	}

	////////////////////////////////////////////////////////////////////////////

	protected class LimitInner implements StackMapping
	{
		private long count, limit;

		public int arity()
		{
			return 1;
		}

		public LimitInner( final long lim )
		{
			limit = lim;
			count = 0;
		}

        public void apply(final RippleList arg,
                          final Sink<RippleList> solutions,
                          final ModelConnection mc) throws RippleException {
			if ( count < limit )
			{
				count++;
				solutions.put( arg );
			}
		}
		
		public boolean isTransparent()
		{
			return true;
		}

        // TODO
        public StackMapping getInverse() throws RippleException
        {
            return new NullStackMapping();
        }
    }
}

