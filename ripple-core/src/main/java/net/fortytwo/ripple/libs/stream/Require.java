/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.ModelConnection;

/**
 * A filter which discards the stack unless the topmost item is the boolean
 * value stack:true.
 */
public class Require extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

	public Require()
		throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> sink )
		throws RippleException
	{
        ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();
        RippleValue mapping = stack.getFirst();
        final RippleList rest = stack.getRest();

        Sink<Operator, RippleException> opSink = new Sink<Operator, RippleException>()
        {
            public void put( final Operator op ) throws RippleException
            {
                CriterionApplicator applicator = new CriterionApplicator( op );
                sink.put( arg.with( rest.push( new Operator( applicator ) ) ) );
            }
        };
        
        Operator.createOperator(mapping, opSink, mc);
    }

    private class CriterionApplicator extends PrimitiveStackMapping
    {
        private Operator criterion;

        public CriterionApplicator( final Operator criterion )
        {
            this.criterion = criterion;
        }

        // FIXME: the criterion's arity had better be accurate (which it currently may not be, if the criterion is a list dequotation)
        public int arity()
        {
            return criterion.getMapping().arity();
        }

        public void applyTo( final StackContext arg,
                             final Sink<StackContext, RippleException> sink ) throws RippleException
        {
            RippleList stack = arg.getStack();
            Decider decider = new Decider( stack );

            // Apply the criterion, sending the result into the Decider.
            sink.put( arg.with( stack.push( criterion ).push( new Operator( decider ) ) ) );
        }
    }

    private class Decider extends PrimitiveStackMapping
    {
        private RippleList rest;

        public Decider( final RippleList rest )
        {
            this.rest = rest;
        }

        public int arity()
        {
            return 1;
        }

        public void applyTo( final StackContext arg,
                             final Sink<StackContext, RippleException> sink ) throws RippleException
        {
            RippleValue b;
            RippleList stack = arg.getStack();

            b = stack.getFirst();
            stack = stack.getRest();

            if ( b.equals( StackLibrary.getTrueValue() ) )
            {
                sink.put( arg.with( rest ) );
            }
        }
    }
}
