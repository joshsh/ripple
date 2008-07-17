/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.StackContext;

/**
 * A filter which discards the stack.
 */
public class Scrap extends PrimitiveStackMapping
{
	private static final int ARITY = 0;

    private static final String[] IDENTIFIERS = {
            StreamLibrary.NS_2008_08 + "scrap",
            StreamLibrary.NS_2007_08 + "scrap",
            StreamLibrary.NS_2007_05 + "scrap"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Scrap()
		throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		// Do nothing.
	}
}

