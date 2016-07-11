package net.fortytwo.ripple.libs.data;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import org.openrdf.model.Literal;
import org.openrdf.model.Value;

import java.util.Optional;

/**
 * A primitive which consumes a plain literal value and produces its language
 * tag (or an empty string if the literal has no language tag).
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Lang extends PrimitiveStackMapping {
    public String[] getIdentifiers() {
        return new String[]{
                DataLibrary.NS_XML + "lang"};
    }

    public Lang() {
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

        v = mc.toRDF(stack.getFirst());
        stack = stack.getRest();

        if (v instanceof Literal) {
            Optional<String> result = ((Literal) v).getLanguage();

            if (result.isPresent()) {
                solutions.accept(
                        stack.push(result.get()));
            }
        }
    }
}

