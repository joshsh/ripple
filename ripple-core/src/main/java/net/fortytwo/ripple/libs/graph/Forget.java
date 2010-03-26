/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a resource and produces the resource again after
 * removing all statements in the context its description.  The next time a
 * description of the resource is needed, Ripple will request a new
 * representation.
 */
public class Forget extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2008_08 + "forget",
            GraphLibrary.NS_2007_08 + "forget",
            GraphLibrary.NS_2007_05 + "forget"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Forget()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", null, false )};
    }

    public String getComment()
    {
        return "x  =>  x  -- has the side-effect of revoking all statements about x";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		RippleValue v;

		v = stack.getFirst();

		mc.forget( v );
		
		// Note: the stack itself has not been altered.
		solutions.put( arg );
	}
}

