package net.fortytwo.ripple.sail;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.impl.sesame.SesameModel;
import org.openrdf.IsolationLevel;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

import java.io.File;
import java.util.List;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleSail implements Sail {

    private final RippleValueFactory valueFactory = new RippleValueFactory(SimpleValueFactory.getInstance());
    private final Model model;

    public RippleSail(final Model model) {
        this.model = model;
    }

    public RippleSail(final Sail base) throws RippleException {
        model = new SesameModel(base);
    }

    private boolean asynch;

    @Override
    public void initialize() throws SailException {
        // FIXME: cheat to temporarily disable asynchronous query answering
        asynch = Ripple.asynchronousQueries();
        Ripple.enableAsynchronousQueries(false);
    }

    @Override
    public void shutDown() throws SailException {
        Ripple.enableAsynchronousQueries(asynch);

        try {
            model.shutDown();
        } catch (RippleException e) {
            throw new SailException(e);
        }
        //base.shutDown();
    }

    @Override
    public SailConnection getConnection() throws SailException {
        ModelConnection mc;
        try {
            mc = model.createConnection();
        } catch (RippleException e) {
            throw new SailException(e);
        }
        return new RippleSailConnection(mc, valueFactory);
    }

    @Override
    public ValueFactory getValueFactory() {
        return valueFactory;
    }

    @Override
    public List<IsolationLevel> getSupportedIsolationLevels() {
        return model.getSupportedIsolationLevels();
    }

    @Override
    public IsolationLevel getDefaultIsolationLevel() {
        return model.getDefaultIsolationLevel();
    }

    @Override
    public void setDataDir(final File file) {
        throw new UnsupportedOperationException();
    }

    @Override
    public File getDataDir() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWritable() throws SailException {
        return true;
    }
}
