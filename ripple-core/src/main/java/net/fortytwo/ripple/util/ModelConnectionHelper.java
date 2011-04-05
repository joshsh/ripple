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
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;

import java.util.Random;

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
        Collector<RippleValue, RippleException> results = new Collector<RippleValue, RippleException>();
        connection.query(query, results, false);

        return results.isEmpty() ? null : results.iterator().next();
    }

    //TODO: context handling
    public void copyStatements(final RippleValue src, final RippleValue dest)
            throws RippleException {
        Sink<Statement, RippleException> stSink = new Sink<Statement, RippleException>() {
            public void put(final Statement st) throws RippleException {
                Resource context = st.getContext();

                try {
                    if (null == context) {
                        connection.add(dest, new RDFValue(st.getPredicate()), new RDFValue(st.getObject()));
                    } else {
                        connection.add(dest, new RDFValue(st.getPredicate()), new RDFValue(st.getObject()), new RDFValue(st.getContext()));
                    }
                }

                catch (Throwable t) {
                    throw new RippleException(t);
                }
            }
        };

        connection.getStatements(src.toRDF(connection), null, null, stSink);
    }

    public void findPredicates(final RippleValue subject,
                               final Sink<RippleValue, RippleException> sink)
            throws RippleException {
        final Sink<Value, RippleException> valueSink = new Sink<Value, RippleException>() {
            public void put(final Value v) throws RippleException {
                sink.put(connection.canonicalValue(v));
            }
        };

        Sink<Statement, RippleException> predSelector = new Sink<Statement, RippleException>() {
            Sink<Value, RippleException> predSink = new DistinctFilter<Value, RippleException>(valueSink);

            public void put(final Statement st) throws RippleException {
                //TODO: don't create a new RdfValue before checking for uniqueness
                predSink.put(st.getPredicate());
            }
        };

        connection.getStatements(subject.toRDF(connection), null, null, predSelector);
    }

    public RippleValue createRandomURI() throws RippleException {
        // Local name will be a UUID (without the dashes).
        byte[] bytes = new byte[32];

        // Artificially constrain the fist character to be a letter, so the
        // local part of the URI is N3-friendly.
        bytes[0] = (byte) ('a' + RANDOM.nextInt(5));

        // Remaining characters are hexadecimal digits.
        for (int i = 1; i < 32; i++) {
            int c = RANDOM.nextInt(16);
            bytes[i] = (byte) ((c > 9) ? c - 10 + 'a' : c + '0');
        }

        return connection.uriValue(Ripple.RANDOM_URN_PREFIX + new String(bytes));
    }
}
