/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;

import java.util.regex.Pattern;
import java.math.BigInteger;

public class IntegerAST extends NumberAST
{
    /*
    [Definition:]   integer is derived from decimal by fixing the value of
    fractionDigits to be 0 and disallowing the trailing decimal point. This
    results in the standard mathematical concept of the integer numbers. The
    value space of integer is the infinite set {...,-2,-1,0,1,2,...}. The
    base type of integer is decimal.
    */
    private static final Pattern
            // Note: NaN, positive and negative infinity are apparently not allowed.
            XSD_INTEGER = Pattern.compile("[-+]?\\d+");

	private final BigInteger value;

	public IntegerAST( final BigInteger value )
	{
		this.value = value;
    }

    public IntegerAST( final String rep )
    {
        if ( !XSD_INTEGER.matcher( rep ).matches() )
        {
            throw new IllegalArgumentException( "invalid xsd:integer value: " + rep );    
        }

        value = new BigInteger( canonicalize( rep ) );
    }

    public NumericValue getValue( final ModelConnection mc ) throws RippleException
    {
        // FIXME: xsd:integer values are constrained to the precision of Java
        // int's, whereas they are supposed to have arbitrary precision.
        return mc.numericValue(value.intValue());
    }

	public String toString()
	{
        // This will naturally be the canonical form.
        return "" + value;
	}
}

