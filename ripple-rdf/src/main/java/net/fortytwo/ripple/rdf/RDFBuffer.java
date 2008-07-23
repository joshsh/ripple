/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;


public class RDFBuffer<E extends Exception> extends RDFCollector<E>
{
	private final RDFSink<E> sink;

	public RDFBuffer( final RDFSink<E> sink )
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

