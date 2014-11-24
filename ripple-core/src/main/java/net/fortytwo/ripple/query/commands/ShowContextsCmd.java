package net.fortytwo.ripple.query.commands;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ShowContextsCmd extends Command {
    public void execute(final QueryEngine qe, final ModelConnection mc)
            throws RippleException {
        final RipplePrintStream ps = qe.getPrintStream();

        Sink<Object> printSink = new Sink<Object>() {
            private int i = 0;

            public void put(final Object v) throws RippleException {
                ps.print("[" + i++ + "] ");
                ps.println(v);
            }
        };

        ps.println("");
        mc.getContexts().writeTo(printSink);
        ps.println("");
    }

    public String getName() {
        return "show contexts";
    }

    protected void abort() {
    }
}

