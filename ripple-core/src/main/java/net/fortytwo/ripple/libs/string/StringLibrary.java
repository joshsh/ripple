/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.ModelConnection;
import org.openrdf.model.Value;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * A collection of string manipulation primitives.
 */
public class StringLibrary extends Library
{
    // Note: the prefix "string:" is commonly associated with this URI (per prefix.cc):
    //       http://www.w3.org/2000/10/swap/string#
    public static final String
            NS_2011_08 = "http://fortytwo.net/2011/08/ripple/string#",
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/string#",
            NS_2007_08 = "http://fortytwo.net/2007/08/ripple/string#";

    public void load(final LibraryLoader.Context context)
		throws RippleException
	{
        registerPrimitives( context,
                Concat.class,
                Contains.class,
                EndsWith.class,
                IndexOf.class,
                LastIndexOf.class,
                Length.class,
                Matches.class,
                Md5.class,
                PercentDecoded.class,
                PercentEncoded.class,
                ReplaceAll.class,
                Sha1.class,
                Split.class,
                StartsWith.class,
                Substring.class,
                ToLowerCase.class,
                ToUpperCase.class,
                Trim.class );
	}

    public static RDFValue value( final String label,
                                  final ModelConnection mc,
                                  final RippleValue... operands ) throws RippleException
    {
        boolean stringTyped = false;

        for ( RippleValue o : operands )
        {
            Value v = o.toRDF( mc ).sesameValue();
            if ( v instanceof Literal )
            {
                URI datatype = ( (Literal) v ).getDatatype();

                if ( null != datatype && datatype.equals( XMLSchema.STRING ) )
                {
                    stringTyped = true;
                    break;
                }
            }
        }

        return stringTyped
                ? mc.typedValue(label, XMLSchema.STRING)
                : mc.plainValue(label);
    }
}

