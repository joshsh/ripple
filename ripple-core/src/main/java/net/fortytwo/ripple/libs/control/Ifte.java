/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.libs.logic.LogicLibrary;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.ModelConnection;

/**
 * A primitive which consumes a Boolean filter b, a filter t, and a filter t,
 * then applies an active copy of b to the stack.  If b yields a value of
 * true, then t is applied the rest of the stack.  Otherwise, f is applied to
 * the rest of the stack.
 */
public class Ifte extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            ControlLibrary.NS_2011_04 + "ifte",
            // Note: the previous implementation of ifte had different semantics
            // (rather than the current, Joy semantics).
            };

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }
    
    public Ifte()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "b", "a boolean mapping", true ),
                new Parameter( "ifPart", "this program is executed if the condition is true", true ),
                new Parameter( "elsePart", "this program is executed if the condition is false", true )};
    }

    public String getComment()
    {
        return "b t f =>  p!  -- where p is t if the application of b evaluates to true, and f if it evaluates to false";
    }

	public void apply( final StackContext arg,
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

        public StackMapping getInverse() throws RippleException
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
            RippleValue b = arg.getStack().getFirst();

            RippleList stack = mc.toBoolean( b )
                    ? originalStack.push( trueProgram ).push( Operator.OP )
                    : originalStack.push( falseProgram ).push( Operator.OP );

            solutions.put( arg.with( stack ) );
        }
    }
}

