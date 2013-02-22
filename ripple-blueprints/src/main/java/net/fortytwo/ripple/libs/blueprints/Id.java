package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.Element;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * User: josh
 * Date: 4/5/11
 * Time: 8:11 PM
 */
public class Id extends PrimitiveStackMapping {
    @Override
    public String[] getIdentifiers() {
        return new String[]{
                BlueprintsLibrary.NS_2011_08 + "id"
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
        RippleValue first = stack.getFirst();
        stack = stack.getRest();

        if (first instanceof ElementValue) {
            Element el = ((ElementValue) first).getElement();
            solutions.put(stack.push(BlueprintsLibrary.createRippleValue(el.getId(), mc)));
        }
    }
}
