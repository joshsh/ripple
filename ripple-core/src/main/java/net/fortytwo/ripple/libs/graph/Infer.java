/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RdfPredicateMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RdfValue;
import net.fortytwo.ripple.flow.Sink;
import org.openrdf.model.Resource;

/**
 * A primitive which follows inferred forward triples from a resource.
 */
public class Infer extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            // FIXME: this is a kludge for programs created by Ripple 0.5-dev.  Remove this alias when it is no longer needed
            GraphLibrary.NS_2007_08 + "infer",

            GraphLibrary.NS_2008_06 + "infer"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Infer()
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

		RdfValue pred = stack.getFirst().toRDF( mc );
		stack = stack.getRest();

        // FIXME: bit of a hack
        if ( !( pred.sesameValue() instanceof Resource ) )
        {
            return;
        }

        solutions.put( arg.with(
				stack.push( new Operator( new RdfPredicateMapping( pred, true ) ) ) ) );
	}
}

