package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.Vertex;
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
public class VertexType extends KeyValueType<Vertex> {

    public VertexType() {
        super(Vertex.class);
    }

    @Override
    public Collection<String> getKeys(Vertex instance) {
        return instance.getPropertyKeys();
    }

    @Override
    public Object getValue(Vertex instance, String key, ModelConnection mc) throws RippleException {
        Object result = instance.getProperty(key);
        return null == result ? null : BlueprintsLibrary.toRipple(result, mc);
    }

    @Override
    public int compare(Vertex o1, Vertex o2) {
        return ((Integer) o1.getId().hashCode()).compareTo(o2.getId().hashCode());
    }

    @Override
    public boolean isInstance(Vertex instance) {
        return false;
    }

    @Override
    public Value toRDF(Vertex instance, ModelConnection mc) throws RippleException {
        return null;
    }

    @Override
    public StackMapping getMapping(Vertex instance) {
        return null;
    }

    @Override
    public void print(Vertex instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        p.print("[vertex " + instance.getId() + "]");
    }
}
