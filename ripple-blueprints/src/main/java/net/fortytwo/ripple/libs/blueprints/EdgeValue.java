package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.Edge;
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
public class EdgeValue extends ElementValue {
    private final Edge edge;

    public EdgeValue(final Edge edge) {
        this.edge = edge;
    }

    public Edge getElement() {
        return edge;
    }

    public RDFValue toRDF(final ModelConnection mc) throws RippleException {
        return null;
    }

    public StackMapping getMapping() {
        return null;
    }

    public void printTo(final RipplePrintStream p, final ModelConnection mc) throws RippleException {
        p.print("[edge " + edge.getId() + "]");
    }

    public Object getValue(final String key,
                                final ModelConnection mc) throws RippleException {
        Object result = edge.getProperty(key);
        return null == result ? null : BlueprintsLibrary.createRippleValue(result, mc);
    }

    public Collection<String> getKeys() {
        return edge.getPropertyKeys();
    }

    public int compareTo(final KeyValueValue other) {
        if (other instanceof EdgeValue) {
            String otherLabel = ((EdgeValue) other).edge.getLabel();
            int cmp = this.edge.getLabel().compareTo(otherLabel);
            if (0 == cmp) {
                return ((Integer) edge.getId().hashCode()).compareTo(((EdgeValue) other).edge.hashCode());
            } else {
                return cmp;
            }
        } else {
            return this.getClass().getName().compareTo(other.getClass().getName());
        }
    }
}
