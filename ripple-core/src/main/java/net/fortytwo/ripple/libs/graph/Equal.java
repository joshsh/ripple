/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.libs.logic.LogicLibrary;

/**
 * A primitive which consumes two items and produces a Boolean value of true if
 * they are equal according to their data types, otherwise false.
 */
public class Equal extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2008_06 + "equal",
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

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();

		RippleValue a, b, result;

		a = stack.getFirst();
		stack = stack.getRest();
		b = stack.getFirst();
		stack = stack.getRest();

		// Note: equals() is not suitable for this operation (for instance,
		//       it may yield false for RdfValues containing identical
		//       Literals).
		result = LogicLibrary.fromBoolean( 0 == a.compareTo( b ) );

		solutions.put( arg.with( stack.push( result ) ) );
	}
}
