/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which permutes the second, third and fourth items on the stack
 * such that (... x y z ...) becomes (... y z x ...).
 */
public class Rolldownd extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2011_08 + "rolldownd",
            StackLibrary.NS_2008_08 + "rolldownd",
            StackLibrary.NS_2007_08 + "rolldownd",
            StackLibrary.NS_2007_05 + "rolldownd"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Rolldownd()
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
        return "x y z a  =>  y z x a";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext> solutions )
            throws RippleException
	{
		RippleList stack = arg.getStack();
		RippleValue w, z, y, x;

		w = stack.getFirst();
		stack = stack.getRest();
		z = stack.getFirst();
		stack = stack.getRest();
		y = stack.getFirst();
		stack = stack.getRest();
		x = stack.getFirst();
		stack = stack.getRest();

		solutions.put( arg.with(
				stack.push( y ).push( z ).push( x ).push( w ) ) );
	}
}

