/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
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
        try {
            writeTo( sink );
        } catch (Exception e) {
            throw (E) e;
        }

        clear();
	}
}

