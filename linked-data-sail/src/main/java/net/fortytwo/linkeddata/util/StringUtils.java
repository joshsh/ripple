package net.fortytwo.linkeddata.util;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public final class StringUtils {
    private static final String UTF_8 = "UTF-8";

    private StringUtils() {
    }

    // Note: extended characters are not escaped for printing.
    public static String escapeURIString(final String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '>':
                    sb.append("\\>");
                    break;
                default:
                    sb.append(c);
            }
        }

        return sb.toString();
    }
}
