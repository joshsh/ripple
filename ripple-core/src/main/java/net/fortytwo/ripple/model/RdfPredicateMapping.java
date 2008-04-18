/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.Ripple;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

/**
 * Author: josh
 * Date: Feb 7, 2008
 * Time: 10:29:26 AM
 */
public class RdfPredicateMapping implements StackMapping
{
    private static final int ARITY = 1;

    private RippleValue predicate;
    private RdfValue context;
	private boolean includeInferred;

    // Note: only the types SP_O and OP_S are supported for now
    private GetStatementsQuery.Type type = GetStatementsQuery.Type.SP_O;

    public RdfPredicateMapping( final RippleValue pred, final boolean includeInferred )
	{
		this.predicate = pred;
		this.includeInferred = includeInferred;
		this.context = null;
	}

    public void setContext( final RdfValue context )
    {
        this.context = context;
    }

    public int arity()
	{
		return ARITY;
	}

	public boolean isTransparent()
	{
		return true;
	}

    public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> sink ) throws RippleException
	{
        final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();
		RippleValue sourceVal = stack.getFirst();

        Resource subj;
        URI pred;
        Value obj;
        Resource ctx;
        try {
            Value src = sourceVal.toRdf( mc ).getRdfValue();
            pred = (URI) predicate.toRdf( mc ).getRdfValue();
            ctx = ( null == context ) ? null : (Resource) context.toRdf( mc ).getRdfValue();
            switch ( type )
            {
                case SP_O:
                    subj = (Resource) src;
                    obj = null;
                    break;
                case PO_S:
                    subj = null;
                    obj = src;
                    break;
                default:
                    throw new RippleException( "unsupported query type: " + type );
            }
        } catch ( ClassCastException e ) {
            throw new RippleException( e );
        }

        GetStatementsQuery query = new GetStatementsQuery();
        query.type = type;
        query.subject = subj;
        query.predicate = pred;
        query.object = obj;
        if ( null != ctx )
        {
            query.contexts = new Resource[1];
            query.contexts[0] = ctx;
        }

        Sink<RippleValue, RippleException> resultSink = new ValueSink( arg, sink );

		if ( Ripple.asynchronousQueries() )
		{
            mc.queryAsynch( query, resultSink );
            //mc.multiplyAsynch( sourceVal, predicate, resultSink, includeInferred );
		}

		else
		{
            mc.query( query, resultSink );
            //mc.multiply( sourceVal, predicate, resultSink, includeInferred );
		}
	}

	public String toString()
	{
		return "Predicate(" + predicate + ")";
	}

    public StackMapping inverse() throws RippleException
    {
//System.out.println("inverting RDF predicate mapping with predicate " + predicate + " and context " + context);
        RdfPredicateMapping inv = new RdfPredicateMapping( this.predicate, this.includeInferred );
        inv.context = this.context;
        switch ( this.type )
        {
            case SP_O:
                inv.type = GetStatementsQuery.Type.PO_S;
                break;
            case PO_S:
                inv.type = GetStatementsQuery.Type.SP_O;
                break;
            default:
                throw new RippleException( "unsupported query type: " + type );
        }

        return inv;
    }

    private class ValueSink implements Sink<RippleValue, RippleException>
	{
		private Sink<StackContext, RippleException> sink;
		private StackContext arg;

		public ValueSink( final StackContext arg, final Sink<StackContext, RippleException> sink )
		{
			this.arg = arg;
			this.sink = sink;
		}

		public void put( final RippleValue v ) throws RippleException
		{
            // Note: relies on this mapping's arity being equal to 1
            sink.put( arg.with( arg.getStack().getRest().push( v ) ) );
		}
	}
}
