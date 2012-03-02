/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.model.regex;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * Author: josh
 * Date: Feb 14, 2008
 * Time: 4:30:14 PM
 */
public class OptionalQuantifier implements StackMapping
{
	private final Operator innerOperator;

	public OptionalQuantifier( final Operator oper )
	{
		innerOperator = oper;
	}

	public int arity()
	{
		// TODO
		return 1;
	}

	public boolean isTransparent()
	{
		return innerOperator.getMapping().isTransparent();
	}

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		solutions.put( arg );

		solutions.put( arg
				.push( innerOperator ) );
	}

    public StackMapping getInverse() throws RippleException
    {
        return new NullStackMapping();
    }
}
