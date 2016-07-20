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
        return instance.getLanguage().isPresent();
    }

    @Override
    public Category getCategory() {
        return Category.STRING_LITERAL_WITH_LANGUAGE_TAG;
    }

    @Override
    public int compare(Literal o1, Literal o2, ModelConnection mc) {
        int c = o1.getLanguage().get().compareTo(o2.getLanguage().get());
        return 0 == c ? o1.getLabel().compareTo(o2.getLabel()) : c;
    }
}