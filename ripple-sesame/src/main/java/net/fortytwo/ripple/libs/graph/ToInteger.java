/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.flow.Sink;

import org.apache.log4j.Logger;

/**
 * A primitive which consumes a literal value and produces its xsd:integer
 * equivalent (if any).
 */
public class ToInteger extends PrimitiveStackMapping
{
	private static final Logger LOGGER
		= Logger.getLogger( ToInteger.class );

    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2011_04 + "to-integer",
            GraphLibrary.NS_2008_08 + "toInteger",
            GraphLibrary.NS_2007_08 + "toInteger",
            GraphLibrary.NS_2007_05 + "toInteger"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public ToInteger()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", null, true )};
    }

    public String getComment()
    {
        return "x  =>  x as integer literal";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		String s;

		s = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		int i;

		try
		{
			i = new Integer( s ).intValue();
		}

		catch ( NumberFormatException e )
		{
			LOGGER.debug( "bad integer value: " + s );
			return;
		}

		solutions.put( arg.with(
				stack.push( mc.numericValue(i) ) ) );
	}
}

