/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.flow.diff;

import net.fortytwo.flow.Sink;

/**
 * A pair of sinks, one for "added" data items and another for "removed" data items
 */
public interface DiffSink<T>
{
    /**
     * @return the sink for "added" data items
     */
    Sink<T> getPlus();

    /**
     * @return the sink for "removed" data items
     */
    Sink<T> getMinus();
}
