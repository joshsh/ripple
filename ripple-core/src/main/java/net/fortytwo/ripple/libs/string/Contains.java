/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;

/**
 * A primitive which tells whether a string contains another string as a
 * substring.
 */
public class Contains extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_08 + "contains"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Contains() throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "s1", null, true ),
                new Parameter( "s2", null, true )};
    }

    public String getComment()
    {
        return "s1 s2  =>  b  -- where b is true/false if s1 contains s2 as a substring";
    }

	public void apply( final StackContext arg,
                       final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		String b = mc.toString( stack.getFirst() );
		stack = stack.getRest();
		String a = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		RDFValue result = mc.value( a.contains( b ) );

		solutions.put( arg.with(
				stack.push( result ) ) );
	}
}
