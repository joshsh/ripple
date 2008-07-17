/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which activates ("applies") the topmost item on the stack.
 */
public class Apply extends PrimitiveStackMapping
{
// Arguably 0...
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "apply",
            StackLibrary.NS_2007_08 + "i",
            StackLibrary.NS_2007_05 + "i"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Apply() throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions	)
		throws RippleException
	{
// hack...
		RippleList stack = arg.getStack();
		solutions.put( arg.with(
				stack.push( Operator.OP ) ) );
	}
}

