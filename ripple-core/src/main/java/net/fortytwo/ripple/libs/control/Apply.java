package net.fortytwo.ripple.libs.control;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which activates ("applies") the topmost item on the stack.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Apply extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                ControlLibrary.NS_2011_08 + "apply",
                StackLibrary.NS_2008_08 + "apply",
                StackLibrary.NS_2007_08 + "i",
                StackLibrary.NS_2007_05 + "i"};
    }

    public Apply() throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("p", "the program to be executed", true)};
    }

    public String getComment() {
        return "p  => p!  -- push an active copy of p, or 'execute p'";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

// hack...
        solutions.put(arg.push(Operator.OP));
    }
}

