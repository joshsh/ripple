package net.fortytwo.flow.rdf.ranking;

import info.aduna.iteration.CloseableIteration;
import org.openrdf.model.IRI;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

import java.util.Random;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Ranking {
    private static final ValueFactory valueFactory = SimpleValueFactory.getInstance();

    public static final IRI INDEX = valueFactory.createIRI("http://example.org/index");

    private static final boolean INCLUDE_INFERRED = false;

    private static final int TOTAL_RESOURCES = 408422;

    private static final Random RANDOM = new Random();

    public Ranking() {
    }

    public static boolean getStatements(final SailConnection sc,
                                        final Handler<Statement, HandlerException> handler,
                                        final Resource subject,
                                        final IRI predicate,
                                        final Value object) throws HandlerException {
        try {
            CloseableIteration<? extends Statement, SailException> iter
                    = sc.getStatements(subject, predicate, object, INCLUDE_INFERRED);
            try {
                while (iter.hasNext()) {
                    handler.handle(iter.next());
                }
            } finally {
                iter.close();
            }
        } catch (SailException e) {
            throw new HandlerException(e);
        }

        return true;
    }

    public static boolean traverseForward(final SailConnection sc,
                                          final Handler<Value, HandlerException> handler,
                                          final Resource subject,
                                          final IRI... predicates) throws HandlerException {
        final Handler<Statement, HandlerException> objectGrabber = new Handler<Statement, HandlerException>() {
            public boolean handle(Statement st) throws HandlerException {
                return handler.handle(st.getObject());
            }
        };

        for (IRI p : predicates) {
            if (!getStatements(sc, objectGrabber, subject, p, null)) {
                return false;
            }

        }

        return true;
    }

    public static boolean traverseBackward(final SailConnection sc,
                                           final Handler<Value, HandlerException> handler,
                                           final Resource object,
                                           final IRI... predicates) throws HandlerException {
        final Handler<Statement, HandlerException> subjectGrabber = new Handler<Statement, HandlerException>() {
            public boolean handle(Statement st) throws HandlerException {
                return handler.handle(st.getSubject());
            }
        };

        for (IRI p : predicates) {
            if (!getStatements(sc, subjectGrabber, null, p, object)) {
                return false;
            }

        }

        return true;
    }

    public static Resource randomResource(final SailConnection sc) throws HandlerException {
        Resource r;
        int i = RANDOM.nextInt(TOTAL_RESOURCES) + 1;

        try {
            CloseableIteration<? extends Statement, SailException> iter
                    = sc.getStatements(null, INDEX, valueFactory.createLiteral("" + i), false);
            try {
                r = iter.next().getSubject();
            } finally {
                iter.close();
            }
        } catch (SailException e) {
            throw new HandlerException(e);
        }

        return r;
    }
}
