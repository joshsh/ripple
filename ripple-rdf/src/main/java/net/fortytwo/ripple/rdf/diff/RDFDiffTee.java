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

public class RDFDiffTee implements RDFDiffSink
{
	private RdfTee adderTee, subtractorTee;

	public RDFDiffTee( final RDFDiffSink sinkA, final RDFDiffSink sinkB )
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

