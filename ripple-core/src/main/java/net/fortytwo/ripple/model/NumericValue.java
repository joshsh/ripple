/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.vocabulary.XMLSchema;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;

/**
 * A numeric (xsd:integer or xsd:double) literal value.
 */
public abstract class NumericValue implements RippleValue, Comparable<NumericValue>
{
	/**
	 * Distinguishes between numeric literals of type xsd:integer and xsd:double.
	 */
	public enum NumericLiteralType { INTEGER, LONG, DOUBLE, FLOAT, DECIMAL };

    protected static final Map<URI, NumericLiteralType> uriToTypeMap;

    static
    {
        uriToTypeMap = new HashMap<URI, NumericLiteralType>();
        uriToTypeMap.put( XMLSchema.INTEGER, NumericLiteralType.INTEGER );
        uriToTypeMap.put( XMLSchema.LONG, NumericLiteralType.LONG );
        uriToTypeMap.put( XMLSchema.DOUBLE, NumericLiteralType.DOUBLE );
        uriToTypeMap.put( XMLSchema.FLOAT, NumericLiteralType.FLOAT );
        uriToTypeMap.put( XMLSchema.DECIMAL, NumericLiteralType.DECIMAL );
    }

    protected NumericLiteralType type;
	protected Number number;

// TODO: move into implementation
	protected RdfValue rdfEquivalent = null;

	public abstract RdfValue toRDF( final ModelConnection mc ) throws RippleException;
	
	public NumericLiteralType getType()
	{
		return type;
	}
	
	protected Number getNumber()
	{
		return number;
	}

	public int intValue()
	{
		return number.intValue();
	}

	public long longValue()
	{
		return number.longValue();
	}
	
	public double doubleValue()
	{
		return number.doubleValue();
	}
	
	public float floatValue()
	{
		return number.floatValue();
	}

	public BigDecimal decimalValue()
	{
        return ( number instanceof BigDecimal )
                ? (BigDecimal) number
                : BigDecimal.valueOf( number.doubleValue() );
	}

    public boolean isZero()
	{
		return ( 0.0 == number.doubleValue() );
	}

	public int sign()
	{
		double x = number.doubleValue();
        return x < 0.0 ? -1 : x > 0 ? 1 : 0;
    }

	////////////////////////////////////////////////////////////////////////////

	public boolean isActive()
	{
		return false;
	}

	public void printTo( final RipplePrintStream p )
		throws RippleException
	{
        switch ( getType() )
        {
            case INTEGER:
                p.printInteger( intValue() );
                break;
            case LONG:
                p.printTypedLiteral( toString(), XMLSchema.LONG );
                break;
            case DOUBLE:
                p.printDouble( doubleValue() );
                break;
            case FLOAT:
                p.printTypedLiteral( toString(), XMLSchema.FLOAT );
                break;
            case DECIMAL:
                p.printDecimal( decimalValue() );
                break;
            default:
                // Shouldn't happen.
        }
    }

    public int compareTo( final NumericValue other)
    {
        NumericLiteralType precision = maxPrecision( this, other);
//System.out.println("comparing " + a + " with " + b + " (precision = " + precision + ", a.getType() = " + a.getType() + ", b.getType() = " + b.getType() + ")");

        switch ( precision )
        {
            case INTEGER:
                return compare( this.intValue(), other.intValue() );
            case LONG:
                return compare( this.longValue(), other.longValue() );
            case FLOAT:
                return compare( this.floatValue(), other.floatValue() );
            case DOUBLE:
                return compare( this.doubleValue(), other.doubleValue() );
            case DECIMAL:
//System.out.println("    a.decimalValue().compareTo( b.decimalValue() ) = " + a.decimalValue().compareTo( b.decimalValue() ));
//System.out.println("    a.number.getClass() = " + a.number.getClass() + ", b.number.getClass() = " + b.number.getClass());
                return this.decimalValue().compareTo( other.decimalValue() );
            default:
                // Shouldn't happen.
                return 0;
        }
    }

    public static int compareNumericLiterals( final Literal a, final Literal b )
    {
        NumericLiteralType aType = inferPrecision( a );
        NumericLiteralType bType = inferPrecision( b );

        if ( null == aType || null == bType )
        {
            throw new IllegalArgumentException( "literal has non-numeric type" );
        }

        NumericLiteralType precision = maxPrecision( aType, bType );

        switch ( precision )
        {
            case INTEGER:
                return compare( a.intValue(), b.intValue() );
            case LONG:
                return compare( a.longValue(), b.longValue() );
            case FLOAT:
                return compare( a.floatValue(), b.floatValue() );
            case DOUBLE:
                return compare( doubleValue( a ), doubleValue( b ) );
            case DECIMAL:
                return a.decimalValue().compareTo( b.decimalValue() );
            default:
                // Shouldn't happen.
                return 0;
        }
    }

    public static int compare( final Literal a, final NumericValue b )
    {
        NumericLiteralType aType = inferPrecision( a );

        if ( null == aType )
        {
            throw new IllegalArgumentException( "literal has non-numeric type: " + a );
        }

        else
        {
            NumericLiteralType precision = maxPrecision( aType, b.getType() );

            switch ( precision )
            {
                case INTEGER:
                    return compare( a.intValue(), b.intValue() );
                case LONG:
                    return compare( a.longValue(), b.longValue() );
                case FLOAT:
                    return compare( a.floatValue(), b.floatValue() );
                case DOUBLE:
                    return compare( doubleValue( a ), b.doubleValue() );
                case DECIMAL:
                    return a.decimalValue().compareTo( b.decimalValue() );
                default:
                    // Shouldn't happen.
                    return 0;
            }
        }
    }

    // Note: the literal is assumed to be of type xsd:double.
    // TODO: FLOAT also has special values
    protected static double doubleValue( final Literal l )
    {
        String label = l.getLabel();

        // Sesame's literals apparently don't handle these special
        // cases, so we need to check for them here.
        if ( label.equals( "NaN" ) )
        {
            return Double.NaN;
        }

        else if ( label.equals( "INF" ) )
        {
            return Double.POSITIVE_INFINITY;
        }

        else if ( label.equals( "-INF" ) )
        {
            return Double.NEGATIVE_INFINITY;
        }

        else
        {
            return l.doubleValue();
        }
    }

    private static int compare( final int a, final int b )
    {
        return a < b ? -1 : a > b ? 1 : 0;
    }

    private static int compare( final long a, final long b )
    {
        return a < b ? -1 : a > b ? 1 : 0;
    }

    private static int compare( final float a, final float b )
    {
        return a < b ? -1 : a > b ? 1 : 0;
    }

    private static int compare( final double a, final double b )
    {
        return a < b ? -1 : a > b ? 1 : 0;
    }

    public String toString()
	{
		return number.toString();
	}

    public static boolean isNumericLiteral( final Literal l )
    {
        return null != inferPrecision( l );
    }

    private static NumericLiteralType inferPrecision( final Literal l )
    {
        URI datatype = l.getDatatype();

        return ( null == datatype )
                ? null
                : uriToTypeMap.get( datatype );
    }

    protected static NumericLiteralType maxPrecision( final NumericValue a, final NumericValue b )
    {
        return maxPrecision( a.getType(), b.getType() );
    }

    protected static NumericLiteralType maxPrecision( final NumericLiteralType a, final NumericLiteralType b )
    {
        NumericLiteralType max = NumericLiteralType.INTEGER;

        if ( a == NumericLiteralType.LONG || b == NumericLiteralType.LONG )
        {
            max = NumericLiteralType.LONG;
        }

        if ( a == NumericLiteralType.FLOAT || b == NumericLiteralType.FLOAT )
        {
            max = NumericLiteralType.FLOAT;
        }

        if ( a == NumericLiteralType.DOUBLE || b == NumericLiteralType.DOUBLE )
        {
            max = NumericLiteralType.DOUBLE;
        }

        if ( a == NumericLiteralType.DECIMAL || b == NumericLiteralType.DECIMAL )
        {
            max = NumericLiteralType.DECIMAL;
        }

        return max;
    }

    public abstract NumericValue abs();
	public abstract NumericValue neg();
	public abstract NumericValue add( final NumericValue b );
	public abstract NumericValue sub( final NumericValue b );
	public abstract NumericValue mul( final NumericValue b );
	public abstract NumericValue div( final NumericValue b );
	public abstract NumericValue mod( final NumericValue b );
	public abstract NumericValue pow( final NumericValue pow );
}

