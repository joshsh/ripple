package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.SynchronizedSink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SynchronizedRDFSink implements RDFSink {
	private final SynchronizedSink<Statement> stSink;
	private final SynchronizedSink<Namespace> nsSink;
	private final SynchronizedSink<String> comSink;

	public SynchronizedRDFSink( final RDFSink sink, final Object mutex )
	{
		stSink = new SynchronizedSink<Statement>( sink.statementSink(), mutex );
		nsSink = new SynchronizedSink<Namespace>( sink.namespaceSink(), mutex );
		comSink = new SynchronizedSink<String>( sink.commentSink(), mutex );
	}

	public Sink<Statement> statementSink()
	{
		return stSink;
	}

	public Sink<Namespace> namespaceSink()
	{
		return nsSink;
	}

	public Sink<String> commentSink()
	{
		return comSink;
	}

    public Object getMutex()
    {
        return stSink.getMutex();
    }
}

