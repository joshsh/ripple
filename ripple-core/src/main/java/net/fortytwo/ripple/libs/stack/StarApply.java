/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.regex.StarQuantifier;


/**
 * A primitive which optionally activates ("applies") the topmost item on the
 * stack.
 */
public class StarApply extends PrimitiveStackMapping
{
	// TODO: arity should really be 1, but this is a nice temporary solution
    @Override
    public int arity()
    {
        return 2;
    }

    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "starApply"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public StarApply() throws RippleException
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
        return "p  => p*  -- optionally execute the program p any number of times";
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
						new Operator( new StarQuantifier( op ) ) ) ) );
			}
		};

		Operator.createOperator( first, opSink, arg.getModelConnection() );
	}
}
