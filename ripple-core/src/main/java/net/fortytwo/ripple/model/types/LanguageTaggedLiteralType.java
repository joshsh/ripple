package net.fortytwo.ripple.model.types;

import org.openrdf.model.Literal;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LanguageTaggedLiteralType extends RDFValueType<Literal> {
    public LanguageTaggedLiteralType() {
        super(Literal.class);
    }

    @Override
    public boolean isInstance(Literal instance) {
        return null != instance.getLanguage();
    }

    @Override
    public Category getCategory() {
        return Category.PLAIN_LITERAL_WITH_LANGUAGE_TAG;
    }
}