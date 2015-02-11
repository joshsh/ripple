package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.impl.sesame.SesameModelConnection;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class StringType extends SimpleType<String> {

    public StringType() {
        super(String.class);
    }

    @Override
    public boolean isInstance(String instance) {
        return true;
    }

    @Override
    public Value toRDF(String instance, ModelConnection mc) throws RippleException {
        //return ((SesameModelConnection) mc).getValueFactory().createLiteral(instance);
        return ((SesameModelConnection) mc).getValueFactory().createLiteral(instance, XMLSchema.STRING);
    }

    @Override
    public StackMapping getMapping(String instance) {
        return null;
        //return new KeyValueMapping(instance);
    }

    @Override
    public void print(String instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        p.print("\"" + StringUtils.escapeString(instance) + "\"");
    }

    @Override
    public Category getCategory() {
        return Category.STRING_TYPED_LITERAL;
    }

    @Override
    public int compare(String o1, String o2, ModelConnection mc) {
        return o1.compareTo(o2);
    }
}
