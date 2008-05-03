/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.LibraryLoader;

/**
 * A collection of mathematical primitives.
 */
public class MathLibrary extends Library
{
	private static final String NS = "http://fortytwo.net/2007/08/ripple/math#";

    private static PrimitiveStackMapping
            addVal, subVal,
            mulVal, divVal,
            cosVal, acosVal,
            sinVal, asinVal,
            tanVal, atanVal,
            logVal, expVal;

    public void load( final URIMap uf,
                      final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
		uf.put(
			NS, getClass().getResource( "math.ttl" ) + "#" );

		// Comparison
		registerPrimitive( Gt.class, NS + "gt", context );
		registerPrimitive( Lt.class, NS + "lt", context );

		// Arithmetic
		registerPrimitive( Abs.class, NS + "abs", context );
		addVal = registerPrimitive( Add.class, NS + "add", context );
		divVal = registerPrimitive( Div.class, NS + "div", context );
		registerPrimitive( Mod.class, NS + "mod", context );
		mulVal = registerPrimitive( Mul.class, NS + "mul", context );
		registerPrimitive( Neg.class, NS + "neg", context );
		registerPrimitive( Signum.class, NS + "signum", context );
		subVal = registerPrimitive( Sub.class, NS + "sub", context );

		// Exponents
		registerPrimitive( Cbrt.class, NS + "cbrt", context );
		expVal = registerPrimitive( Exp.class, NS + "exp", context );
		logVal = registerPrimitive( Log.class, NS + "log", context );
		registerPrimitive( Log10.class, NS + "log10", context );
		registerPrimitive( Pow.class, NS + "pow", context );
		registerPrimitive( Sqrt.class, NS + "sqrt", context );

		// Trigonometry
		acosVal = registerPrimitive( Acos.class, NS + "acos", context );
		asinVal = registerPrimitive( Asin.class, NS + "asin", context );
		atanVal = registerPrimitive( Atan.class, NS + "atan", context );
		cosVal = registerPrimitive( Cos.class, NS + "cos", context );
		registerPrimitive( Cosh.class, NS + "cosh", context );
		sinVal = registerPrimitive( Sin.class, NS + "sin", context );
		registerPrimitive( Sinh.class, NS + "sinh", context );
		tanVal = registerPrimitive( Tan.class, NS + "tan", context );
		registerPrimitive( Tanh.class, NS + "tanh", context );

		// Misc
		registerPrimitive( Ceil.class, NS + "ceil", context );
		registerPrimitive( Floor.class, NS + "floor", context );
		registerPrimitive( Random.class, NS + "random", context );
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

// kate: tab-width 4
