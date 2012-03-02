/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.control;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackMappingWrapper;
import net.fortytwo.ripple.model.regex.OptionalQuantifier;


/**
 * A primitive which activates ("applies") the topmost item on the stack any
 * number of times.
 */
public class OptionApply extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            // Note: this primitive different semantics than its predecessor, stack:optApply
            ControlLibrary.NS_2011_08 + "option-apply"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public OptionApply() throws RippleException
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
        return "p  =>  p?  -- optionally execute the program p";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

		RippleList stack = arg;
		RippleValue first = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<Operator> opSink = new Sink<Operator>()
		{
			public void put( final Operator op ) throws RippleException
			{
				solutions.put( rest.push(
						new StackMappingWrapper( new OptionalQuantifier( op ), mc ) ) );
			}
		};

		Operator.createOperator( first, opSink, mc );
	}
}
