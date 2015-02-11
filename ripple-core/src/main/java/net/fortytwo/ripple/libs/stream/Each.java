package net.fortytwo.ripple.libs.stream;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A primitive which consumes a list and produces each item in the list in a
 * separate stack.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Each extends PrimitiveStackMapping {
    private static final Logger logger = Logger.getLogger(Each.class.getName());

    private static final String[] IDENTIFIERS = {
            StreamLibrary.NS_2013_03 + "each",
            StreamLibrary.NS_2008_08 + "each",
            StreamLibrary.NS_2007_08 + "each",
            StreamLibrary.NS_2007_05 + "each"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Each()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("l", "a list", true)};
    }

    public String getComment() {
        return "l => each item in l";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        Object l;

        l = arg.getFirst();
        final RippleList rest = arg.getRest();

        Sink<RippleList> listSink = new Sink<RippleList>() {
            public void put(RippleList list) throws RippleException {
                while (!list.isNil()) {
                    try {
                        solutions.put(
                                rest.push(list.getFirst()));
                    } catch (RippleException e) {
                        // Soft fail
                        logger.log(Level.WARNING, "failed to put solution", e);
                    }

                    list = list.getRest();
                }
            }
        };

        mc.toList(l, listSink);
    }
}

