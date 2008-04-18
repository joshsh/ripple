/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.flow;

/**
 * Author: josh
 * Date: Mar 7, 2008
 * Time: 3:07:52 PM
 */
public class DiffBuffer<T, E extends Exception> extends Diff<T, E>
{
    private DiffSink<T, E> sink;

    public DiffBuffer( final DiffSink<T, E> sink )
    {
        this.sink = sink;
    }

                        // throws E
    public void flush() throws Exception
    {
        writeTo(sink);

        clear();
    }
}
