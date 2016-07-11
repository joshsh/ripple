package net.fortytwo.ripple.scriptengine;

import junit.framework.TestCase;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Ignore;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleScriptEngineFactoryTest extends RippleTestCase {
    @Override
    public void setUp() throws Exception {
        Properties config = new Properties();
        Ripple.initialize(config);
    }

    @Test
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

    @Test
    public void testGetMethodCallSyntax() throws Exception {
        ScriptEngineFactory f = new RippleScriptEngineFactory();

        assertEquals("ex:bob foaf:name >> .",
                f.getMethodCallSyntax("ex:bob", "foaf:name"));

        assertEquals("1 2 add >> .",
                f.getMethodCallSyntax("1", "add", "2"));
    }

    @Test
    public void testGetProgram() throws Exception {
        ScriptEngineFactory f = new RippleScriptEngineFactory();

        assertEquals("",
                f.getProgram());
        assertEquals("1 2 add >>.",
                f.getProgram("1 2 add >>"));
        assertEquals("ex:bob foaf:name >> ..\n1 2 add >> 5 mul >>.",
                f.getProgram("ex:bob foaf:name >> .", "1 2 add >> 5 mul >>"));
    }

    @Ignore
    @Test
    public void testGetParameter() throws Exception {
        // TODO
    }

    @Ignore
    @Test
    public void testGetOutputStatement() throws Exception {
        // TODO
    }

    @Test
    public void testGetScriptEngine() throws Exception {
        ScriptEngineFactory f = new RippleScriptEngineFactory();
        ScriptEngine e = f.getScriptEngine();

        assertEquals(RippleScriptEngine.class, e.getClass());
        assertTrue(f == e.getFactory());
    }
}
