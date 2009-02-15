/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which pushes a copy of the third-to-topmost item on the stack to
 * the head of the stack.
 */
public class Dupdd extends PrimitiveStackMapping
{
	private static final int ARITY = 3;

    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "dupdd",
            StackLibrary.NS_2007_08 + "dupdd"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Dupdd()
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
		RippleValue z, y, x;
		RippleList stack = arg.getStack();

		z = stack.getFirst();
		stack = stack.getRest();
		y = stack.getFirst();
		stack = stack.getRest();
		x = stack.getFirst();
		stack = stack.getRest();

		solutions.put( arg.with(
				stack.push( x ).push( x ).push( y ).push( z ) ) );
	}
}

