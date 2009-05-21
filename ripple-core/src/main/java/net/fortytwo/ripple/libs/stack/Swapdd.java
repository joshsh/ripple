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
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which swaps the third- and fourth-to-topmost items on the stack.
 */
public class Swapdd extends PrimitiveStackMapping
{
	private static final int ARITY = 4;

    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "swapdd",
            StackLibrary.NS_2007_08 + "swapdd"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Swapdd()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", null, true ),
                new Parameter( "y", null, true ),
                new Parameter( "z", null, true ),
                new Parameter( "a", null, true )};
    }

    public String getComment()
    {
        return "x y z a  =>  y x z a";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		RippleValue a, z, y, x;

		a = stack.getFirst();
		stack = stack.getRest();
		z = stack.getFirst();
		stack = stack.getRest();
		y = stack.getFirst();
		stack = stack.getRest();
		x = stack.getFirst();
		stack = stack.getRest();

		solutions.put( arg.with(
				stack.push( y ).push( x ).push( z ).push( a ) ) );
	}
}

