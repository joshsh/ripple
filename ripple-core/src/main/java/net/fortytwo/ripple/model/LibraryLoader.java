package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Value;

import java.util.LinkedHashMap;
import java.util.ServiceLoader;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LibraryLoader extends ClassLoader {
    public LibraryLoader() {
        super(LibraryLoader.class.getClassLoader());
    }

    public SpecialValueMap load(final ModelConnection mc) throws RippleException {
        Context specialValues = new Context(mc);

        ServiceLoader<Library> loader = ServiceLoader.load(Library.class);
        for (Library l : loader) {
            l.load(specialValues);
        }

        return specialValues.createSpecialValueMap();
    }

    public class Context {
        // Note: LinkedHashMap is used because the order of added values is
        // significant.
        private final LinkedHashMap<Value, Object>
                primaryMap,
                aliasMap;

        private final ModelConnection modelConnection;

        public Context(final ModelConnection mc) {
            this.modelConnection = mc;
            primaryMap = new LinkedHashMap<Value, Object>();
            aliasMap = new LinkedHashMap<Value, Object>();
        }

        public ModelConnection getModelConnection() {
            return modelConnection;
        }

        public void addPrimaryValue(final Value v, final Object rv) {
            primaryMap.put(v, rv);
        }

        public void addAlias(final Value v, final Object rv) {
            aliasMap.put(v, rv);
        }

        public SpecialValueMap createSpecialValueMap() {
            SpecialValueMap map = new SpecialValueMap();

            // Primary values are added first, so that they have priority
            // over aliases.
            for (Value key : primaryMap.keySet()) {
                map.put(key, primaryMap.get(key));
            }

            // Aliases are added second.
            for (Value key : aliasMap.keySet()) {
                map.put(key, aliasMap.get(key));
            }

            return map;
        }
    }
}
