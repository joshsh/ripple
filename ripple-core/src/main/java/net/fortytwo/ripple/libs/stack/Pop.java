/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which removes the topmost item from the stack.
 */
public class Pop extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
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

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		stack = stack.getRest();

		if ( !stack.isNil() )
		{
			solutions.put( arg.with(	stack ) );
		}
	}
}

