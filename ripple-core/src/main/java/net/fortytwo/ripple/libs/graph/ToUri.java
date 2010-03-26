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
import net.fortytwo.flow.Sink;

import org.openrdf.model.URI;

/**
 * A primitive which consumes a literal value and produces the resource
 * identified by the corresponding URI (if any).
 */
public class ToUri extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2008_08 + "toUri",
            GraphLibrary.NS_2007_08 + "toUri"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public ToUri()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "s", null, true )};
    }

    public String getComment()
    {
        return "s  =>  uri -- where uri is the URI whose string representation is s";
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

		URI uri = mc.createURI( s );

		solutions.put( arg.with(
				stack.push( mc.value( uri ) ) ) );
	}
}

