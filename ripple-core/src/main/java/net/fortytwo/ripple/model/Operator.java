/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.flow.Sink;

import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.Resource;

public class Operator implements RippleValue
{
	public static final Operator OP = new Operator( new Op() );

	private static final RdfValue RDF_FIRST = new RdfValue( RDF.FIRST );
	private static final RdfValue RDF_NIL = new RdfValue( RDF.NIL );

	private StackMapping mapping;
	private RdfValue rdfEquivalent = null;

	private Operator( final RdfValue pred ) throws RippleException
	{
		mapping = new RdfPredicateMapping( pred, Ripple.useInference() );
	}

	public Operator( final StackMapping mapping )
	{
//System.out.println( "Operator[" + this + "](" + function + ")" );
		this.mapping = mapping;
	}

	public Operator( final RippleList list )
	{
//System.out.println( "Operator[" + this + "](" + list + ")" );
		mapping = new ListDequotation( list );
	}

	public StackMapping getMapping()
	{
		return mapping;
	}

	public String toString()
	{
		return "Operator(" + mapping + ")";
	}

	public void printTo( final RipplePrintStream p )
		throws RippleException
	{
		p.print( rdfEquivalent );
	}

	public boolean isActive()
	{
		return true;
	}

public RdfValue toRdf( final ModelConnection mc )
	throws RippleException
{
// Note: only correct for OP, but I expect this method only to be used with OP anyway
if ( null == rdfEquivalent )
{
	rdfEquivalent = new RdfValue( mc.createUri( "http://fortytwo.net/2007/03/ripple/schema#op" ) );
}
return rdfEquivalent;
}

	public int compareTo( final RippleValue other )
	{
		if ( other instanceof Operator )
		{
			// For now, all Operators are considered equal, as the only Operator
			// which should ever be subjected to a compareTo is rpl:op.
			return 0;
		}

		else
		{
			return this.getClass().getName().compareTo( other.getClass().getName() );
		}
	}

	/**
	 *  Finds the type of a value and creates an appropriate "active" wrapper.
	 */
	public static void createOperator( final RippleValue v,
										final Sink<Operator, RippleException> opSink,
										final ModelConnection mc )
		throws RippleException
	{
		// A function becomes active.
		if ( v instanceof StackMapping)
		{
			opSink.put( new Operator( (StackMapping) v ) );
			return;
		}

		// A list is dequoted.
		else if ( v instanceof RippleList )
		{
			opSink.put( new Operator( (RippleList) v ) );
			return;
		}

		// This is the messy part.  Attempt to guess the type of the object from
		// the available RDF statements, and create the appropriate object.
		if ( v instanceof RdfValue )
		{
			if ( isRdfList( (RdfValue) v, mc ) )
			{
				Sink<RippleList, RippleException> listSink = new Sink<RippleList, RippleException>()
				{
					public void put( final RippleList list )
						throws RippleException
					{
						opSink.put( new Operator( list ) );
					}
				};

				mc.toList( v, listSink );
				return;
			}

			// An RDF value not otherwise recognizable becomes a predicate filter.
			else if ( ( (RdfValue) v ).getRdfValue() instanceof Resource)
			{
				opSink.put( new Operator( (RdfValue) v ) );
				return;
			}
		}

		// Anything else becomes an active nullary filter with no output.
		else
		{
			opSink.put( new Operator( new NullStackMapping() ) );
			return;
		}
	}

// TODO: replace this with something a little more clever
	private static boolean isRdfList( final RdfValue v, final ModelConnection mc )
		throws RippleException
	{
		return ( v.equals( RDF_NIL )
			|| null != mc.findSingleObject( v, RDF_FIRST ) );
	}
}

// kate: tab-width 4
