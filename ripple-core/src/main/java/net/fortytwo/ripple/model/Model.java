/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.flow.rdf.diff.RDFDiffSink;

public interface Model
{
	ModelConnection createConnection() throws RippleException;
	ModelConnection createConnection( RDFDiffSink listener ) throws RippleException;

    SpecialValueMap getSpecialValues();

    void shutDown() throws RippleException;
}
