package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

/**
 * A data transformer which takes individual data items of one type (the "arguments"),
 * maps them to zero or more items of a second type (the "solutions") and
 * pushes the solutions into a downstream <code>Sink</code>
 *
 * @param <D> the argument data type
 * @param <R> the solution data type
 * @param <C> a helper type which provides "context" to the mapping
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface Mapping<D, R, C> {
    /**
     * @return whether this function is referentially transparent w.r.t. all of its
     * parameters.
     */
    boolean isTransparent();

    // Open-world, push-based

    /**
     * Applies the mapping to a single argument,
     * producing zero or more solutions and passing them into a downstream <code>Sink</code>
     *
     * @param arg       the argument data item
     * @param solutions the solutions the argument is mapped to
     * @param context   a helper object providing context (may be null)
     * @throws RippleException if a data handling error occurs
     */
    void apply(D arg, Sink<R> solutions, C context) throws RippleException;

    /*
    // Open-world, pull-based
    Iterator<R> apply( D arg ) throws E;

    // Closed-world, push-based
    void apply( Iterator<D> source, Sink<R, E> sink ) throws E;

    // Closed-world, pull-based
    Iterator<R> apply( Iterator<D> source ) throws E;
    

    public class Count<D> implements Mapping<D, Integer, RippleException>
    {
        public void apply( Collection<D> source, Sink<Integer, RippleException> sink ) throws RippleException
        {
            sink.put( new Integer( source.size() ) );
        }
    }*/

}
