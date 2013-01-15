package net.fortytwo.ripple.scriptengine;

import junit.framework.TestCase;
import net.fortytwo.ripple.Ripple;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleScriptEngineTest extends TestCase {
    private ScriptEngine engine;
    private boolean initialized = false;

    public void setUp() throws Exception {
        if (!initialized) {
            Ripple.initialize();
        }

        ScriptEngineFactory factory = new RippleScriptEngineFactory();
        engine = factory.getScriptEngine();
    }

    public void tearDown() throws Exception {
    }

    public void testSimple() throws Exception {
        //System.out.println(engine.eval("2 3 add. .").getClass());
        assertEquals(5, engine.eval("2 3 add.\n"));
        assertEquals("foobar", engine.eval("\"foo\" \"bar\" strCat.\n"));
    }

    public void testMultipleValues() throws Exception {
        assertEquals(2, engine.eval("2 dup. both.\n"));
    }

    public void testNoValues() throws Exception {
        assertNull(engine.eval("-1 sqrt.\n"));
        assertNull(engine.eval("\n"));
        assertNull(engine.eval(".\n"));
    }

    public void testTopValueFromStack() throws Exception {
        assertEquals(2, engine.eval("1 2\n"));
    }
}
