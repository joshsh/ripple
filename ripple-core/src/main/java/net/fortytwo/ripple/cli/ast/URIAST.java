package net.fortytwo.ripple.cli.ast;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.QueryEngine;

import java.net.URI;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class URIAST implements AST<RippleList> {
    private final URI value;

    public URIAST(final String escapedValue) {
        String s;

        try {
            s = StringUtils.unescapeUriString(escapedValue);
        } catch (RippleException e) {
            e.logError();
            s = null;
            System.exit(1);
        }

        this.value = URI.create(s);
    }

    public void evaluate(final Sink<RippleList> sink,
                         final QueryEngine qe,
                         final ModelConnection mc)
            throws RippleException {
        RDFValue v = mc.valueOf(value);
        sink.put(mc.list().push(mc.canonicalValue(v)));
    }

    public String toString() {
        return "<" + StringUtils.escapeURIString(value.toString()) + ">";
    }
}

