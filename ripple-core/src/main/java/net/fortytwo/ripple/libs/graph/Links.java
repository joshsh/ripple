package net.fortytwo.ripple.libs.graph;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.impl.sesame.SesameModel;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;

import java.util.logging.Logger;

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

    private static final Logger logger = Logger.getLogger(Links.class.getName());

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

            RippleValue subj;

            subj = stack.getFirst();

            Sink<Statement> stSink = new Sink<Statement>() {
                public void put(final Statement st) throws RippleException {
                    Resource context = st.getContext();

                    RippleValue pred = mc.canonicalValue(new RDFValue(st.getPredicate()));
                    RippleValue obj = mc.canonicalValue(new RDFValue(st.getObject()));
                    RippleValue ctx = (null == context) ? mc.list() : mc.canonicalValue(new RDFValue(context));

                    solutions.put(stack.push(pred).push(obj).push(ctx));
                }
            };

            // FIXME: only SesameModel supports getStatements()
            mc.getStatements(subj.toRDF(mc), null, null, stSink);
        } else {
            logger.warning("primitive is compatible only with the Sesame model: " + this);
        }
    }
}

