package net.fortytwo.ripple.cli.ast;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.QueryEngine;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class KeywordAST implements AST<RippleList> {
    private final String name;

    public KeywordAST(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public void evaluate(final Sink<RippleList> sink,
                         final QueryEngine qe,
                         final ModelConnection mc)
            throws RippleException {
        Sink<Object> uriSink = new Sink<Object>() {
            public void put(final Object v) throws RippleException {
                sink.put(mc.list().push(v));
            }
        };

        qe.getLexicon().resolveKeyword(name, uriSink, mc, qe.getErrorPrintStream());
    }
}

