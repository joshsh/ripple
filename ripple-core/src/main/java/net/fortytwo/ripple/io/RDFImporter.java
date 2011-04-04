/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.io;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RDFValue;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;

// TODO: change this class to use a SailConnection instead of a ModelConnection
public class RDFImporter implements RDFSink {
    private final Sink<Statement, RippleException> stSink;
    private final Sink<Namespace, RippleException> nsSink;
    private final Sink<String, RippleException> cmtSink;

    public RDFImporter(final ModelConnection mc,
                       final Resource... contexts) throws RippleException {
        final boolean override = Ripple.getConfiguration().getBoolean(Ripple.PREFER_NEWEST_NAMESPACE_DEFINITIONS);

        stSink = new Sink<Statement, RippleException>() {
            public void put(final Statement st) throws RippleException {
//System.out.println( "adding statement: " + st );
                if (0 == contexts.length) {
                    mc.add(new RDFValue(st.getSubject()),
                            new RDFValue(st.getPredicate()),
                            new RDFValue(st.getObject()));
                } else {
                    for (Resource c : contexts) {
                        mc.add(new RDFValue(st.getSubject()),
                                new RDFValue(st.getPredicate()),
                                new RDFValue(st.getObject()),
                                new RDFValue(c));
                    }
                }
            }
        };

        nsSink = new Sink<Namespace, RippleException>() {
            public void put(final Namespace ns) throws RippleException {
                mc.setNamespace(ns.getPrefix(), ns.getName(), override);
            }
        };

        cmtSink = new Sink<String, RippleException>() {
            public void put(final String comment) throws RippleException {
            }
        };
    }

    public Sink<Statement, RippleException> statementSink() {
        return stSink;
    }

    public Sink<Namespace, RippleException> namespaceSink() {
        return nsSink;
    }

    public Sink<String, RippleException> commentSink() {
        return cmtSink;
    }
}

