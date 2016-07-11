package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.types.PrimitiveStackMappingType;

import java.net.URI;

/**
 * RDF data and Java implementation of a library of primitive functions.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class Library {
    public abstract void load(LibraryLoader.Context context)
            throws RippleException;

    protected void registerPrimitives(final LibraryLoader.Context context,
                                      final Class... classes)
            throws RippleException {
        for (Class c : classes) {
            registerPrimitive(c, context);
        }
    }

    protected PrimitiveStackMapping registerPrimitive(final Class<? extends PrimitiveStackMapping> c,
                                                      final LibraryLoader.Context context)
            throws RippleException {
        final ModelConnection mc = context.getModelConnection();
        PrimitiveStackMapping prim;

        try {
            prim = c.newInstance();
            prim.setRdfEquivalent(mc.valueOf(URI.create(prim.getIdentifiers()[0])));
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RippleException(e);
        }

        PrimitiveStackMappingType type = new PrimitiveStackMappingType(c);
        mc.getModel().register(type);

        // Add the primitive's stated URI to the map.
        context.addPrimaryValue(mc.toRDF(prim), prim);

        // Add all stated aliases (but no aliases of aliases) to the map.
        String[] identifiers = prim.getIdentifiers();
        for (int i = 1; i < identifiers.length; i++) {
            context.addAlias(mc.valueOf(URI.create(identifiers[i])), prim);
        }

        return prim;
    }
}
