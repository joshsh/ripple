package net.fortytwo.ripple.model.impl.blueprints;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StatementPatternQuery;
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
 * User: josh
 * Date: Nov 19, 2010
 * Time: 2:38:40 PM
 */
public class BlueprintsModelConnection implements ModelConnection {
    public Model getModel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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

    ////////

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

    public void toList(RippleValue v, Sink<RippleList, RippleException> sink) throws RippleException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public RDFValue uriValue(String s) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RDFValue createTypedLiteral(String label, RippleValue datatype) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RDFValue value(String s) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RDFValue value(String s, String language) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RDFValue value(String s, URI datatype) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RDFValue value(boolean b) throws RippleException {
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
    }

    public NumericValue value(BigDecimal bd) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    // TODO: hide the Value class
    public RippleValue canonicalValue(Value v) {
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

    ///////

    public void add(RippleValue subj, RippleValue pred, RippleValue obj, RippleValue... contexts) throws RippleException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void remove(RippleValue subj, RippleValue pred, RippleValue obj, RippleValue... contexts) throws RippleException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Comparator<RippleValue> getComparator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setNamespace(String prefix, String ns, boolean override) throws RippleException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void query(StatementPatternQuery query, Sink<RippleValue, RippleException> sink, boolean asynchronous) throws RippleException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Source<Namespace, RippleException> getNamespaces() throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    // TODO: hide the Statement class
    public void getStatements(RDFValue subj, RDFValue pred, RDFValue obj, Sink<Statement, RippleException> sink, boolean includeInferred) throws RippleException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluate(String query) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Source<RippleValue, RippleException> getContexts() throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
