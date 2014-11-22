package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.Vertex;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.keyval.KeyValueValue;

import java.util.Collection;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class VertexValue extends ElementValue {
    private final Vertex vertex;

    public VertexValue(final Vertex vertex) {
        this.vertex = vertex;
    }

    public Vertex getElement() {
        return vertex;
    }

    public RDFValue toRDF(final ModelConnection mc) throws RippleException {
        return null;
    }

    public StackMapping getMapping() {
        return null;
    }

    public void printTo(final RipplePrintStream p, final ModelConnection mc) throws RippleException {
        p.print("[vertex " + vertex.getId() + "]");
    }

    public Object getValue(final String key,
                                final ModelConnection mc) throws RippleException {
        Object result = vertex.getProperty(key);
        return null == result ? null : BlueprintsLibrary.createRippleValue(result, mc);
    }

    public Collection<String> getKeys() {
        return vertex.getPropertyKeys();
    }

    public int compareTo(final KeyValueValue other) {
        if (other instanceof VertexValue) {
            return ((Integer) vertex.getId().hashCode()).compareTo(((VertexValue) other).vertex.hashCode());
        } else {
            return this.getClass().getName().compareTo(other.getClass().getName());
        }
    }
}
