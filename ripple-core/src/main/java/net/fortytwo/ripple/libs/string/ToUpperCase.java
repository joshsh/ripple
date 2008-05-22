/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a string, maps its characters to upper case, and
 * produces the result.
 */
public class ToUpperCase extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_06 + "toUpperCase",
            StringLibrary.NS_2007_08 + "toUpperCase"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public ToUpperCase()
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
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		String s, result;

		s = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		result = s.toUpperCase();

		sink.put( arg.with(
				stack.push( mc.value( result ) ) ) );
	}
}

