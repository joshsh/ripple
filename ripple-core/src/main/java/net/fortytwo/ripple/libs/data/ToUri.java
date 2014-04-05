package net.fortytwo.ripple.libs.data;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.graph.GraphLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;

import java.net.URI;

/**
 * A primitive which consumes a literal value and produces the resource
 * identified by the corresponding URI (if any).
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ToUri extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                DataLibrary.NS_2013_03 + "to-uri",
                GraphLibrary.NS_2008_08 + "toUri",
                GraphLibrary.NS_2007_08 + "toUri"};
    }

    public ToUri()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("s", null, true)};
    }

    public String getComment() {
        return "s  =>  uri -- where uri is the URI whose string representation is s";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;

        URI s;

        s = URI.create(mc.toString(stack.getFirst()));
        stack = stack.getRest();

        RDFValue uri = mc.valueOf(s);

        solutions.put(
                stack.push(uri));
    }
}

