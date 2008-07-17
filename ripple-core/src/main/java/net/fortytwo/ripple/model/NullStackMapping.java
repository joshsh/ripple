/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.flow.Mapping;
import net.fortytwo.ripple.flow.NullMapping;

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
    public StackMapping inverse() throws RippleException
    {
        return this;
    }

}

