/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.rdf.diff.RDFDiffSink;

/**
 * A gateway between an RDF data store and the native Ripple environment of stacks and streams.
 * Most of the things you can do in Ripple involve a <code>ModelConnection</code>,
 * which provides transactional access to a <code>Model</code>.
 *
 * Native Ripple values are totally ordered in a Model,
 * and each one maps to exactly one RDF resource.
 */
public interface Model {
    /**
     * @return a new, transactional connection to this model
     * @throws RippleException if creation of the connection fails
     */
    ModelConnection createConnection() throws RippleException;

    /**
     * @param listener a handler for RDF diffs of data added or removed from the model through the returned connection
     * @return a new, transactional connection to this model, to which the given listener is attached
     * @throws RippleException if creation of the connection fails
     */
    ModelConnection createConnection(RDFDiffSink listener) throws RippleException;

    /**
     * @return a mapping of RDF space into Ripple space
     */
    SpecialValueMap getSpecialValues();

    /**
     * Shuts down this model, releasing its resources.
     *
     * @throws RippleException if anything goes wrong
     */
    void shutDown() throws RippleException;
}
