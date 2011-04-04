/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.ModelConnection;

/**
 * A primitive which consumes two Boolean values and produces the result of
 * their inclusive logical disjunction.
 */
public class Or extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

    private static final String[] IDENTIFIERS = {
            LogicLibrary.NS_2008_08 + "or",
            StackLibrary.NS_2007_08 + "or",
            StackLibrary.NS_2007_05 + "or"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }
    
    public Or()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", "a boolean value (xsd:true or xsd:false)", true ),
                new Parameter( "y", "a boolean value (xsd:true or xsd:false)", true )};
    }

    public String getComment()
    {
        return "x y  =>  z  -- where z is true if x or y is true, otherwise false";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
        ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();
		boolean x, y;

		x = mc.toBoolean( stack.getFirst() );
		stack = stack.getRest();
		y = mc.toBoolean( stack.getFirst() );
		stack = stack.getRest();

		RippleValue result = mc.value( x || y );

		solutions.put( arg.with(
				stack.push( result ) ) );
	}
}

