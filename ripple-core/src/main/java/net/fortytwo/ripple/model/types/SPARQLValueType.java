package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.libs.graph.SPARQLValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackMapping;
import org.openrdf.model.Value;

import java.util.Collection;
import java.util.Comparator;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SPARQLValueType extends KeyValueType<SPARQLValue> implements Comparator<SPARQLValue> {

    public SPARQLValueType() {
        super(SPARQLValue.class);
    }

    @Override
    public boolean isInstance(SPARQLValue instance) {
        return true;
    }

    @Override
    public Value toRDF(SPARQLValue instance, ModelConnection mc) throws RippleException {
        return null;
    }

    @Override
    public StackMapping getMapping(SPARQLValue instance) {
        return null;
    }

    @Override
    public void print(SPARQLValue instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        p.print("{");
        boolean first = true;
        for (String key : instance.getPairs().keySet()) {
            if (first) {
                first = false;
            } else {
                p.print(", ");
            }

            p.print(key);
            p.print(":");
            mc.print(instance.getPairs().get(key), p);
        }
    }

    @Override
    public int compare(SPARQLValue o1, SPARQLValue o2, ModelConnection mc) {
        return o1.toString().compareTo(o2.toString());
    }

    @Override
    public Collection<String> getKeys(final SPARQLValue instance) {
        return instance.getPairs().keySet();
    }

    @Override
    public Object getValue(final SPARQLValue instance,
                           final String key,
                           final ModelConnection mc) {

        return mc.canonicalValue(instance.getPairs().get(key));
    }

    @Override
    public int compare(final SPARQLValue o1, final SPARQLValue o2) {
        // TODO
        return 0;
    }
}
