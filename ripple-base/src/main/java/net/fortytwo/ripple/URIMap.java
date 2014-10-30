package net.fortytwo.ripple;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/*
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class URIMap {
    private static final Logger LOGGER = Logger.getLogger(URIMap.class.getName());

    private final Map<String, String> map;

    private String[] fromUris;
    private String[] toUris;

    private boolean upToDate = true;

    public URIMap() {
        map = new Hashtable<String, String>();

        // Avoid NullPointerException of get() before update().
        update();
    }

    private String getPrivate(String urlStr)
            throws RippleException {
        // Strip off the fragment identifier, if any.
        int i = urlStr.lastIndexOf('#');
        if (i >= 0) {
            urlStr = urlStr.substring(0, i);
        }

        return urlStr;
    }

    private void update() {
        Set<String> keySet = map.keySet();

        fromUris = new String[keySet.size()];
        Iterator<String> keySetIter = keySet.iterator();
        int j = 0;
        while (keySetIter.hasNext()) {
            fromUris[j++] = keySetIter.next();
        }
        Arrays.sort(fromUris);

        toUris = new String[fromUris.length];
        for (int i = 0; i < fromUris.length; i++) {
            toUris[i] = map.get(fromUris[i]);
            LOGGER.fine("map " + fromUris[i] + " to " + toUris[i]);
        }

        upToDate = true;
    }

    public void put(final String from, final String to) {
        if (null == from) {
            throw new IllegalArgumentException("can't map from null");
        }

        if (null == to) {
            throw new IllegalArgumentException("can't map to null");
        }

        upToDate = false;

        map.put(from, to);
    }

    public String get(final String uri) throws RippleException {
        if (!upToDate) {
            update();
        }

        int fromIndex = 0, toIndex = fromUris.length - 1;
        int mid, cmp;
        int i = -1;

        while (fromIndex <= toIndex) {
            mid = (fromIndex + toIndex) / 2;
            cmp = uri.compareTo(fromUris[mid]);

            if (cmp > 0) {
                fromIndex = mid + 1;
                i = mid;
            } else if (cmp < 0) {
                toIndex = mid - 1;
            }

            // Exact match.  Return the corresponding target URI.
            else {
                return getPrivate(toUris[mid]);
            }
        }

        if (-1 < i && uri.startsWith(fromUris[i])) {
            return getPrivate(
                    toUris[i] + uri.substring(fromUris[i].length()));
        }

        // No rewrite necessary.
        else {
            return getPrivate(uri);
        }
    }
}

