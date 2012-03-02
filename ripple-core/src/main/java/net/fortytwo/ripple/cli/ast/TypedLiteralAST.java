/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.RippleList;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

public class TypedLiteralAST implements AST<RippleList> {
    private final String value;
    private final AST<RippleList> type;

    public TypedLiteralAST(final String value, final AST<RippleList> type) {
        this.value = value;
        this.type = type;
    }

    public void evaluate(final Sink<RippleList> sink,
                         final QueryEngine qe,
                         final ModelConnection mc)
            throws RippleException {
        Sink<RippleList> typeSink = new Sink<RippleList>() {
            public void put(final RippleList l) throws RippleException {
                RippleValue type = l.getFirst();
                Value t = (URI) type.toRDF(mc).sesameValue();
                if (t instanceof URI) {
                    sink.put(mc.list().push(mc.typedValue(value, (URI) t)));
                } else {
                    qe.getErrorPrintStream().println("datatype does not map to a URI reference: " + type);
                }
            }
        };

        type.evaluate(typeSink, qe, mc);
    }

    public String toString() {
        return "\"" + value + "\"^^" + type;
    }
}

