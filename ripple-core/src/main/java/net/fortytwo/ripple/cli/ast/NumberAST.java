package net.fortytwo.ripple.cli.ast;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.QueryEngine;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class NumberAST implements AST<RippleList> {
    public abstract Number getValue(ModelConnection mc) throws RippleException;

    public void evaluate(final Sink<RippleList> sink,
                         final QueryEngine qe,
                         final ModelConnection mc)
            throws RippleException {
        sink.accept(mc.list().push(getValue(mc)));
    }

    // Strip off leading "+" from mantissa and/or exponent.
    // Convert exponent portion to lower case.
    // Eliminate trailing decimal point.
    // Do not bother with leading or trailing zeroes.
    protected String canonicalize(final String rep) {
        return rep.toLowerCase()
                .replaceAll("[+]", "")
                .replaceAll("[.]$", "")
                .replaceAll("[.]e", "");
    }
}
