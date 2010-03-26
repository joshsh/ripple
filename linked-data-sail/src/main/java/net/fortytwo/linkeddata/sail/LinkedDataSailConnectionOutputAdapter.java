/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.linkeddata.sail;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.NullSink;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.diff.DiffSink;
import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.flow.rdf.diff.RDFDiffSink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

public class LinkedDataSailConnectionOutputAdapter implements RDFDiffSink<RippleException>
{
	private final LinkedDataSailConnection sailConnection;
	private final RDFSink<RippleException> addSink, subSink;
    private final DiffSink<Statement, RippleException> stSink;
    private final DiffSink<Namespace, RippleException> nsSink;
    private final DiffSink<String, RippleException> cmtSink;

    public LinkedDataSailConnectionOutputAdapter( final LinkedDataSailConnection sc )
	{
		sailConnection = sc;

        final Sink<Statement, RippleException> stAddSink = new Sink<Statement, RippleException>()
        {
            public void put( final Statement st ) throws RippleException
            {
//System.out.println( "adding statement: " + st );
                sailConnection.addStatement( st );
            }
        };

        final Sink<Statement, RippleException> stSubSink = new Sink<Statement, RippleException>()
        {
            public void put( final Statement st ) throws RippleException
            {
                sailConnection.removeStatement( st );
            }
        };

        final Sink<Namespace, RippleException> nsAddSink = new Sink<Namespace, RippleException>()
        {
            public void put( final Namespace ns ) throws RippleException
            {
//System.out.println( "adding namespace: " + ns );
                sailConnection.addNamespace( ns );
            }
        };

        final Sink<Namespace, RippleException> nsSubSink = new Sink<Namespace, RippleException>()
        {
            public void put( final Namespace ns ) throws RippleException
            {
                sailConnection.removeNamespace( ns );
            }
        };

        final Sink<String, RippleException> cmtAddSink = new NullSink<String, RippleException>();

        final Sink<String, RippleException> cmtSubSink = new NullSink<String, RippleException>();

        addSink = new RDFSink<RippleException>()
		{
            public Sink<Statement, RippleException> statementSink()
			{
				return stAddSink;
			}
		
			public Sink<Namespace, RippleException> namespaceSink()
			{
				return nsAddSink;
			}
		
			public Sink<String, RippleException> commentSink()
			{
				return cmtAddSink;
			}
		};

		subSink = new RDFSink<RippleException>()
		{
			public Sink<Statement, RippleException> statementSink()
			{
				return stSubSink;
			}
		
			public Sink<Namespace, RippleException> namespaceSink()
			{
				return nsSubSink;
			}
		
			public Sink<String, RippleException> commentSink()
			{
				return cmtSubSink;
			}
		};

        stSink = new DiffSink<Statement, RippleException>()
        {
            public Sink<Statement, RippleException> getPlus()
            {
                return addSink.statementSink();
            }

            public Sink<Statement, RippleException> getMinus()
            {
                return subSink.statementSink();
            }
        };

        nsSink = new DiffSink<Namespace, RippleException>()
        {
            public Sink<Namespace, RippleException> getPlus()
            {
                return addSink.namespaceSink();
            }

            public Sink<Namespace, RippleException> getMinus()
            {
                return subSink.namespaceSink();
            }
        };

        cmtSink = new DiffSink<String, RippleException>()
        {
            public Sink<String, RippleException> getPlus()
            {
                return addSink.commentSink();
            }

            public Sink<String, RippleException> getMinus()
            {
                return subSink.commentSink();
            }
        };
	}

	public RDFSink adderSink()
	{
		return addSink;
	}

	public RDFSink subtractorSink()
	{
		return subSink;
	}

    public DiffSink<Statement, RippleException> statementSink()
    {
        return stSink;
    }

    public DiffSink<Namespace, RippleException> namespaceSink()
    {
        return nsSink;
    }

    public DiffSink<String, RippleException> commentSink()
    {
        return cmtSink;
    }
}

