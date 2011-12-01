package net.fortytwo.ripple.config;

import net.fortytwo.linkeddata.WebClosure;
import net.fortytwo.linkeddata.sail.LinkedDataSail;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.RippleProperties;
import net.fortytwo.ripple.URIMap;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LinkedDataSailFactory implements SailFactory {
    public Class getSailClass() {
        return LinkedDataSail.class;
    }

    public Sail createSail(final URIMap uriMap,
                           final SailConfiguration config) throws RippleException {
        RippleProperties props = Ripple.getConfiguration();
        String baseSailType = props.getString(Ripple.LINKEDDATASAIL_BASE_SAIL);

        Sail base = config.createSail(baseSailType, uriMap);
        Sail sail = new LinkedDataSail(base, WebClosure.createDefault(base, uriMap));
        try {
            // Note: base Sail is already initialized.
            sail.initialize();
        } catch (SailException e) {
            throw new RippleException(e);
        }

        return sail;
    }
}
