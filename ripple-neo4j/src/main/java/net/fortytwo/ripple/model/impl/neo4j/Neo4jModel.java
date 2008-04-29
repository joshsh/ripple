/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.neo4j;

import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelBridge;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.LexiconUpdater;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.model.impl.sesame.SesameModel;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;

import java.util.Collection;

import org.neo4j.api.core.NeoService;

/**
 * Author: josh
 * Date: Mar 13, 2008
 * Time: 12:03:01 PM
 */
public class Neo4jModel extends SesameModel {
    private final NeoService service;

    public Neo4jModel(final NeoService service, final URIMap uriMap) throws RippleException {
        super(null, uriMap);
        this.service = service;
    }

    @Override
    public ModelConnection getConnection(final String name) throws RippleException {
        return new Neo4jModelConnection(this, service, name);
    }

    @Override
    public ModelConnection getConnection(final String name, final LexiconUpdater updater) throws RippleException {
        // TODO: use the LexiconUpdater
        return getConnection(name);
    }
}
