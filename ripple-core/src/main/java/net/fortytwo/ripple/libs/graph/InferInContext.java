/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */

package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RDFPredicateMapping;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.StackMapping;

public class InferInContext extends RDFPredicateStackMapping
{
	private static final int ARITY = 3;

    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2007_08 + "inferInContext"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public InferInContext() throws RippleException
	{
		super( false );

        this.inverse = new InferInContext( this );
	}

    private InferInContext( final StackMapping original ) throws RippleException
    {
        super( true );

        this.inverse = original;
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

        RDFValue context = stack.getFirst().toRDF( mc );
        stack = stack.getRest();
        RDFValue pred = stack.getFirst().toRDF( mc );
        stack = stack.getRest();

        RDFPredicateMapping mapping = getMapping( pred, context, true );

        solutions.put( arg.with(
				stack.push( new Operator( mapping ) ) ) );
	}
}