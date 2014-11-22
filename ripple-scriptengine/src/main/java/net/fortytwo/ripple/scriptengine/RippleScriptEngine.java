package net.fortytwo.ripple.scriptengine;

import net.fortytwo.flow.Collector;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.config.SailConfiguration;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.impl.sesame.SesameModel;
import net.fortytwo.ripple.query.LazyStackEvaluator;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.query.QueryPipe;
import net.fortytwo.ripple.query.StackEvaluator;
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.XMLSchema;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleScriptEngine implements ScriptEngine {
    private final ScriptEngineFactory factory;
    private final SailConfiguration sailConfig;
    private final Model model;
    //private final QueryEngine queryEngine;
    private final QueryPipe queryPipe;
    private final Collector<RippleList> results;
    private ScriptContext context;

    public RippleScriptEngine(final ScriptEngineFactory factory) throws RippleException {
        this.factory = factory;

        results = new Collector<>();

        URIMap uriMap = new URIMap();
        sailConfig = new SailConfiguration(uriMap);

        // TODO: shutDown on failure
        model = new SesameModel(sailConfig.getSail());

        StackEvaluator eval = new LazyStackEvaluator();
        QueryEngine queryEngine = new QueryEngine(model, eval, System.out, System.err);

        queryPipe = new QueryPipe(queryEngine, results);
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();

        shutDown();
    }

    private void shutDown() throws RippleException {
        model.shutDown();
    }

    public Object eval(final String script,
                       final ScriptContext context) throws ScriptException {
        // TODO: use context
        return eval(script);
    }

    public Object eval(final Reader reader,
                       final ScriptContext context) throws ScriptException {
        // TODO: use context
        return eval(reader);
    }

    public Object eval(final String script) throws ScriptException {
        results.clear();

        try {
            queryPipe.put(script);
        } catch (RippleException e) {
            throw new ScriptException(e);
        }

        if (0 == results.size()) {
            return null;
        } else {
            for (Object result : results) {
                RippleList l = (RippleList) result;
                if (!l.isNil()) {
                    return toJavaObject(l.getFirst());
                }
            }

            return null;
        }
    }

    // TODO: temporary... kludged during a major revision of type mappings. Double-check this
    private Object toJavaObject(final Object v) throws ScriptException {
        if (v instanceof Value) {
            return toJavaObject((Value) v);
        } else {
            return v;
        }
    }

    private Object toJavaObject(final Value v) throws ScriptException {
        if (v instanceof URI) {
            try {
                return new java.net.URI(v.toString());
            } catch (URISyntaxException e) {
                throw new ScriptException(e);
            }
        } else if (v instanceof Literal) {
            Literal l = (Literal) v;
            URI datatype = l.getDatatype();
            if (null == datatype) {
                return l.getLabel();
            } else if (XMLSchema.STRING.equals(datatype)) {
                return l.getLabel();
            } else if (XMLSchema.INTEGER.equals(datatype)) {
                return l.integerValue();
            } else if (XMLSchema.INT.equals(datatype)) {
                return l.intValue();
            } else if (XMLSchema.ANYURI.equals(datatype)) {
                try {
                    return new java.net.URI(l.getLabel());
                } catch (URISyntaxException e) {
                    throw new ScriptException(e);
                }
            } else if (XMLSchema.SHORT.equals(datatype)) {
                return l.shortValue();
            } else if (XMLSchema.LONG.equals(datatype)) {
                return l.longValue();
            } else if (XMLSchema.FLOAT.equals(datatype)) {
                return l.floatValue();
            } else if (XMLSchema.BYTE.equals(datatype)) {
                return l.byteValue();
            } else if (XMLSchema.DATETIME.equals(datatype)) {
                return l.calendarValue();
            } else if (XMLSchema.DOUBLE.equals(datatype)) {
                return l.doubleValue();
            } else if (XMLSchema.BOOLEAN.equals(datatype)) {
                return l.booleanValue();
            } else if (XMLSchema.DECIMAL.equals(datatype)) {
                return l.decimalValue();
            } else {
                return l.getLabel();
            }
        } else if (v instanceof BNode) {
            return ((BNode) v).getID();
        } else {
            throw new ScriptException("RDF value of unexpected type: " + v);
        }
    }

    public Object eval(final Reader reader) throws ScriptException {
        // TODO: use reader stream (instead of converting to a String)
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                int ch;
                while (-1 != (ch = reader.read())) {
                    bos.write(ch);
                }

                return eval(bos.toString());
            } finally {
                bos.close();
            }
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    public Object eval(final String script,
                       final Bindings n) throws ScriptException {
        // TODO: use bindings
        return eval(script);
    }

    public Object eval(final Reader reader,
                       final Bindings n) throws ScriptException {
        // TODO: use bindings
        return eval(reader);
    }

    public void put(final String key,
                    final Object value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object get(final String key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Bindings getBindings(final int scope) {
        switch (scope) {
            case ScriptContext.ENGINE_SCOPE:
                break;
            case ScriptContext.GLOBAL_SCOPE:
                break;
        }

        // FIXME
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setBindings(final Bindings bindings,
                            final int scope) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Bindings createBindings() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ScriptContext getContext() {
        return context;
    }

    public void setContext(final ScriptContext context) {
        this.context = context;
    }

    public ScriptEngineFactory getFactory() {
        return factory;
    }
}
