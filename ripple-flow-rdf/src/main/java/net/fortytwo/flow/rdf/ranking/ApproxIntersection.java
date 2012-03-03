package net.fortytwo.flow.rdf.ranking;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ApproxIntersection<T, E extends Exception> extends ApproxFairOperation<T, E> {
    public ApproxIntersection(final WeightedVectorApproximation<T, E>... operands) {
        super(operands);
    }

    public WeightedVector<T> currentResult() {
        if (0 == operands.length) {
            return new WeightedVector<T>();
        } else {
            WeightedVector<T> cur = operands[0].currentResult();

            for (int i = 1; i < operands.length; i++) {
                cur = cur.multiplyByTransposeOf(operands[i].currentResult());
            }

            return cur;
        }
    }
}