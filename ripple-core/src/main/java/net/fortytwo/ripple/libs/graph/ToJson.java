package net.fortytwo.ripple.libs.graph;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.keyval.JSONValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A primitive which consumes a literal value and produces the resource
 * identified by the corresponding URI (if any).
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ToJson extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2013_03 + "to-json"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public ToJson()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("s", "the string representation of a JSON object", true)};
    }

    public String getComment() {
        return "s  =>  json -- where json is the JSON object whose string representation is s";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;

        String s;

        s = mc.toString(stack.getFirst());
        stack = stack.getRest();

        Object json = toJson(s, mc);

        solutions.put(
                stack.push(json));
    }

    private Object toJson(final String s,
                               final ModelConnection mc) throws RippleException {
        String st = s.trim();
        try {
            return st.startsWith("[")
                    ? JSONValue.toRippleValue(new JSONArray(st), mc)
                    : JSONValue.toRippleValue(new JSONObject(st), mc);
        } catch (JSONException e) {
            throw new RippleException(e);
        }
    }
}
