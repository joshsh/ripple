/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.StackContext;

/**
 * A filter which discards the stack.
 */
public class Scrap extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StreamLibrary.NS_2011_04 + "scrap",
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

    public Parameter[] getParameters()
    {
        return new Parameter[] {};
    }

    public String getComment()
    {
        return "transmits no stacks";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		// Do nothing.
	}
}

