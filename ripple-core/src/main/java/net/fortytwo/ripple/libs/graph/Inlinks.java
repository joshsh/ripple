/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


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
import org.apache.log4j.Logger;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;

/**
 * A primitive which consumes a resource and produces a three-element list
 * (subject, resource, object) for each statement about the resource.
 */
public class Inlinks extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2011_08 + "inlinks",
            GraphLibrary.NS_2008_08 + "inlinks"};

    private static final Logger LOGGER = Logger.getLogger(Inlinks.class);

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
            final RippleList stack = arg;

            final RippleValue obj = stack.getFirst();
            final RippleList rest = stack.getRest();

            Sink<Statement> stSink = new Sink<Statement>() {
                public void put(final Statement st) throws RippleException {
                    Resource context = st.getContext();

                    RippleValue subj = mc.canonicalValue(new RDFValue(st.getSubject()));
                    RippleValue pred = mc.canonicalValue(new RDFValue(st.getPredicate()));
                    RippleValue ctx = (null == context) ? mc.list() : mc.canonicalValue(new RDFValue(context));

                    solutions.put(rest.push(subj).push(pred).push(obj).push(ctx));
                }
            };

            mc.getStatements(null, null, obj.toRDF(mc), stSink);
        } else {
            LOGGER.warn("primitive is compatible only with the Sesame model: " + this);
        }
    }
}
