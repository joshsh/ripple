package net.fortytwo.ripple.sesametools.readonly;

import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.helpers.SailWrapper;

/**
 * Author: josh
 * Date: May 28, 2009
 * Time: 12:04:59 PM
 */
public class ReadOnlySail extends SailWrapper {
    public ReadOnlySail(final Sail baseSail) {
        super(baseSail);
    }

    @Override
    public SailConnection getConnection() throws SailException {
        return new ReadOnlySailConnection(getBaseSail().getConnection());
    }

    @Override
    public boolean isWritable() {
        return false;
    }
}
