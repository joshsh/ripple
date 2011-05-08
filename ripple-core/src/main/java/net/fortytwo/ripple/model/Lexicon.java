/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import jline.Completor;
import jline.NullCompletor;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.jline.LexicalCompletor;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Defines a mapping between keywords and URIs, and between namespace prefixes
 * and URIs.
 */
public class Lexicon {
    // Note: these definitions are to be kept in exact agreement with those in
    // the Ripple parser grammar.
    private static final String
            NAME_START_CHAR_NOUSC =
            "[A-Z]|[a-z]" +
                    "|[\u00C0-\u00D6]" +
                    "|[\u00D8-\u00F6]" +
                    "|[\u00F8-\u02FF]" +
                    "|[\u0370-\u037D]" +
                    "|[\u037F-\u1FFF]" +
                    "|[\u200C-\u200D]" +
                    "|[\u2070-\u218F]" +
                    "|[\u2C00-\u2FEF]" +
                    "|[\u3001-\uD7FF]" +
                    "|[\uF900-\uFDCF]" +
                    "|[\uFDF0-\uFFFD]",
            NAME_CHAR = NAME_START_CHAR_NOUSC +
                    "|_|-" +
                    "|\\d" +
                    "|'\u00B7'" +
                    "|['\u0300'-'\u036F']" +
                    "|['\u203F'-'\u2040']";
    private static final Pattern
            NAME_OR_PREFIX = Pattern.compile("(" + NAME_START_CHAR_NOUSC + ")(" + NAME_CHAR + ")*"),
            NAME_NOT_PREFIX = Pattern.compile("_(" + NAME_CHAR + ")*");

    private final Map<String, Set<URI>> keywordToUri;
    private final Map<URI, String> uriToKeyword;
    private final Map<String, String> prefixToUri;
    private final Map<String, String> uriToPrefix;
    private final Collection<String> allQNames;

    public Lexicon(final Model model) throws RippleException {
        prefixToUri = new HashMap<String, String>();
        uriToPrefix = new HashMap<String, String>();
        allQNames = new ArrayList<String>();

        ModelConnection mc = model.createConnection();
        try {
            keywordToUri = new HashMap<String, Set<URI>>();
            uriToKeyword = new HashMap<URI, String>();

            for (Value key : model.getSpecialValues().keySet()) {
                if (key instanceof URI) {
                    // The keyword for a special URI is its local part.
                    String keyword = ((URI) key).getLocalName();

                    Set<URI> siblings = keywordToUri.get(keyword);

                    // If there is no existing value for the key, simply add it.
                    if (null == siblings) {
                        siblings = new HashSet<URI>();
                        siblings.add((URI) key);
                        keywordToUri.put(keyword, siblings);
                    } else {
                        boolean thisIsPrimary = isPrimaryValue(key, mc);
                        boolean othersArePrimary = isPrimaryValue(siblings.iterator().next(), mc);

                        if (thisIsPrimary) {
                            // Primary values override any alias values.
                            if (!othersArePrimary) {
                                siblings.clear();
                            }

                            siblings.add((URI) key);
                        } else {
                            // Alias values may only be added if there are no
                            // competing primary values.
                            if (!othersArePrimary) {
                                siblings.add((URI) key);
                            }
                        }
                    }
                }
            }

            // Assign keywords to URIs only after the final configuration
            // has been determined.
            for (String keyword : keywordToUri.keySet()) {
                for (URI uri : keywordToUri.get(keyword)) {
                    uriToKeyword.put(uri, keyword);
                }
            }
        } finally {
            mc.close();
        }
    }

    private boolean isPrimaryValue(final Value key,
                                   final ModelConnection mc) throws RippleException {
        Value mapsTo = mc.canonicalValue(new RDFValue(key)).toRDF(mc).sesameValue();
        return key.equals(mapsTo);
    }

    public boolean isValidPrefix(final String prefix) {
        return (0 == prefix.length())
                || NAME_OR_PREFIX.matcher(prefix).matches();
    }

    public boolean isValidLocalName(final String localName) {
        return (0 == localName.length())
                || NAME_OR_PREFIX.matcher(localName).matches()
                || NAME_NOT_PREFIX.matcher(localName).matches();
    }

    public Set<URI> uriForKeyword(final String localName) {
        Set<URI> result = keywordToUri.get(localName);

        // If there are no results, return an empty list instead of null.
        return (null == result)
                ? new HashSet<URI>()
                : result;
    }

    public String getNamespaceUri(final String prefix) {
        return prefixToUri.get(prefix);
    }

    public String findSymbol(final URI uri) {
        // Does it have a keyword?
        String symbol = uriToKeyword.get(uri);

        // If not, does it have a namespace prefix?
        if (null == symbol) {
            String nsPrefix = uriToPrefix.get(uri.getNamespace());

            // Namespace prefix may be empty but non-null.
            if (null != nsPrefix) {
                String localName = uri.getLocalName();

                // Note: assumes that the local name is never null (although it
                //       may be empty).
                symbol = (isValidPrefix(nsPrefix) && isValidLocalName(localName))
                        ? nsPrefix + ":" + uri.getLocalName()
                        : null;
            }
        }

        return symbol;
    }

    public Completor getCompletor() throws RippleException {
        Set<String> keywords = keywordToUri.keySet();
        Set<String> prefixes = prefixToUri.keySet();

        int size = keywords.size() + prefixes.size() + allQNames.size();
        if (0 < size) {
            Collection<String> alts = new ArrayList<String>();

            for (String keyword : keywords) {
                alts.add(keyword);
            }

            for (String allQName : allQNames) {
                alts.add(allQName);
            }

            for (String prefixe : prefixes) {
                alts.add(prefixe + ":");
            }

            return new LexicalCompletor(alts);
        } else {
            return new NullCompletor();
        }
    }

    public void uriForKeyword(final String localName,
                              final Sink<RippleValue, RippleException> sink,
                              final ModelConnection mc,
                              final PrintStream errors)
            throws RippleException {
        Collection<URI> options = uriForKeyword(localName);

        // Creating a set of values eliminates the possibility of a keyword
        // resolving to the same runtime value more than once (as is the case,
        // for instance, when two or more URIs mapping to a special value have
        // the same local name).
        Set<RippleValue> uniqueValues = new HashSet<RippleValue>();
        for (URI u : options) {
            uniqueValues.add(mc.canonicalValue(new RDFValue(u)));
        }

        if (0 == uniqueValues.size()) {
            errors.println("Warning: keyword '" + localName + "' is not defined\n");
        } else if (1 < uniqueValues.size()) {
            errors.println("Warning: keyword '" + localName + "' is ambiguous\n");
        }

        for (RippleValue v : uniqueValues) {
            sink.put(v);
        }
    }

    public void uriForQName(final String nsPrefix,
                            final String localName,
                            final Sink<RippleValue, RippleException> sink,
                            final ModelConnection mc,
                            final PrintStream errors) throws RippleException {
        String ns = getNamespaceUri(nsPrefix);

        if (null == ns) {
            errors.println("Warning: prefix '" + nsPrefix + "' does not identify a namespace\n");
        } else {
            // TODO: using URIImpl is a bit of a hack
            sink.put(mc.canonicalValue(new RDFValue(new URIImpl(ns + localName))));
        }
    }

    public String getDefaultNamespace() throws RippleException {
        String uri = getNamespaceUri("");

        if (null == uri) {
            throw new RippleException("no default namespace is defined.  Use '@prefix : <...>.'\n");
        }

        return uri;
    }

    ////////////////////////////////////////////////////////////////////////////

    /**
     * Defines a new prefix:namespace pair.
     * In order to maintain a one-to-one mapping of prefixes and URIs, any namespaces in which the given prefix or URI exist will be removed
     * before the new namespace is added.
     *
     * @param prefix the prefix of the namespace, e.g. <code>"foaf"</code>
     * @param uri    the URI to which the prefix is bound, e.g. <code>"http://xmlns.com/foaf/0.1/"</code>
     */
    public void setNamespace(final String prefix,
                             final String uri,
                             final ModelConnection mc) throws RippleException {
        mc.setNamespace(prefix, uri, true);

        String p = uriToPrefix.remove(uri);
        String u = prefixToUri.remove(prefix);
        if (null != p) {
            prefixToUri.remove(p);
        }
        if (null != u) {
            uriToPrefix.remove(u);
        }

        prefixToUri.put(prefix, uri);
        uriToPrefix.put(uri, prefix);
    }

    /**
     * Removes a namespace definition.
     * In order to maintain a one-to-one mapping of prefixes and URIs, both prefix and URI of an existing namespace will be unbound.
     *
     * @param prefix the prefix of the namespace to remove
     */
    public void removeNamespace(final String prefix) {
        String u = prefixToUri.remove(prefix);
        if (null != u) {
            uriToPrefix.remove(u);
        }
    }

    // Note: assumes that the same URI will not be added twice.
    public void addURI(final URI uri) throws RippleException {
        // If possible, add a qualified name as well.
        String prefix = uriToPrefix.get(uri.getNamespace());
        if (null != prefix) {
            String qName = prefix + ":" + uri.getLocalName();
            allQNames.add(qName);
        }
    }

    public void addCommonNamespaces( final ModelConnection mc ) throws RippleException {
        try {
            InputStream is = Ripple.class.getResourceAsStream("common-namespaces.txt");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String l;
                while ((l = br.readLine()) != null) {
                    l = l.trim();
                    if (0 < l.length() && !l.startsWith("#")) {
                        int i = l.indexOf("\t");
                        String prefix = l.substring(0, i);
                        String uri = l.substring(i + 1);

                        setNamespace(prefix, uri, mc);
                    }
                }
            } finally {
                is.close();
            }
        } catch (IOException e) {
            throw new RippleException(e);
        }
    }
}

