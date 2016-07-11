package net.fortytwo.ripple.libs.data;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.graph.GraphLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * A primitive which consumes a resource or literal value and produces its
 * string representation.  For literal values, this is the same literal
 * value but with a type of xsd:string.  For resources identified by URIs,
 * this is the URI as a string.  For blank nodes, this is the identifier of
 * the node.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ToString extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                DataLibrary.NS_2013_03 + "to-string",
                GraphLibrary.NS_2008_08 + "toString",
                GraphLibrary.NS_2007_08 + "toString",
                GraphLibrary.NS_2007_05 + "toString"};
    }

    public ToString() {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true)};
    }

    public String getComment() {
        return "x  =>  string representation of x";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;

        Object v;

        v = stack.getFirst();
        stack = stack.getRest();

        solutions.accept(
                stack.push(mc.valueOf(mc.toString(v), XMLSchema.STRING)));
    }
}

