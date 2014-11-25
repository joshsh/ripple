package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;
import org.json.JSONArray;
import org.json.JSONException;
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
                           final ModelConnection mc) throws RippleException {
        return toNative(instance.opt(key), mc);
    }

    @Override
    public int compare(JSONObject o1, JSONObject o2, ModelConnection mc) {
        return o1.toString().compareTo(o2.toString());
    }

    public static RippleList toList(final JSONArray array, final ModelConnection mc) throws RippleException {
        int n = array.length();
        RippleList cur = mc.list();
        for (int i = n - 1; i >= 0; i--) {
            try {
                cur = cur.push(toNative(array.get(i), mc));
            } catch (JSONException e) {
                throw new RippleException(e);
            }
        }
        return cur;
    }

    public static Object toNative(final Object v, final ModelConnection mc) throws RippleException {
        return v instanceof JSONArray ? toList((JSONArray) v, mc) : v;
    }
}
