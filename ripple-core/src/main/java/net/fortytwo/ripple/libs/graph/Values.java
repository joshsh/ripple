package net.fortytwo.ripple.libs.graph;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleType;
import net.fortytwo.ripple.model.types.KeyValueType;

import java.util.Collection;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Values extends PrimitiveStackMapping {
    @Override
    public String[] getIdentifiers() {
        return new String[]{
                GraphLibrary.NS_2013_03 + "values"
        };
    }

    @Override
    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("obj", "a key/value object", true)};
    }

    @Override
    public String getComment() {
        return "finds all values of key/value pairs attached to a given object";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;
        Object first = stack.getFirst();
        stack = stack.getRest();

        RippleType t = mc.getModel().getTypeOf(first);
        if (t instanceof KeyValueType) {
            Collection<String> keys = ((KeyValueType) t).getKeys(first);
            for (String key : keys) {
                Object value = ((KeyValueType) t).getValue(first, key, mc);
                solutions.put(stack.push(value));
            }
        }
    }
}
