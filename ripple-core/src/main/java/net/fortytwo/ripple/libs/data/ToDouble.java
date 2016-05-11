package net.fortytwo.ripple.libs.data;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.graph.GraphLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

import java.util.logging.Logger;

/**
 * A primitive which consumes a literal value and produces its xsd:double
 * equivalent (if any).
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ToDouble extends PrimitiveStackMapping {
    private static final Logger logger
            = Logger.getLogger(ToDouble.class.getName());

    public String[] getIdentifiers() {
        return new String[]{
                DataLibrary.NS_2013_03 + "to-double",
                GraphLibrary.NS_2008_08 + "toDouble",
                GraphLibrary.NS_2007_08 + "toDouble",
                GraphLibrary.NS_2007_05 + "toDouble"};
    }

    public ToDouble()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("x", null, true)};
    }

    public String getComment() {
        return "x  =>  x as double literal";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;

        String s;

        s = mc.toString(stack.getFirst());
        stack = stack.getRest();

        double d;

        try {
            d = new Double(s);
        } catch (NumberFormatException e) {
            logger.fine("bad integer value: " + s);
            return;
        }

        solutions.accept(
                stack.push(d));
    }
}

