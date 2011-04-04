/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/RDFBuffer.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow.rdf;


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

