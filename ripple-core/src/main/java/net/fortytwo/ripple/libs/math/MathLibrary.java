/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.LibraryLoader;

/**
 * A collection of mathematical primitives.
 */
public class MathLibrary extends Library
{
    public static final String
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/math#",
            NS_2007_08 = "http://fortytwo.net/2007/08/ripple/math#",
            NS_2007_05 = "http://fortytwo.net/2007/05/ripple/math#";

    private static PrimitiveStackMapping
            addVal, subVal,
            mulVal, divVal,
            cosVal, acosVal,
            sinVal, asinVal,
            tanVal, atanVal,
            logVal, expVal;

    public void load( final URIMap uf,
                      final LibraryLoader.Context context )
		throws RippleException
	{
		uf.put( NS_2008_08, getClass().getResource( "math.ttl" ) + "#" );

		// Comparison
		registerPrimitive( Gt.class, context );
		registerPrimitive( Lt.class, context );

		// Arithmetic
		registerPrimitive( Abs.class, context );
		addVal = registerPrimitive( Add.class, context );
		divVal = registerPrimitive( Div.class, context );
		registerPrimitive( Mod.class, context );
		mulVal = registerPrimitive( Mul.class, context );
		registerPrimitive( Neg.class, context );
		registerPrimitive( Sign.class, context );
		subVal = registerPrimitive( Sub.class, context );

		// Exponents
		registerPrimitive( Cbrt.class, context );
		expVal = registerPrimitive( Exp.class, context );
		logVal = registerPrimitive( Log.class, context );
		registerPrimitive( Log10.class, context );
		registerPrimitive( Pow.class, context );
		registerPrimitive( Sqrt.class, context );

		// Trigonometry
		acosVal = registerPrimitive( Acos.class, context );
		asinVal = registerPrimitive( Asin.class, context );
		atanVal = registerPrimitive( Atan.class, context );
		cosVal = registerPrimitive( Cos.class, context );
		registerPrimitive( Cosh.class, context );
		sinVal = registerPrimitive( Sin.class, context );
		registerPrimitive( Sinh.class, context );
		tanVal = registerPrimitive( Tan.class, context );
		registerPrimitive( Tanh.class, context );

		// Misc
		registerPrimitive( Ceil.class, context );
		registerPrimitive( Floor.class, context );
		registerPrimitive( Random.class, context );
	}

    public static PrimitiveStackMapping getAddValue()
    {
        return addVal;
    }

    public static PrimitiveStackMapping getSubValue()
    {
        return subVal;
    }

    public static PrimitiveStackMapping getMulValue()
    {
        return mulVal;
    }

    public static PrimitiveStackMapping getDivValue()
    {
        return divVal;
    }

    public static PrimitiveStackMapping getCosValue()
    {
        return cosVal;
    }

    public static PrimitiveStackMapping getAcosValue()
    {
        return acosVal;
    }

    public static PrimitiveStackMapping getSinValue()
    {
        return sinVal;
    }

    public static PrimitiveStackMapping getAsinValue()
    {
        return asinVal;
    }

    public static PrimitiveStackMapping getTanValue()
    {
        return tanVal;
    }

    public static PrimitiveStackMapping getAtanValue()
    {
        return atanVal;
    }

    public static PrimitiveStackMapping getExpValue()
    {
        return expVal;
    }

    public static PrimitiveStackMapping getLogValue()
    {
        return logVal;
    }
}

