package net.fortytwo.ripple.libs.system;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleValue;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * User: josh
 * Date: 5/20/11
 * Time: 10:26 PM
 */
public abstract class ScriptEngineWrapper {
    private final ScriptEngine scriptEngine;

    public ScriptEngineWrapper(ScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    protected abstract RippleValue nativize(Object externalValue,
                                            ModelConnection mc) throws RippleException;

    public RippleValue evaluate(final String script,
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
