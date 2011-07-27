package net.fortytwo.flow.rdf.ranking;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface Handler<T, E extends Exception> {
    boolean handle(T t) throws E;
}

