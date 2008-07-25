package net.fortytwo.ripple.flow.diff;

import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.flow.NullSink;

/**
 * Author: josh
 * Date: Jul 23, 2008
 * Time: 3:06:09 PM
 */
public class NullDiffSink<T, E extends Exception> implements DiffSink<T, E>
{
    private final Sink<T, E> nullSink = new NullSink<T, E>();

    public Sink<T, E> getPlus()
    {
        return nullSink;
    }

    public Sink<T, E> getMinus()
    {
        return nullSink;
    }
}
