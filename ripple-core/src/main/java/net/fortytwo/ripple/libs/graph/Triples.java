/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.NullSink;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.flow.rdf.SesameInputAdapter;
import net.fortytwo.ripple.util.HTTPUtils;
import net.fortytwo.ripple.util.RDFHTTPUtils;

import org.apache.commons.httpclient.HttpMethod;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

/**
 * A primitive which consumes an information resource and produces a list
 * (subject, predicate, object) for each RDF triple in the corresponding
 * Semantic Web document.
 */
public class Triples extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2011_08 + "triples",
            GraphLibrary.NS_2008_08 + "triples",
            GraphLibrary.NS_2007_08 + "triples"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Triples()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("doc", "a Semantic Web document", true)};
    }

    public String getComment() {
        return "doc  =>  s p o  -- for each triple (s p o) in document doc";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        String uri = arg.getFirst().toString();

        SesameInputAdapter sc = createAdapter(arg, solutions, mc);

        HttpMethod method = HTTPUtils.createGetMethod(uri);
        HTTPUtils.setRdfAcceptHeader(method);
        RDFHTTPUtils.read(method, sc, uri, null);
    }

    static SesameInputAdapter createAdapter(final RippleList arg,
                                            final Sink<RippleList> resultSink,
                                            final ModelConnection mc) {
        final RippleList rest = arg.getRest();

        RDFSink rdfSink = new RDFSink() {
            // Push statements.
            private Sink<Statement> stSink = new Sink<Statement>() {
                public void put(final Statement st) throws RippleException {
                    resultSink.put(
                            rest.push(mc.canonicalValue(new RDFValue(st.getSubject())))
                                    .push(mc.canonicalValue(new RDFValue(st.getPredicate())))
                                    .push(mc.canonicalValue(new RDFValue(st.getObject()))));
                }
            };

            // Discard namespaces.
            private Sink<Namespace> nsSink = new NullSink<Namespace>();

            // Discard comments.
            private Sink<String> cmtSink = new NullSink<String>();

            public Sink<Statement> statementSink() {
                return stSink;
            }

            public Sink<Namespace> namespaceSink() {
                return nsSink;
            }

            public Sink<String> commentSink() {
                return cmtSink;
            }
        };

        return new SesameInputAdapter(rdfSink);
    }
}

