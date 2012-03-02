/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.data;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import org.openrdf.model.Literal;
import org.openrdf.model.Value;

/**
 * A primitive which consumes a plain literal value and produces its language
 * tag (or an empty string if the literal has no language tag).
 */
public class Lang extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                DataLibrary.NS_XML + "lang"};
    }

    public Lang()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("l", null, true)};
    }

    public String getComment() {
        return "l  =>  language tag of literal l";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;

        Value v;
        String result;

        v = stack.getFirst().toRDF(mc).sesameValue();
        stack = stack.getRest();

        if (v instanceof Literal) {
            result = ((Literal) v).getLanguage();

            if (null != result) {
                solutions.put(
                        stack.push(mc.plainValue(result)));
            }
        }
    }
}

