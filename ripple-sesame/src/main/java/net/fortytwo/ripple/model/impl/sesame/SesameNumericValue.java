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

import java.math.BigDecimal;
import java.math.BigInteger;

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

    public SesameNumericValue( final BigDecimal b )
	{
		type = NumericLiteralType.DECIMAL;
		number = b;
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

        else if ( dataType.equals( XMLSchema.DECIMAL ) )
		{
			try
			{
				type = NumericLiteralType.DECIMAL;
				number = ( (Literal) v ).decimalValue();
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
                    // Don't use ValueFactory.creatLiteral(int), which (at
                    // least in this case) produces xsd:int instead of xsd:integer
                    rdfEquivalent = new RdfValue( smc.getValueFactory().createLiteral( "" + number.intValue(), XMLSchema.INTEGER ) );
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
                case DECIMAL:
                    rdfEquivalent = new RdfValue( smc.getValueFactory().createLiteral( number.toString(), XMLSchema.DECIMAL ) );
                    break;
            }
		}
	
		return rdfEquivalent;
	}
	
	public NumericValue abs()
	{
		NumericValue a = this;

        switch ( a.getType() )
        {
            case INTEGER:
                return new SesameNumericValue( Math.abs( a.intValue() ) );
            case LONG:
                return new SesameNumericValue( Math.abs( a.longValue() ) );
            case FLOAT:
                return new SesameNumericValue( Math.abs( a.floatValue() ) );
            case DOUBLE:
                return new SesameNumericValue( Math.abs( a.doubleValue() ) );
            case DECIMAL:
                return new SesameNumericValue( a.decimalValue().abs() );
            default:
                // Shouldn't happen.
                return null;
        }
	}

	public NumericValue neg()
	{
		NumericValue a = this;

        switch ( a.getType() )
        {
            case INTEGER:
                return new SesameNumericValue( -a.intValue() );
            case LONG:
                return new SesameNumericValue( -a.longValue() );
            case FLOAT:
                return new SesameNumericValue( -a.floatValue() );
            case DOUBLE:
                // Note: avoids negative zero.
                return new SesameNumericValue( 0.0 - a.doubleValue() );
            case DECIMAL:
                return new SesameNumericValue( a.decimalValue().negate() );
            default:
                // Shouldn't happen.
                return null;
        }
	}

    private NumericLiteralType maxPrecision( final NumericValue a, final NumericValue b )
    {
        NumericLiteralType max = NumericLiteralType.INTEGER;

        if ( a.getType() == NumericLiteralType.LONG || b.getType() == NumericLiteralType.LONG )
        {
            max = NumericLiteralType.LONG;
        }

        if ( a.getType() == NumericLiteralType.FLOAT || b.getType() == NumericLiteralType.FLOAT )
        {
            max = NumericLiteralType.FLOAT;
        }

        if ( a.getType() == NumericLiteralType.DOUBLE || b.getType() == NumericLiteralType.DOUBLE )
        {
            max = NumericLiteralType.DOUBLE;
        }

        if ( a.getType() == NumericLiteralType.DECIMAL || b.getType() == NumericLiteralType.DECIMAL )
        {
            max = NumericLiteralType.DECIMAL;
        }

        return max;
    }

    public NumericValue add( final NumericValue b )
	{
		NumericValue a = this;

        NumericLiteralType precision = maxPrecision( a, b );
        switch ( precision )
        {
            case INTEGER:
                return new SesameNumericValue( a.intValue() + b.intValue() );
            case LONG:
                return new SesameNumericValue( a.longValue() + b.longValue() );
            case FLOAT:
                return new SesameNumericValue( a.floatValue() + b.floatValue() );
            case DOUBLE:
                return new SesameNumericValue( a.doubleValue() + b.doubleValue() );
            case DECIMAL:
                return new SesameNumericValue( a.decimalValue().add( b.decimalValue() ) );
            default:
                // Shouldn't happen.
                return null;
        }
	}

	public NumericValue sub( final NumericValue b )
	{
		NumericValue a = this;

        NumericLiteralType precision = maxPrecision( a, b );
        switch ( precision )
        {
            case INTEGER:
                return new SesameNumericValue( a.intValue() - b.intValue() );
            case LONG:
                return new SesameNumericValue( a.longValue() - b.longValue() );
            case FLOAT:
                return new SesameNumericValue( a.floatValue() - b.floatValue() );
            case DOUBLE:
                return new SesameNumericValue( a.doubleValue() - b.doubleValue() );
            case DECIMAL:
                return new SesameNumericValue( a.decimalValue().subtract( b.decimalValue() ) );
            default:
                // Shouldn't happen.
                return null;
        }
	}

	public NumericValue mul( final NumericValue b )
	{
		NumericValue a = this;

        NumericLiteralType precision = maxPrecision( a, b );
        switch ( precision )
        {
            case INTEGER:
                return new SesameNumericValue( a.intValue() * b.intValue() );
            case LONG:
                return new SesameNumericValue( a.longValue() * b.longValue() );
            case FLOAT:
                return new SesameNumericValue( a.floatValue() * b.floatValue() );
            case DOUBLE:
                return new SesameNumericValue( a.doubleValue() * b.doubleValue() );
            case DECIMAL:
                return new SesameNumericValue( a.decimalValue().multiply( b.decimalValue() ) );
            default:
                // Shouldn't happen.
                return null;
        }
	}

	// Note: does not check for divide-by-zero.
	public NumericValue div( final NumericValue b )
	{
		NumericValue a = this;

        NumericLiteralType precision = maxPrecision( a, b );
        switch ( precision )
        {
            case INTEGER:
                return new SesameNumericValue( a.intValue() / b.intValue() );
            case LONG:
                return new SesameNumericValue( a.longValue() / b.longValue() );
            case FLOAT:
                return new SesameNumericValue( a.floatValue() / b.floatValue() );
            case DOUBLE:
                return new SesameNumericValue( a.doubleValue() / b.doubleValue() );
            case DECIMAL:
                return new SesameNumericValue( a.decimalValue().divide( b.decimalValue() ) );
            default:
                // Shouldn't happen.
                return null;
        }
	}

	// Note: does not check for divide-by-zero.
	public NumericValue mod( final NumericValue b )
	{
		NumericValue a = this;

        NumericLiteralType precision = maxPrecision( a, b );
        switch ( precision )
        {
            case INTEGER:
                return new SesameNumericValue( a.intValue() % b.intValue() );
            case LONG:
                return new SesameNumericValue( a.longValue() % b.longValue() );
            case FLOAT:
                return new SesameNumericValue( a.floatValue() % b.floatValue() );
            case DOUBLE:
                return new SesameNumericValue( a.doubleValue() % b.doubleValue() );
            case DECIMAL:
                return new SesameNumericValue( a.decimalValue().remainder( b.decimalValue() ).abs() );
            default:
                // Shouldn't happen.
                return null;
        }
	}

	public NumericValue pow( final NumericValue pow )
	{
		NumericValue a = this;

        if ( NumericLiteralType.DECIMAL == a.getType() && NumericLiteralType.INTEGER == pow.getType() )
        {
            return new SesameNumericValue( a.decimalValue().pow( a.intValue() ) );
        }

        else
        {
            double r = Math.pow( a.doubleValue(), pow.doubleValue() );

            NumericLiteralType precision = maxPrecision( a, pow );
            switch ( precision )
            {
                case INTEGER:
                    return new SesameNumericValue( (int) r );
                case LONG:
                    return new SesameNumericValue( (long) r );
                case FLOAT:
                    return new SesameNumericValue( (float) r );
                case DOUBLE:
                    return new SesameNumericValue( r );
                case DECIMAL:
                    return new SesameNumericValue( r );
                default:
                    // Shouldn't happen.
                    return null;
            }
        }
	}
}
