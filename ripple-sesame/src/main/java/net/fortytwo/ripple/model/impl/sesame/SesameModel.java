/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.SpecialValueMap;
import net.fortytwo.ripple.rdf.diff.RDFDiffSink;
import org.apache.log4j.Logger;
import org.openrdf.sail.Sail;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

public class SesameModel implements Model
{
	private static final Logger LOGGER = Logger.getLogger( SesameModel.class );

	final Sail sail;
    SpecialValueMap specialValues;
	final Set<ModelConnection> openConnections = new LinkedHashSet<ModelConnection>();
    final URIMap uriMap;

    public SesameModel( final Sail baseSail, final URL libraries, final URIMap uriMap )
		throws RippleException
	{
		LOGGER.debug( "Creating new SesameModel" );

		sail = baseSail;
        this.uriMap = uriMap;

        ModelConnection mc = getConnection( null );

        try
        {
            // TODO: eliminate this temporary value map
            specialValues = new SpecialValueMap();
            specialValues = new LibraryLoader().load( libraries, uriMap, mc );

            // At the moment, op needs to be a special value for the sake of the
            // evaluator.  This has the side-effect of making it a keyword.
            specialValues.add( Operator.OP, mc );
        }

        finally
        {
		    mc.close();
        }
    }

    public SpecialValueMap getSpecialValues()
    {
        return specialValues;
    }

    public URIMap getURIMap()
    {
        return uriMap;
    }

    public ModelConnection getConnection( final String name )
		throws RippleException
	{
		return new SesameModelConnection( this, name, null );
	}

	public ModelConnection getConnection( final String name, final RDFDiffSink listener ) throws RippleException
	{
		return new SesameModelConnection( this, name, listener );
	}

    public void shutDown() throws RippleException {
        // Warn of any open connections, then close them
        synchronized (openConnections) {
            if ( openConnections.size() > 0 )
            {
                StringBuilder sb = new StringBuilder();
                sb.append( openConnections.size() ).append( " dangling connections: \"" );
                boolean first = true;
                for ( ModelConnection mc : openConnections )
                {
                    if ( first )
                    {
                        first = false;
                    }
                    
                    else
                    {
                        sb.append( "," );
                    }

                    sb.append( mc.getName() );
                }

                LOGGER.warn( sb.toString() );

                for ( ModelConnection mc : openConnections )
                {
                    mc.close();
                }
            }
        }
    }
}
