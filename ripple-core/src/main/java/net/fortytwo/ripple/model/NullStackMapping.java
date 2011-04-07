/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;

/**
 *  A filter which yields no results, regardless of its inputs.
 */
public class NullStackMapping implements StackMapping
{
	public int arity()
	{
		return 0;
	}

	public void apply( final StackContext arg,
					   final Sink<StackContext, RippleException> sink )
		throws RippleException
	{
        // Do nothing.
    }
	
	public boolean isTransparent()
	{
		return true;
	}

    // The null filter is considered to be its own inverse
    public StackMapping getInverse() throws RippleException
    {
        return this;
    }

    @Override
    public String toString() {
        return "[NullStackMapping]";
    }
}

