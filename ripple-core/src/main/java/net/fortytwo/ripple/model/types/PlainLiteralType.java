package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.model.KeyValueMapping;
import net.fortytwo.ripple.model.StackMapping;
import org.openrdf.model.Literal;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class PlainLiteralType extends RDFValueType<Literal> {
    public PlainLiteralType() {
        super(Literal.class);
    }

    @Override
    public boolean isInstance(Literal instance) {
        return null == instance.getDatatype() && null == instance.getLanguage();
    }

    @Override
    public Category getCategory() {
        return Category.PLAIN_LITERAL_WITHOUT_LANGUAGE_TAG;
    }

    @Override
    public StackMapping getMapping(Literal instance) {
        return new KeyValueMapping(instance.getLabel());
    }
}