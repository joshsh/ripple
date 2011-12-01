package net.fortytwo.ripple.config;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.RippleProperties;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.sesametools.readonly.ReadOnlySail;
import org.openrdf.sail.Sail;

import java.util.ServiceLoader;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SailConfiguration {
    private final String sailType;
    private final Sail sail;
    private final ServiceLoader<SailFactory> loader;

    public SailConfiguration() throws RippleException {
        this(new URIMap());
    }

    public SailConfiguration(final URIMap uriMap) throws RippleException {
        loader = ServiceLoader.load(SailFactory.class);

        RippleProperties props = Ripple.getConfiguration();
        sailType = props.getString(Ripple.SAIL_TYPE).trim();
        sail = createSail(sailType, uriMap);
    }

    public Sail getSail() throws RippleException {
        return sail;
    }

    public Sail createSail(final String sailType,
                           final URIMap uriMap) throws RippleException {
        Sail sail = null;

        for (SailFactory f : loader) {
            if (f.getSailClass().getName().equals(sailType)) {
                sail = f.createSail(uriMap, this);
            }
        }

        if (null == sail) {
            throw new RippleException("unrecognized Sail type: " + sailType);
        }
        //System.out.println("creating Sail of type: " + sailType);

        boolean readOnly = Ripple.getConfiguration().getBoolean(Ripple.READ_ONLY, false);
        if (readOnly) {
            sail = new ReadOnlySail(sail);
        }

        return sail;
    }

    /*public static void main(final String[] args) throws Exception {
        URIMap map = new URIMap();
        SailConfiguration c = new SailConfiguration(map);
    }*/
}
