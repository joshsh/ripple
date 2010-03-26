/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.io;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.flow.rdf.RDFSink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;

// TODO: change this class to use a SailConnection instead of a ModelConnection
public class RDFImporter implements RDFSink
{
	private final Sink<Statement, RippleException> stSink;
	private final Sink<Namespace, RippleException> nsSink;
	private final Sink<String, RippleException> cmtSink;

	public RDFImporter( final ModelConnection mc,
						final Resource... contexts ) throws RippleException
	{
		final boolean override = Ripple.getProperties().getBoolean(Ripple.PREFER_NEWEST_NAMESPACE_DEFINITIONS);

		stSink = new Sink<Statement, RippleException>()
		{
			public void put( final Statement st ) throws RippleException
			{
//System.out.println( "adding statement: " + st );
				mc.add( st, contexts );
			}
		};

		nsSink = new Sink<Namespace, RippleException>()
		{
			public void put( final Namespace ns ) throws RippleException
			{
				mc.setNamespace( ns.getPrefix(), ns.getName(), override );
			}
		};

		cmtSink = new Sink<String, RippleException>()
		{
			public void put( final String comment ) throws RippleException
			{
			}
		};
	}

	public Sink<Statement, RippleException> statementSink()
	{
		return stSink;
	}

	public Sink<Namespace, RippleException> namespaceSink()
	{
		return nsSink;
	}

	public Sink<String, RippleException> commentSink()
	{
		return cmtSink;
	}
}

