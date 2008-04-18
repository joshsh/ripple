/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.LexiconUpdater;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelBridge;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.URIMap;

import org.apache.log4j.Logger;
import org.openrdf.sail.Sail;

public class SesameModel implements Model
{
	private static final Logger LOGGER = Logger.getLogger( Model.class );

	Sail sail;
	Set<ModelConnection> openConnections = new LinkedHashSet<ModelConnection>();

	private ModelBridge bridge;
	public ModelBridge getBridge()
	{
		return bridge;
	}

	public SesameModel( final Sail baseSail, final URIMap uriMap )
		throws RippleException
	{
		LOGGER.debug( "Creating new Model" );

		bridge = new ModelBridge();
		sail = baseSail;

		loadSymbols( uriMap );
	}

	public Collection<ModelConnection> openConnections()
	{
		Collection<ModelConnection> copy = new LinkedList<ModelConnection>();
		
		synchronized ( openConnections )
		{
			copy.addAll( openConnections );
		}
		
		return copy;
	}

	public void closeOpenConnections() throws RippleException
	{
		synchronized ( openConnections )
		{
			Iterator<ModelConnection> i = openConnections.iterator();
			while ( i.hasNext() )
			{
	//			ModelConnection mc = i.next();
	//			mc.close();
			}
		}
	}
	
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

	public ModelConnection getConnection( final String name )
		throws RippleException
	{
		return new SesameModelConnection( this, name, null );
	}

	public ModelConnection getConnection( final String name, final LexiconUpdater updater ) throws RippleException
	{
		return new SesameModelConnection( this, name, updater );
	}
}
