/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.flow.Sink;

import org.openrdf.model.Value;
import org.openrdf.model.Literal;

/**
 * A primitive which consumes a plain literal value and produces its language
 * tag (or an empty string if the literal has no language tag).
 */
public class Lang extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

	public Lang()
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

		Value v;
		String result;

		v = stack.getFirst().toRDF( mc ).sesameValue();
		stack = stack.getRest();

		if ( v instanceof Literal )
		{
			result = ( (Literal) v ).getLanguage();

			if ( null == result )
			{
				result = "";
			}

			sink.put( arg.with(
					stack.push( mc.value( result ) ) ) );
		}
	}
}

// kate: tab-width 4
