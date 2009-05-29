/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.rdf.diff.RDFDiffSink;

public interface Model
{
	ModelConnection getConnection( String name ) throws RippleException;
	ModelConnection getConnection( String name, RDFDiffSink listener ) throws RippleException;

    SpecialValueMap getSpecialValues();

    URIMap getURIMap();

    void shutDown() throws RippleException;
}
