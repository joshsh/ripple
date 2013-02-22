package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngine;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.system.ScriptEngineWrapper;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleValue;

/**
 * User: josh
 * Date: 5/20/11
 * Time: 10:26 PM
 */
public class GremlinWrapper extends ScriptEngineWrapper {
    public GremlinWrapper() {
        super(new GremlinGroovyScriptEngine());
    }

    @Override
    protected RippleValue nativize(final Object externalObject,
                                   final ModelConnection mc) throws RippleException {
        return BlueprintsLibrary.createRippleValue(externalObject, mc);
    }
}
