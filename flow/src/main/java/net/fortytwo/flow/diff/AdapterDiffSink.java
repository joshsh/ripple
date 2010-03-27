package net.fortytwo.flow.diff;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.AdapterSink;
import net.fortytwo.flow.diff.DiffSink;

/**
 * User: josh
 * Date: Mar 26, 2010
 * Time: 6:56:05 PM
 */
public class AdapterDiffSink<T, E extends Exception, F extends Exception> implements DiffSink<T, F> {
    private final Sink<T, F> plus;
    private final Sink<T, F> minus;

    public AdapterDiffSink(final DiffSink<T, E> baseSink,
                          final AdapterSink.ExceptionAdapter<F> exAdapter) {
        plus = new AdapterSink<T, E, F>(baseSink.getPlus(), exAdapter);
        minus = new AdapterSink<T, E, F>(baseSink.getMinus(), exAdapter);
    }

    public Sink<T, F> getPlus() {
        return plus;
    }

    public Sink<T, F> getMinus() {
        return minus;
    }
}
