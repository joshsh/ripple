/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf.diff;

import net.fortytwo.ripple.RippleException;

public class RdfDiffBuffer extends RdfDiffCollector
{
	private RdfDiffSink sink;

	public RdfDiffBuffer( final RdfDiffSink sink )
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

// kate: tab-width 4
