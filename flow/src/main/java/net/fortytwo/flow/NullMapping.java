/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

/**
 * Author: josh
 * Date: Apr 2, 2008
 * Time: 3:43:54 PM
 */
public class NullMapping<D, R> implements Mapping<D, R>
{
    public boolean isTransparent()
    {
        return true;
    }

    public void apply( final D arg, final Sink<R> solutions ) throws RippleException
    {
        // Do nothing
    }
}
