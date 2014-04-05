package net.fortytwo.ripple.libs.graph;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.keyval.KeyValueValue;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Keys extends PrimitiveStackMapping {
    @Override
    public String[] getIdentifiers() {
        return new String[]{
                GraphLibrary.NS_2013_03 + "keys"
        };
    }

    @Override
    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("obj", "a key/value object", true)};
    }

    @Override
    public String getComment() {
        return "finds all keys with values on the given object";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;
        RippleValue first = stack.getFirst();
        stack = stack.getRest();

        if (first instanceof KeyValueValue) {
            for (String key : ((KeyValueValue) first).getKeys()) {
                solutions.put(stack.push(mc.valueOf(key)));
            }
        }
    }
}
