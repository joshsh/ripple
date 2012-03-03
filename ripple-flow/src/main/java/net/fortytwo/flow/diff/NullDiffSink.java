package net.fortytwo.flow.diff;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.NullSink;

/**
 * Author: josh
 * Date: Jul 23, 2008
 * Time: 3:06:09 PM
 */
public class NullDiffSink<T> implements DiffSink<T>
{
    private final Sink<T> nullSink = new NullSink<T>();

    public Sink<T> getPlus()
    {
        return nullSink;
    }

    public Sink<T> getMinus()
    {
        return nullSink;
    }
}
