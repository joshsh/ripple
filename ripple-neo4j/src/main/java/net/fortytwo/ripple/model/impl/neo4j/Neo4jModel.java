/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.neo4j;

import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.impl.sesame.SesameModel;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.rdf.diff.RDFDiffSink;

import java.util.Collection;
import java.net.URL;

import org.neo4j.api.core.NeoService;

/**
 * Author: josh
 * Date: Mar 13, 2008
 * Time: 12:03:01 PM
 */
public class Neo4jModel extends SesameModel {
    private final NeoService service;

    public Neo4jModel(final NeoService service, final URL libraries, final URIMap uriMap) throws RippleException {
        super(null, libraries, uriMap);
        this.service = service;
    }

    @Override
    public ModelConnection getConnection(final String name) throws RippleException {
        return new Neo4jModelConnection(this, service, name);
    }

    @Override
    public ModelConnection getConnection(final String name, final RDFDiffSink listener) throws RippleException {
        // TODO: use the listener
        return getConnection(name);
    }
}
