/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/diff/RDFDiffBuffer.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.flow.rdf.diff;

import net.fortytwo.ripple.RippleException;

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

