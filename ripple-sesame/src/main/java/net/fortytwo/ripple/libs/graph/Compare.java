/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes two resources and produces a comparison value
 * according to their data type.
 */
public class Compare extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2011_04 + "compare",
            GraphLibrary.NS_2008_08 + "compare",
            GraphLibrary.NS_2007_08 + "compare",
            GraphLibrary.NS_2007_05 + "compare"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Compare()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", null, true ),
                new Parameter( "x", null, true )};
    }

    public String getComment()
    {
        return "x y  =>  i  -- where i is -1 if x < y, 0 if x = y, and 1 if x > y";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		RippleValue y, x;

		y = stack.getFirst();
		stack = stack.getRest();
		x = stack.getFirst();
		stack = stack.getRest();

		int result = mc.getComparator().compare( x, y );
        
        // Constrain the result to three possible values.
        result = ( result < 0 ) ? -1 : ( result > 0 ) ? 1 : 0;

        solutions.put( arg.with( stack.push( mc.numericValue(result) ) ) );
	}
}

