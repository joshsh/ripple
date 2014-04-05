package net.fortytwo.ripple.util;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.DistinctFilter;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StatementPatternQuery;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;

import java.net.URI;
import java.util.Random;
import java.util.UUID;

/**
 * User: josh
 * Date: Nov 19, 2010
 * Time: 4:21:35 PM
 */
public class ModelConnectionHelper {
    private static final Random RANDOM = new Random();

    private final ModelConnection connection;

    public ModelConnectionHelper(ModelConnection connection) {
        this.connection = connection;
    }

    public RippleValue findSingleObject(final RippleValue subj, final RippleValue pred)
            throws RippleException {
        StatementPatternQuery query = new StatementPatternQuery(subj, pred, null);
        Collector<RippleValue> results = new Collector<RippleValue>();
        connection.query(query, results, false);

        return results.isEmpty() ? null : results.iterator().next();
    }

    public void findPredicates(final RippleValue subject,
                               final Sink<RippleValue> sink)
            throws RippleException {
        final Sink<Value> valueSink = new Sink<Value>() {
            public void put(final Value v) throws RippleException {
                sink.put(connection.canonicalValue(new RDFValue(v)));
            }
        };

        Sink<Statement> predSelector = new Sink<Statement>() {
            Sink<Value> predSink = new DistinctFilter<Value>(valueSink);

            public void put(final Statement st) throws RippleException {
                //TODO: don't create a new RdfValue before checking for uniqueness
                predSink.put(st.getPredicate());
            }
        };

        RDFValue v = subject.toRDF(connection);
        // Not all RippleValues have an RDF identity.
        if (null != v) {
            connection.getStatements(v, null, null, predSelector);
        }
    }

    public RDFValue createRandomURI() throws RippleException {
        return connection.valueOf(URI.create(Ripple.RANDOM_URN_PREFIX + UUID.randomUUID()));
    }
}
