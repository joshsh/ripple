package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.model.ModelConnection;
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

    @Override
    public int compare(Literal o1, Literal o2, ModelConnection mc) {
        int c = o1.getLanguage().compareTo(o2.getLanguage());
        return 0 == c ? o1.getLabel().compareTo(o2.getLabel()) : c;
    }
}