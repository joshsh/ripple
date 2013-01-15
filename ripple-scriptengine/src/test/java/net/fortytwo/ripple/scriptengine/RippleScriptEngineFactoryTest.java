package net.fortytwo.ripple.scriptengine;

import junit.framework.TestCase;
import net.fortytwo.ripple.Ripple;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.List;
import java.util.Properties;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleScriptEngineFactoryTest extends TestCase {
    @Override
    public void setUp() throws Exception {
        Properties config = new Properties();
        Ripple.initialize(config);
    }

    public void testInfo() throws Exception {
        ScriptEngineFactory f = new RippleScriptEngineFactory();

        assertEquals("Ripple Script Engine", f.getEngineName());

        List<String> names = f.getNames();
        assertEquals(1, names.size());
        assertEquals("ripple", names.get(0));

        List<String> extensions = f.getExtensions();
        assertEquals(1, extensions.size());
        assertEquals("rpl", extensions.get(0));

        assertEquals(Ripple.getVersion(), f.getEngineVersion());

        assertEquals("Ripple", f.getLanguageName());

        assertEquals(Ripple.getVersion(), f.getLanguageVersion());

        List<String> mimeTypes = f.getMimeTypes();
        assertEquals(0, mimeTypes.size());
    }

    public void testGetMethodCallSyntax() throws Exception {
        ScriptEngineFactory f = new RippleScriptEngineFactory();

        assertEquals("ex:bob foaf:name >> .",
                f.getMethodCallSyntax("ex:bob", "foaf:name"));

        assertEquals("1 2 add >> .",
                f.getMethodCallSyntax("1", "add", "2"));
    }

    public void testGetProgram() throws Exception {
        ScriptEngineFactory f = new RippleScriptEngineFactory();

        assertEquals("",
                f.getProgram());
        assertEquals("1 2 add >>.",
                f.getProgram("1 2 add >>"));
        assertEquals("ex:bob foaf:name >> ..\n1 2 add >> 5 mul >>.",
                f.getProgram("ex:bob foaf:name >> .", "1 2 add >> 5 mul >>"));
    }

    public void testGetParameter() throws Exception {
        // TODO
    }

    public void testGetOutputStatement() throws Exception {
        // TODO
    }

    public void testGetScriptEngine() throws Exception {
        ScriptEngineFactory f = new RippleScriptEngineFactory();
        ScriptEngine e = f.getScriptEngine();

        assertEquals(RippleScriptEngine.class, e.getClass());
        assertTrue(f == e.getFactory());
    }
}
