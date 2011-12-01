package net.fortytwo.ripple.config;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.RippleProperties;
import net.fortytwo.ripple.URIMap;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;

import java.io.File;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class MemoryStoreFactory implements SailFactory {

    public Class getSailClass() {
        return MemoryStore.class;
    }

    public Sail createSail(URIMap uriMap, SailConfiguration config) throws RippleException {
        MemoryStore sail = new MemoryStore();

        RippleProperties props = Ripple.getConfiguration();
        File persistFile = props.getFile(Ripple.MEMORYSTORE_PERSIST_FILE, null);

        // If a persist file has been specified, attempt to load from it.  A
        // missing persist file is tolerated.
        if (null != persistFile) {
            sail.setDataDir(persistFile);
            sail.setPersist(true);
        }

        try {
            sail.initialize();
        } catch (SailException e) {
            throw new RippleException(e);
        }

        return sail;
    }
}
