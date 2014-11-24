package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackMapping;
import org.json.JSONArray;
import org.openrdf.model.Value;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class JSONArrayType extends SimpleType<JSONArray> {

    public JSONArrayType() {
        super(JSONArray.class);
    }

    @Override
    public boolean isInstance(JSONArray instance) {
        return true;
    }

    @Override
    public Value toRDF(JSONArray instance, ModelConnection mc) throws RippleException {
        return null;
    }

    @Override
    public StackMapping getMapping(JSONArray instance) {
        return null;
    }

    @Override
    public void print(JSONArray instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        p.print(instance.toString());
    }

    @Override
    public Category getCategory() {
        return Category.OTHER_RESOURCE;
    }
}
