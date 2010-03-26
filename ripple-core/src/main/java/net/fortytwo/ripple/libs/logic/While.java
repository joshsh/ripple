/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Collector;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.ModelConnection;

import java.util.Iterator;

/**
 * A primitive which consumes a Boolean filter b, a filter t, and a filter t,
 * then applies an active copy of b to the stack.  If b yields a value of
 * true, then t is applied the rest of the stack.  Otherwise, f is applied to
 * the rest of the stack.
 */
public class While extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            LogicLibrary.NS_2008_08 + "while"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public While() throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "b", "loop condition", true ),
                new Parameter( "p", "loop body, executed as long as condition is satisfied", true )};
    }

    public String getComment()
    {
        return "b p =>  p! p! p! ...  -- where p is executed as long as executing b yields true";
    }

	public void apply( final StackContext arg,
                       final Sink<StackContext, RippleException> solutions	)
		throws RippleException
	{
        final ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();

		RippleValue program = stack.getFirst();
		stack = stack.getRest();
		RippleValue criterion = stack.getFirst();
		stack = stack.getRest();

        Collector<Operator, RippleException> programOps
                = new Collector<Operator, RippleException>();
        Operator.createOperator( program, programOps, mc );
        Collector<Operator, RippleException> criterionOps
                = new Collector<Operator, RippleException>();
        Operator.createOperator( criterion, criterionOps, mc );

        for ( Iterator<Operator> programIter = programOps.iterator(); programIter.hasNext(); )
        {
            Operator programOp = programIter.next();

            for ( Iterator<Operator> criterionIter = criterionOps.iterator(); criterionIter.hasNext(); )
            {
                Operator criterionOp = criterionIter.next();

                StackMapping a = new WhileApplicator( programOp, criterionOp );

                solutions.put( arg.with( stack.push( new Operator( a ) ) ) );
            }
        }
	}

    private class WhileDecider implements StackMapping
    {
        private final RippleList originalStack;
        private final Operator program;
        private final Operator criterion;

        public WhileDecider( final RippleList originalStack,
                             final Operator program,
                             final Operator criterion )
        {
            this.originalStack = originalStack;
            this.program = program;
            this.criterion = criterion;
        }

        public int arity()
        {
            return 1;
        }

        public StackMapping inverse() throws RippleException
        {
            return new NullStackMapping();
        }

        public boolean isTransparent()
        {
            return true;
        }

        public void apply( final StackContext arg,
                           final Sink<StackContext, RippleException> solutions ) throws RippleException
        {
            ModelConnection mc = arg.getModelConnection();
            boolean b = mc.toBoolean( arg.getStack().getFirst() );

            if ( b )
            {
                StackMapping a = new WhileApplicator( program, criterion );
                RippleList stack = originalStack.push( program ).push( new Operator( a ) );
                solutions.put( arg.with( stack ) );
            }

            else
            {
                solutions.put( arg.with( originalStack ) );
            }
        }
    }

    private class WhileApplicator implements StackMapping
    {
        private final Operator program;
        private final Operator criterion;

        public WhileApplicator( final Operator program,
                                final Operator criterion )
        {
            this.program = program;
            this.criterion = criterion;
        }

        public int arity()
        {
            // Cheat which forces the program below to be applied.
            return 1;
        }

        public StackMapping inverse() throws RippleException
        {
            return new NullStackMapping();
        }

        public boolean isTransparent()
        {
            return true;
        }

        public void apply( final StackContext arg,
                           final Sink<StackContext, RippleException> solutions ) throws RippleException
        {
            RippleList stack = arg.getStack();
            StackMapping d = new WhileDecider( stack, program, criterion );

            solutions.put( arg.with( stack.push( criterion ).push( new Operator( d ) ) ) );
        }
    }
}
