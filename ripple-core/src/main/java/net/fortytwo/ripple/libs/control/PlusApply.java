package net.fortytwo.ripple.libs.control;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMappingWrapper;
import net.fortytwo.ripple.model.regex.PlusQuantifier;


/**
 * A primitive which activates ("applies") the topmost item on the stack one or
 * more times.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class PlusApply extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                // Note: this primitive has different semantics than its predecessor, stack:plusApply
                ControlLibrary.NS_2013_03 + "plus-apply"};
    }

    public PlusApply() {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("p", "the program to be executed", true)};
    }

    public String getComment() {
        return "p  =>  p+  -- execute the program p at least one time, and up to any number of times";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        Object first = arg.getFirst();
        final RippleList rest = arg.getRest();

        Sink<Operator> opSink = op -> solutions.accept(rest.push(
                new StackMappingWrapper(new PlusQuantifier(op), mc)));

        Operator.createOperator(first, opSink, mc);
    }
}
