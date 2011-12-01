package net.fortytwo.ripple.config;

import com.knowledgereefsystems.agsail.AllegroSail;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.RippleProperties;
import net.fortytwo.ripple.URIMap;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailException;

import java.io.File;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class AllegroSailFactory implements SailFactory {
    @Override
    public Class getSailClass() {
        return AllegroSail.class;
    }

    @Override
    public Sail createSail(URIMap uriMap, SailConfiguration config) throws RippleException {
        RippleProperties props = Ripple.getConfiguration();

        String host = props.getString(Ripple.ALLEGROSAIL_HOST);
        int port = props.getInt(Ripple.ALLEGROSAIL_PORT);
        boolean start = props.getBoolean(Ripple.ALLEGROSAIL_START);
        String name = props.getString(Ripple.ALLEGROSAIL_NAME);
        File directory = props.getFile(Ripple.ALLEGROSAIL_DIRECTORY);

        Sail sail;
        //try {
        sail = new AllegroSail(host, port, start, name, directory, 0, 0, false, false);
        //} catch (AllegroSailException e) {
        //    throw new RippleException(e);
        //}

        try {
            sail.initialize();
        } catch (SailException e) {
            throw new RippleException(e);
        }

        return sail;
    }
}
