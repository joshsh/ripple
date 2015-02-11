package net.fortytwo.ripple.cli.ast;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.QueryEngine;
import org.openrdf.model.Value;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class URIAST implements AST<RippleList> {
    private static final Logger logger = Logger.getLogger(URIAST.class.getName());
    private final URI value;

    public URIAST(final String escapedValue) {
        String s;

        try {
            s = StringUtils.unescapeUriString(escapedValue);
        } catch (RippleException e) {
            logger.log(Level.SEVERE, "failed to unescape URI", e);
            throw new IllegalStateException(e);
        }

        this.value = URI.create(s);
    }

    public void evaluate(final Sink<RippleList> sink,
                         final QueryEngine qe,
                         final ModelConnection mc)
            throws RippleException {
        Value v = mc.valueOf(value);
        sink.put(mc.list().push(mc.canonicalValue(v)));
    }

    public String toString() {
        return "<" + StringUtils.escapeURIString(value.toString()) + ">";
    }
}

