package net.fortytwo.ripple.query;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.control.Task;
import net.fortytwo.ripple.model.ModelConnection;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class Command extends Task {
    private QueryEngine queryEngine = null;

    protected abstract void abort();

    public abstract void execute(QueryEngine qe, ModelConnection mc)
            throws RippleException;

    public abstract String getName();

    public void setQueryEngine(final QueryEngine qe) {
        queryEngine = qe;
    }

    protected void executeProtected() throws RippleException {
        if (null == queryEngine) {
            throw new RippleException("null QueryEngine");
        }

        queryEngine.executeCommand(this);
    }

    protected void stopProtected() {
        abort();
    }
}

