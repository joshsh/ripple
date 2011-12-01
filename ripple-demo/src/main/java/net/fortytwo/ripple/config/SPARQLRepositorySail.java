package net.fortytwo.ripple.config;

import net.fortytwo.sesametools.reposail.RepositorySail;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.SPARQLRepository;
import org.openrdf.sail.SailException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SPARQLRepositorySail extends RepositorySail {
    public SPARQLRepositorySail(final String endpoint) throws SailException {
        super(createRepo(endpoint), false);
    }

    private static Repository createRepo(final String endpoint) throws SailException {
        Repository repo = new SPARQLRepository(endpoint);
        try {
            repo.initialize();
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
        return repo;
    }
}
