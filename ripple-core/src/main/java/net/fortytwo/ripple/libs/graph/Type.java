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
import net.fortytwo.ripple.model.RdfValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.flow.Sink;

import org.openrdf.model.Value;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;

/**
 * A primitive which consumes a literal value and produces its data type (if
 * any).
 */
public class Type extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

	public Type()
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

		v = stack.getFirst().toRDF( mc ).sesameValue();
		stack = stack.getRest();

		if ( v instanceof Literal )
		{
			URI type = ( (Literal) v ).getDatatype();

			if ( null != type )
			{
				sink.put( arg.with(
						stack.push( new RdfValue( type ) ) ) );
			}
		}
	}
}

// kate: tab-width 4
