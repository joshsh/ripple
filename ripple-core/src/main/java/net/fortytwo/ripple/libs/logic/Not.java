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
 * A primitive which consumes a Boolean value and produces its inverse.
 */
public class Not extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            LogicLibrary.NS_2008_08 + "not",
            StackLibrary.NS_2007_08 + "not",
            StackLibrary.NS_2007_05 + "not"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }
    
    public Not()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", "a boolean value (xsd:true or xsd:false)", true )};
    }

    public String getComment()
    {
        return "x  =>  y  -- where y is true if x is false, otherwise false";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
        ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();
		boolean x;

		x = mc.toBoolean( stack.getFirst() );
		stack = stack.getRest();

		RippleValue result = mc.value( !x );

		solutions.put( arg.with(
				stack.push( result ) ) );
	}
}

