package net.fortytwo.flow.rdf.ranking;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class Pipe<T, S, E extends Exception> implements Handler<T, E> {
    protected final Handler<S, E> innerHandler;

    public Pipe(final Handler<S, E> innerHandler) {
        this.innerHandler = innerHandler;
    }
}
