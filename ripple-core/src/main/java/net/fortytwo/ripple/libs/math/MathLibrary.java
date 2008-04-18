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

    public void load( final URIMap uf, final ModelConnection mc )
		throws RippleException
	{
		uf.put(
			NS, getClass().getResource( "math.ttl" ) + "#" );

		// Comparison
		registerPrimitive( Gt.class, NS + "gt", mc );
		registerPrimitive( Lt.class, NS + "lt", mc );

		// Arithmetic
		registerPrimitive( Abs.class, NS + "abs", mc );
		addVal = registerPrimitive( Add.class, NS + "add", mc );
		divVal = registerPrimitive( Div.class, NS + "div", mc );
		registerPrimitive( Mod.class, NS + "mod", mc );
		mulVal = registerPrimitive( Mul.class, NS + "mul", mc );
		registerPrimitive( Neg.class, NS + "neg", mc );
		registerPrimitive( Signum.class, NS + "signum", mc );
		subVal = registerPrimitive( Sub.class, NS + "sub", mc );

		// Exponents
		registerPrimitive( Cbrt.class, NS + "cbrt", mc );
		expVal = registerPrimitive( Exp.class, NS + "exp", mc );
		logVal = registerPrimitive( Log.class, NS + "log", mc );
		registerPrimitive( Log10.class, NS + "log10", mc );
		registerPrimitive( Pow.class, NS + "pow", mc );
		registerPrimitive( Sqrt.class, NS + "sqrt", mc );

		// Trigonometry
		acosVal = registerPrimitive( Acos.class, NS + "acos", mc );
		asinVal = registerPrimitive( Asin.class, NS + "asin", mc );
		atanVal = registerPrimitive( Atan.class, NS + "atan", mc );
		cosVal = registerPrimitive( Cos.class, NS + "cos", mc );
		registerPrimitive( Cosh.class, NS + "cosh", mc );
		sinVal = registerPrimitive( Sin.class, NS + "sin", mc );
		registerPrimitive( Sinh.class, NS + "sinh", mc );
		tanVal = registerPrimitive( Tan.class, NS + "tan", mc );
		registerPrimitive( Tanh.class, NS + "tanh", mc );

		// Misc
		registerPrimitive( Ceil.class, NS + "ceil", mc );
		registerPrimitive( Floor.class, NS + "floor", mc );
		registerPrimitive( Random.class, NS + "random", mc );
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
