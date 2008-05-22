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
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;

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
            LogicLibrary.NS_2008_06 + "ifte",
            StackLibrary.NS_2007_08 + "ifte",
            StackLibrary.NS_2007_05 + "ifte"};

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
						 final Sink<StackContext, RippleException> sink
	)
		throws RippleException
	{
		RippleValue b, trueProg, falseProg;
		RippleList stack = arg.getStack();

		falseProg = stack.getFirst();
		stack = stack.getRest();
		trueProg = stack.getFirst();
		stack = stack.getRest();
		b = stack.getFirst();
		stack = stack.getRest();

		sink.put( arg.with(	stack.push( b )
				.push( Operator.OP )
				.push( trueProg )
				.push( falseProg )
				.push( LogicLibrary.getBranchValue() )
				.push( Operator.OP ) ) );
	}
}

