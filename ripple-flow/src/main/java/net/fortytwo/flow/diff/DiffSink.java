package net.fortytwo.flow.diff;

import net.fortytwo.flow.Sink;

/**
 * A pair of sinks, one for "added" data items and another for "removed" data items
 *
 * @author Joshua Shinavier (http://fortytwo.net)
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
