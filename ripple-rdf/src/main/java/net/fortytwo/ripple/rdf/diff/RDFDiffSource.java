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

public interface RDFDiffSource
{
	RDFSource adderSource();
	RDFSource subtractorSource();
	void writeTo( RDFDiffSink sink ) throws RippleException;
}

