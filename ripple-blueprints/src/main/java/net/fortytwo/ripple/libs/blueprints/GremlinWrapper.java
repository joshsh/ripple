package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.gremlin.jsr223.GremlinScriptEngine;
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
        super(new GremlinScriptEngine());
    }

    @Override
    protected RippleValue nativize(final Object externalObject,
                                   final ModelConnection mc) throws RippleException {
        return BlueprintsLibrary.createRippleValue(externalObject, mc);
    }
}
