/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.logic.LogicLibrary;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.flow.Sink;

/**
 * A primitive which consumes a Boolean value b, an item t, and an item f, then
 * produces t if b is true, otherwise f.
 */
public class Choice extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            ControlLibrary.NS_2011_08 + "choice",
            LogicLibrary.NS_2008_08 + "choice",
            StackLibrary.NS_2007_08 + "choice",
            StackLibrary.NS_2007_05 + "choice"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }
  
	public Choice()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "b", "a boolean condition", true ),
                new Parameter( "t", "the value chosen if b is true", true ),
                new Parameter( "f", "the value chosen if b is not true", true )};
    }

    public String getComment()
    {
        return "b t f  =>  x  -- where x is t if b is true, otherwise f";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleValue f, t, b;
        ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		f = stack.getFirst();
		stack = stack.getRest();
		t = stack.getFirst();
		stack = stack.getRest();
		b = stack.getFirst();
		stack = stack.getRest();

		RippleValue result = mc.toBoolean( b ) ? t : f;

		solutions.put( arg.with(
				stack.push( result ) ) );
	}
}

