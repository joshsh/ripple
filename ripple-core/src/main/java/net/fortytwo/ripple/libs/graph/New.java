/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RdfValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which produces a new blank node.
 */
public class New extends PrimitiveStackMapping
{
	private static final int ARITY = 0;

    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2008_06 + "new",
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

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		// Note: stack may be null (and this should not be a problem).
		RippleList result = stack.push(
			new RdfValue( mc.createBNode() ) );
//System.out.println( "Creating a new node" );

		solutions.put( arg.with( result ) );
	}
}

