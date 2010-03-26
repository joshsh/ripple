/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf.diff;

import net.fortytwo.ripple.rdf.RDFSource;

public interface RDFDiffSource<E extends Exception>
{
	RDFSource<E> adderSource();
	RDFSource<E> subtractorSource();

	void writeTo( RDFDiffSink<E> sink ) throws E;
}

