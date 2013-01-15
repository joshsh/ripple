package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.keyval.KeyValueValue;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SPARQLValue extends KeyValueValue {
    private final Map<String, Value> pairs;

    public SPARQLValue(BindingSet bindings) {
        pairs = new HashMap<String, Value>();
        for (String key : bindings.getBindingNames()) {
            Value value = bindings.getValue(key);
            if (null != value) {
                pairs.put(key, value);
            }
        }
    }

    @Override
    public RippleValue getValue(final String key,
                                final ModelConnection mc) throws RippleException {
        return mc.canonicalValue(new RDFValue(pairs.get(key)));
    }

    @Override
    public Collection<String> getKeys() {
        return pairs.keySet();
    }

    public int compareTo(final KeyValueValue other) {
        if (other instanceof SPARQLValue) {
            return 0;
        } else {
            return this.getClass().getName().compareTo(other.getClass().getName());
        }
    }

    public RDFValue toRDF(final ModelConnection mc) throws RippleException {
        return null;
    }

    public StackMapping getMapping() {
        return null;
    }

    public void printTo(final RipplePrintStream p) throws RippleException {
        p.print("{");
        boolean first = true;
        for (String key : pairs.keySet()) {
            if (first) {
                first = false;
            } else {
                p.print(", ");
            }

            p.print(key);
            p.print(":");
            p.print(new RDFValue(pairs.get(key)));
        }
    }
}
