package net.fortytwo.ripple.libs.extras.ranking;

import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;

/**
 * User: josh
 * Date: 4/4/11
 * Time: 9:24 AM
 */
public class RankingContext extends StackContext {
    private double priority = 1.0;

    public RankingContext(final RippleList stack,
                          final ModelConnection mc) {
        super(stack, mc);
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "(" + priority + ": " + stack + ")";
    }
}
