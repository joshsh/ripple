package net.fortytwo.ripple.libs.extras.ranking;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.query.StackEvaluator;

/**
 * User: josh
 * Date: 4/4/11
 * Time: 9:19 AM
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

    public void apply(final StackContext arg,
                      final Sink<StackContext, RippleException> solutions) throws RippleException {
        RankingEvaluatorHelper h = new RankingEvaluatorHelper(arg);
        ModelConnection mc = arg.getModelConnection();

        for (int i = 0; i < steps; i++) {
            if (!stopped) {
                if (!h.reduceOnce()) {
                    break;
                }
            }
        }

        for (RankingContext c : h.getResults()) {
            //System.out.println("solution: " + c.getStack());
            solutions.put(arg.with(c.getStack().push(mc.value(c.getPriority()))));
        }
    }
}
