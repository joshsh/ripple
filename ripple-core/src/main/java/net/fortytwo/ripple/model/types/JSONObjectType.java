package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackMapping;
import org.json.JSONObject;
import org.openrdf.model.Value;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class JSONObjectType extends KeyValueType<JSONObject> {

    public JSONObjectType() {
        super(JSONObject.class);
    }

    @Override
    public boolean isInstance(JSONObject instance) {
        return true;
    }

    @Override
    public Value toRDF(JSONObject instance, ModelConnection mc) throws RippleException {
        return null;
    }

    @Override
    public StackMapping getMapping(JSONObject instance) {
        return null;
    }

    @Override
    public void print(JSONObject instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        p.print(instance.toString());
    }

    @Override
    public Collection<String> getKeys(final JSONObject instance) {
        List<String> keys = new LinkedList<>();
        Iterator i = instance.keys();
        while (i.hasNext()) {
            Object key = i.next();
            if (key instanceof String) {
                keys.add((String) key);
            }
        }

        return keys;
    }

    @Override
    public Object getValue(final JSONObject instance,
                           final String key,
                           final ModelConnection mc) {
        return instance.opt(key);
    }

    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        return o1.toString().compareTo(o2.toString());
    }
}
