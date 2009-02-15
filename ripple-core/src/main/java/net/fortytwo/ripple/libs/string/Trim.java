/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes a string, strips off all leading and trailing
 * white space, and produces the result.
 */
public class Trim extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_08 + "trim",
            StringLibrary.NS_2007_08 + "trim"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Trim()
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

		RippleValue s = stack.getFirst();
		stack = stack.getRest();

        String result = mc.toString( s ).trim();

		solutions.put( arg.with(
				stack.push( StringLibrary.value( result, mc, s ) ) ) );
	}
}

