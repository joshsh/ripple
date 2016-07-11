package net.fortytwo.ripple.cli.ast;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.QueryEngine;
import org.openrdf.model.IRI;
import org.openrdf.model.Value;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
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
        Sink<RippleList> typeSink = l -> {
            Object type1 = l.getFirst();
            Value t = mc.toRDF(type1);
            if (t instanceof IRI) {
                Object p = mc.valueOf(value, (IRI) t);
                sink.accept(mc.list().push(p));
            } else {
                qe.getErrorPrintStream().println("datatype does not map to a URI reference: " + type1);
            }
        };

        type.evaluate(typeSink, qe, mc);
    }

    public String toString() {
        return "\"" + value + "\"^^" + type;
    }
}

