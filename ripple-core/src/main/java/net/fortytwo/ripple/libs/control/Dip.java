/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.flow.Sink;

/**
 * A primitive which activates the second-to-topmost item on the stack.
 */
public class Dip extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2011_04 + "dip",
            StackLibrary.NS_2008_08 + "dip",
            StackLibrary.NS_2007_08 + "dip",
            StackLibrary.NS_2007_05 + "dip"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Dip()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", "placed above the executed program y on the stack", true ),
                new Parameter( "y", "the program to be executed", true )};
    }

    public String getComment()
    {
        return "x y  =>  y! x";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleValue y, x;
		RippleList stack = arg.getStack();

// hack...
		y = stack.getFirst();
		stack = stack.getRest();
		x = stack.getFirst();
		stack = stack.getRest();

		solutions.put( arg.with(
				stack.push( y ).push( Operator.OP ).push( x ) ) );
	}
}

