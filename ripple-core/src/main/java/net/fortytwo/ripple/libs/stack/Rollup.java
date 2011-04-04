/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.flow.Sink;

/**
 * A primitive which permutes the first, second and third items on the stack
 * such that (... x y z) becomes (... z x y).
 */
public class Rollup extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "rollup",
            StackLibrary.NS_2007_08 + "rollup",
            StackLibrary.NS_2007_05 + "rollup"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Rollup()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", null, true ),
                new Parameter( "y", null, true ),
                new Parameter( "z", null, true )};
    }

    public String getComment()
    {
        return "x y z  =>  z x y";
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
				stack.push( z ).push( x ).push( y ) ) );
	}
}

