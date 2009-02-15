/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
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
        try {
            writeTo( sink );
		clear();
        } catch (Exception e) {
            throw (E) e;
        }
    }
}

