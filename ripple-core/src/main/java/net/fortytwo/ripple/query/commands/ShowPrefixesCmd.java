package net.fortytwo.ripple.query.commands;

import net.fortytwo.flow.Collector;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import org.openrdf.model.Namespace;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ShowPrefixesCmd extends Command
{
	public void execute( final QueryEngine qe, final ModelConnection mc )
		throws RippleException
	{
		final RipplePrintStream ps = qe.getPrintStream();

        // Create a map of prefixes to names and find the longest prefix.
        Map<String, String> prefixesToNames = new HashMap<String, String>();
        Collector<Namespace> coll = new Collector<Namespace>();
		mc.getNamespaces().writeTo(coll);
		int max = 0;
        int j = 0;
        for (Object aColl : coll) {
            Namespace ns = (Namespace) aColl;
            prefixesToNames.put(ns.getPrefix(), ns.getName());

            int len = (ns.getPrefix() + j).length();
            if (len > max) {
                max = len;
            }
            j++;
        }
		final int maxlen = max + 4;

        // Alphabetize the prefixes.
        String[] array = new String[prefixesToNames.size()];
        prefixesToNames.keySet().toArray( array );
        Arrays.sort(array);

        ps.println( "" );

        // Print the namespaces, aligning the name portions with one another.
        int i = 0;
        for ( String prefix : array )
        {
            String s = "[" + i++ + "] " + prefix + ":";
            int len = s.length();
            ps.print( s );

            for ( int l = 0; l < maxlen - len + 2; l++ )
            {
                ps.print( ' ' );
            }

            ps.print( prefixesToNames.get( prefix ) );
            ps.print( '\n' );
        }

		ps.println( "" );
	}

    public String getName() {
        return "show prefixes";
    }

    protected void abort()
	{
	}
}

