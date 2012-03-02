/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which removes the topmost item from the stack.
 */
public class Pop extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2011_08 + "pop",
            StackLibrary.NS_2008_08 + "pop",
            StackLibrary.NS_2007_08 + "pop",
            StackLibrary.NS_2007_05 + "pop"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Pop()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", null, true )};
    }

    public String getComment()
    {
        return "x  =>";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;
		stack = stack.getRest();

		if ( !stack.isNil() )
		{
			solutions.put( stack );
		}
	}
}

