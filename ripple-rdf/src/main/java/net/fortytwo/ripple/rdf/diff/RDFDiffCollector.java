/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf.diff;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.rdf.RDFCollector;
import net.fortytwo.ripple.rdf.RDFSink;
import net.fortytwo.ripple.rdf.RDFSource;

public class RDFDiffCollector implements RDFDiffSource, RDFDiffSink
{
	private final RDFCollector adderCollector, subtractorCollector;

	public RDFDiffCollector()
	{
		adderCollector = new RDFCollector();
		subtractorCollector = new RDFCollector();
	}

	public RDFSink adderSink()
	{
		return adderCollector;
	}

	public RDFSink subtractorSink()
	{
		return subtractorCollector;
	}

	public RDFSource adderSource()
	{
		return adderCollector;
	}

	public RDFSource subtractorSource()
	{
		return subtractorCollector;
	}

	public void writeTo( final RDFDiffSink sink ) throws RippleException
	{
		adderCollector.writeTo( sink.adderSink() );
		subtractorCollector.writeTo( sink.subtractorSink() );
	}

	public void clear()
	{
		adderCollector.clear();
		subtractorCollector.clear();
	}
}

