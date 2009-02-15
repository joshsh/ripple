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
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.ModelConnection;

/**
 * A primitive which consumes two Boolean values and produces the result of
 * their logical conjunction.
 */
public class And extends PrimitiveStackMapping
{
    private static final int ARITY = 2;
    
    private static final String[] IDENTIFIERS = {
            LogicLibrary.NS_2008_08 + "and",
            StackLibrary.NS_2007_08 + "and",
            StackLibrary.NS_2007_05 + "and"};

    public And() throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions ) throws RippleException
	{
        ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		boolean x, y;

		x = mc.toBoolean( stack.getFirst() );
		stack = stack.getRest();
		y = mc.toBoolean( stack.getFirst() );
		stack = stack.getRest();

		RippleValue result = mc.value( x && y );

		solutions.put( arg.with(
				stack.push( result ) ) );
	}
}

