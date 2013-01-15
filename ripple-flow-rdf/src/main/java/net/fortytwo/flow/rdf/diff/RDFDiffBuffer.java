package net.fortytwo.flow.rdf.diff;

import net.fortytwo.ripple.RippleException;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RDFDiffBuffer extends RDFDiffCollector {
	private final RDFDiffSink sink;

	public RDFDiffBuffer( final RDFDiffSink sink )
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

