package net.fortytwo.ripple.libs.control;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMappingWrapper;
import net.fortytwo.ripple.model.regex.TimesQuantifier;

/**
 * A primitive which consumes an item and a number n, then pushes n active
 * copies of the item to the stack.  This has the effect of applying the
 * filter "n times" to the remainder of the stack.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class TimesApply extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                // Note: this primitive has different semantics than its predecessors, stack:times and stack:timesApply
                ControlLibrary.NS_2013_03 + "times-apply"};
    }

    public TimesApply() throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("p", "the program the be executed", true),
                new Parameter("n", "the number of times to execute p", true)};
    }

    public String getComment() {
        return "p n  =>  p{n}  -- pushes n active copies of the program p, or 'executes p n times'";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;

        final int times;

        times = mc.toNumber(stack.getFirst()).intValue();
        stack = stack.getRest();
        Object p = stack.getFirst();
        final RippleList rest = stack.getRest();

        Sink<Operator> opSink = new Sink<Operator>() {
            public void accept(final Operator op) throws RippleException {
                solutions.accept(rest.push(
                        new StackMappingWrapper(new TimesQuantifier(op, times, times), mc)));
            }
        };

        Operator.createOperator(p, opSink, mc);
    }
}

