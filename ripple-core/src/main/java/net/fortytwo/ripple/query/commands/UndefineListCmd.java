package net.fortytwo.ripple.query.commands;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import org.openrdf.model.Value;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class UndefineListCmd extends Command {
    private final String term;

    public UndefineListCmd(final String term) {
        this.term = term;
    }

    public void execute(final QueryEngine qe, final ModelConnection mc)
            throws RippleException {
        Value uri = mc.valueOf(java.net.URI.create(qe.getLexicon().getDefaultNamespace() + term));
//System.out.println("uri = " + uri);
        mc.remove(uri, null, null);
        mc.commit();
        mc.getModel().getSpecialValues().remove(uri);
    }

    public String getName() {
        return "unlist";
    }

    protected void abort() {
    }
}

