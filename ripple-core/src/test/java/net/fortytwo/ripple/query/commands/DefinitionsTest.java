package net.fortytwo.ripple.query.commands;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.cli.ast.PlainLiteralAST;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.test.RippleTestCase;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class DefinitionsTest extends RippleTestCase {
    public void testDefinitions() throws Exception {
        Sail sail = getTestSail();
        SailConnection sc = sail.getConnection();
        QueryEngine qe = getTestQueryEngine();

        ListAST foobar = new ListAST(
                new PlainLiteralAST("foo"), new ListAST(new PlainLiteralAST("bar"), new ListAST()));
        ListAST foobar2 = new ListAST(
                new PlainLiteralAST("foo2"), new ListAST(new PlainLiteralAST("bar2"), new ListAST()));
        URI foobarUri = sail.getValueFactory().createURI(qe.getLexicon().getDefaultNamespace() + "foobar");
        Literal foo = sail.getValueFactory().createLiteral("foo", XMLSchema.STRING);
        Literal foo2 = sail.getValueFactory().createLiteral("foo2", XMLSchema.STRING);

        Command defCmd = new DefineListCmd("foobar", foobar);
        Command undefCmd = new UndefineListCmd("foobar");
        Command redefCmd = new RedefineListCmd("foobar", foobar2);

        int count;
        Value obj;

        count = countStatements(sc.getStatements(foobarUri, null, null, false));
        assertEquals(0, count);

        // Undefine a term which has not been defined.
        qe.executeCommand(undefCmd);
        count = countStatements(sc.getStatements(foobarUri, null, null, false));
        assertEquals(0, count);

        // Define a term which is not yet defined.
        qe.executeCommand(defCmd);
        count = countStatements(sc.getStatements(foobarUri, null, null, false));
        assertEquals(3, count);
        obj = getSingleStatement(sc.getStatements(foobarUri, RDF.FIRST, null, false)).getObject();
        assertTrue(obj.equals(foo));
        assertFalse(obj.equals(foo2));

        // Undefine a term which has been defined.
        qe.executeCommand(undefCmd);
        count = countStatements(sc.getStatements(foobarUri, null, null, false));
        assertEquals(0, count);

        // Redefine a term which has already been defined.
        qe.executeCommand(defCmd);
        count = countStatements(sc.getStatements(foobarUri, null, null, false));
        assertEquals(3, count);
        obj = getSingleStatement(sc.getStatements(foobarUri, RDF.FIRST, null, false)).getObject();
        assertTrue(obj.equals(foo));
        assertFalse(obj.equals(foo2));
        qe.executeCommand(redefCmd);
        count = countStatements(sc.getStatements(foobarUri, null, null, false));
        assertEquals(3, count);
        obj = getSingleStatement(sc.getStatements(foobarUri, RDF.FIRST, null, false)).getObject();
        assertTrue(obj.equals(foo2));
        assertFalse(obj.equals(foo));

        // Undefine a term which has been redefined.
        qe.executeCommand(undefCmd);
        count = countStatements(sc.getStatements(foobarUri, null, null, false));
        assertEquals(0, count);

        // Redefine a term which has not yet been defined.
        qe.executeCommand(redefCmd);
        count = countStatements(sc.getStatements(foobarUri, null, null, false));
        assertEquals(3, count);
        obj = getSingleStatement(sc.getStatements(foobarUri, RDF.FIRST, null, false)).getObject();
        assertTrue(obj.equals(foo2));
        assertFalse(obj.equals(foo));

        sc.close();
    }

    private int countStatements(final CloseableIteration<? extends Statement, SailException> iter)
            throws SailException {

        int count = 0;
        while (iter.hasNext()) {
            count++;
            iter.next();
        }
        iter.close();
        return count;
    }

    private Statement getSingleStatement(final CloseableIteration<? extends Statement, SailException> iter)
            throws SailException {

        Statement st = (iter.hasNext()) ? iter.next() : null;
        iter.close();
        return st;
    }
}
