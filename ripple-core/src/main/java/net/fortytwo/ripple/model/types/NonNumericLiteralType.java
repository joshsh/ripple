package net.fortytwo.ripple.model.types;

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
}