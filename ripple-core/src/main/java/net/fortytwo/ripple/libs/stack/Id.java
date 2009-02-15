/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive function which has no effect on the stack.
 */
public class Id extends PrimitiveStackMapping
{
	private static final int ARITY = 0;

    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "id",
            StackLibrary.NS_2007_08 + "id",
            StackLibrary.NS_2007_05 + "id"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Id()
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
		solutions.put( arg );
	}
}

