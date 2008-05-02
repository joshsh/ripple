/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.linkeddata.sail;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.NullSink;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.rdf.RDFSink;
import net.fortytwo.ripple.rdf.diff.RDFDiffSink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

// Note: for maximum clarity, this class should probably be called
//       "LinkedDataSailConnectionOutputAdapter"
public class SailConnectionOutputAdapter implements RDFDiffSink
{
	private LinkedDataSailConnection sailConnection;
	private RDFSink addSink, subtractSink;

	public SailConnectionOutputAdapter( final LinkedDataSailConnection sc )
	{
		sailConnection = sc;

		addSink = new RDFSink()
		{
			private Sink<Statement, RippleException> stSink = new Sink<Statement, RippleException>()
			{
				public void put( final Statement st ) throws RippleException
				{
//System.out.println( "adding statement: " + st );
					sailConnection.addStatement( st );
				}
			};

			private Sink<Namespace, RippleException> nsSink = new Sink<Namespace, RippleException>()
			{
				public void put( final Namespace ns ) throws RippleException
				{
//System.out.println( "adding namespace: " + ns );
					sailConnection.addNamespace( ns );
				}
			};

			private Sink<String, RippleException> cmtSink = new NullSink<String, RippleException>();

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
		};

		subtractSink = new RDFSink()
		{
			private Sink<Statement, RippleException> stSink = new Sink<Statement, RippleException>()
			{
				public void put( final Statement st ) throws RippleException
				{
					sailConnection.removeStatement( st );
				}
			};

			private Sink<Namespace, RippleException> nsSink = new Sink<Namespace, RippleException>()
			{
				public void put( final Namespace ns ) throws RippleException
				{
					sailConnection.removeNamespace( ns );
				}
			};

			private Sink<String, RippleException> cmtSink = new NullSink<String, RippleException>();

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
		};
	}

	public RDFSink adderSink()
	{
		return addSink;
	}

	public RDFSink subtractorSink()
	{
		return subtractSink;
	}
}

// kate: tab-width 4
