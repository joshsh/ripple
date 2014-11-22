package net.fortytwo.ripple.libs.control;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.system.SystemLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackMappingWrapper;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Inverse extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                ControlLibrary.NS_2013_03 + "inverse",
                SystemLibrary.NS_2008_08 + "invert",
        };
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("mapping", null, true)};
    }

    public String getComment() {
        return "consumes a mapping and produces the inverse of that mapping" +
                " (or a null mapping if the inverse is not otherwise defined)";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;

        final RippleList rest = stack.getRest();
        Object f = stack.getFirst();

        Sink<Operator> opSink = new Sink<Operator>() {
            public void put(final Operator op) throws RippleException {
                Object inverse = new StackMappingWrapper(op.getMapping().getInverse(), mc);
                solutions.put(rest.push(inverse));
            }
        };

        Operator.createOperator(f, opSink, mc);
    }
}
