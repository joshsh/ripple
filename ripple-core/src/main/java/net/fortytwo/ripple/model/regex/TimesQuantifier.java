/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.model.regex;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.NullStackMapping;

/**
 * Author: josh
 * Date: Jan 15, 2008
 * Time: 9:24:59 PM
 */
public class TimesQuantifier implements StackMapping
{
	private final Operator innerOperator;
	private final int min, max;

	public TimesQuantifier( final Operator oper, final int min, final int max )
	{
		this.innerOperator = oper;
		this.min = min;
		this.max = max;
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

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> sink ) throws RippleException
	{
		RippleList stack = arg.getStack();

		if ( 0 == min )
		{
			sink.put( arg );
		}

		if ( max > 0 )
		{
			if ( 1 == max )
			{
				sink.put( arg.with( stack.push( innerOperator ) ) );
			}

			else
			{
				int newMin = ( 0 == min) ? 0 : min - 1, newMax = max - 1;

				sink.put( arg.with( stack
						.push( innerOperator )
						.push(new Operator( new TimesQuantifier( innerOperator, newMin, newMax ) ) ) ) );
			}
		}
	}

    public StackMapping inverse() throws RippleException
    {
        return new NullStackMapping();
    }
}
