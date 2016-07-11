package net.fortytwo.ripple.cli.ast;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.QueryEngine;
import org.openrdf.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class URIAST implements AST<RippleList> {
    private static final Logger logger = LoggerFactory.getLogger(URIAST.class);
    private final URI value;

    public URIAST(final String escapedValue) {
        String s;

        try {
            s = StringUtils.unescapeUriString(escapedValue);
        } catch (RippleException e) {
            logger.error("failed to unescape URI", e);
            throw new IllegalStateException(e);
        }

        this.value = URI.create(s);
    }

    public void evaluate(final Sink<RippleList> sink,
                         final QueryEngine qe,
                         final ModelConnection mc)
            throws RippleException {
        Value v = mc.valueOf(value);
        sink.accept(mc.list().push(mc.canonicalValue(v)));
    }

    public String toString() {
        return "<" + StringUtils.escapeURIString(value.toString()) + ">";
    }
}

