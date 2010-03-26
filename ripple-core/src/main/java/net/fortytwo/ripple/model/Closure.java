/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;

public class Closure implements StackMapping
{
	private final StackMapping innerMapping;
	private final RippleValue argument;
	private final int calculatedArity;

    /**
     *
     * @param innerMapping a mapping
     * @param argument an inactive value
     */
    public Closure( final StackMapping innerMapping, final RippleValue argument )
	{
		this.innerMapping = innerMapping;
		this.argument = argument;
		calculatedArity = innerMapping.arity() - 1;
	}

	public int arity()
	{
		return calculatedArity;
	}

	public void apply( final StackContext arg,
						final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
        innerMapping.apply( arg.with( arg.getStack().push( argument ) ), solutions );
	}
	
	public boolean isTransparent()
	{
		return innerMapping.isTransparent();
	}
	
	public String toString()
	{
		return "Closure(" + innerMapping + ", " + argument + ")";
	}

    public StackMapping inverse() throws RippleException
    {
        return new Closure( innerMapping.inverse(), argument );
    }
}

