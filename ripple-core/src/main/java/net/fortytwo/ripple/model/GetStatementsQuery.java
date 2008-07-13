/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.flow.Sink;

import org.openrdf.model.Value;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Statement;
import org.openrdf.model.Literal;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import net.fortytwo.ripple.flow.Buffer;
import net.fortytwo.ripple.RippleException;
import info.aduna.iteration.CloseableIteration;

/**
 * Author: josh
 * Date: Feb 7, 2008
 * Time: 10:46:12 AM
 */
public class GetStatementsQuery
{
    public class InvalidQueryException extends RippleException
    {
        public InvalidQueryException( final String message )
        {
            super( message );
        }
    }

    public enum Type { SP_O, PO_S, SO_P };

    // TODO: make this into a configuration property, or find another solution
    private static final boolean STRING_LITERALS_EQUIVALENT_TO_PLAIN_LITERALS = true;

    // FIXME: this is temporary
    private static final ValueFactory VALUE_FACTORY = new ValueFactoryImpl();

    public final boolean includeInferred;

    public final Resource subject;
	public final URI predicate;
	public final Value object;
	public final Resource[] contexts;
    public Type type = Type.SP_O;

    public GetStatementsQuery( final StatementPatternQuery patternQuery,
                               final ModelConnection mc ) throws RippleException
    {
        try {
            switch ( patternQuery.getPattern() )
            {
                case SP_O:
                    type = Type.SP_O;
                    subject = getResource( patternQuery.getSubject(), mc );
                    predicate = getURI( patternQuery.getPredicate(), mc );
                    object = null;
                    break;
                case PO_S:
                    type = Type.PO_S;
                    subject = null;
                    predicate = getURI( patternQuery.getPredicate(), mc );
                    object = getValue( patternQuery.getObject(), mc );
                    break;
                case SO_P:
                    type = Type.SO_P;
                    subject = getResource( patternQuery.getSubject(), mc );
                    predicate = null;
                    object = getValue( patternQuery.getObject(), mc );
                    break;
                default:
                    throw new InvalidQueryException( "unsupported query pattern: " + patternQuery.getPattern() );
            }

            RippleValue[] rippleContexts = patternQuery.getContexts();
            this.contexts = new Resource[rippleContexts.length];

            for ( int i = 0; i < rippleContexts.length; i++ )
            {
                Resource context = getResource( rippleContexts[i], mc );
//System.out.println("context is: " + context);
                
                // rdf:nil is a special case -- as a graph name in Ripple, it
                // actually represents the null graph.
                if ( null != context && context.equals( RDF.NIL ) )
                {
//                    System.out.println("    context is null");
                    context = null;
                }

                this.contexts[i] = context;
            }
        }

        catch ( ClassCastException e )
        {
            throw new InvalidQueryException( "value could not be cast to the appropriate Sesame type" );
        }

        this.includeInferred = patternQuery.getIncludeInferred();
    }

    private URI getURI( final RippleValue rv, final ModelConnection mc ) throws RippleException, ClassCastException
    {
        return (URI) rv.toRDF( mc ).sesameValue();
    }

    private Resource getResource( final RippleValue rv, final ModelConnection mc ) throws RippleException, ClassCastException
    {
        return (Resource) rv.toRDF( mc ).sesameValue();
    }

    private Value getValue( final RippleValue rv, final ModelConnection mc ) throws RippleException
    {
        return rv.toRDF( mc ).sesameValue();
    }

    public void getStatements( final SailConnection sc, final Sink<Statement, RippleException> results ) throws RippleException
	{
        getStatementsPrivate( results, sc, subject, predicate, object );

        if ( STRING_LITERALS_EQUIVALENT_TO_PLAIN_LITERALS
                && null != object
                && object instanceof Literal)
        {
            URI datatype = ( (Literal) object ).getDatatype();
            if ( null == datatype )
            {
                Literal newObj = VALUE_FACTORY.createLiteral( ( (Literal) object ).getLabel(), XMLSchema.STRING );
                getStatementsPrivate( results, sc, subject, predicate, newObj );
            }

            else if ( XMLSchema.STRING == datatype )
            {
                Literal newObj = VALUE_FACTORY.createLiteral( ( (Literal) object ).getLabel() );
                getStatementsPrivate( results, sc, subject, predicate, newObj );
            }
        }
    }

    private void getStatementsPrivate( final Sink<Statement, RippleException> results,
                                              final SailConnection sc,
                                              final Resource subject,
                                              final URI predicate,
                                              final Value object ) throws RippleException
    {
		// Note: we must collect results in a buffer before putting anything
		//       into the sink, as inefficient as that is, because otherwise
		//       we might end up opening another RepositoryResult before
		//       the one below closes, which currently causes Sesame to
		//       deadlock.  Even using a separate RepositoryConnection for
		//       each RepositoryResult doesn't seem to help.
		Buffer<Statement, RippleException> buffer = new Buffer<Statement, RippleException>( results );
		CloseableIteration<? extends Statement, SailException> stmtIter;

//TODO: use CloseableIterationSource

		// Perform the query and collect results.
		try
		{
			stmtIter = sc.getStatements( subject, predicate, object, includeInferred, contexts );
//stmtIter.enableDuplicateFilter();
            try
            {
                while ( stmtIter.hasNext() )
                {
                    buffer.put( stmtIter.next() );
                }
            }

            finally
            {
                stmtIter.close();
            }
        }

		catch ( SailException e )
		{
			throw new RippleException( e );
		}

		buffer.flush();
    }

    public void getValues( final SailConnection sc, final Sink<Value, RippleException> results ) throws RippleException
	{
		Sink<Statement, RippleException> stSink = new Sink<Statement, RippleException>()
		{
			public void put( final Statement st ) throws RippleException
			{
                Value result;

                switch ( type )
                {
                    case SP_O:
                        result = st.getObject();
                        break;
                    case PO_S:
                        result = st.getSubject();
                        break;
                    case SO_P:
                        result = st.getPredicate();
                        break;
                    default:
                        throw new RippleException( "unhandled query type: " + type );
                }

                results.put( result );
			}
		};

        getStatements( sc, stSink );
    }
}
