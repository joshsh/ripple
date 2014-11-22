package net.fortytwo.ripple.io;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleType;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.test.RippleTestCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RipplePrintStreamTest extends RippleTestCase {
    public void testLiterals() throws Exception {
        Model model = getTestModel();
        ModelConnection mc = model.createConnection();
        QueryEngine qe = new QueryEngine(model, null, System.out, System.err);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        RipplePrintStream ps = new RipplePrintStream(new PrintStream(bos), qe.getLexicon());

        mc.setNamespace("xsd", "http://www.w3.org/2001/XMLSchema#", true);

        ps.print(42);
        assertEquals("42", bos.toString());
        bos.reset();
        ps.print(0);
        assertEquals("0", bos.toString());
        bos.reset();
        ps.print(-42);
        assertEquals("-42", bos.toString());
        bos.reset();

        ps.print(42.0);
        assertEquals("42.0E0", bos.toString());
        bos.reset();
        ps.print(0.0);
        assertEquals("0.0E0", bos.toString());
        bos.reset();
        ps.print(-42.0);
        assertEquals("-42.0E0", bos.toString());
        bos.reset();

        ps.print(true);
        assertEquals("true", bos.toString());
        bos.reset();
        ps.print(false);
        assertEquals("false", bos.toString());
        bos.reset();

        //...

        ps.close();
        bos.close();
        mc.close();
    }
}
