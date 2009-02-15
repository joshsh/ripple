/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which consumes a string and produces its length.
 */
public class Length extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_08 + "length",
            StringLibrary.NS_2007_08 + "length"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Length()
		throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		String s;

		s = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		solutions.put( arg.with(
				stack.push( mc.value( s.length() ) ) ) );
	}
}

