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
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RdfPredicateMapping;
import net.fortytwo.ripple.model.RdfValue;
import net.fortytwo.ripple.model.StackMappingWrapper;

// kate: tab-width 4

/**
 * A primitive which follows consumes an object and predicate, producing
 * all subjects such that there is a backlink from the object to the subject
 * via the predicate.  Note: the backward traversal of links is much more
 * dependent on the history of query evaluation than forward traversal, which is
 * built into Ripple's query model.
 */
public class InContext extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

	public InContext() throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> sink
	)
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();
        RdfValue context = stack.getFirst().toRdf( mc );
        stack = stack.getRest();
        RippleValue pred = stack.getFirst();
        stack = stack.getRest();

        boolean includeInferred = false;
        RdfPredicateMapping map = new RdfPredicateMapping( pred, includeInferred );
        map.setContext( context );
//System.out.println("created RDF predicate mapping with predicate " + pred + " and context " + context);

        RippleValue result = new StackMappingWrapper( map, mc );
        
        sink.put( arg.with( stack.push( result ) ) );
	}
}
