package net.fortytwo.flow.rdf.ranking;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ApproxUnion<T, E extends Exception> extends ApproxFairOperation<T, E> {
    public ApproxUnion(final WeightedVectorApproximation<T, E>... operands) {
        super(operands);
    }

    public WeightedVector<T> currentResult() {
        WeightedVector<T> cur = new WeightedVector<>();

        for (WeightedVectorApproximation<T, E> o : operands) {
            cur = cur.add(o.currentResult());
        }

        return cur;
    }
}
