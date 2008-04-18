/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.neo4j;

import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RdfValue;
import net.fortytwo.ripple.model.GetStatementsQuery;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.rdf.RDFSource;
import net.fortytwo.ripple.flow.Sink;
import org.openrdf.model.URI;
import org.openrdf.model.Statement;
import org.openrdf.model.Resource;
import org.openrdf.model.Value;
import org.openrdf.model.Namespace;

import java.io.OutputStream;
import java.util.Date;

/**
 * Author: josh
 * Date: Mar 13, 2008
 * Time: 12:03:11 PM
 */
public class Neo4jModelConnection implements ModelConnection {
    private String name;
    private Model model;

    public Neo4jModelConnection(final Model model, final String name) {
        this.model = model;
        this.name = name;
    }

    public Model getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public void close() throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }

    public void reset(boolean rollback) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }

    public void commit() throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean toBoolean(RippleValue v) throws RippleException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public NumericValue toNumericValue(RippleValue v) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Date toDateValue(RippleValue v) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String toString(RippleValue v) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public URI toUri(RippleValue v) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void toList(RippleValue v, Sink<RippleList, RippleException> sink) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }

    public RdfValue findSingleObject(RippleValue subj, RippleValue pred) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RdfValue findAtLeastOneObject(RippleValue subj, RippleValue pred) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RdfValue findAtMostOneObject(RippleValue subj, RippleValue pred) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RdfValue findUniqueProduct(RippleValue subj, RippleValue pred) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void copyStatements(RippleValue src, RippleValue dest) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeStatementsAbout(URI subj) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }

    public void putContainerMembers(RippleValue head, Sink<RippleValue, RippleException> sink) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }

    public void forget(RippleValue v) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }

    public void findPredicates(RippleValue subject, Sink<RippleValue, RippleException> sink) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }// FIXME: Statements should not be part of the ModelConnection API

    public void add(Statement st, Resource... contexts) throws RippleException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void add(RippleValue subj, RippleValue pred, RippleValue obj) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }

    public void remove(RippleValue subj, RippleValue pred, RippleValue obj) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }// FIXME: URIs should not be part of the ModelConnection API

    public void removeStatementsAbout(RdfValue subj, URI context) throws RippleException {
        //To change body of implemented methods use File | Settings | File Templates.
    }// FIXME: Resources should not be part of the ModelConnection API

    // FIXME: Resources should not be part of the ModelConnection API
    public long countStatements(Resource... contexts) throws RippleException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public long countStatements(Resource context) throws RippleException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }// FIXME: Sesame URIs should not be part of the ModelConnection API

    public URI createUri(String s) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public URI createUri(String ns, String s) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public URI createUri(URI ns, String s) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }// FIXME: BNodes should not be part of the ModelConnection API

    public Resource createBNode() throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Resource createBNode(String id) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }// FIXME: Statements should not be part of the ModelConnection API

    public Statement createStatement(Resource subj, URI pred, Value obj) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RippleValue createTypedLiteral(String value, RippleValue type) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RippleValue value(String s) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RippleValue value(String s, String language) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }// FIXME: this should use an implementation-independent URI class

    public RippleValue value(String s, URI dataType) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RippleValue value(boolean b) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public NumericValue value(int i) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public NumericValue value(long l) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public NumericValue value(double d) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }// FIXME: this should use an implementation-independent URI class

    public RippleValue value(URI uri) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RippleList list() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RippleList list(RippleValue v) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RippleList list(RippleValue v, RippleList rest) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RippleList invert(RippleList l) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RippleList concat(RippleList head, RippleList tail) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setNamespace(String prefix, String ns, boolean override) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }

    public void query( final GetStatementsQuery query, final Sink<RippleValue, RippleException> sink) throws RippleException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void queryAsynch( final GetStatementsQuery query, final Sink<RippleValue, RippleException> sink ) throws RippleException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void multiplyAsynch(RippleValue subj, RippleValue pred, Sink<RippleValue, RippleException> sink, boolean includeInferred) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }

    public void multiply(RippleValue subj, RippleValue pred, Sink<RippleValue, RippleException> sink, boolean includeInferred) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }

    public void divide(RippleValue obj, RippleValue pred, Sink<RippleValue, RippleException> sink) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }// FIXME: Namespaces should not be part of the ModelConnection API

    public void getNamespaces(Sink<Namespace, RippleException> sink) throws RippleException {
        //To change body of implemented methods use File | Settings | File Templates.
    }// FIXME: Statements should not be part of the ModelConnection API

    public void getStatements(RdfValue subj, RdfValue pred, RdfValue obj, Sink<Statement, RippleException> sink, boolean includeInferred) throws RippleException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public RDFSource getSource() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }// TODO: Namespaces should not be part of the ModelConnection API

    public void putNamespaces(Sink<Namespace, RippleException> sink) throws RippleException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void putContexts(Sink<RippleValue, RippleException> sink) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }

    public void exportNamespace(String ns, OutputStream os) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }
}
