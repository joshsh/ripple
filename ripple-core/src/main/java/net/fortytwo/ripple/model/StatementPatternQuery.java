/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

/**
 * Author: josh
 * Date: Feb 7, 2008
 * Time: 10:46:12 AM
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
            SPO_ };

	private final RippleValue subject;
	private final RippleValue predicate;
	private final RippleValue object;
	private final RippleValue[] contexts;
    private final Pattern pattern;

    private final boolean includeInferred;

    public StatementPatternQuery( final RippleValue subject,
                                  final RippleValue predicate,
                                  final RippleValue object,
                                  final boolean includeInferred,
                                  final RippleValue... contexts )
    {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
        this.includeInferred = includeInferred;
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

    public boolean getIncludeInferred()
    {
        return includeInferred;
    }

    public RippleValue[] getContexts()
    {
        return contexts;
    }
}
