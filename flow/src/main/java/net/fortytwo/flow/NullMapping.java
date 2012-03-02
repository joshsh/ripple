/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

/**
 * Author: josh
 * Date: Apr 2, 2008
 * Time: 3:43:54 PM
 */
public class NullMapping<D, R, C> implements Mapping<D, R, C>
{
    public boolean isTransparent()
    {
        return true;
    }

    public void apply( final D arg, final Sink<R> solutions, final C context ) throws RippleException
    {
        // Do nothing
    }
}
