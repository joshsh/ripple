package net.fortytwo.ripple.libs.graph;

import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SPARQLValue {
    private final Map<String, Value> pairs;

    public SPARQLValue(BindingSet bindings) {
        pairs = new HashMap<>();
        for (String key : bindings.getBindingNames()) {
            Value value = bindings.getValue(key);
            if (null != value) {
                pairs.put(key, value);
            }
        }
    }

    public Map<String, Value> getPairs() {
        return pairs;
    }
}
