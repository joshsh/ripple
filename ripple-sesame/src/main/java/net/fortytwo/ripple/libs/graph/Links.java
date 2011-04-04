/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.impl.sesame.SesameModel;

import org.openrdf.model.Statement;
import org.openrdf.model.Resource;
import org.apache.log4j.Logger;

/**
 * A primitive which consumes a resource and produces a three-element list
 * (subject, resource, object) for each statement about the resource.
 */
public class Links extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2008_08 + "links"};

    private static final Logger LOGGER = Logger.getLogger(Links.class);

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

    public void apply(final StackContext arg,
                      final Sink<StackContext, RippleException> solutions)
            throws RippleException {
        final ModelConnection mc = arg.getModelConnection();

        Model model = mc.getModel();
        if (model instanceof SesameModel) {
            final RippleList stack = arg.getStack();

            RippleValue subj;

            subj = stack.getFirst();
            //final RippleList rest = stack.getRest();

            Sink<Statement, RippleException> stSink = new Sink<Statement, RippleException>() {
                public void put(final Statement st) throws RippleException {
                    Resource context = st.getContext();

                    //RippleValue subj = bridge.get( st.getSubject() );
                    RippleValue pred = mc.canonicalValue(st.getPredicate());
                    RippleValue obj = mc.canonicalValue(st.getObject());
                    RippleValue ctx = (null == context) ? mc.list() : mc.canonicalValue(context);
                    //RippleList triple = mc.list( obj ).push( pred ).push( subj );

                    //sink.put( arg.with(
                    //	rest.push( triple ) ) );
                    solutions.put(arg.with(stack.push(pred).push(obj).push(ctx)));
                }
            };

            // FIXME: only SesameModel supports getStatements()
            mc.getStatements(subj.toRDF(mc), null, null, stSink, Ripple.useInference());
        } else {
            LOGGER.warn("primitive is compatible only with the Sesame model: " + this);
        }
    }
}

