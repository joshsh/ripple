package net.fortytwo.ripple.util;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.DistinctFilter;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StatementPatternQuery;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;

import java.net.URI;
import java.util.UUID;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ModelConnectionHelper {

    private final ModelConnection connection;

    public ModelConnectionHelper(ModelConnection connection) {
        this.connection = connection;
    }

    public Object findSingleObject(final Object subj, final Object pred)
            throws RippleException {
        StatementPatternQuery query = new StatementPatternQuery(subj, pred, null);
        Collector<Object> results = new Collector<>();
        connection.query(query, results, false);

        return results.isEmpty() ? null : results.iterator().next();
    }

    public void findPredicates(final Object subject,
                               final Sink<Object> sink)
            throws RippleException {
        final Sink<Value> valueSink = new Sink<Value>() {
            public void put(final Value v) throws RippleException {
                sink.put(connection.canonicalValue(v));
            }
        };

        Sink<Statement> predSelector = new Sink<Statement>() {
            Sink<Value> predSink = new DistinctFilter<Value>(valueSink);

            public void put(final Statement st) throws RippleException {
                //TODO: don't create a new RdfValue before checking for uniqueness
                predSink.put(st.getPredicate());
            }
        };

        Value v = connection.toRDF(subject);
        // Not all values have an RDF identity.
        if (null != v) {
            connection.getStatements(v, null, null, predSelector);
        }
    }

    public Value createRandomURI() throws RippleException {
        return connection.valueOf(URI.create(Ripple.RANDOM_URN_PREFIX + UUID.randomUUID()));
    }

    public static boolean isRDFList(final Object r, final ModelConnection mc) throws RippleException {
        if (r instanceof RippleList) {
            return true;
        }

        if (!(r instanceof Resource)) {
            return false;
        }

        if (r.equals(RDF.NIL)) {
            return true;
        }

        final boolean[] b = {false};
        mc.getStatements((Resource) r, RDF.FIRST, null, new Sink<Statement>(){
            @Override
            public void put(Statement statement) throws RippleException {
                b[0] = true;
            }
        });
        return b[0];
    }
}
