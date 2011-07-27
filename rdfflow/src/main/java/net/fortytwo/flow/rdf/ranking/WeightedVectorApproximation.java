package net.fortytwo.flow.rdf.ranking;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface WeightedVectorApproximation<T, E extends Exception> extends Approximation<WeightedVector<T>, E> {
    WeightedVector<T> currentResult();
}
