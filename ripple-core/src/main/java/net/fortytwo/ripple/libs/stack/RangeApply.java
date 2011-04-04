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
import net.fortytwo.ripple.model.*;
import net.fortytwo.ripple.model.regex.TimesQuantifier;


/**
 * A primitive which activates ("applies") the topmost item on the stack one or
 * more times.
 */
public class RangeApply extends PrimitiveStackMapping
{
	// TODO: arity should really be 2
	private static final int ARITY = 3;

    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "rangeApply"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public RangeApply() throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "p", null, true ),
                new Parameter( "min", null, true ),
                new Parameter( "max", null, true )};
    }

    public String getComment()
    {
        return "p min max  =>  ... p{min, max}!  -- pushes between min (inclusive) and max (inclusive) active copies of the program p, or 'executes p min times to max times'";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		final int min, max;

		max = mc.toNumericValue( stack.getFirst() ).intValue();
		stack = stack.getRest();
		min = mc.toNumericValue( stack.getFirst() ).intValue();
		stack = stack.getRest();
		RippleValue p = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<Operator, RippleException> opSink = new Sink<Operator, RippleException>()
		{
			public void put( final Operator op ) throws RippleException
			{
				solutions.put( arg.with( rest.push(
						new Operator( new TimesQuantifier( op, min, max ) ) ) ) );
			}
		};

		Operator.createOperator( p, opSink, mc );
	}
}
