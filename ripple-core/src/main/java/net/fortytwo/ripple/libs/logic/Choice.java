/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which consumes a Boolean value b, an item t, and an item f, then
 * produces t if b is true, otherwise f.
 */
public class Choice extends PrimitiveStackMapping
{
	private static final int ARITY = 3;

    private static final String[] IDENTIFIERS = {
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

	public int arity()
	{
		return ARITY;
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

