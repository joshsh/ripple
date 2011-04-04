/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow;

import java.util.Collection;

/**
 * Author: josh
 * Date: Mar 11, 2008
 * Time: 8:45:29 AM
 */
public class AddToCollectionSink<T, E extends Exception> implements Sink<T, E>
{
    private final Collection<T> collection;

    public AddToCollectionSink(final Collection<T> coll)
    {
        this.collection = coll;
    }

    public void put(final T t) throws E
    {
        this.collection.add(t);
    }
}
