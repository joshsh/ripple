/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which follows inferred forward triples from a resource.
 */
public class Infer extends RDFPredicateStackMapping
{
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2008_08 + "infer"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Infer() throws RippleException
	{
		super( false );

        this.inverse = new Infer( this );
	}

    private Infer( final StackMapping original ) throws RippleException
    {
        super( true );

        this.inverse = original;
    }

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "s", null, true ),
                new Parameter( "p", null, true )};
    }

    public String getComment()
    {
        return "s p  =>  o (where o may be an inferred object)";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		RDFValue pred = stack.getFirst().toRDF( mc );
		stack = stack.getRest();

        StackMapping mapping = getMapping( pred, null, true );

        solutions.put( arg.with(
				stack.push( new Operator( mapping ) ) ) );
	}
}

