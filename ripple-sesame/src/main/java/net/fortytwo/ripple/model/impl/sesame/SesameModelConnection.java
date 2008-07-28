/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import info.aduna.iteration.CloseableIteration;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Comparator;
import java.math.BigDecimal;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Buffer;
import net.fortytwo.ripple.flow.NullSink;
import net.fortytwo.ripple.control.Task;
import net.fortytwo.ripple.control.TaskSet;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.GetStatementsQuery;
import net.fortytwo.ripple.model.StatementPatternQuery;
import net.fortytwo.ripple.model.RippleValueComparator;
import net.fortytwo.ripple.rdf.BNodeClosureFilter;
import net.fortytwo.ripple.rdf.RDFSource;
import net.fortytwo.ripple.rdf.RDFUtils;
import net.fortytwo.ripple.rdf.SesameOutputAdapter;
import net.fortytwo.ripple.rdf.CloseableIterationSource;
import net.fortytwo.ripple.rdf.diff.RDFDiffSink;
import net.fortytwo.linkeddata.sail.SailConnectionListenerAdapter;
import net.fortytwo.ripple.flow.NullSource;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.flow.Source;
import net.fortytwo.ripple.flow.UniqueFilter;
import net.fortytwo.ripple.flow.Collector;

import org.apache.log4j.Logger;
import org.openrdf.model.Literal;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailConnectionListener;
import org.openrdf.sail.SailException;
import org.openrdf.rio.RDFFormat;

import javax.xml.datatype.XMLGregorianCalendar;

public class SesameModelConnection implements ModelConnection
{
	private static final Logger LOGGER
		= Logger.getLogger( ModelConnection.class );

    protected final SesameModel model;
	protected SailConnection sailConnection;
	protected final RDFDiffSink listenerSink;
	protected final ValueFactory valueFactory;
	protected final String name;
    private final TaskSet taskSet = new TaskSet();
    private final Comparator<RippleValue> comparator;

    private boolean useBlankNodes;
	
	// TODO: For now, this is just a convenience which allows graph:assert and
	//       graph:deny to manipulate the triple store without committing after
	//       every operation.
	private boolean uncommittedChanges = false;
	
	////////////////////////////////////////////////////////////////////////////
	
	protected SesameModelConnection( final SesameModel model, final String name, final RDFDiffSink listenerSink )
		throws RippleException
	{
		this.model = model;
		this.name = name;
		this.listenerSink = listenerSink;
	
		try
		{
			valueFactory = model.sail.getValueFactory();
		}
	
		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
	
		openSailConnection();
	
		synchronized ( model.openConnections )
		{
			model.openConnections.add( this );
		}

        this.useBlankNodes = Ripple.getProperties().getBoolean(Ripple.USE_BLANK_NODES);
        
        comparator = new RippleValueComparator( this );
    }
	
	public String getName()
	{
		return name;
	}
	
	public Model getModel()
	{
		return model;
	}
	
	ValueFactory getValueFactory()
	{
		return valueFactory;
	}
	
	public void toList( final RippleValue v,
                        final Sink<RippleList, RippleException> sink ) throws RippleException
	{
		SesameList.from( v, sink, this );
	}

    public RippleList list()
    {
        return SesameList.nilList();
    }

    public RippleList list( RippleValue v )
	{
		return new SesameList( v );
	}
	
	public RippleList list( RippleValue v, RippleList rest )
	{
		return new SesameList( v, rest );
	}

	public RippleList concat( final RippleList head, final RippleList tail )
	{
		return SesameList.concat( head, tail );
	}
	
	public void close() throws RippleException
	{
//System.out.println("closing...");
		// Complete any still-executing tasks.
		taskSet.waitUntilEmpty();
//System.out.println("    empty.");
		if ( uncommittedChanges ) {
			commit();
		}
//System.out.println("    committed");

		closeSailConnection( false );
//System.out.println("    closed sail connection");
		
		synchronized ( model.openConnections )
		{
			model.openConnections.remove( this );
		}
	}
	
	/**
	*  Returns the ModelConnection to a normal state after an Exception has
	*  been thrown.
	*/
	public void reset( final boolean rollback ) throws RippleException
	{
		closeSailConnection( rollback );
		uncommittedChanges = false;
		openSailConnection();
	}
	
	public void commit() throws RippleException
	{
//System.out.println("committing...");
		try
		{
			sailConnection.commit();
			uncommittedChanges = false;
		}
	
		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
//System.out.println("    done.");
	}
	
	private synchronized void openSailConnection()
		throws RippleException
	{
		try
		{
			sailConnection = model.sail.getConnection();
		
// FIXME: this doesn't give the LexiconUpdater any information about namespaces
			if ( null != listenerSink )
			{
				SailConnectionListener listener
					= new SailConnectionListenerAdapter( listenerSink );
				
				sailConnection.addConnectionListener(listener);
			}
		}
	
		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
	}
	
	private synchronized void closeSailConnection( final boolean rollback )
		throws RippleException
	{
		try
		{
			if ( sailConnection.isOpen() )
			{
				if ( rollback )
				{
					sailConnection.rollback();
				}
	
				sailConnection.close();
			}
	
			else
			{
				// Don't throw an exception: we could easily end up in a loop.
				LOGGER.error( "tried to close an already-closed connection" );
			}
		}
	
		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
	}
	
	public synchronized SailConnection getSailConnection()
	{
	return sailConnection;
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	private Resource castToResource( final Value v ) throws RippleException
	{
		if ( v instanceof Resource )
		{
			return (Resource) v;
		}
	
		else
		{
			throw new RippleException( "value " + v + " is not a Resource" );
		}
	}
	
	private Literal castToLiteral( final Value v ) throws RippleException
	{
		if ( v instanceof Literal )
		{
			return (Literal) v;
		}
	
		else
		{
			throw new RippleException( "value " + v + " is not a Literal" );
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	
    // Note: everything apart from xsd:true is considered false.
    // Eventually, this method may throw a type mismatch exception if it is
    // given a value other than "true"^^xsd:boolean or "false"^^xsd:boolean.
    public boolean toBoolean( final RippleValue rv ) throws RippleException
	{
		Literal l = castToLiteral( rv.toRDF( this ).sesameValue() );

	    //TODO: is capitalization relevant? Can 'true' also be represented as '1'?
        URI datatype = l.getDatatype();
        return null != datatype
                && XMLSchema.BOOLEAN.equals( datatype )
                && l.getLabel().equals( "true" );
	}
	
	public NumericValue toNumericValue( final RippleValue rv )
		throws RippleException
	{
		if ( rv instanceof NumericValue )
		{
			return (NumericValue) rv;
		}
	
		else
		{
			return new SesameNumericValue( rv.toRDF( this ) );
		}
	}

    public Date toDateValue( RippleValue v ) throws RippleException
    {
        Literal l = castToLiteral( v.toRDF( this ).sesameValue() );
        
        XMLGregorianCalendar c = l.calendarValue();
        return c.toGregorianCalendar().getTime();
    }

    // TODO: this method is incomplete
	public String toString( final RippleValue v ) throws RippleException
	{
		if ( v instanceof RDFValue)
		{
			Value r = ( (RDFValue) v ).sesameValue();

			if ( r instanceof Literal )
			{
				return ( (Literal) r ).getLabel();
			}

			else
			{
				return r.toString();
			}
		}

		else
		{
			return v.toString();
		}
	}
	
	public URI toUri( final RippleValue rv )
		throws RippleException
	{
		Value v = rv.toRDF( this ).sesameValue();
	
		if ( v instanceof URI )
		{
			return (URI) v;
		}
	
		else
		{
			throw new RippleException( "value " + v.toString() + " is not a URI" );
		}
	}
	
	/**
	*  A <code>Sink</code> which remembers how many times it has received a
	*  value, as well as the last value received.
	*/
	private class SingleValueSink implements Sink<RDFValue, RippleException>
	{
		private RDFValue value = null;
		private int valuesReceived = 0;
	
		public void put( final RDFValue v ) throws RippleException
		{
			value = v;
			valuesReceived++;
		}
	
		public RDFValue getValue()
		{
			return value;
		}
	
		public int countReceived()
		{
			return valuesReceived;
		}
	}
	
	public RDFValue findSingleObject( final RippleValue subj, final RippleValue pred )
		throws RippleException
	{
		RDFValue subjRdf = subj.toRDF( this );
		RDFValue predRdf = pred.toRDF( this );
		
		SingleValueSink sink = new SingleValueSink();
	
		multiplyRDFValues( subjRdf, predRdf, sink );
	
		return sink.getValue();
	}
	
	public RDFValue findAtLeastOneObject( final RippleValue subj, final RippleValue pred )
		throws RippleException
	{
		RDFValue subjRdf = subj.toRDF( this );
		RDFValue predRdf = pred.toRDF( this );
		
		SingleValueSink sink = new SingleValueSink();
	
		multiplyRDFValues( subjRdf, predRdf, sink );
	
		if ( 0 == sink.countReceived() )
		{
			throw new RippleException( "no values resolved for " + pred.toString() + " of " + subj.toString() );
		}
	
		else
		{
			return sink.getValue();
		}
	}
	
	public RDFValue findAtMostOneObject( final RippleValue subj, final RippleValue pred )
		throws RippleException
	{
		RDFValue subjRdf = subj.toRDF( this );
		RDFValue predRdf = pred.toRDF( this );
		
		SingleValueSink sink = new SingleValueSink();
	
		multiplyRDFValues( subjRdf, predRdf, sink );
	
		int count = sink.countReceived();
	
		if ( 1 < count )
		{
			throw new RippleException( pred.toString() + " of " + subj.toString() + " resolved to more than one value" );
		}
	
		else
		{
			return sink.getValue();
		}
	}
	
	public RDFValue findUniqueProduct( final RippleValue subj, final RippleValue pred )
		throws RippleException
	{
		RDFValue subjRdf = subj.toRDF( this );
		RDFValue predRdf = pred.toRDF( this );
		
		RDFValue v = findAtMostOneObject( subjRdf, predRdf );
	
		if ( null == v )
		{
			throw new RippleException( "no values resolved for " + pred.toString() + " of " + subj.toString() );
		}
	
		else
		{
			return v;
		}
	}
	
	//TODO: context handling
	public void copyStatements( final RippleValue src, final RippleValue dest )
		throws RippleException
	{
		final Resource destResource = castToResource( dest.toRDF( this ).sesameValue() );
	
		Sink<Statement, RippleException> stSink = new Sink<Statement, RippleException>()
		{
			public void put( final Statement st ) throws RippleException
			{
				Resource context = st.getContext();
	
				try
				{
					if ( null == context )
					{
						sailConnection.addStatement(
							destResource, st.getPredicate(), st.getObject() );
					}
	
					else
					{
						sailConnection.addStatement(
							destResource, st.getPredicate(), st.getObject(), context );
					}
				}
		
				catch ( Throwable t )
				{
					reset( true );
					throw new RippleException( t );
				}
			}
		};
	
		getStatements( src.toRDF( this ), null, null, stSink, false );
	}
	
	public void removeStatementsAbout( final URI subj )
		throws RippleException
	{
		try
		{
			sailConnection.removeStatements( subj, null, null );
		}
	
		catch ( Throwable t )
		{
			reset( true );
			throw new RippleException( t );
		}
	}
	
	public void putContainerMembers( final RippleValue head, final Sink<RippleValue, RippleException> sink )
		throws RippleException
	{
		Sink<Statement, RippleException> stSink = new Sink<Statement, RippleException>()
		{
			public void put( final Statement st ) throws RippleException
			{
				if ( '_' == st.getPredicate().getLocalName().charAt( 0 ) )
				{
					sink.put( new RDFValue( st.getObject() ) );
				}
			}
		};
	
		getStatements( head.toRDF( this ), null, null, stSink, false );
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	public void forget( final RippleValue v ) throws RippleException
	{
/*
		// FIXME: messy
		model.sail.getDereferencer().forget( v.toRdf( this ), this );
*/
	}
	
	////////////////////////////////////////////////////////////////////////////

	public void findPredicates( final RippleValue subject,
								final Sink<RippleValue, RippleException> sink )
		throws RippleException
	{
		final Sink<Value, RippleException> valueSink = new Sink<Value, RippleException>()
		{
			public void put( final Value v ) throws RippleException
			{
				sink.put( value( v ) );
			}
		};
		
		Sink<Statement, RippleException> predSelector = new Sink<Statement, RippleException>()
		{
			Sink<Value, RippleException> predSink = new UniqueFilter<Value, RippleException>( valueSink );
	
			public void put( final Statement st ) throws RippleException
			{
	//TODO: don't create a new RdfValue before checking for uniqueness
				predSink.put( st.getPredicate() );
			}
		};
	
		getStatements( subject.toRDF( this ), null, null, predSelector, false );
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	//FIXME: Statements should be absent from the ModelConnection API
	public void add( final Statement st, final Resource... contexts )
		throws RippleException
	{
		try
		{
			sailConnection.addStatement( st.getSubject(), st.getPredicate(), st.getObject(), contexts );
			uncommittedChanges = true;
		}
	
		catch ( Throwable t )
		{
			reset( true );
			throw new RippleException( t );
		}
	}
	
	public void add( final RippleValue subj, final RippleValue pred, final RippleValue obj, RippleValue... contexts )
		throws RippleException
	{
		Value subjValue = subj.toRDF( this ).sesameValue();
		Value predValue = pred.toRDF( this ).sesameValue();
		Value objValue = obj.toRDF( this ).sesameValue();

        if ( !( subjValue instanceof Resource )
				|| !( predValue instanceof URI ) )
		{
			return;
		}

        try
        {
            // Trick to be able to iterate through contexts even if none are given
            if ( 0 == contexts.length )
            {
                sailConnection.addStatement(
                        (Resource) subjValue, (URI) predValue, objValue );
            }

            else
            {
                for ( RippleValue context : contexts )
                {
                    Value contextValue;

                    if ( null == context )
                    {
                        contextValue = null;
                    }

                    else
                    {
                        contextValue = context.toRDF( this ).sesameValue();

                        // rdf:nil is a special case -- as a graph name in Ripple, it
                        // actually represents the null graph.
                        if ( contextValue.equals( RDF.NIL ) )
                        {
                            contextValue = null;
                        }

                        else if ( !( contextValue instanceof Resource ) )
                        {
                            return;
                        }
                    }

                    sailConnection.addStatement(
                            (Resource) subjValue, (URI) predValue, objValue, (Resource) contextValue );
                }
            }
        }

        catch ( SailException e )
        {
            reset( true );
            throw new RippleException( e );
        }

        uncommittedChanges = true;
    }
	
	public void remove( final RippleValue subj, final RippleValue pred, final RippleValue obj, final RippleValue... contexts )
		throws RippleException
	{
        Value subjValue, predValue, objValue;

        if ( null == subj )
        {
            subjValue = null;
        }

        else
        {
            subjValue = subj.toRDF( this ).sesameValue();
            if ( !( subjValue instanceof Resource ) )
            {
                return;
            }
        }

        if ( null == pred )
        {
            predValue = null;
        }

        else
        {
            predValue = pred.toRDF( this ).sesameValue();
            if ( !( predValue instanceof URI ) )
            {
                return;
            }
        }

        if ( null == obj )
        {
            objValue = null;
        }

        else
        {
            objValue = obj.toRDF( this ).sesameValue();
        }

        try
        {
            // Trick to be able to iterate through contexts even if none are given
            if ( 0 == contexts.length )
            {
                sailConnection.removeStatements(
				    (Resource) subjValue, (URI) predValue, objValue );
            }

            else
            {
                for ( RippleValue context : contexts )
                {
                    Value contextValue;

                    if ( null == context )
                    {
                        contextValue = null;
                    }

                    else
                    {
                        contextValue = context.toRDF( this ).sesameValue();

                        // rdf:nil is a special case -- as a graph name in Ripple, it
                        // actually represents the null graph.
                        if ( contextValue.equals( RDF.NIL ) )
                        {
                            contextValue = null;
                        }

                        else if ( !( contextValue instanceof Resource ) )
                        {
                            return;
                        }
                    }

                    sailConnection.removeStatements(
                            (Resource) subjValue, (URI) predValue, objValue, (Resource) contextValue );
                }
            }
        }

        catch ( SailException e )
        {
            reset( true );
            throw new RippleException( e );
        }

        uncommittedChanges = true;
	}
	
	public void removeStatementsAbout( final RDFValue subj, final URI context )
		throws RippleException
	{
		Value subjValue = subj.toRDF( this ).sesameValue();
	
		if ( !( subjValue instanceof Resource ) )
		{
			return;
		}
	
		try
		{
			if ( null == context )
			{
				sailConnection.removeStatements( (Resource) subjValue, null, null );
			}
	
			else
			{
				sailConnection.removeStatements( (Resource) subjValue, null, null, context );
			}

			uncommittedChanges = true;
		}
	
		catch ( SailException e )
		{
			reset( true );
			throw new RippleException( e );
		}
	}
	
	//FIXME: Resources should be absent from the ModelConnection API
	public long countStatements( final Resource... contexts )
			throws RippleException
	{
		int count = 0;
	
		try
		{
			//synchronized ( model )
			{
				CloseableIteration<? extends Statement, SailException> stmtIter
					= sailConnection.getStatements(
						null, null, null, Ripple.useInference(), contexts );
	
				while ( stmtIter.hasNext() )
				{
					stmtIter.next();
					count++;
				}
	
				stmtIter.close();
			}
		}
	
		catch ( Throwable t )
		{
			reset( true );
			throw new RippleException( t );
		}
	
		return count;
	}
	
	////////////////////////////////////////////////////////////////////////////

    private RDFFormat getExportFormat() throws RippleException
    {
        String formatStr = Ripple.getProperties().getString( Ripple.EXPORT_FORMAT );
        RDFFormat format = RDFUtils.findFormat( formatStr );
        
        if ( null == format )
        {
            throw new RippleException( "unknown RDF format: " + formatStr );
        }

        return format;
    }

    public void exportNamespace( final String ns, final OutputStream os )
		throws RippleException
	{
		SesameOutputAdapter adapter = RDFUtils.createOutputAdapter(
                os, getExportFormat() );
	
		final Sink<Resource, RippleException> bnodeClosure = new BNodeClosureFilter(
			adapter.statementSink(), getSailConnection() );
	
		// Hackishly find all terms in the given namespace which are the subject
		// of statements.
		Sink<Statement, RippleException> sink = new Sink<Statement, RippleException>()
		{
			public void put( final Statement st ) throws RippleException
			{
				Resource subj = st.getSubject();
				if ( subj instanceof URI
						&& subj.toString().startsWith( ns ) )
				{
					bnodeClosure.put( subj );
				}
			}
		};
	
		Buffer<Statement, RippleException> buffer = new Buffer<Statement, RippleException>( sink );
		getStatements( null, null, null, buffer, false );
	
		adapter.startRDF();
		buffer.flush();
		adapter.endRDF();
	}
	
	////////////////////////////////////////////////////////////////////////////

	public URI createURI( final String s ) throws RippleException
	{
		try
		{
			return valueFactory.createURI( s );
		}
	
		catch ( Throwable t )
		{
			reset( true );
			throw new RippleException( t );
		}
	}
	
	public URI createURI( final String ns, final String s )
		throws RippleException
	{
		try
		{
			return valueFactory.createURI( ns + s );
		}
	
		catch ( Throwable t )
		{
			reset( true );
			throw new RippleException( t );
		}
	}
	
	public URI createURI( final URI ns, final String s )
		throws RippleException
	{
		try
		{
			return valueFactory.createURI( ns.toString() + s );
		}
	
		catch ( Throwable t )
		{
			reset( true );
			throw new RippleException( t );
		}
	}

	public Resource createBNode() throws RippleException
	{
		if ( useBlankNodes )
		{
			try
			{
				return valueFactory.createBNode();
			}
		
			catch ( Throwable t )
			{
				reset( true );
				throw new RippleException( t );
			}
		}
		
		else
		{
			return RDFUtils.createBNodeUri( valueFactory );
		}
	}
	
	public Resource createBNode( final String id ) throws RippleException
	{
		if ( useBlankNodes )
		{
			try
			{
				return valueFactory.createBNode( id );
			}
		
			catch ( Throwable t )
			{
				reset( true );
				throw new RippleException( t );
			}
		}
		
		else
		{
			return RDFUtils.createBNodeUri( id, valueFactory );
		}
	}
	
	/*
	private static int randomInt( final int lo, final int hi )
	{
		int n = hi - lo + 1;
		int i = rand.nextInt() % n;
	
		if (i < 0)
		{
			i = -i;
		}
	
		return lo + i;
	}
	
	URI createRandomUri() throws RippleException
	{
		return createUri( "urn:random:" + randomInt( 0, Integer.MAX_VALUE ) );
	}
	*/
	
	////////////////////////////////////////////////////////////////////////////
	
	public Statement createStatement( final Resource subj, final URI pred, final Value obj )
		throws RippleException
	{
		try
		{
			return valueFactory.createStatement( subj, pred, obj );
		}
	
		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
	}
	
	////////////////////////////////////////////////////////////////////////////

    public RDFValue createTypedLiteral( final String value, final RippleValue type ) throws RippleException
	{
		Value v = type.toRDF( this ).sesameValue();
	
		if ( !( v instanceof URI ) )
		{
			throw new RippleException( "literal type is not a URI" );
		}
	
		else
		{
			return value( value, (URI) v );
		}
	}
	
	////////////////////////////////////////////////////////////////////////////

    public Comparator<RippleValue> getComparator()
    {
        return comparator;
    }

    ////////////////////////////////////////////////////////////////////////////

    public RippleValue value( final Value v )
	{
		return model.specialValues.get( v );
	}
	
	public RDFValue value( final String s ) throws RippleException
	{
		try
		{
			return new RDFValue(
                    valueFactory.createLiteral( s ) );
//                    valueFactory.createLiteral( s, XMLSchema.STRING ) );
		}
	
		catch ( Throwable t )
		{
			reset( true );
			throw new RippleException( t );
		}
	}
	
	public RDFValue value( final String s, final String language )
		throws RippleException
	{
		try
		{
			return new RDFValue(
				valueFactory.createLiteral( s, language ) );
		}
	
		catch ( Throwable t )
		{
			reset( true );
			throw new RippleException( t );
		}
	}
	
	public RDFValue value( final String s, final URI dataType )
		throws RippleException
	{
		try
		{
			return new RDFValue(
				valueFactory.createLiteral( s, dataType ) );
		}
	
		catch ( Throwable t )
		{
			reset( true );
			throw new RippleException( t );
		}
	}
	
	public RDFValue value( final boolean b )
		throws RippleException
	{
		try
		{
			return new RDFValue(
				valueFactory.createLiteral( "" + b, XMLSchema.BOOLEAN ) );
		}
	
		catch ( Throwable t )
		{
			reset( true );
			throw new RippleException( t );
		}
	}
	
	public NumericValue value( final int i ) throws RippleException
	{
		return new SesameNumericValue( i );
	}

    public NumericValue value( final long l ) throws RippleException
	{
		return new SesameNumericValue( l );
	}
	
	public NumericValue value( final double d ) throws RippleException
	{
		return new SesameNumericValue( d );
	}

    public NumericValue value( final BigDecimal b ) throws RippleException
    {
        return new SesameNumericValue( b );
    }

    ////////////////////////////////////////////////////////////////////////////
	
	public void setNamespace( final String prefix, final String ns, final boolean override )
		throws RippleException
	{
	//LOGGER.info( "### setting namespace: '" + prefix + "' to " + ns );
		try
		{
			//synchronized ( model )
			{
//System.out.println("--- z");
				if ( override || null == sailConnection.getNamespace( prefix ) )
				{
//System.out.println("--- x");
					if ( null == ns )
					{
						sailConnection.removeNamespace( prefix );
					}
					
					else
					{
//System.out.println("--- c");
						sailConnection.setNamespace( prefix, ns );
					}

					uncommittedChanges = true;
//System.out.println("--- v");
				}
			}
		}
	
		catch ( Throwable t )
		{
			reset( true );
			throw new RippleException( t );
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	// e.g. CONSTRUCT * FROM {x} p {y}
	//FIXME: Statements should be absent from the ModelConnection API
	public Collection<Statement> serqlQuery( final String queryStr )
		throws RippleException
	{
		Collection<Statement> statements = new ArrayList<Statement>();
	/* TODO
		try
		{
			synchronized ( sailConnection )
			{
				GraphQueryResult result = sailConnection.prepareGraphQuery(
					QueryLanguage.SERQL, queryStr ).evaluate();
	
				while ( result.hasNext() )
				{
					statements.add( result.next() );
				}
	
				result.close();
			}
		}
	
		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
	*/
		return statements;
	}
	
	////////////////////////////////////////////////////////////////////////////

    private class QueryTask extends Task
	{
		private StatementPatternQuery query;
        private Sink<RippleValue, RippleException> sink;

        public QueryTask( final StatementPatternQuery query, final Sink<RippleValue, RippleException> sink )
		{
			this.query = query;
            this.sink = sink;
        }

		public void executeProtected() throws RippleException
		{
			query( query, sink );
		}

		protected void stopProtected()
		{
			synchronized ( sink )
			{
				sink = new NullSink<RippleValue, RippleException>();
			}
		}
	}

    public void query( final StatementPatternQuery query, final Sink<RippleValue, RippleException> sink) throws RippleException
    {
        GetStatementsQuery sesameQuery;

        try {
            sesameQuery = new GetStatementsQuery( query, this );
        } catch ( GetStatementsQuery.InvalidQueryException e ) {
            LOGGER.debug( "invalid query: " + e.getMessage() );
            return;
        }

        Sink<Value, RippleException> valueSink = new Sink<Value, RippleException>()
		{
			public void put( final Value val ) throws RippleException
			{
				sink.put( value( val ) );
			}
		};
	
        try {
            sesameQuery.getValues( sailConnection, valueSink );
        } catch ( RippleException e ) {
            reset( true );
            throw e;
        }
        //getStatements( query.subject, query.predicate, query.object, stSink, query.includeInferred );
    }

    public void queryAsynch( final StatementPatternQuery query, final Sink<RippleValue, RippleException> sink ) throws RippleException
    {
		QueryTask task = new QueryTask( query, sink );
		taskSet.add( task );
    }

    public Source<Namespace, RippleException> getNamespaces() throws RippleException
	{
        Collector<Namespace, RippleException> results = new Collector<Namespace, RippleException>();
        Source<Namespace, RippleException> source;

        try
        {
            source = new CloseableIterationSource<Namespace, SailException>(
                    (CloseableIteration<Namespace, SailException>) sailConnection.getNamespaces() );
        }

        catch (SailException e)
        {
            throw new RippleException( e );
        }

        source.writeTo( results );
        return results;
	}
	
	//FIXME: Statements should be absent from the ModelConnection API
	public void getStatements( final RDFValue subj,
								final RDFValue pred,
								final RDFValue obj,
								final Sink<Statement, RippleException> sink,
								final boolean includeInferred)
		throws RippleException
	{
//System.out.println("getStatements(" + subj + ", " + pred + ", " + obj + ")");
        Value rdfSubj = ( null == subj ) ? null : subj.sesameValue();
		Value rdfPred = ( null == pred ) ? null : pred.sesameValue();
		Value rdfObj = ( null == obj ) ? null : obj.sesameValue();
	
		if ( ( null == rdfSubj || rdfSubj instanceof Resource )
				&& ( null == rdfPred || rdfPred instanceof URI ) )
		{
			// Note: we must collect results in a buffer before putting anything
			//       into the sink, as inefficient as that is, because otherwise
			//       we might end up opening another RepositoryResult before
			//       the one below closes, which currently causes Sesame to
			//       deadlock.  Even using a separate RepositoryConnection for
			//       each RepositoryResult doesn't seem to help.
			Buffer<Statement, RippleException> buffer = new Buffer<Statement, RippleException>( sink );
			CloseableIteration<? extends Statement, SailException> stmtIter = null;
	
	//TODO: use CloseableIterationSource
			
			// Perform the query and collect results.
			try
			{
				//synchronized ( model )
				{
					stmtIter = sailConnection.getStatements(
						(Resource) rdfSubj, (URI) rdfPred, rdfObj, includeInferred );
	//stmtIter.enableDuplicateFilter();
	
					while ( stmtIter.hasNext() )
					{
                        Statement st = stmtIter.next();
//System.out.println("    result: " + st);
                        buffer.put( st );
					}
	
					stmtIter.close();
				}
			}
	
			catch ( Throwable t )
			{
				try
				{
					if ( null != stmtIter )
					{
						stmtIter.close();
					}
				}
	
				catch ( Throwable t2 )
				{
					t2.printStackTrace( System.err );
					System.exit( 1 );
				}
	
				reset( true );
				throw new RippleException( t );
			}
	
			buffer.flush();
		}
	}
	
	public RDFSource getSource()
	{
		return new RDFSource()
		{
			private Source<Statement, RippleException> stSource = new Source<Statement, RippleException>()
			{
				public void writeTo( final Sink<Statement, RippleException> sink )
					throws RippleException
				{
					getStatements( null, null, null, sink, false );
				}
			};

			private Source<String, RippleException> comSource = new NullSource<String, RippleException>();
	
			public Source<Statement, RippleException> statementSource() throws RippleException {
				return stSource;
			}
	
			public Source<Namespace, RippleException> namespaceSource() throws RippleException {
				return getNamespaces();
			}
	
			public Source<String, RippleException> commentSource() throws RippleException {
				return comSource;
			}
		};
	}

	private void multiplyRDFValues( final RDFValue subj, final RDFValue pred, final Sink<RDFValue, RippleException> sink )
		throws RippleException
	{
		Sink<Statement, RippleException> stSink = new Sink<Statement, RippleException>()
		{
			public void put( final Statement st ) throws RippleException
			{
				sink.put( new RDFValue( st.getObject() ) );
			}
		};
	
		getStatements( subj, pred, null, stSink, false );
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	//TODO: Namespaces should not be part of the ModelConnection API
	public void putNamespaces( final Sink<Namespace, RippleException> sink )
		throws RippleException
	{
		try
		{
			CloseableIteration<? extends Namespace, SailException> iter
				= sailConnection.getNamespaces();
	
			while ( iter.hasNext() )
			{
				sink.put( iter.next() );
			}
		}
	
		catch ( SailException e )
		{
			throw new RippleException( e );
		}
	}
	
	public void putContexts( final Sink<RippleValue, RippleException> sink )
		throws RippleException
	{
		try
		{
			CloseableIteration<? extends Resource, SailException> iter
				= sailConnection.getContextIDs();
	
			while ( iter.hasNext() )
			{
				sink.put( new RDFValue( iter.next() ) );
			}
			
			iter.close();
		}
	
		catch ( SailException e )
		{
			throw new RippleException( e );
		}
	}
}
