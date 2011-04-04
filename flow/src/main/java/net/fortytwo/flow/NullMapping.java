/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow;

/**
 * Author: josh
 * Date: Apr 2, 2008
 * Time: 3:43:54 PM
 */
public class NullMapping<D, R, E extends Exception> implements Mapping<D, R, E>
{
    public boolean isTransparent()
    {
        return true;
    }

    public void apply( final D arg, final Sink<R, E> solutions ) throws E
    {
        // Do nothing
    }
}
