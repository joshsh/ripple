package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleValue;

/**
 * User: josh
 * Date: 4/5/11
 * Time: 8:09 PM
 */
public class BlueprintsLibrary extends Library {
    public static final String
            NS_2011_04 = "http://fortytwo.net/2011/04/ripple/blueprints#";

    @Override
    public void load(final URIMap uf,
                     final LibraryLoader.Context context) throws RippleException {
        // TODO: no such file
        uf.put(NS_2011_04, getClass().getResource("blueprints.ttl") + "#");

        registerPrimitives(context,
                Tail.class,
                Id.class,
                Label.class,
                Head.class,
                Gremlin.class);
    }

    public static RippleValue createRippleValue(final Object tinker,
                                                final ModelConnection mc) throws RippleException {
        if (tinker instanceof Vertex) {
            return new VertexValue((Vertex) tinker);
        } else if (tinker instanceof Edge) {
            return new EdgeValue((Edge) tinker);
        } else if (tinker instanceof String) {
            return mc.value((String) tinker);
        } else if (tinker instanceof Double) {
            return mc.value((Double) tinker);
        } else if (tinker instanceof Integer) {
            return mc.value((Integer) tinker);
        } else if (tinker instanceof Long) {
            return mc.value((Long) tinker);
        } else if (tinker instanceof Boolean) {
            return mc.value((Boolean) tinker);
        } else if (tinker instanceof Float) {
            // Cheat and call it a double.
            return mc.value(Double.valueOf((Float) tinker));
        } else {
            return mc.value("[" + tinker.toString() + " (" + tinker.getClass() + ")]");
        }
    }
}
