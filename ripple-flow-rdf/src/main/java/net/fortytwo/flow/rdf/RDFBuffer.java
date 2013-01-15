package net.fortytwo.flow.rdf;

import net.fortytwo.ripple.RippleException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RDFBuffer extends RDFCollector {
	private final RDFSink sink;

	public RDFBuffer( final RDFSink sink )
	{
		super();
		this.sink = sink;
	}

	public void flush() throws RippleException
	{
            writeTo( sink );

        clear();
	}
}

