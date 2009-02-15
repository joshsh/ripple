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
 * A primitive which permutes the first, second and third items on the stack
 * such that (... x y z) becomes (... y z x).
 */
public class Rolldown extends PrimitiveStackMapping
{
	private static final int ARITY = 3;

    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "rolldown",
            StackLibrary.NS_2007_08 + "rolldown",
            StackLibrary.NS_2007_05 + "rolldown"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Rolldown()
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
		RippleValue z, y, x;

		z = stack.getFirst();
		stack = stack.getRest();
		y = stack.getFirst();
		stack = stack.getRest();
		x = stack.getFirst();
		stack = stack.getRest();

		solutions.put( arg.with(
				stack.push( y ).push( z ).push( x ) ) );
	}
}

