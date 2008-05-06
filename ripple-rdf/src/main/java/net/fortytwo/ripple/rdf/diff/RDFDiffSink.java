/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf.diff;

import net.fortytwo.ripple.rdf.RDFSink;

public interface RDFDiffSink
{
	RDFSink adderSink();
	RDFSink subtractorSink();
}

