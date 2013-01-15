package net.fortytwo.flow.diff;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.NullSink;

/**
 * A trivial diff sink which simply throws away all of its input
 *
 * @author Joshua Shinavier (http://fortytwo.net)
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
