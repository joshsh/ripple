/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a number and produces its absolute value.
 */
public class Abs extends PrimitiveStackMapping
{
	private static final int ARITY = 1;
    private final StackMapping self = this;

    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2011_08 + "abs",
            MathLibrary.NS_2008_08 + "abs",
            MathLibrary.NS_2007_08 + "abs",
            MathLibrary.NS_2007_05 + "abs"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Abs()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", null, true )};
    }

    public String getComment()
    {
        return "x  =>  absolute value of x";
    }


    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

		RippleList stack = arg;

		NumericValue a, result;

		a = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();

		result = a.abs();

		solutions.put(
				stack.push( result ) );
	}

    @Override
    public StackMapping getInverse() throws RippleException
    {
        return absInverse;
    }

    private StackMapping absInverse = new StackMapping()
    {
        public int arity()
        {
            return ARITY;
        }

        public StackMapping getInverse() throws RippleException
        {
            return self;
        }

        public boolean isTransparent()
        {
            return true;
        }

        public void apply(final RippleList arg,
                          final Sink<RippleList> solutions,
                          final ModelConnection mc) throws RippleException {

            RippleList stack = arg;

            NumericValue a;

            a = mc.toNumericValue( stack.getFirst() );
            stack = stack.getRest();

            // Negative values are not the absolute value of any number.
            if ( a.doubleValue() >= 0 )
            {
                // Push the number itself.
                solutions.put(
                        stack.push( a ) );

                // If the number is nonzero, also push its negation.
                if ( a.doubleValue() > 0 )
                {
                    solutions.put(
                            stack.push( a.neg() ) );
                }
            }
        }
    };
}

