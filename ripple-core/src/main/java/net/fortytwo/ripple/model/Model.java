/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import java.util.Collection;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.rdf.diff.RDFDiffSink;

public interface Model
{
	ModelBridge getBridge();

	ModelConnection getConnection( String name ) throws RippleException;
	ModelConnection getConnection( String name, RDFDiffSink listener ) throws RippleException;

    void shutDown() throws RippleException;
}
