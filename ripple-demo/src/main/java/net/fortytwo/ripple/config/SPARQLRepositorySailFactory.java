package net.fortytwo.ripple.config;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SPARQLRepositorySailFactory implements SailFactory {
    public Class getSailClass() {
        return SPARQLRepositorySail.class;
    }

    public Sail createSail(URIMap uriMap, SailConfiguration config) throws RippleException {
        String endpoint = Ripple.getConfiguration().getString(Ripple.SPARQL_ENDPOINTURL);
        try {
            return new SPARQLRepositorySail(endpoint);
        } catch (SailException e) {
            throw new RippleException(e);
        }
    }
}
