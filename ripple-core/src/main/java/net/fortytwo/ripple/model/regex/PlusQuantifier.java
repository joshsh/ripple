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
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.NullStackMapping;
import net.fortytwo.ripple.model.regex.StarQuantifier;

/**
 * Author: josh
 * Date: Feb 14, 2008
 * Time: 4:31:15 PM
 */
public class PlusQuantifier implements StackMapping
{
	private final Operator innerOperator;

	public PlusQuantifier( final Operator oper )
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

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> sink ) throws RippleException
	{
		sink.put( arg.with( arg.getStack()
				.push( innerOperator )
				.push( new Operator( new StarQuantifier( innerOperator ) ) ) ) );
	}

    public StackMapping inverse() throws RippleException
    {
        return new NullStackMapping();
    }
}

