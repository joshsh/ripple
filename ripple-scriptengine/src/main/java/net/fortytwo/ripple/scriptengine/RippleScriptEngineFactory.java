package net.fortytwo.ripple.scriptengine;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngine;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleScriptEngineFactory implements ScriptEngineFactory {
    private static final String
            NAME = "ripple",
            ENGINE_NAME = "Ripple Script Engine";

    private final List<String> names;
    private final List<String> extensions;

    public RippleScriptEngineFactory() {
        names = new ArrayList<String>();
        names.add(NAME);

        extensions = new ArrayList<String>();
        extensions.add("rpl");
    }

    public String getEngineName() {
        return ENGINE_NAME;
    }

    public String getEngineVersion() {
        // Versioning of the Ripple language and its Java implementation is identical for now.
        return Ripple.getVersion();
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public List<String> getMimeTypes() {
        // Ripple script files currently don't have a MIME type.
        return new ArrayList<String>();
    }

    public List<String> getNames() {
        return names;
    }

    public String getLanguageName() {
        return Ripple.getName();
    }

    public String getLanguageVersion() {
        return Ripple.getVersion();
    }

    // TODO
    public Object getParameter(final String key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getMethodCallSyntax(final String obj,
                                      final String m,
                                      final String... args) {
        StringBuilder sb = new StringBuilder(obj);
        
        for (String arg : args) {
            sb.append(" ");
            sb.append(arg);
        }

        sb.append(" ");
        sb.append(m);
        sb.append(" >> .");

        return sb.toString();
    }

    // TODO
    public String getOutputStatement(final String toDisplay) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getProgram(final String... statements) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String st : statements) {
            if (first) {
                first = false;
            } else {
                sb.append("\n");
            }

            sb.append(st);
            
            // It doesn't hurt to add an end-statement token (unless the last
            // statement ended with continue-statement token...)
            sb.append(".");
        }
        
        return sb.toString();
    }

    public ScriptEngine getScriptEngine() {
        try {
            return new RippleScriptEngine(this);
        } catch (RippleException e) {
            // TODO: there might be a better way to deal with this exception
            throw new RuntimeException(e);
        }
    }
}
