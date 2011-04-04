/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.util.ModelConnectionHelper;

/**
 * A primitive which produces a new blank node.
 */
public class New extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2008_08 + "new",
            GraphLibrary.NS_2007_08 + "new",
            GraphLibrary.NS_2007_05 + "new"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public New()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {};
    }

    public String getComment()
    {
        return "n  -- where n is a new blank node";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		// Note: stack may be null (and this should not be a problem).
		RippleList result = stack.push(
			new ModelConnectionHelper(mc).createRandomURI() );
//System.out.println( "Creating a new node" );

		solutions.put( arg.with( result ) );
	}
}

