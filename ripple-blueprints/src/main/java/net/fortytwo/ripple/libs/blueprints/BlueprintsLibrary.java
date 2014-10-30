package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngine;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.system.SystemLibrary;
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
            NS_2013_03 = "http://fortytwo.net/2013/03/ripple/blueprints#";

    @Override
    public void load(final LibraryLoader.Context context) throws RippleException {
        registerPrimitives(context,
                //Edit.class,
                Head.class,
                Id.class,
                Label.class,
                Tail.class);

        SystemLibrary.registerScriptEngine("gremlin", new GremlinWrapper());
    }

    public static RippleValue createRippleValue(final Object tinker,
                                                final ModelConnection mc) throws RippleException {
        if (tinker instanceof Vertex) {
            return new VertexValue((Vertex) tinker);
        } else if (tinker instanceof Edge) {
            return new EdgeValue((Edge) tinker);
        } else if (tinker instanceof String) {
            return mc.valueOf((String) tinker);
        } else if (tinker instanceof Double) {
            return mc.valueOf((Double) tinker);
        } else if (tinker instanceof Integer) {
            return mc.valueOf((Integer) tinker);
        } else if (tinker instanceof Long) {
            return mc.valueOf((Long) tinker);
        } else if (tinker instanceof Boolean) {
            return mc.valueOf((Boolean) tinker);
        } else if (tinker instanceof Float) {
            // Cheat and call it a double.
            return mc.valueOf(Double.valueOf((Float) tinker));
        } else {
            return mc.valueOf("[" + tinker.toString() + " (" + tinker.getClass() + ")]");
        }
    }
}
