/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.ModelConnection;

/**
 * A primitive which consumes a Boolean value b, a filter t, and a filter f,
 * then produces an active copy of t if b is true, otherwise an active copy of
 * f.
 */
public class Branch extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            LogicLibrary.NS_2008_08 + "branch",
            StackLibrary.NS_2007_08 + "branch",
            StackLibrary.NS_2007_05 + "branch"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }
    
    public Branch()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "b", "a boolean condition", true ),
                new Parameter( "t", "this program is executed if the condition is true", true ),
                new Parameter( "f", "this program is executed if the condition is false", true )};
    }

    public String getComment()
    {
        return "b t f  =>  p!  -- where p is t if b is true, f if b is false";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
        ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		RippleValue falseProg = stack.getFirst();
		stack = stack.getRest();
		RippleValue trueProg = stack.getFirst();
		stack = stack.getRest();
		boolean b = mc.toBoolean( stack.getFirst() );
		stack = stack.getRest();

		RippleValue result = b ? trueProg : falseProg;

		solutions.put( arg.with(
				stack.push( result ).push( Operator.OP ) ) );
	}
}
