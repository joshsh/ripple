/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-core/src/main/java/net/fortytwo/ripple/libs/analysis/Infer.java $
 * $Revision: 51 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RDFPredicateMapping;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.StatementPatternQuery;

public abstract class RDFPredicateStackMapping extends PrimitiveStackMapping
{
    private final boolean inverted;
    protected StackMapping inverse;

    public RDFPredicateStackMapping( final boolean inverted ) throws RippleException
	{
		super();

        this.inverted = inverted;
    }

    @Override
    public StackMapping inverse() throws RippleException
    {
        return inverse;
    }

    protected RDFPredicateMapping getMapping( final RDFValue predicate,
                                              final RDFValue context,
                                              final boolean includeInferred )
		throws RippleException
	{
        StatementPatternQuery.Pattern type = inverted
                ? StatementPatternQuery.Pattern.PO_S
                : StatementPatternQuery.Pattern.SP_O;
        return new RDFPredicateMapping( type, predicate, context, includeInferred );
    }
}
