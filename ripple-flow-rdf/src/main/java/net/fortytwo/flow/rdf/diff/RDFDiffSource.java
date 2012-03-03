/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/diff/RDFDiffSource.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.rdf.RDFSource;
import net.fortytwo.ripple.RippleException;

public interface RDFDiffSource {
	RDFSource adderSource();
	RDFSource subtractorSource();

	void writeTo( RDFDiffSink sink ) throws RippleException;
}

