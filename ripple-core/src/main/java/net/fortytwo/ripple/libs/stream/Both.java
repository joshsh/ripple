/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;

/**
 * A filter which consumes two items and produces each item in its own stack.
 */
public class Both extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StreamLibrary.NS_2008_08 + "both",
            StreamLibrary.NS_2007_08 + "union",
            StreamLibrary.NS_2007_05 + "union"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Both()
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
        return "x y -> x, y";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		RippleValue x, y;

		x = stack.getFirst();
		stack = stack.getRest();
		y = stack.getFirst();
		stack = stack.getRest();

		solutions.put( arg.with( stack.push( x ) ) );
		solutions.put( arg.with( stack.push( y ) ) );
	}
}

