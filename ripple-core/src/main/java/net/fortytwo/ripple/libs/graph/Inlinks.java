package net.fortytwo.ripple.libs.graph;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.impl.sesame.SesameModel;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A primitive which consumes a resource and produces a three-element list
 * (subject, resource, object) for each statement about the resource.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Inlinks extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2013_03 + "inlinks",
            GraphLibrary.NS_2008_08 + "inlinks"};

    private static final Logger logger = LoggerFactory.getLogger(Inlinks.class);

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Inlinks()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("o", null, true)};
    }

    public String getComment() {
        return "o  =>  s p o g";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        Model model = mc.getModel();
        if (model instanceof SesameModel) {

            final Object obj = arg.getFirst();
            final RippleList rest = arg.getRest();

            Sink<Statement> stSink = new Sink<Statement>() {
                public void accept(final Statement st) throws RippleException {
                    Resource context = st.getContext();

                    Object subj = mc.canonicalValue(st.getSubject());
                    Object pred = mc.canonicalValue(st.getPredicate());
                    Object ctx = (null == context) ? mc.list() : mc.canonicalValue(context);

                    solutions.accept(rest.push(subj).push(pred).push(obj).push(ctx));
                }
            };

            mc.getStatements(null, null, mc.toRDF(obj), stSink);
        } else {
            logger.warn("primitive is compatible only with the Sesame model: " + this);
        }
    }
}
