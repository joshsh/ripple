/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which pushes a copy of the topmost item on the stack to the
 * head of the stack.
 */
public class Dup extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2011_08 + "dup",
            StackLibrary.NS_2008_08 + "dup",
            StackLibrary.NS_2007_08 + "dup",
            StackLibrary.NS_2007_05 + "dup"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Dup()
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
        return "x  =>  x x";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleValue x;
		RippleList stack = arg;

		x = stack.getFirst();
		stack = stack.getRest();

		solutions.put(
				stack.push( x ).push( x ) );
	}
}

