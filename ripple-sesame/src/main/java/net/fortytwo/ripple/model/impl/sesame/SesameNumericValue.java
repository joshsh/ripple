/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.XMLSchema;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.RdfValue;

public class SesameNumericValue extends NumericValue {

	private RdfValue rdfEquivalent = null;

	public SesameNumericValue( final int i )
	{
		type = NumericLiteralType.INTEGER;
		number = new Integer( i );
	}
	
	public SesameNumericValue( final long l )
	{
		type = NumericLiteralType.LONG;
		number = new Long( l );
	}
	
	public SesameNumericValue( final double d )
	{
		type = NumericLiteralType.DOUBLE;
		number = new Double( d );
	}
	
	public SesameNumericValue( final float f )
	{
		type = NumericLiteralType.FLOAT;
		number = new Float( f );
	}
	
	public SesameNumericValue( final RdfValue rdf )
		throws RippleException
	{
		rdfEquivalent = rdf;
		Value v = rdf.sesameValue();

		if ( !( v instanceof Literal ) )
		{
			throw new RippleException( "value " + v.toString() + " is not numeric" );
		}

		URI dataType = ( (Literal) v ).getDatatype();

		if ( null == dataType )
		{
			throw new RippleException( "value is untyped" );
		}

		else if ( dataType.equals( XMLSchema.INTEGER )
			|| dataType.equals( XMLSchema.INT ) )
		{
			try
			{
				type = NumericLiteralType.INTEGER;
				number = new Integer( ( (Literal) v ).intValue() );
			}

			catch ( Throwable t )
			{
				throw new RippleException( t );
			}
		}

		else if ( dataType.equals( XMLSchema.LONG ) )
		{
			try
			{
				type = NumericLiteralType.LONG;
				number = new Long( ( (Literal) v ).intValue() );
			}

			catch ( Throwable t )
			{
				throw new RippleException( t );
			}
		}
		
		else if ( dataType.equals( XMLSchema.DOUBLE ) )
		{
			try
			{
				type = NumericLiteralType.DOUBLE;
				number = new Double( ( (Literal) v ).doubleValue() );
			}

			catch ( Throwable t )
			{
				throw new RippleException( t );
			}
		}

		else if ( dataType.equals( XMLSchema.FLOAT ) )
		{
			try
			{
				type = NumericLiteralType.FLOAT;
				number = new Float( ( (Literal) v ).floatValue() );
			}

			catch ( Throwable t )
			{
				throw new RippleException( t );
			}
		}
		
		else
		{
			throw new RippleException( "not a recognized numeric data type: " + dataType );
		}
	}
	
	public RdfValue toRDF( final ModelConnection mc )
		throws RippleException
	{
		SesameModelConnection smc = (SesameModelConnection) mc;
		if ( null == rdfEquivalent )
		{
			switch ( type )
			{
				case INTEGER:
					rdfEquivalent = new RdfValue( smc.getValueFactory().createLiteral( number.intValue() ) );
					break;
				case LONG:
					rdfEquivalent = new RdfValue( smc.getValueFactory().createLiteral( number.longValue() ) );
					break;
				case DOUBLE:
					rdfEquivalent = new RdfValue( smc.getValueFactory().createLiteral( number.doubleValue() ) );
					break;
				case FLOAT:
					rdfEquivalent = new RdfValue( smc.getValueFactory().createLiteral( number.floatValue() ) );
					break;
			}
		}
	
		return rdfEquivalent;
	}
	
	public NumericValue abs()
	{
		NumericValue a = this;
		
		if ( NumericLiteralType.INTEGER == a.getType() )
		{
			return new SesameNumericValue( Math.abs( a.intValue() ) );
		}

		else if ( NumericLiteralType.LONG == a.getType() )
		{
			return new SesameNumericValue( Math.abs( a.longValue() ) );
		}
	
		else if ( NumericLiteralType.FLOAT == a.getType() )
		{
			return new SesameNumericValue( Math.abs( a.floatValue() ) );
		}
		
		else
		{
			return new SesameNumericValue( Math.abs( a.doubleValue() ) );
		}
	}

	public NumericValue neg()
	{
		NumericValue a = this;

		if ( NumericLiteralType.INTEGER == a.getType() )
		{
			return new SesameNumericValue( -a.intValue() );
		}

		else if ( NumericLiteralType.LONG == a.getType() )
		{
			return new SesameNumericValue( -a.longValue() );
		}

		else if ( NumericLiteralType.FLOAT == a.getType() )
		{
			return new SesameNumericValue( -a.floatValue() );
		}
		
		else
		{
			// Note: avoids negative zero.
			return new SesameNumericValue( 0.0 - a.doubleValue() );
		}
	}

	public NumericValue add( final NumericValue b )
	{
		NumericValue a = this;

		if ( NumericLiteralType.INTEGER == a.getType() && NumericLiteralType.INTEGER == b.getType() )
		{
			return new SesameNumericValue( a.intValue() + b.intValue() );
		}

		else if ( NumericLiteralType.LONG == a.getType() && NumericLiteralType.LONG == b.getType() )
		{
			return new SesameNumericValue( a.longValue() + b.longValue() );
		}

		else if ( NumericLiteralType.FLOAT == a.getType() && NumericLiteralType.FLOAT == b.getType() )
		{
			return new SesameNumericValue( a.floatValue() + b.floatValue() );
		}
		
		else
		{
			return new SesameNumericValue( a.doubleValue() + b.doubleValue() );
		}
	}

	public NumericValue sub( final NumericValue b )
	{
		NumericValue a = this;

		if ( NumericLiteralType.INTEGER == a.getType() && NumericLiteralType.INTEGER == b.getType() )
		{
			return new SesameNumericValue( a.intValue() - b.intValue() );
		}

		else if ( NumericLiteralType.LONG == a.getType() && NumericLiteralType.LONG == b.getType() )
		{
			return new SesameNumericValue( a.longValue() - b.longValue() );
		}
	
		else if ( NumericLiteralType.FLOAT == a.getType() && NumericLiteralType.FLOAT == b.getType() )
		{
			return new SesameNumericValue( a.floatValue() - b.floatValue() );
		}
		
		else
		{
			return new SesameNumericValue( a.doubleValue() - b.doubleValue() );
		}
	}

	public NumericValue mul( final NumericValue b )
	{
		NumericValue a = this;

		if ( NumericLiteralType.INTEGER == a.getType() && NumericLiteralType.INTEGER == b.getType() )
		{
			return new SesameNumericValue( a.intValue() * b.intValue() );
		}

		else if ( NumericLiteralType.LONG == a.getType() && NumericLiteralType.LONG == b.getType() )
		{
			return new SesameNumericValue( a.longValue() * b.longValue() );
		}
	
		else if ( NumericLiteralType.FLOAT == a.getType() && NumericLiteralType.FLOAT == b.getType() )
		{
			return new SesameNumericValue( a.floatValue() * b.floatValue() );
		}
		
		else
		{
			return new SesameNumericValue( a.doubleValue() * b.doubleValue() );
		}
	}

	// Note: does not check for divide-by-zero.
	public NumericValue div( final NumericValue b )
	{
		NumericValue a = this;

		if ( NumericLiteralType.INTEGER == a.getType() && NumericLiteralType.INTEGER == b.getType() )
		{
			return new SesameNumericValue( a.intValue() / b.intValue() );
		}

		else if ( NumericLiteralType.LONG == a.getType() && NumericLiteralType.LONG == b.getType() )
		{
			return new SesameNumericValue( a.longValue() / b.longValue() );
		}

		else if ( NumericLiteralType.FLOAT == a.getType() && NumericLiteralType.FLOAT == b.getType() )
		{
			return new SesameNumericValue( a.floatValue() / b.floatValue() );
		}
		
		else
		{
			return new SesameNumericValue( a.doubleValue() / b.doubleValue() );
		}
	}

	// Note: does not check for divide-by-zero.
	public NumericValue mod( final NumericValue b )
	{
		NumericValue a = this;

		if ( NumericLiteralType.INTEGER == a.getType() && NumericLiteralType.INTEGER == b.getType() )
		{
			return new SesameNumericValue( a.intValue() % b.intValue() );
		}

		else if ( NumericLiteralType.LONG == a.getType() && NumericLiteralType.LONG == b.getType() )
		{
			return new SesameNumericValue( a.longValue() % b.longValue() );
		}

		else if ( NumericLiteralType.FLOAT == a.getType() && NumericLiteralType.FLOAT == b.getType() )
		{
			return new SesameNumericValue( a.floatValue() % b.floatValue() );
		}
		
		else
		{
			return new SesameNumericValue( a.doubleValue() % b.doubleValue() );
		}
	}

	public NumericValue pow( final NumericValue pow )
	{
		NumericValue a = this;

		double r = Math.pow( a.doubleValue(), pow.doubleValue() );

		if ( NumericLiteralType.INTEGER == a.getType() && NumericLiteralType.INTEGER == pow.getType() )
		{
			return new SesameNumericValue( (int) r );
		}

		else if ( NumericLiteralType.LONG == a.getType() && NumericLiteralType.LONG == pow.getType() )
		{
			return new SesameNumericValue( (long) r );
		}
		
		else if ( NumericLiteralType.FLOAT == a.getType() && NumericLiteralType.FLOAT == pow.getType() )
		{
			return new SesameNumericValue( (float) r );
		}
		
		else
		{
			return new SesameNumericValue( r );
		}
	}
}
