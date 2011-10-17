package net.fortytwo.ripple.libs.stream.ranking;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stream.StreamLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;

/**
 * User: josh
 * Date: 4/4/11
 * Time: 9:19 AM
 */
public class Amp extends PrimitiveStackMapping {
    @Override
    public String[] getIdentifiers() {
        return new String[]{
                StreamLibrary.NS_2011_08 + "amp"
        };
    }

    @Override
    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("factor", "the real-valued factor by which to multiply the weight of the current path", true)};
    }

    @Override
    public String getComment() {
        return "amplifies or attenuates the weight (strength or priority) of a given path by a real-valued factor.  Use in conjunction with etc:rank.";
    }

    public void apply(final StackContext arg,
                      final Sink<StackContext> solutions) throws RippleException {
        final ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();
        double factor = mc.toNumericValue(stack.getFirst()).doubleValue();
        if (arg instanceof RankingContext) {
            ((RankingContext) arg).setPriority(((RankingContext) arg).getPriority() * factor);
        }

        solutions.put(arg.with(stack.getRest()));
    }
}
