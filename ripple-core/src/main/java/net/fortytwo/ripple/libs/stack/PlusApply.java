/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
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
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "plusApply"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public PlusApply() throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "p", "the program to be executed", true )};
    }

    public String getComment()
    {
        return "p  =>  p+  -- execute the program p at least one time, and up to any number of times";
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
						new Operator( new PlusQuantifier( op ) ) ) ) );
			}
		};

		Operator.createOperator( first, opSink, arg.getModelConnection() );
	}
}
