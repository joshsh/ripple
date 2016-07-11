package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngine;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.system.ScriptEngineWrapper;
import net.fortytwo.ripple.model.ModelConnection;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class GremlinWrapper extends ScriptEngineWrapper {
    public GremlinWrapper() {
        super(new GremlinGroovyScriptEngine());
    }

    @Override
    protected Object nativize(final Object externalObject,
                              final ModelConnection mc) throws RippleException {
        return BlueprintsLibrary.toRipple(externalObject);
    }
}
