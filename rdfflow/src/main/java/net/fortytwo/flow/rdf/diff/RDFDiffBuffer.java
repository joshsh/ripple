/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/diff/RDFDiffBuffer.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.flow.rdf.diff;

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

