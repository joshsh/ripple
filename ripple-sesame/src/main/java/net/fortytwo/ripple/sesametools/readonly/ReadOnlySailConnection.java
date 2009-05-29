package net.fortytwo.ripple.sesametools.readonly;

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.helpers.SailConnectionWrapper;
import org.openrdf.sail.SailReadOnlyException;

/**
 * Author: josh
 * Date: May 28, 2009
 * Time: 12:07:56 PM
 */
public class ReadOnlySailConnection extends SailConnectionWrapper {
    public ReadOnlySailConnection(final SailConnection wrapped) {
        super(wrapped);
    }

    @Override
    public void addStatement(final Resource subject,
                             final URI predicate,
                             final Value object,
                             final Resource... contexts) throws SailException {
        throw new SailReadOnlyException("write operation not allowed");
    }

    @Override
    public void clear(final Resource... contexts) throws SailException {
        throw new SailReadOnlyException("write operation not allowed");
    }

    @Override
    public void clearNamespaces() throws SailException {
        throw new SailReadOnlyException("write operation not allowed");
    }

    @Override
    public void removeNamespace(final String prefix) throws SailException {
        throw new SailReadOnlyException("write operation not allowed");
    }

    @Override
    public void removeStatements(final Resource subject,
                                 final URI predicate,
                                 final Value object,
                                 final Resource... contexts) throws SailException {
        throw new SailReadOnlyException("write operation not allowed");
    }

    @Override
    public void setNamespace(final String prefix,
                             final String uri) throws SailException {
        throw new SailReadOnlyException("write operation not allowed");
    }
}
