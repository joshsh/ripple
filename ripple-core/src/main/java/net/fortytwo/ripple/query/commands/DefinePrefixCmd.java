package net.fortytwo.ripple.query.commands;

import net.fortytwo.flow.Collector;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.ast.URIAST;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class DefinePrefixCmd extends Command {
    private final String prefix;
    private final URIAST uri;

    public DefinePrefixCmd(final String prefix, final URIAST uri) {
        this.prefix = prefix;
        this.uri = uri;
    }

    public String getPrefix() {
        return prefix;
    }

    public URIAST getUri() {
        return uri;
    }

    public void execute(final QueryEngine qe, final ModelConnection mc)
            throws RippleException {
        Collector<RippleList> sink = new Collector<RippleList>();
        uri.evaluate(sink, qe, mc);

        if (sink.size() == 0) {
            throw new RippleException("URI could not be constructed from " + uri);
        } else if (sink.size() > 1) {
            throw new RippleException("multiple values constructed from " + uri);
        }

        String ns = sink.iterator().next().getFirst().toString();

        mc.setNamespace(prefix, ns, true);

        // Note: when a namespace is manually defined, it may both override an
        // existing prefix with the same name, or duplicate another namespace
        // with the same URI.
        qe.getLexicon().setNamespace(prefix, ns, mc);
    }

    public String getName() {
        return "prefix";
    }

    protected void abort() {
    }
}

