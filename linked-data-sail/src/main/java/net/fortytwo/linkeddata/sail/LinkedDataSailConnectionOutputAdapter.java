package net.fortytwo.linkeddata.sail;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.NullSink;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.diff.DiffSink;
import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.flow.rdf.diff.RDFDiffSink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LinkedDataSailConnectionOutputAdapter implements RDFDiffSink {
	private final LinkedDataSailConnection sailConnection;
	private final RDFSink addSink, subSink;
    private final DiffSink<Statement> stSink;
    private final DiffSink<Namespace> nsSink;
    private final DiffSink<String> cmtSink;

    public LinkedDataSailConnectionOutputAdapter( final LinkedDataSailConnection sc )
	{
		sailConnection = sc;

        final Sink<Statement> stAddSink = new Sink<Statement>()
        {
            public void put( final Statement st ) throws RippleException
            {
//System.out.println( "adding statement: " + st );
                sailConnection.addStatement( st );
            }
        };

        final Sink<Statement> stSubSink = new Sink<Statement>()
        {
            public void put( final Statement st ) throws RippleException
            {
                sailConnection.removeStatement( st );
            }
        };

        final Sink<Namespace> nsAddSink = new Sink<Namespace>()
        {
            public void put( final Namespace ns ) throws RippleException
            {
//System.out.println( "adding namespace: " + ns );
                sailConnection.addNamespace( ns );
            }
        };

        final Sink<Namespace> nsSubSink = new Sink<Namespace>()
        {
            public void put( final Namespace ns ) throws RippleException
            {
                sailConnection.removeNamespace( ns );
            }
        };

        final Sink<String> cmtAddSink = new NullSink<String>();

        final Sink<String> cmtSubSink = new NullSink<String>();

        addSink = new RDFSink()
		{
            public Sink<Statement> statementSink()
			{
				return stAddSink;
			}
		
			public Sink<Namespace> namespaceSink()
			{
				return nsAddSink;
			}
		
			public Sink<String> commentSink()
			{
				return cmtAddSink;
			}
		};

		subSink = new RDFSink()
		{
			public Sink<Statement> statementSink()
			{
				return stSubSink;
			}
		
			public Sink<Namespace> namespaceSink()
			{
				return nsSubSink;
			}
		
			public Sink<String> commentSink()
			{
				return cmtSubSink;
			}
		};

        stSink = new DiffSink<Statement>()
        {
            public Sink<Statement> getPlus()
            {
                return addSink.statementSink();
            }

            public Sink<Statement> getMinus()
            {
                return subSink.statementSink();
            }
        };

        nsSink = new DiffSink<Namespace>()
        {
            public Sink<Namespace> getPlus()
            {
                return addSink.namespaceSink();
            }

            public Sink<Namespace> getMinus()
            {
                return subSink.namespaceSink();
            }
        };

        cmtSink = new DiffSink<String>()
        {
            public Sink<String> getPlus()
            {
                return addSink.commentSink();
            }

            public Sink<String> getMinus()
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

    public DiffSink<Statement> statementSink()
    {
        return stSink;
    }

    public DiffSink<Namespace> namespaceSink()
    {
        return nsSink;
    }

    public DiffSink<String> commentSink()
    {
        return cmtSink;
    }
}

