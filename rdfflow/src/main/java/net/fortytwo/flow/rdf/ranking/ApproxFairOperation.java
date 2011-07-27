package net.fortytwo.flow.rdf.ranking;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class ApproxFairOperation<T, E extends Exception> implements WeightedVectorApproximation<T, E> {
    protected final WeightedVectorApproximation<T, E>[] operands;
    private int startIndex = 0;

    public ApproxFairOperation(final WeightedVectorApproximation<T, E>... operands) {
        this.operands = operands;
    }

    public int compute(final int cycles) throws E {
        if (0 == operands.length) {
            return 0;
        } else {
            int remainingCycles = cycles;
            int avgCycles = cycles / operands.length;
            for (int i = 1; i < operands.length; i++) {
                int consumed = operands[(startIndex + i) % operands.length]
                        .compute(avgCycles);
                remainingCycles -= consumed;
                if (consumed != avgCycles) {
                    avgCycles = remainingCycles / (operands.length - i);
                }
            }

            remainingCycles -= operands[startIndex].compute(remainingCycles);

            startIndex++;
            return cycles - remainingCycles;
        }
    }
}
