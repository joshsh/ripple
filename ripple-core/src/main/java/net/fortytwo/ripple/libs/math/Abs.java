/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.math;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
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


	public void apply( final StackContext arg,
					   final Sink<StackContext> solutions )
            throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		NumericValue a, result;

		a = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();

		result = a.abs();

		solutions.put( arg.with(
				stack.push( result ) ) );
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

        public void apply( final StackContext arg,
                           final Sink<StackContext> solutions ) throws RippleException
        {
            final ModelConnection mc = arg.getModelConnection();
            RippleList stack = arg.getStack();

            NumericValue a;

            a = mc.toNumericValue( stack.getFirst() );
            stack = stack.getRest();

            // Negative values are not the absolute value of any number.
            if ( a.doubleValue() >= 0 )
            {
                // Push the number itself.
                solutions.put( arg.with(
                        stack.push( a ) ) );

                // If the number is nonzero, also push its negation.
                if ( a.doubleValue() > 0 )
                {
                    solutions.put( arg.with(
                            stack.push( a.neg() ) ) );
                }
            }
        }
    };
}

