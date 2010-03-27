package net.fortytwo.flow;

import net.fortytwo.flow.Sink;

/**
 * User: josh
 * Date: Mar 26, 2010
 * Time: 6:58:12 PM
 */
public class AdapterSink<T, E extends Exception, F extends Exception> implements Sink<T, F> {
    private final Sink<T, E> baseSink;
    private final ExceptionAdapter<F> exAdapter;

    public AdapterSink(final Sink<T, E> baseSink,
                      final ExceptionAdapter<F> exAdapter) {
        this.baseSink = baseSink;
        this.exAdapter = exAdapter;
    }

    public void put(final T t) throws F {
        try {
            baseSink.put(t);
        } catch (Exception e) {
            exAdapter.doThrow(e);
        }
    }

    public interface ExceptionAdapter<F extends Exception> {
        void doThrow(Exception e) throws F;
    }
}
