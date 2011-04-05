package net.fortytwo.ripple.model.impl.blueprints;

import com.tinkerpop.blueprints.pgm.Graph;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.SpecialValueMap;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.flow.rdf.diff.RDFDiffSink;

/**
 * User: josh
 * Date: Nov 19, 2010
 * Time: 2:37:33 PM
 */
public class BlueprintsModel implements Model {
    private final Graph graph;

    public BlueprintsModel(final Graph graph) {
        this.graph = graph;
    }

    public ModelConnection createConnection() throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ModelConnection createConnection(final RDFDiffSink listener) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public SpecialValueMap getSpecialValues() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public URIMap getURIMap() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void shutDown() throws RippleException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
