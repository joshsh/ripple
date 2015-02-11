package net.fortytwo.ripple.libs.system;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class ScriptEngineWrapper {
    private final ScriptEngine scriptEngine;

    public ScriptEngineWrapper(ScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    protected abstract Object nativize(Object externalValue,
                                       ModelConnection mc) throws RippleException;

    public Object evaluate(final String script,
                           final ModelConnection mc) throws RippleException {
        Object result;
        try {
            result = scriptEngine.eval(script);
        } catch (ScriptException e) {
            throw new RippleException(e);
        }

        return null == result
                ? null
                : nativize(result, mc);
    }
}
