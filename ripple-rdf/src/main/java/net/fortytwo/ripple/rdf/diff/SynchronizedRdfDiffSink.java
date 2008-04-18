/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf.diff;

import net.fortytwo.ripple.rdf.RDFSink;
import net.fortytwo.ripple.rdf.SynchronizedRDFSink;

public class SynchronizedRdfDiffSink implements RdfDiffSink
{
	private RDFSink addSink, subSink;

	public SynchronizedRdfDiffSink( final RdfDiffSink sink, final Object synch )
	{
		addSink = new SynchronizedRDFSink( sink.adderSink(), synch );
		subSink = new SynchronizedRDFSink( sink.subtractorSink(), synch );
	}

	public RDFSink adderSink()
	{
		return addSink;
	}

	public RDFSink subtractorSink()
	{
		return subSink;
	}
}

// kate: tab-width 4
