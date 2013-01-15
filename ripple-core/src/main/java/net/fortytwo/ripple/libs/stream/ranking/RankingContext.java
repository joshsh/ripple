package net.fortytwo.ripple.libs.stream.ranking;

import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
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
