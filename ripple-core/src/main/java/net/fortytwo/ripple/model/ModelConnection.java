/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

public interface ModelConnection
{
	Model getModel();
	
	void close() throws RippleException;

	/**
	*  Returns the ModelConnection to a normal state after an Exception has
	*  been thrown.
	*/
	void reset( boolean rollback ) throws RippleException;

	void commit() throws RippleException;

	boolean toBoolean( RippleValue v ) throws RippleException;
	NumericValue toNumericValue( RippleValue v )	throws RippleException;
    Date toDateValue( RippleValue v ) throws RippleException;

    /**
	 *
	 * @param v
	 * @return the string representation of a value.  This is not identical to
	 * Object.toString(), and may involve a loss of information.
	 * @throws RippleException
	 */
	String toString( RippleValue v ) throws RippleException;

	URI toUri( RippleValue v ) throws RippleException;
	void toList( RippleValue v, Sink<RippleList, RippleException> sink ) throws RippleException;

// FIXME: Statements should not be part of the ModelConnection API
	void add( RippleValue subj, RippleValue pred, RippleValue obj, RippleValue... contexts ) throws RippleException;
	void remove( RippleValue subj, RippleValue pred, RippleValue obj, RippleValue... contexts ) throws RippleException;

// FIXME: Sesame URIs should not be part of the ModelConnection API
	URI createURI( String s ) throws RippleException;
	URI createURI( String ns, String s ) throws RippleException;
	URI createURI( URI ns, String s ) throws RippleException;
// FIXME: Statements should not be part of the ModelConnection API
	Statement createStatement( Resource subj, URI pred, Value obj ) throws RippleException;

    RDFValue createTypedLiteral( String label, RippleValue datatype ) throws RippleException;

    Comparator<RippleValue> getComparator();

    RDFValue value( String s ) throws RippleException;
	RDFValue value( String s, String language ) throws RippleException;
// FIXME: this should use an implementation-independent URI class
	RDFValue value( String s, URI datatype ) throws RippleException;
	RDFValue value( boolean b ) throws RippleException;
    NumericValue value( int i ) throws RippleException;
	NumericValue value( long l ) throws RippleException;
    NumericValue value( double d ) throws RippleException;
    NumericValue value( BigDecimal bd ) throws RippleException;
// FIXME: this should use an implementation-independent URI class
	RippleValue value( Value v );
	
    RippleList list();
    RippleList list( RippleValue v );
	RippleList list( RippleValue v, RippleList rest );

    /**
     * Define a namespace prefix.
     * @param prefix  the namespace prefix
     * @param ns  the namespace URI, or null to undefine a prefix
     * @param override  whether to override an existing namespace with this prefix, if there is one
     * @throws RippleException
     */
    void setNamespace( String prefix, String ns, boolean override ) throws RippleException;

    void query( StatementPatternQuery query, Sink<RippleValue, RippleException> sink, boolean asynchronous ) throws RippleException;

// FIXME: Namespaces should not be part of the ModelConnection API
	Source<Namespace, RippleException> getNamespaces() throws RippleException;
// FIXME: Statements should not be part of the ModelConnection API
	void getStatements( RDFValue subj, RDFValue pred, RDFValue obj, Sink<Statement, RippleException> sink, boolean includeInferred ) throws RippleException;

// FIXME: this is a hack
    CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluate( String query ) throws RippleException ;
    
	Source<RippleValue, RippleException> getContexts() throws RippleException;
	
}
