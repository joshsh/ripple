package net.fortytwo.ripple.model;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class StatementPatternQuery<T> {
    // Note: typically, not all patterns are supported
    public enum Pattern {
        _SOP,
        O_SP,
        P_SO,
        PO_S,
        S_PO,
        SO_P,
        SP_O,
        SPO_
    }

    private final T subject;
    private final T predicate;
    private final T object;
    private final T[] contexts;
    private final Pattern pattern;

    public StatementPatternQuery(final T subject,
                                 final T predicate,
                                 final T object,
                                 final T... contexts) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
        this.contexts = contexts;

        this.pattern = (null == subject)
                ? (null == predicate)
                ? (null == object)
                ? Pattern._SOP
                : Pattern.O_SP
                : (null == object)
                ? Pattern.P_SO
                : Pattern.PO_S
                : (null == predicate)
                ? (null == object)
                ? Pattern.S_PO
                : Pattern.SO_P
                : (null == object)
                ? Pattern.SP_O
                : Pattern.SPO_;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public T getSubject() {
        return subject;
    }

    public T getPredicate() {
        return predicate;
    }

    public T getObject() {
        return object;
    }

    public T[] getContexts() {
        return contexts;
    }
}
