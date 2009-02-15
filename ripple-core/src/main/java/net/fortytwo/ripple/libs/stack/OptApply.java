/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.regex.OptionalQuantifier;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.flow.Sink;


/**
 * A primitive which activates ("applies") the topmost item on the stack any
 * number of times.
 */
public class OptApply extends PrimitiveStackMapping
{
// Arguably 0...
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "optApply"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public OptApply() throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		RippleValue first = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<Operator, RippleException> opSink = new Sink<Operator, RippleException>()
		{
			public void put( final Operator op ) throws RippleException
			{
				solutions.put( arg.with( rest.push(
						new Operator( new OptionalQuantifier( op ) ) ) ) );
			}
		};

		Operator.createOperator( first, opSink, arg.getModelConnection() );
	}
}
