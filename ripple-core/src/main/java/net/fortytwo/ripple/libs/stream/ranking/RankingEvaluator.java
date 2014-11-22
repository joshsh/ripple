package net.fortytwo.ripple.libs.stream.ranking;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.StackEvaluator;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RankingEvaluator extends StackEvaluator {
    private final int steps;
    private boolean stopped = false;

    public RankingEvaluator(final int steps) {
        this.steps = steps;
    }

    @Override
    public void stop() {
        stopped = true;
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RankingEvaluatorHelper h = new RankingEvaluatorHelper(arg.push(Operator.OP), mc);

        for (int i = 0; i < steps; i++) {
            if (!stopped) {
                if (!h.reduceOnce(mc)) {
                    break;
                }
            }
        }

        for (RankingContext c : h.getResults()) {
            //System.out.println("solution: " + c.getStack());
            solutions.put(c.getStack().getRest().push(c.getPriority()));
        }
    }
}
