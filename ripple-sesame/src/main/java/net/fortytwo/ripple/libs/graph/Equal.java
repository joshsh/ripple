/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.flow.Sink;

/**
 * A primitive which consumes two items and produces a Boolean value of true if
 * they are equal according to their data types, otherwise false.
 */
public class Equal extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2008_08 + "equal",
            GraphLibrary.NS_2007_08 + "equal",
            GraphLibrary.NS_2007_05 + "equal"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Equal() throws RippleException
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
        return "x y  =>  b  -- where b is true if x and y are equal, otherwise false";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
        ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();

		RippleValue a, b, result;

		a = stack.getFirst();
		stack = stack.getRest();
		b = stack.getFirst();
		stack = stack.getRest();

		// Note: equals() is not suitable for this operation (for instance,
		//       it may yield false for RdfValues containing identical
		//       Literals).
		result = mc.value( 0 == mc.getComparator().compare( a, b ) );

		solutions.put( arg.with( stack.push( result ) ) );
	}
}
