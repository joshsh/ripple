package net.fortytwo.ripple.util;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.DistinctFilter;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleValue;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;

/**
 * User: josh
 * Date: Nov 19, 2010
 * Time: 4:21:35 PM
 */
public class ModelConnectionHelper {
    private final ModelConnection connection;

    public ModelConnectionHelper(ModelConnection connection) {
        this.connection = connection;
    }


    public RDFValue findSingleObject(final RippleValue subj, final RippleValue pred)
            throws RippleException {
        RDFValue subjRdf = subj.toRDF(connection);
        RDFValue predRdf = pred.toRDF(connection);

        SingleValueSink sink = new SingleValueSink();

        multiplyRDFValues(subjRdf, predRdf, sink);

        return sink.getValue();
    }

    public RDFValue findAtLeastOneObject(final RippleValue subj, final RippleValue pred)
            throws RippleException {
        RDFValue subjRdf = subj.toRDF(connection);
        RDFValue predRdf = pred.toRDF(connection);

        SingleValueSink sink = new SingleValueSink();

        multiplyRDFValues(subjRdf, predRdf, sink);

        if (0 == sink.countReceived()) {
            throw new RippleException("no values resolved for " + pred.toString() + " of " + subj.toString());
        } else {
            return sink.getValue();
        }
    }

    public RDFValue findAtMostOneObject(final RippleValue subj, final RippleValue pred)
            throws RippleException {
        RDFValue subjRdf = subj.toRDF(connection);
        RDFValue predRdf = pred.toRDF(connection);

        SingleValueSink sink = new SingleValueSink();

        multiplyRDFValues(subjRdf, predRdf, sink);

        int count = sink.countReceived();

        if (1 < count) {
            throw new RippleException(pred.toString() + " of " + subj.toString() + " resolved to more than one value");
        } else {
            return sink.getValue();
        }
    }

    public RDFValue findUniqueProduct(final RippleValue subj, final RippleValue pred)
            throws RippleException {
        RDFValue subjRdf = subj.toRDF(connection);
        RDFValue predRdf = pred.toRDF(connection);

        RDFValue v = findAtMostOneObject(subjRdf, predRdf);

        if (null == v) {
            throw new RippleException("no values resolved for " + pred.toString() + " of " + subj.toString());
        } else {
            return v;
        }
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

        connection.getStatements(src.toRDF(connection), null, null, stSink, false);
    }

    public void findPredicates(final RippleValue subject,
                               final Sink<RippleValue, RippleException> sink)
            throws RippleException {
        final Sink<Value, RippleException> valueSink = new Sink<Value, RippleException>() {
            public void put(final Value v) throws RippleException {
                sink.put(connection.value(v));
            }
        };

        Sink<Statement, RippleException> predSelector = new Sink<Statement, RippleException>() {
            Sink<Value, RippleException> predSink = new DistinctFilter<Value, RippleException>(valueSink);

            public void put(final Statement st) throws RippleException {
                //TODO: don't create a new RdfValue before checking for uniqueness
                predSink.put(st.getPredicate());
            }
        };

        connection.getStatements(subject.toRDF(connection), null, null, predSelector, false);
    }

    private void multiplyRDFValues(final RDFValue subj, final RDFValue pred, final Sink<RDFValue, RippleException> sink)
            throws RippleException {
        Sink<Statement, RippleException> stSink = new Sink<Statement, RippleException>() {
            public void put(final Statement st) throws RippleException {
                sink.put(new RDFValue(st.getObject()));
            }
        };

        connection.getStatements(subj, pred, null, stSink, false);
    }

    /**
     * A <code>Sink</code> which remembers how many times it has received a
     * value, as well as the last value received.
     */
    private class SingleValueSink implements Sink<RDFValue, RippleException> {
        private RDFValue value = null;
        private int valuesReceived = 0;

        public void put(final RDFValue v) throws RippleException {
            value = v;
            valuesReceived++;
        }

        public RDFValue getValue() {
            return value;
        }

        public int countReceived() {
            return valuesReceived;
        }
    }
}
