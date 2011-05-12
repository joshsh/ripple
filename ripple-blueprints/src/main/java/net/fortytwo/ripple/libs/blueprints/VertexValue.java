package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.pgm.Vertex;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.keyval.KeyValueValue;

import java.util.Collection;

/**
 * User: josh
 * Date: 4/5/11
 * Time: 9:06 PM
 */
public class VertexValue extends ElementValue {
    private final Vertex vertex;

    public VertexValue(final Vertex vertex) {
        this.vertex = vertex;
    }

    public Vertex getElement() {
        return vertex;
    }

    @Override
    public RDFValue toRDF(final ModelConnection mc) throws RippleException {
        // FIXME: add a data type
        return mc.plainValue("[vertex " + vertex.getId() + "]");
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void printTo(final RipplePrintStream p) throws RippleException {
        p.print("[vertex " + vertex.getId() + "]");
    }

    @Override
    public RippleValue getValue(final String key,
                                final ModelConnection mc) throws RippleException {
        Object result = vertex.getProperty(key);
        return null == result ? null : BlueprintsLibrary.createRippleValue(result, mc);
    }

    @Override
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
