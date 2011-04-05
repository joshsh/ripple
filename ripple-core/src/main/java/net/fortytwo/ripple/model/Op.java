/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.io.RipplePrintStream;

public class Op implements StackMapping, RippleValue
{
    private final static int ARITY = 1;

    public void apply( final StackContext arg,
				final Sink<StackContext, RippleException> sink )
		throws RippleException
	{
		RippleValue v;
	    RippleList stack = arg.getStack();

		v = stack.getFirst();
		final RippleList rest = stack.getRest();
	
		Sink<Operator, RippleException> opSink = new Sink<Operator, RippleException>()
		{
			public void put( final Operator oper )
				throws RippleException
			{
				sink.put( arg.with( rest.push( oper ) ) );
			}
		};

		Operator.createOperator( v, opSink, arg.getModelConnection() );
	}

	public int arity()
	{
		return ARITY;
	}

	public void printTo( final RipplePrintStream p )
		throws RippleException
	{
		System.err.println( "You should not need to print op directly." );
		System.exit( 1 );
	}

	public RDFValue toRDF( final ModelConnection mc )
		throws RippleException
	{
		System.err.println( "You should not need to convert op explicitly." );
		System.exit( 1 );
		return null;
	}

	public boolean isActive()
	{
		return true;
	}
	
	public boolean isTransparent()
	{
		return true;
	}

    public StackMapping getInverse() throws RippleException
    {
        return new OpInverse();
    }

    public boolean equals( final Object other )
    {
        return other instanceof Op;
    }

    public int hashCode()
    {
        // Arbitrary.
        return 1056205736;
    }

    public String toString()
    {
        return "op";
    }

    public class OpInverse extends Op
    {
        @Override
        public void apply( final StackContext arg,
                    final Sink<StackContext, RippleException> sink )
            throws RippleException
        {
            RippleValue v;
            RippleList stack = arg.getStack();

            v = stack.getFirst();
            final RippleList rest = stack.getRest();

            Sink<Operator, RippleException> opSink = new Sink<Operator, RippleException>()
            {
                public void put( final Operator oper )
                    throws RippleException
                {
                    Operator inv = new Operator( oper.getMapping().getInverse() );
                    sink.put( arg.with( rest.push( inv ) ) );
                }
            };

            Operator.createOperator( v, opSink, arg.getModelConnection() );
        }

        @Override
        public StackMapping getInverse() throws RippleException
        {
            return Operator.OP.getMapping();
        }

        @Override
        public String toString()
        {
            return "opInverse";
        }
    }
}

