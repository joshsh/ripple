package net.fortytwo.ripple.libs.control;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMappingWrapper;
import net.fortytwo.ripple.model.regex.StarQuantifier;


/**
 * A primitive which optionally activates ("applies") the topmost item on the
 * stack.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class StarApply extends PrimitiveStackMapping {
    // TODO: arity should really be 1, but this is a nice temporary solution
    @Override
    public int arity() {
        return 2;
    }

    public String[] getIdentifiers() {
        return new String[]{
                // Note: this primitive has different semantics than its predecessor, stack:starApply
                ControlLibrary.NS_2013_03 + "star-apply"};
    }

    public StarApply() {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("p", "the program to be executed", true)};
    }

    public String getComment() {
        return "p  => p*  -- optionally execute the program p any number of times";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        Object first = arg.getFirst();
        final RippleList rest = arg.getRest();

        Sink<Operator> opSink = op -> solutions.accept(rest.push(
                new StackMappingWrapper(new StarQuantifier(op), mc)));

        Operator.createOperator(first, opSink, mc);
    }
}
