/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.flow.NullMapping;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.NullStackMapping;

/**
 * A primitive which consumes a Boolean filter b, a filter t, and a filter t,
 * then applies an active copy of b to the stack.  If b yields a value of
 * true, then t is applied the rest of the stack.  Otherwise, f is applied to
 * the rest of the stack.
 */
public class Ifte extends PrimitiveStackMapping
{
	private static final int ARITY = 3;

    private static final String[] IDENTIFIERS = {
            // Note: the previous implementation of ifte had different semantics
            // (rather than the current, Joy semantics).
            LogicLibrary.NS_2008_06 + "ifte"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }
    
    public Ifte()
		throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions	)
		throws RippleException
	{
		RippleList stack = arg.getStack();

		RippleValue falseProg = stack.getFirst();
		stack = stack.getRest();
		RippleValue trueProg = stack.getFirst();
		stack = stack.getRest();
		RippleValue criterion = stack.getFirst();
		stack = stack.getRest();

        StackMapping inner = new IfteInner( stack, trueProg, falseProg );
        RippleList newStack = stack.push( criterion ).push( Operator.OP )
                .push( new Operator( inner ) );
               
        solutions.put( arg.with( newStack ) );
	}

    private class IfteInner implements StackMapping
    {
        private final RippleList originalStack;
        private final RippleValue trueProgram, falseProgram;

        public IfteInner( final RippleList originalStack,
                          final RippleValue trueProgram,
                          final RippleValue falseProgram )
        {
            this.originalStack = originalStack;
            this.trueProgram = trueProgram;
            this.falseProgram = falseProgram;
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

        public void applyTo( final StackContext arg,
                             final Sink<StackContext, RippleException> sink ) throws RippleException
        {
            RippleValue b = arg.getStack().getFirst();

            RippleList stack = LogicLibrary.toBoolean( b )
                    ? originalStack.push( trueProgram ).push( Operator.OP )
                    : originalStack.push( falseProgram ).push( Operator.OP );

            sink.put( arg.with( stack ) );
        }
    }
}

