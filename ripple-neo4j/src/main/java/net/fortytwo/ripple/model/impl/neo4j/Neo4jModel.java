/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.neo4j;

import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelBridge;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.LexiconUpdater;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;

import java.util.Collection;

/**
 * Author: josh
 * Date: Mar 13, 2008
 * Time: 12:03:01 PM
 */
public class Neo4jModel implements Model {
	private ModelBridge bridge;

    public Neo4jModel(final URIMap uriMap) throws RippleException {
        bridge = new ModelBridge();
        loadSymbols(uriMap);
    }

    public ModelBridge getBridge() {
        return bridge;
    }

    public ModelConnection getConnection(final String name) throws RippleException {
        return new Neo4jModelConnection(this, name);
    }

    public ModelConnection getConnection(final String name, final LexiconUpdater updater) throws RippleException {
        return new Neo4jModelConnection(this, name);
    }

    public long countStatements() throws RippleException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Collection<ModelConnection> openConnections() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void closeOpenConnections() throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }

    // FIXME: code duplicated from SesameModel
    private void loadSymbols( final URIMap uriMap )
		throws RippleException
	{
		ModelConnection mc = getConnection( "for Model.loadSymbols" );

        try {
            // At the moment, op needs to be a special value for the sake of the
            // evaluator.  This has the side-effect of making it a keyword.
            bridge.add( Operator.OP, mc );

            LibraryLoader loader = new LibraryLoader();

			loader.load( uriMap, mc );
		} catch ( RippleException e ) {
			throw e;
		} finally {
		    mc.close();
        }
    }
}
