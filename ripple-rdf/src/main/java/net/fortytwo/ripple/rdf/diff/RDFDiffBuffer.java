/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf.diff;

public class RDFDiffBuffer<E extends Exception> extends RDFDiffCollector<E>
{
	private final RDFDiffSink<E> sink;

	public RDFDiffBuffer( final RDFDiffSink<E> sink )
	{
		super();
		this.sink = sink;
	}

	public void flush() throws E
	{
		writeTo( sink );
		clear();
	}
}

