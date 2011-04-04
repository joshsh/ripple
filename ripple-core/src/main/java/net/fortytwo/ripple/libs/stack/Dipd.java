/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.flow.Sink;

/**
 * A primitive which activates the third-to-topmost item on the stack.
 */
public class Dipd extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "dipd",
            StackLibrary.NS_2007_08 + "dipd",
            StackLibrary.NS_2007_05 + "dipd"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Dipd()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", null, true ),
                new Parameter( "y", null, true ),
                new Parameter( "z", "the program to be executed", true )};
    }

    public String getComment()
    {
        return "x y z  =>  z! x y";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleValue z, y, x;
		RippleList stack = arg.getStack();

// hack...
		z = stack.getFirst();
		stack = stack.getRest();
		y = stack.getFirst();
		stack = stack.getRest();
		x = stack.getFirst();
		stack = stack.getRest();

		solutions.put( arg.with(
				stack.push( z ).push( Operator.OP ).push( x ).push( y ) ) );
	}
}

