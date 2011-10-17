package net.fortytwo.ripple.libs.system;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;

import java.util.HashMap;
import java.util.Map;

/**
 * User: josh
 * Date: 4/5/11
 * Time: 9:35 PM
 */
public class Script extends PrimitiveStackMapping {
    private final Map<String, ScriptEngineWrapper> scriptEngines;

    public Script() {
        scriptEngines = new HashMap<String, ScriptEngineWrapper>();
    }

    public void addScriptEngine(final String name,
                                final ScriptEngineWrapper engineWrapper) {
        scriptEngines.put(name, engineWrapper);
    }

    @Override
    public String[] getIdentifiers() {
        return new String[]{
                SystemLibrary.NS_2011_08 + "script"
        };
    }

    @Override
    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("expression", "an expression to evaluate in the attached scripting environment", true),
                new Parameter("engine", "the name of the script engine", true)};
    }

    @Override
    public String getComment() {
        return "evaluates an expression in the attached scripting environment, yielding the result as a native value";
    }

    public void apply(final StackContext arg,
                      final Sink<StackContext> solutions) throws RippleException {
        final ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();

        String name = mc.toString(stack.getFirst());
        stack = stack.getRest();

        String script = mc.toString(stack.getFirst());
        stack = stack.getRest();

        ScriptEngineWrapper w = scriptEngines.get(name);
        if (null == w) {
            java.lang.System.err.println("no such script engine: " + name);
        } else {
            RippleValue result = w.evaluate(script, mc);

            if (null != result) {
                solutions.put(arg.with(stack.push(result)));
            }
        }
    }
}
