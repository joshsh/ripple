package net.fortytwo.flow.rdf.ranking;

import info.aduna.iteration.CloseableIteration;
import org.openrdf.model.IRI;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Ranking {

    private static final boolean INCLUDE_INFERRED = false;

    public Ranking() {
    }

    private static boolean getStatements(final SailConnection sc,
                                         final Handler<Statement, HandlerException> handler,
                                         final Resource subject,
                                         final IRI predicate,
                                         final Value object) throws HandlerException {
        try {
            try (CloseableIteration<? extends Statement, SailException> iter
                         = sc.getStatements(subject, predicate, object, INCLUDE_INFERRED)) {
                while (iter.hasNext()) {
                    handler.handle(iter.next());
                }
            }
        } catch (SailException e) {
            throw new HandlerException(e);
        }

        return true;
    }

    public static void traverseForward(final SailConnection sc,
                                       final Handler<Value, HandlerException> handler,
                                       final Resource subject,
                                       final IRI... predicates) throws HandlerException {
        final Handler<Statement, HandlerException> objectGrabber = st -> handler.handle(st.getObject());

        for (IRI p : predicates) {
            if (!getStatements(sc, objectGrabber, subject, p, null)) {
                return;
            }
        }
    }

    public static boolean traverseBackward(final SailConnection sc,
                                           final Handler<Value, HandlerException> handler,
                                           final Resource object,
                                           final IRI... predicates) throws HandlerException {
        final Handler<Statement, HandlerException> subjectGrabber = st -> handler.handle(st.getSubject());

        for (IRI p : predicates) {
            if (!getStatements(sc, subjectGrabber, null, p, object)) {
                return false;
            }

        }

        return true;
    }
}
