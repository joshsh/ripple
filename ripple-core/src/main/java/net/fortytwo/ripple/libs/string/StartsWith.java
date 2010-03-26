/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.flow.Sink;

/**
 * A primitive which consumes a string and prefix, producing a Boolean value of
 * true if the given string starts with the given prefix, otherwise false.
 */
public class StartsWith extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_08 + "startsWith",
            StringLibrary.NS_2007_08 + "startsWith"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public StartsWith()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "s", null, true ),
                new Parameter( "prefix", null, true )};
    }

    public String getComment()
    {
        return "s prefix  =>  b -- where b is true if the given string begins with the given prefix, otherwise false";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		String affix, s;
		RippleValue result;

		affix = mc.toString( stack.getFirst() );
		stack = stack.getRest();
		s = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		result = mc.value( s.startsWith( affix ) );
        
        solutions.put( arg.with(
			stack.push( result ) ) );
	}
}

