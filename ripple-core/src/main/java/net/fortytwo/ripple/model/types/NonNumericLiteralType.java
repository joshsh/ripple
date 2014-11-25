package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.model.ModelConnection;
import org.openrdf.model.Literal;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class NonNumericLiteralType extends RDFValueType<Literal> {

    public NonNumericLiteralType() {
        super(Literal.class);
    }

    @Override
    public boolean isInstance(Literal instance) {
        return null != instance.getDatatype() && !NumericType.isNumericLiteral(instance);
    }

    @Override
    public Category getCategory() {
        return Category.OTHER_TYPED_LITERAL;
    }

    @Override
    public int compare(Literal o1, Literal o2, ModelConnection mc) {
        int c = o1.getDatatype().stringValue().compareTo(o2.getDatatype().stringValue());
        return 0 == c ? o1.getLabel().compareTo(o2.getLabel()) : c;
    }
}