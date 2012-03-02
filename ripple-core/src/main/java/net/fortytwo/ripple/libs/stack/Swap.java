/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which swaps the two topmost items on the stack.
 */
public class Swap extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2011_08 + "swap",
            StackLibrary.NS_2008_08 + "swap",
            StackLibrary.NS_2007_08 + "swap",
            StackLibrary.NS_2007_05 + "swap"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Swap()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", null, true ),
                new Parameter( "y", null, true )};
    }

    public String getComment()
    {
        return "x y  =>  y x";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;
		RippleValue y, x;

		y = stack.getFirst();
		stack = stack.getRest();
		x = stack.getFirst();
		stack = stack.getRest();

		solutions.put(
				stack.push( y ).push( x ) );
	}
}

