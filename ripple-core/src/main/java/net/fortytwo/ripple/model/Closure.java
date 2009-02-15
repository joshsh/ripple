/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.RippleException;

public class Closure implements StackMapping
{
	private final StackMapping innerRelation;
	private final RippleValue argument;
	private final int cachedArity;

    /**
     *
     * @param innerRelation a mapping
     * @param argument an inactive value
     */
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

	public void apply( final StackContext arg,
						final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
        innerRelation.apply( arg.with( arg.getStack().push( argument ) ), solutions );
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

