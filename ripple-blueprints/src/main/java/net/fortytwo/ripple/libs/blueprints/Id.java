package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.Element;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Id extends PrimitiveStackMapping {
    @Override
    public String[] getIdentifiers() {
        return new String[]{
                BlueprintsLibrary.NS_2013_03 + "id"
        };
    }

    @Override
    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("el", "a Blueprints element (vertex or edge)", true)};
    }

    @Override
    public String getComment() {
        return "finds the id of a Blueprints element (vertex or edge)";
    }

    @Override
    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        RippleList stack = arg;
        Object first = stack.getFirst();
        stack = stack.getRest();

        if (first instanceof Element) {
            Element el = (Element) first;
            solutions.put(stack.push(BlueprintsLibrary.toRipple(el.getId(), mc)));
        }
    }
}
