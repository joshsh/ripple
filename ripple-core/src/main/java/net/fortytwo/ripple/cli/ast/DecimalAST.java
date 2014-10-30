package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class DecimalAST extends NumberAST {
    /*
    decimal has a lexical representation consisting of a finite-length sequence
    of decimal digits (#x30-#x39) separated by a period as a decimal indicator.
    An optional leading sign is allowed. If the sign is omitted, "+" is assumed.
    Leading and trailing zeroes are optional. If the fractional part is zero,
    the period and following zero(es) can be omitted. For example: -1.23,
    12678967.543233, +100000.00, 210.
    */
    private static final Pattern
            // TODO: apparently, digits after the decimal point are not required,
            // but are digits before the decimal point required?
            // Note: NaN, positive and negative infinity are apparently not allowed.
            XSD_DECIMAL = Pattern.compile("[-+]?\\d+([.]\\d*)?");

    private final BigDecimal value;

    public DecimalAST(final BigDecimal value) {
        this.value = value;
    }

    /**
     * @param rep the string representation of an xsd:decimal value
     */
    public DecimalAST(final String rep) {
        if (!XSD_DECIMAL.matcher(rep).matches()) {
            throw new IllegalArgumentException("invalid xsd:decimal value: " + rep);
        }

        value = new BigDecimal(canonicalize(rep));
    }

    public NumericValue getValue(final ModelConnection mc) throws RippleException {
        return mc.valueOf(value);
    }

    public String toString() {
        String s = value.toString();

        // Add a decimal point, if necessary, to make this an unambiguous
        // xsd:decimal value in Ripple syntax.
        if (!s.contains(".")) {
            s += ".0";
        }

        // Note: this is not necessarily the canonical form.
        return s;
    }
}
