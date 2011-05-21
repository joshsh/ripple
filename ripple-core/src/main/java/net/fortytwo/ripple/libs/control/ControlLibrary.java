/*
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.model.PrimitiveStackMapping;

/**
 * A collection of primitives for controlling the flow of Ripple programs.
 */
public class ControlLibrary extends Library {
    public static final String
            NS_2011_04 = "http://fortytwo.net/2011/04/ripple/control#";

    // Special values.
    private static PrimitiveStackMapping
            optionApply,
            plusApply,
            rangeApply,
            starApply,
            timesApply,
            inverse;


    public void load(final LibraryLoader.Context context)
            throws RippleException {

        registerPrimitives(context,

                // Application primitives
                Apply.class,
                Dip.class,
                Dipd.class,

                // Conditions and branching
                Branch.class,
                Choice.class,
                Ifte.class,
                While.class,
                Require.class,

                // Other
                Ary.class);

        optionApply = registerPrimitive(OptionApply.class, context);
        plusApply = registerPrimitive(PlusApply.class, context);
        starApply = registerPrimitive(StarApply.class, context);
        rangeApply = registerPrimitive(RangeApply.class, context);
        timesApply = registerPrimitive(TimesApply.class, context);

        inverse = registerPrimitive(Inverse.class, context);
    }

    public static PrimitiveStackMapping getOptionApplyValue() {
        return optionApply;
    }

    public static PrimitiveStackMapping getStarApplyValue() {
        return starApply;
    }

    public static PrimitiveStackMapping getPlusApplyValue() {
        return plusApply;
    }

    public static PrimitiveStackMapping getTimesApplyValue() {
        return timesApply;
    }

    public static PrimitiveStackMapping getRangeApplyValue() {
        return rangeApply;
    }

    public static PrimitiveStackMapping getInvertValue() {
        return inverse;
    }
}

