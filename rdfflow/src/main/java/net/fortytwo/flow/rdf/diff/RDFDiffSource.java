/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/diff/RDFDiffSource.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.rdf.RDFSource;

public interface RDFDiffSource<E extends Exception>
{
	RDFSource<E> adderSource();
	RDFSource<E> subtractorSource();

	void writeTo( RDFDiffSink<E> sink ) throws E;
}

