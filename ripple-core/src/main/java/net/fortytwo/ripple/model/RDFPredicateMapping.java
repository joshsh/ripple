/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-core/src/main/java/net/fortytwo/ripple/model/RDFPredicateMapping.java $
 * $Revision: 60 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.Ripple;

/**
 * Author: josh
 * Date: Feb 7, 2008
 * Time: 10:29:26 AM
 */
public class RDFPredicateMapping implements StackMapping
{
    private static final int ARITY = 1;

    // Note: only the types SP_O and OP_S are supported for now
    private final StatementPatternQuery.Pattern type;

    private final RDFValue predicate;
    private final RDFValue context;
    private final boolean includeInferred;

    /**
     * @param pred must contain a Resource value
     * @param includeInferred
     */
    public RDFPredicateMapping( final StatementPatternQuery.Pattern type,
                                final RDFValue pred,
                                final RDFValue context,
                                final boolean includeInferred )
	{
        this.type = type;
        this.predicate = pred;
        this.context = context;
        this.includeInferred = includeInferred;
	}

    public int arity()
	{
		return ARITY;
	}

	public boolean isTransparent()
	{
		return true;
	}

    public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> sink ) throws RippleException
	{
        final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();
		RippleValue sourceVal = stack.getFirst();
        StatementPatternQuery query;

        switch ( this.type )
        {
            case SP_O:
                query = ( null == context )
                        ? new StatementPatternQuery( sourceVal, predicate, null, includeInferred )
                        : new StatementPatternQuery( sourceVal, predicate, null, includeInferred, context );
                break;
            case PO_S:
                query = ( null == context )
                        ? new StatementPatternQuery( null, predicate, sourceVal, includeInferred )
                        : new StatementPatternQuery( null, predicate, sourceVal, includeInferred, context );
                break;
            default:
                throw new RippleException( "unsupported query type: " + type );
        }

        Sink<RippleValue, RippleException> resultSink = new ValueSink( arg, sink );

        if ( Ripple.asynchronousQueries() )
		{
            mc.queryAsynch( query, resultSink );
		}

		else
		{
            mc.query( query, resultSink );
		}
    }

    public String toString()
	{
		return "Predicate(" + predicate + ")";
	}

    public StackMapping inverse() throws RippleException
    {
        StatementPatternQuery.Pattern inverseType;

//System.out.println("inverting RDF predicate mapping with predicate " + predicate + " and context " + context);
        switch ( this.type )
        {
            case SP_O:
                inverseType = StatementPatternQuery.Pattern.PO_S;
                break;
            case PO_S:
                inverseType = StatementPatternQuery.Pattern.SP_O;
                break;
            default:
                throw new RippleException( "unsupported query type: " + type );
        }

        return new RDFPredicateMapping( inverseType, this.predicate, this.context, this.includeInferred );
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
