package net.fortytwo.ripple.model;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import org.openrdf.model.Literal;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

/**
 * A transactional connection to a <code>Model</code>.
 * It permits the addition and removal of data to and from the model,
 * handling of transactions,
 * access to the total order of Ripple values in the model,
 * and mapping of values into and out of Ripple space.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface ModelConnection {
    /**
     * @return the <code>Model</code> of this connection
     */
    Model getModel();

    /**
     * Closes this connection, releasing its resources and rolling back any uncommitted changes.
     *
     * @throws RippleException if anything goes wrong
     */
    void close() throws RippleException;

    /**
     * Returns the ModelConnection to a normal state after an Exception has
     * been thrown.
     *
     * @param rollback whether to roll back the current transaction
     * @throws RippleException if anything goes wrong
     */
    void reset(boolean rollback) throws RippleException;

    /**
     * Commits the current transaction.
     *
     * @throws RippleException if the transaction cannot be committed
     */
    void commit() throws RippleException;

    // TODO: JavaDoc
    void print(Object v, RipplePrintStream ps) throws RippleException;

    // TODO: JavaDoc
    Value toRDF(Object v) throws RippleException;

    // TODO: JavaDoc
    StackMapping toMapping(Object v);

    /**
     * Attempts to convert a Ripple value to a boolean value.
     *
     * @param v the value to convert.  It must be of a type which is convertible to boolean.
     * @return the resulting boolean value
     * @throws RippleException if the value cannot be converted
     */
    boolean toBoolean(Object v) throws RippleException;

    /**
     * Attempts to convert an object to an instance of a supported numeric type.
     *
     * @param v the value to convert.  It must be of a type which is convertible to a number.
     * @return the resulting numeric value
     * @throws RippleException if the value cannot be converted
     */
    Number toNumber(Object v) throws RippleException;

    /**
     * Attempts to convert a Ripple value to a date value.
     *
     * @param v the value to convert.  It must be of a type which is convertible to a date.
     * @return the resulting date
     * @throws RippleException if the value cannot be converted
     */
    Date toDate(Object v) throws RippleException;

    /**
     * Attempts to convert a Ripple value to a string value.
     *
     * @param v the value to convert.
     * @return the string representation of a value.  This is not identical to
     * Object.toString(), and may involve a loss of information.
     * @throws RippleException if conversion fails
     */
    String toString(Object v) throws RippleException;

    /**
     * Attempts to convert a Ripple value to a Ripple List.
     *
     * @param v    the value to convert.  It must be of a type which is convertible to a list,
     *             or be the head of an RDF list in the model's RDF knowledge base.
     * @param sink a handler for successfully converted lists
     * @throws RippleException if conversion fails
     */
    void toList(Object v, Sink<RippleList> sink) throws RippleException;

    /**
     * Construct an RDF URI reference.
     *
     * @param uri the URI to convert
     * @return the resulting RDF value
     * @throws RippleException if the argument is not a valid URI
     */
    URI valueOf(java.net.URI uri) throws RippleException;

    /**
     * Construct a date/time value.
     *
     * @param d the date to convert
     * @return the resulting RDF value
     * @throws RippleException if the argument is not a valid date
     */
    Literal valueOf(final Date d) throws RippleException;

    /**
     * Constructs a language-tagged literal value in Ripple space.
     *
     * @param s        the label of the literal
     * @param language the language tag of the literal (e.g. "en" or "de")
     * @return the resulting value
     * @throws RippleException if the literal cannot be constructed
     */
    Literal valueOf(String s, String language) throws RippleException;

    /**
     * Constructs a typed RDF literal value.
     *
     * @param label    the label of the literal
     * @param datatype the data type of the value
     * @return the resulting RDF value
     * @throws RippleException if the arguments do not define a valid literal
     */
    // TODO: this should use an implementation-independent URI class
    Literal valueOf(String label, URI datatype) throws RippleException;

    /**
     * Finds the "canonical" value, in Ripple space, for a given RDF value.
     * Several RDF values may map to the same canonical value.
     * See also <code>SpecialValueMap</code>.
     *
     * @param v the RDF value to look up
     * @return the canonical value.  Always non-null.
     */
    Object canonicalValue(Value v);

    /**
     * @return the empty list, of which there is exactly one in this model
     */
    RippleList list();

    /**
     * Returns a comparator providing the total order of objects in this model.
     * Only those objects with associated types in the model are expected to be compared.
     *
     * @return the total order of objects in this model
     */
    Comparator<Object> getComparator();

    /**
     * Define a namespace prefix.
     *
     * @param prefix   the namespace prefix
     * @param ns       the namespace URI, or null to undefine a prefix
     * @param override whether to override an existing namespace with this prefix, if there is one
     * @throws RippleException if the namespace cannot be set
     */
    void setNamespace(String prefix, String ns, boolean override) throws RippleException;

    /**
     * Issues a triple pattern query over the model.
     *
     * @param query        the query to evaluate
     * @param sink         a handler for query results
     * @param asynchronous whether the query should be evaluated in an asynchronous fashion,
     *                     allowing new threads to be spawned in order to compute results in parallel
     * @throws RippleException if the query cannot be evaluated
     */
    void query(StatementPatternQuery query, Sink sink, boolean asynchronous) throws RippleException;

    /**
     * Retrieves all namespaces defined in this model.
     *
     * @return a source producing all namespaces
     * @throws RippleException if namespaces cannot be retrieved
     */
    // TODO: Namespaces should not be part of the ModelConnection API
    Source<Namespace> getNamespaces() throws RippleException;

    /**
     * Retrieves all statements in the model matching a given triple pattern.
     *
     * @param subj the subject of matching statements
     * @param pred the predicate of matching statements
     * @param obj  the object of matching statements
     * @param sink a handler for matched statements
     * @throws RippleException if statements cannot be retrieved
     */
    // TODO: Statements should not be part of the ModelConnection API
    void getStatements(Value subj, Value pred, Value obj, Sink<Statement> sink) throws RippleException;

    /**
     * Evaluates a SPARQL query against the model.
     *
     * @param query the query to evaluate
     * @return an iterator over all solutios to the query
     * @throws RippleException if query evaluation fails
     */
    // TODO: this is a hack
    CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluate(String query) throws RippleException;

    /**
     * @return a source producing all graph contexts in this model
     * @throws RippleException if contexts cannot be retrieved
     */
    Source<Object> getContexts() throws RippleException;

    /**
     * Adds one or more RDF statements to the model.
     *
     * @param subj     the subject of each statement
     * @param pred     the predicate of each statement
     * @param obj      the object of each statement
     * @param contexts the graph contexts of the statements, if any.
     *                 For each non-null context supplied,
     *                 a statement will be created and placed in the corresponding graph.
     *                 If a null context is supplied, a statement will be added to the default graph.
     *                 If no contexts are supplied, a single statement will be added to the default graph.
     * @throws RippleException if the statement(s) cannot be added
     */
    void add(Object subj, Object pred, Object obj, Object... contexts) throws RippleException;

    /**
     * Removes matching RDF statements from the model.
     *
     * @param subj     the subject of each matching statement, or <code>null</code> as a wildcard
     * @param pred     the predicate of each matching statement, or <code>null</code> as a wildcard
     * @param obj      the object of each matching statement, or <code>null</code> as a wildcard
     * @param contexts zero or more graph contexts for matching statements.
     *                 For each non-null context supplied,
     *                 matching statements from the corresponding graph will be removed.
     *                 If a null context is supplied, matching statements from the default context will be removed.
     *                 If no contexts are supplied, matching statements from all contexts will be removed.
     * @throws RippleException if matching statements cannot be removed
     */
    void remove(Object subj, Object pred, Object obj, Object... contexts) throws RippleException;

    /**
     * Adds an RDF description of the given list to the model.
     * This involves adding all appropriate <code>rdf:type</code>, <code>rdf:first</code>, and <code>rdf:rest</code>
     * statements for the list itself,
     * as well as those of any lists it contains, recursively.
     *
     * @param list the list to internalize
     * @return whether the list was internalized successfully.
     * If this operation is unsuccessful, no statements are added to the model.
     * @throws RippleException if internalization fails
     */
    boolean internalize(RippleList list) throws RippleException;

    /**
     * Complete any still-executing tasks.
     */
    void finish() throws RippleException;
}
