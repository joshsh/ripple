/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.RippleException;

public class Closure implements StackMapping
{
	private StackMapping innerRelation;
	private RippleValue argument;
	private int cachedArity;

	public Closure( final StackMapping innerRelation, final RippleValue argument )
	{
		this.innerRelation = innerRelation;
		this.argument = argument;
		cachedArity = innerRelation.arity() - 1;
	}

	public int arity()
	{
		return cachedArity;
	}

	public void applyTo( final StackContext arg,
						final Sink<StackContext, RippleException> sink )
		throws RippleException
	{
        innerRelation.applyTo( arg.with( arg.getStack().push( argument ) ), sink );
	}
	
	public boolean isTransparent()
	{
		return innerRelation.isTransparent();
	}
	
	public String toString()
	{
		return "Closure(" + innerRelation + ", " + argument + ")";
	}

    // TODO: calculate the actual inverse mapping
    public StackMapping inverse() throws RippleException
    {
        return new NullStackMapping();
    }
}

