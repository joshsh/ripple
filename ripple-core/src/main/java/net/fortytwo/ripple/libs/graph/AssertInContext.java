package net.fortytwo.ripple.libs.graph;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;


/**
 * A primitive which consumes a subject, predicate and object, then produces the
 * subject after adding the corresponding RDF statement to the triple store.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class AssertInContext extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2013_03 + "assert-in-context",
            GraphLibrary.NS_2008_08 + "assertInContext"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public AssertInContext()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("s", "the subject of the assertion", false),
                new Parameter("p", "the predicate of the assertion", true),
                new Parameter("o", "the object of the assertion", true),
                new Parameter("g", "the named analysis context of the assertion", true)};
    }

    public String getComment() {
        return "s p o g  =>  s  -- has the side-effect of asserting the statement (s, p, o, g)";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleList stack = arg;

        Object subj, pred, obj, ctx;

        ctx = stack.getFirst();
        stack = stack.getRest();
        obj = stack.getFirst();
        stack = stack.getRest();
        pred = stack.getFirst();
        stack = stack.getRest();
        subj = stack.getFirst();
        stack = stack.getRest();

        mc.add(subj, pred, obj, ctx);

        // TODO: store added and removed statements in a buffer until the
        // ModelConnection commits.  You may not simply wait to commit,
        // as writing and then reading without first committing may result
        // in a deadlock.  The LinkedDataSail already does this sort of
        // buffering, which is why it does not deadlock w.r.t. its base
        // Sail.
        mc.commit();

        solutions.put(stack.push(subj));
    }
}
