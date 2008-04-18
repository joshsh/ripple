/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;

/**
 * A primitive which follows consumes an object and predicate, producing
 * all subjects such that there is a backlink from the object to the subject
 * via the predicate.  Note: the backward traversal of links is much more
 * dependent on the history of query evaluation than forward traversal, which is
 * built into Ripple's query model.
 */
public class Back extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

	public Back()
		throws RippleException
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
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		RippleValue o, p;

		p = stack.getFirst();
		stack = stack.getRest();
		o = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<RippleValue, RippleException> divSink = new Sink<RippleValue, RippleException>()
		{
			public void put( final RippleValue v )
				throws RippleException
			{
				sink.put( arg.with(
					rest.push( v ) ) );
			}
		};

		mc.divide( o, p, divSink );
	}
}

// kate: tab-width 4
