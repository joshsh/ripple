/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.flow.Sink;

import org.apache.log4j.Logger;

/**
 * A primitive which consumes a literal value and produces its xsd:double
 * equivalent (if any).
 */
public class ToDouble extends PrimitiveStackMapping
{
	private static final Logger LOGGER
		= Logger.getLogger( ToDouble.class );

    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2008_08 + "toDouble",
            GraphLibrary.NS_2007_08 + "toDouble",
            GraphLibrary.NS_2007_05 + "toDouble"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public ToDouble()
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
        return "x  =>  x as double literal";
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

		double d;

		try
		{
			d = new Double( s ).doubleValue();
		}

		catch ( NumberFormatException e )
		{
			LOGGER.debug( "bad integer value: " + s );
			return;
		}

		solutions.put( arg.with(
				stack.push( mc.value( d ) ) ) );
	}
}

