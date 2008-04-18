/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf.diff;

import net.fortytwo.ripple.RippleException;

import net.fortytwo.ripple.rdf.RDFSource;

public interface RdfDiffSource
{
	RDFSource adderSource();
	RDFSource subtractorSource();
	void writeTo( RdfDiffSink sink ) throws RippleException;
}

// kate: tab-width 4
