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
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.ModelConnection;

/**
 * A primitive which consumes two Boolean values and produces the result of
 * their exclusive logical disjunction.
 */
public class Xor extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            LogicLibrary.NS_2008_08 + "xor",
            StackLibrary.NS_2007_08 + "xor",
            StackLibrary.NS_2007_05 + "xor"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }
    
    public Xor()
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
        return "x y  =>  z  -- where z is the logical exclusive disjunction of truth values x and y";
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

		RippleValue result = mc.value( ( x && !y ) || ( !x && y ) );

		solutions.put( arg.with(
				stack.push( result ) ) );
	}
}

