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
import net.fortytwo.ripple.model.regex.PlusQuantifier;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;


/**
 * A primitive which activates ("applies") the topmost item on the stack one or
 * more times.
 */
public class PlusApply extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            // FIXME: this is a kludge for programs created by Ripple 0.5-dev.  Remove this alias when it is no longer needed
            StackLibrary.NS_2007_08 + "plusApply",

            StackLibrary.NS_2008_06 + "plusApply"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public PlusApply() throws RippleException
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
		RippleList stack = arg.getStack();
		RippleValue first = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<Operator, RippleException> opSink = new Sink<Operator, RippleException>()
		{
			public void put( final Operator op ) throws RippleException
			{
				sink.put( arg.with( rest.push(
						new Operator( new PlusQuantifier( op ) ) ) ) );
			}
		};

		Operator.createOperator( first, opSink, arg.getModelConnection() );
	}
}
