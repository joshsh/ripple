/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/RDFBuffer.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;


import net.fortytwo.ripple.RippleException;

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

