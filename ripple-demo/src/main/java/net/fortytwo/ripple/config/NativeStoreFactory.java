package net.fortytwo.ripple.config;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.RippleProperties;
import net.fortytwo.ripple.URIMap;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailException;
import org.openrdf.sail.nativerdf.NativeStore;

import java.io.File;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class NativeStoreFactory implements SailFactory {
    @Override
    public Class getSailClass() {
        return NativeStore.class;
    }

    @Override
    public Sail createSail(URIMap uriMap, SailConfiguration config) throws RippleException {
        RippleProperties props = Ripple.getConfiguration();
        File dir = props.getFile(Ripple.NATIVESTORE_DIRECTORY);
        String indexes = props.getString(Ripple.NATIVESTORE_INDEXES, null);

        Sail sail = (null == indexes)
                ? new NativeStore(dir)
                : new NativeStore(dir, indexes.trim());
        try {
            sail.initialize();
        } catch (SailException e) {
            throw new RippleException(e);
        }

        return sail;
    }
}
