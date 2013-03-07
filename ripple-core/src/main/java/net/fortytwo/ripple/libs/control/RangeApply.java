package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.*;
import net.fortytwo.ripple.model.regex.TimesQuantifier;


/**
 * A primitive which activates ("applies") the topmost item on the stack one or
 * more times.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RangeApply extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                // Note: this primitive has different semantics than its predecessor, stack:rangeApply
                ControlLibrary.NS_2013_03 + "range-apply"};
    }

    public RangeApply() throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("p", null, true),
                new Parameter("min", null, true),
                new Parameter("max", null, true)};
    }

    public String getComment() {
        return "p min max  =>  ... p{min, max}!  -- pushes between min (inclusive) and max (inclusive) active copies of the program p, or 'executes p min times to max times'";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;

        final int min, max;

        max = mc.toNumericValue(stack.getFirst()).intValue();
        stack = stack.getRest();
        min = mc.toNumericValue(stack.getFirst()).intValue();
        stack = stack.getRest();
        RippleValue p = stack.getFirst();
        final RippleList rest = stack.getRest();

        Sink<Operator> opSink = new Sink<Operator>() {
            public void put(final Operator op) throws RippleException {
                solutions.put(rest.push(
                        new StackMappingWrapper(new TimesQuantifier(op, min, max), mc)));
            }
        };

        Operator.createOperator(p, opSink, mc);
    }
}
