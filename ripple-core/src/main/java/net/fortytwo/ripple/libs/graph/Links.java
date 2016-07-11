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
public class Links extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2013_03 + "links",
            GraphLibrary.NS_2008_08 + "links"};

    private static final Logger logger = LoggerFactory.getLogger(Links.class.getName());

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Links()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("s", null, true)};
    }

    public String getComment() {
        return "s  =>  s p o g";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        Model model = mc.getModel();
        if (model instanceof SesameModel) {
            final RippleList stack = arg;

            Object subj;

            subj = stack.getFirst();

            Sink<Statement> stSink = new Sink<Statement>() {
                public void accept(final Statement st) throws RippleException {
                    Resource context = st.getContext();

                    Object pred = mc.canonicalValue(st.getPredicate());
                    Object obj = mc.canonicalValue(st.getObject());
                    Object ctx = (null == context) ? mc.list() : mc.canonicalValue(context);

                    solutions.accept(stack.push(pred).push(obj).push(ctx));
                }
            };

            // FIXME: only SesameModel supports getStatements()
            mc.getStatements(mc.toRDF(subj), null, null, stSink);
        } else {
            logger.warn("primitive is compatible only with the Sesame model: " + this);
        }
    }
}

