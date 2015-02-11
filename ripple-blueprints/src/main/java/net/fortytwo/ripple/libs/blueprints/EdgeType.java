package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.Edge;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.types.KeyValueType;
import org.openrdf.model.Value;

import java.util.Collection;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class EdgeType extends KeyValueType<Edge> {

    public EdgeType() {
        super(Edge.class);
    }

    @Override
    public Collection<String> getKeys(Edge instance) {
        return instance.getPropertyKeys();
    }

    @Override
    public Object getValue(Edge instance, String key, ModelConnection mc) throws RippleException {
        Object result = instance.getProperty(key);
        return null == result ? null : BlueprintsLibrary.toRipple(result, mc);
    }

    @Override
    public int compare(Edge o1, Edge o2, ModelConnection mc) {
        String otherLabel = o2.getLabel();
        int cmp = o1.getLabel().compareTo(otherLabel);
        if (0 == cmp) {
            return ((Integer) o1.getId().hashCode()).compareTo(o2.hashCode());
        } else {
            return cmp;
        }
    }

    @Override
    public boolean isInstance(Edge instance) {
        return false;
    }

    @Override
    public Value toRDF(Edge instance, ModelConnection mc) throws RippleException {
        return null;
    }

    @Override
    public StackMapping getMapping(Edge instance) {
        return null;
    }

    @Override
    public void print(Edge instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        p.print("[edge " + instance.getId() + "]");
    }
}
