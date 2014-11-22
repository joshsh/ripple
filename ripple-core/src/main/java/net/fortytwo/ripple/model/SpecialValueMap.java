package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A partial mapping of RDF values to a small set of native Ripple values
 * with special identity in the Ripple environment
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SpecialValueMap {
    private final Map<Value, Object> rdfToNativeMap;

    /**
     * Create a new map.
     */
    public SpecialValueMap() {
        rdfToNativeMap = new HashMap<>();
    }

    /**
     * Maps an RDF value into Ripple space.
     *
     * @param rdf a wrapped RDF value to map into Ripple space
     * @return a native data structure which is equated with the given RDF
     * value.  If there is no such data structure, the value itself.  This
     * method will never return <code>null</code>.
     */
    public Object get(final RDFValue rdf) {
        Object rpl = rdfToNativeMap.get(rdf.sesameValue());

        if (null == rpl) {
            rpl = rdf;
        }

        return rpl;
    }

    /**
     * Adds a new pair to the map.
     *
     * @param key   a value in RDF space
     * @param value a corresponding value in Ripple space
     */
    public void put(final Value key, final Object value) {
        rdfToNativeMap.put(key, value);
    }

    /**
     * Adds a value to the map, binding its associated RDF value back to the Ripple value.
     *
     * @param v  the Ripple value to add
     * @param mc a transactional connection
     * @throws RippleException if the associated RDF value of the Ripple value can't be determined
     */
    public void add(final Object v, final ModelConnection mc)
            throws RippleException {
        rdfToNativeMap.put(mc.toRDF(v).sesameValue(), v);
    }

    /**
     * Removes a value from the map.
     *
     * @param key the RDF value to remove
     */
    public void remove(final Value key) {
        rdfToNativeMap.remove(key);
    }

    /**
     * @return the set of all RDF values in the map
     */
    public Set<Value> keySet() {
        return rdfToNativeMap.keySet();
    }
}
