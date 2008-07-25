package net.fortytwo.ripple.flow.diff;

import net.fortytwo.ripple.flow.SynchronizedSink;
import net.fortytwo.ripple.flow.Sink;

/**
 * Author: josh
 * Date: Jul 23, 2008
 * Time: 1:09:53 PM
 */
public class SynchronizedDiffSink<T, E extends Exception> implements DiffSink<T, E>
{
    private final SynchronizedSink<T, E> plusSink;
    private final SynchronizedSink<T, E> minusSink;

    public SynchronizedDiffSink( final DiffSink<T, E> baseSink, final Object mutex ) {
        plusSink = new SynchronizedSink<T, E>( baseSink.getPlus(), mutex );
        minusSink = new SynchronizedSink<T, E>( baseSink.getMinus(), mutex );
    }

    public Sink<T, E> getPlus()
    {
        return plusSink;
    }

    public Sink<T, E> getMinus()
    {
        return minusSink;
    }

    public Object getMutex()
    {
        return plusSink.getMutex();
    }
}
