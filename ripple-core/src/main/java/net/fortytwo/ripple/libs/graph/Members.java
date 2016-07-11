package net.fortytwo.ripple.libs.graph;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.impl.sesame.SesameModel;
import org.openrdf.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A primitive which consumes an RDF container and produces all items in the
 * container.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Members extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2013_03 + "members",
            GraphLibrary.NS_2008_08 + "members",
            GraphLibrary.NS_2007_08 + "contains",
            GraphLibrary.NS_2007_05 + "contains"};

    private static final Logger logger = LoggerFactory.getLogger(Members.class);

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Members() {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("c", "an RDF Container; for instance, a Bag", true)};
    }

    public String getComment() {
        return "c  =>  x  -- for each member x of Container c";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        Model model = mc.getModel();
        if (model instanceof SesameModel) {

            Object head = arg.getFirst();
            final RippleList rest = arg.getRest();

            final Sink<Object> pushSink = v -> solutions.accept(rest.push(v));

            Sink<Statement> stSink = st -> {
                if ('_' == st.getPredicate().getLocalName().charAt(0)) {
                    pushSink.accept(st.getObject());
                }
            };

            mc.getStatements(mc.toRDF(head), null, null, stSink);
        } else {
            logger.warn("primitive is compatible only with the Sesame model: " + this);
        }
    }
}

