package net.fortytwo.linkeddata;

import net.fortytwo.linkeddata.sail.LinkedDataSail;
import org.openrdf.model.BNode;
import org.openrdf.model.IRI;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;

import java.util.function.Consumer;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class BNodeToURIFilter implements Consumer<Statement> {

    private final Consumer<Statement> wrapped;

    private final ValueFactory valueFactory;

    public BNodeToURIFilter(final Consumer<Statement> wrapped, final ValueFactory vf) {
        valueFactory = vf;
        this.wrapped = wrapped;
    }

    private IRI bnodeToUri(final BNode bnode) {
        return valueFactory.createIRI(LinkedDataSail.RANDOM_URN_PREFIX + bnode.getID());
    }

    @Override
    public void accept(Statement st) {
        boolean s = st.getSubject() instanceof BNode;
        boolean o = st.getObject() instanceof BNode;
        boolean c = (null != st.getContext())
                && st.getContext() instanceof BNode;

        if (s || o || c) {
            Resource subj = s ? bnodeToUri((BNode) st.getSubject()) : st.getSubject();
            IRI pred = st.getPredicate();
            Value obj = o ? bnodeToUri((BNode) st.getObject()) : st.getObject();
            Resource con = c ? bnodeToUri((BNode) st.getContext()) : st.getContext();

            Statement newSt = (null == con)
                    ? valueFactory.createStatement(subj, pred, obj)
                    : valueFactory.createStatement(subj, pred, obj, con);

            wrapped.accept(newSt);
        } else {
            wrapped.accept(st);
        }
    }
}
