package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.jsr223.GremlinScriptEngine;
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
            NS_2011_08 = "http://fortytwo.net/2011/08/ripple/blueprints#";

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
            return mc.plainValue((String) tinker);
        } else if (tinker instanceof Double) {
            return mc.numericValue((Double) tinker);
        } else if (tinker instanceof Integer) {
            return mc.numericValue((Integer) tinker);
        } else if (tinker instanceof Long) {
            return mc.numericValue((Long) tinker);
        } else if (tinker instanceof Boolean) {
            return mc.booleanValue((Boolean) tinker);
        } else if (tinker instanceof Float) {
            // Cheat and call it a double.
            return mc.numericValue(Double.valueOf((Float) tinker));
        } else {
            return mc.plainValue("[" + tinker.toString() + " (" + tinker.getClass() + ")]");
        }
    }

    public static void main(final String[] args) throws Exception {
        GremlinScriptEngine engine = new GremlinScriptEngine();
        Object result = engine.eval("g = TinkerGraphFactory.createTinkerGraph()\ng.v(1)");
        //result = engine.eval("g.v(1)");
        System.out.println("" + result.getClass() + ": " + result);
    }
}
