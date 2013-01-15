package net.fortytwo.ripple.model;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class StatementPatternQuery
{
    // Note: typically, not all patterns are supported
    public enum Pattern {
            _SOP,
            O_SP,
            P_SO,
            PO_S,
            S_PO,
            SO_P,
            SP_O,
            SPO_ }

	private final RippleValue subject;
	private final RippleValue predicate;
	private final RippleValue object;
	private final RippleValue[] contexts;
    private final Pattern pattern;

    public StatementPatternQuery( final RippleValue subject,
                                  final RippleValue predicate,
                                  final RippleValue object,
                                  final RippleValue... contexts )
    {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
        this.contexts = contexts;

        this.pattern = ( null == subject )
                ? ( null == predicate )
                        ? ( null == object )
                                ? Pattern._SOP
                                : Pattern.O_SP
                        : ( null == object )
                                ? Pattern.P_SO
                                : Pattern.PO_S
                : ( null == predicate )
                        ? ( null == object )
                                ? Pattern.S_PO
                                : Pattern.SO_P
                        : ( null == object )
                                ? Pattern.SP_O
                                : Pattern.SPO_;
    }

    public Pattern getPattern()
    {
        return pattern;
    }

    public RippleValue getSubject()
    {
        return subject;
    }

    public RippleValue getPredicate()
    {
        return predicate;
    }

    public RippleValue getObject()
    {
        return object;
    }

    public RippleValue[] getContexts()
    {
        return contexts;
    }
}
