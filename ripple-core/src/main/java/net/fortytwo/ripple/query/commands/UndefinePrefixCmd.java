package net.fortytwo.ripple.query.commands;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
// TODO: unit testing
public class UndefinePrefixCmd extends Command {
    private final String prefix;

    public UndefinePrefixCmd(final String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void execute(final QueryEngine qe, final ModelConnection mc)
            throws RippleException {
        mc.setNamespace(prefix, null, true);

        // Note: when a namespace is manually defined, it may both override an
        // existing prefix with the same name, or duplicate another namespace
        // with the same URI.
        qe.getLexicon().removeNamespace(prefix);
    }

    public String getName() {
        return "unprefix";
    }

    protected void abort() {
    }
}
