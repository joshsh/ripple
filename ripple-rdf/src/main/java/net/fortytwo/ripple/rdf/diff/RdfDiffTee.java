/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf.diff;

import net.fortytwo.ripple.rdf.RDFSink;
import net.fortytwo.ripple.rdf.RdfTee;

public class RdfDiffTee implements RdfDiffSink
{
	private RdfTee adderTee, subtractorTee;

	public RdfDiffTee( final RdfDiffSink sinkA, final RdfDiffSink sinkB )
	{
		adderTee = new RdfTee( sinkA.adderSink(), sinkB.adderSink() );
		subtractorTee = new RdfTee( sinkA.subtractorSink(), sinkB.subtractorSink() );
	}

	public RDFSink adderSink()
	{
		return adderTee;
	}

	public RDFSink subtractorSink()
	{
		return subtractorTee;
	}
}

// kate: tab-width 4
