package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.impl.sesame.types.NumericType;
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

/**
 * Note: this class has a natural ordering that is inconsistent with equals.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RDFValue implements RippleValue {
    private final Value value;

    public RDFValue(final Value value) {
        this.value = value;
    }

    public Value sesameValue() {
        return value;
    }

    public RDFValue toRDF(final ModelConnection mc) {
        return this;
    }

    public void printTo(final RipplePrintStream p,
                        final ModelConnection mc)
            throws RippleException {
        p.print(value);
    }

    public StackMapping getMapping() {
        return null;
    }

    public boolean equals(final Object other) {
        // Note: Sesame's Value classes don't appear to have reasonable
        // implementations of equals() and hashCode().
        return (other instanceof RDFValue) && ((RDFValue) other).value.toString().equals(value.toString());
    }

    public int hashCode() {
        return 957832376 + value.toString().hashCode();
    }

    public String toString() {
        if (value instanceof URI) {
            URI uri = (URI) value;
            return uri.toString();
//            return uri.getNamespace() + uri.getLocalName();
//            return "<" + uri.getNamespace() + uri.getLocalName() + ">";
        } else if (value instanceof Literal) {
            Literal lit = (Literal) value;
            return "\"" + StringUtils.escapeString(lit.getLabel()) + "\""
                    + ((null != lit.getDatatype()) ? "^^<" + lit.getDatatype() + ">" : "");
        } else {
            // Use Sesame's toString() methods.
            return value.toString();
        }
    }

    public RippleType.Category getCategory() {
        return value instanceof URI
                ? RippleType.Category.OTHER_RESOURCE
                : value instanceof BNode
                ? RippleType.Category.OTHER_RESOURCE
                : value instanceof Literal
                ? (null != ((Literal) value).getDatatype()
                ? (NumericType.isNumericLiteral((Literal) value)
                ? RippleType.Category.NUMERIC_TYPED_LITERAL : RippleType.Category.OTHER_TYPED_LITERAL)
                : null == ((Literal) value).getLanguage()
                ? RippleType.Category.PLAIN_LITERAL_WITHOUT_LANGUAGE_TAG
                : RippleType.Category.PLAIN_LITERAL_WITH_LANGUAGE_TAG)
                : null;

    }
}
