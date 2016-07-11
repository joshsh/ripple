package net.fortytwo.ripple.config;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.RippleProperties;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.sesametools.readonly.ReadOnlySail;
import net.fortytwo.sesametools.replay.RecorderSail;
import org.openrdf.sail.Sail;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ServiceLoader;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SailConfiguration {
    private final Sail sail;
    private final ServiceLoader<SailFactory> loader;

    public SailConfiguration() throws RippleException {
        this(new URIMap());
    }

    public SailConfiguration(final URIMap uriMap) throws RippleException {
        loader = ServiceLoader.load(SailFactory.class);

        RippleProperties props = Ripple.getConfiguration();
        String sailType = props.getString(Ripple.SAIL_TYPE).trim();
        sail = createSail(sailType, uriMap);
    }

    public Sail getSail() {
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

        String sailLog = Ripple.getConfiguration().getString(Ripple.SAIL_LOG, null);
        if (null != sailLog) {
            try {
                sail = new RecorderSail(sail, new FileOutputStream(sailLog));
                // note: base Sail is already initialized
            } catch (FileNotFoundException e) {
                throw new RippleException(e);
            }
        }

        boolean readOnly = Ripple.getConfiguration().getBoolean(Ripple.READ_ONLY, false);
        if (readOnly) {
            sail = new ReadOnlySail(sail);
        }

        return sail;
    }
}
