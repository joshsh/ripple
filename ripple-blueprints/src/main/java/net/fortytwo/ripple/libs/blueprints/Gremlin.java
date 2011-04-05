package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.gremlin.jsr223.GremlinScriptEngine;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;

import javax.script.ScriptException;

/**
 * User: josh
 * Date: 4/5/11
 * Time: 9:35 PM
 */
public class Gremlin extends PrimitiveStackMapping {
    private final GremlinScriptEngine engine = new GremlinScriptEngine();

    @Override
    public String[] getIdentifiers() {
        return new String[]{
                BlueprintsLibrary.NS_2011_04 + "gremlin"
        };
    }

    @Override
    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("expression", "a Groovy expression to evaluate in the attached scripting environment", true)};
    }

    @Override
    public String getComment() {
        return "evaluates a Groovy expression in the attached scripting environment, yielding the result as a list";
    }

    @Override
    public void apply(final StackContext arg,
                      final Sink<StackContext, RippleException> solutions) throws RippleException {
        final ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();
        String script = mc.toString(stack.getFirst());
        stack = stack.getRest();

        Object result;
        try {
            result = engine.eval(script);
        } catch (ScriptException e) {
            throw new RippleException(e);
        }

        if (null != result) {
            solutions.put(arg.with(stack.push(BlueprintsLibrary.createRippleValue(result, mc))));
        }
    }

    public static void main(final String[] args) throws Exception {
        GremlinScriptEngine engine = new GremlinScriptEngine();
        Object result = engine.eval("g = TinkerGraphFactory.createTinkerGraph()\ng.v(1)");
        //result = engine.eval("g.v(1)");
        System.out.println("" + result.getClass() + ": " + result);
    }
}
