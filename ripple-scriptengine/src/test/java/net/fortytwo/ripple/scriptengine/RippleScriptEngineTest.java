package net.fortytwo.ripple.scriptengine;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.test.RippleTestCase;
import org.junit.Before;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleScriptEngineTest extends RippleTestCase {
    private ScriptEngine engine;
    private boolean initialized = false;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        if (!initialized) {
            Ripple.initialize();
        }

        initialized = true;

        ScriptEngineFactory factory = new RippleScriptEngineFactory();
        engine = factory.getScriptEngine();
    }

    @Test
    public void testSimple() throws Exception {
        assertEquals(5, engine.eval("2 3 add.\n"));
        assertEquals("foobar", engine.eval("\"foo\" \"bar\" strCat.\n"));
    }

    @Test
    public void testMultipleValues() throws Exception {
        assertEquals(2, engine.eval("2 dup. both.\n"));
    }

    @Test
    public void testNoValues() throws Exception {
        assertNull(engine.eval("-1 sqrt.\n"));
        assertNull(engine.eval("\n"));
        assertNull(engine.eval(".\n"));
    }

    @Test
    public void testTopValueFromStack() throws Exception {
        assertEquals(2, engine.eval("1 2\n"));
    }
}
