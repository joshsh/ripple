package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.Edge;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Label extends PrimitiveStackMapping {
    @Override
    public String[] getIdentifiers() {
        return new String[]{
                BlueprintsLibrary.NS_2013_03 + "label"
        };
    }

    @Override
    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("edge", "a Blueprints edge", true)};
    }

    @Override
    public String getComment() {
        return "finds the label of a Blueprints edge";
    }

    @Override
    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;
        Object first = stack.getFirst();
        stack = stack.getRest();

        if (first instanceof Edge) {
            Edge el = (Edge) first;
            solutions.accept(stack.push(el.getLabel()));
        }
    }
}
