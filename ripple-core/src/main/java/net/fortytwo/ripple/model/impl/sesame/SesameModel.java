package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.flow.rdf.diff.RDFDiffSink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleType;
import net.fortytwo.ripple.model.SpecialValueMap;
import net.fortytwo.ripple.model.impl.sesame.types.BooleanType;
import net.fortytwo.ripple.model.impl.sesame.types.NumberType;
import net.fortytwo.ripple.model.impl.sesame.types.NumericLiteralType;
import net.fortytwo.ripple.model.impl.sesame.types.OpType;
import net.fortytwo.ripple.model.impl.sesame.types.OperatorType;
import net.fortytwo.ripple.model.impl.sesame.types.PrimitiveStackMappingType;
import net.fortytwo.ripple.model.impl.sesame.types.SesameListType;
import net.fortytwo.ripple.model.impl.sesame.types.StringType;
import org.openrdf.sail.Sail;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A Ripple <code>Model</code> implementation using the Sesame RDF toolkit.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SesameModel implements Model {
    private static final Logger logger = Logger.getLogger(SesameModel.class.getName());

    private final Map<Class, List<RippleType>> registeredTypes;

    final Sail sail;
    final Set<ModelConnection> openConnections = new LinkedHashSet<ModelConnection>();
    SpecialValueMap specialValues;

    public SesameModel(final Sail sail) throws RippleException {
        this.sail = sail;

        logger.fine("instantiating SesameModel");

        registeredTypes = new HashMap<>();
        register(new BooleanType());
        register(new NumberType());
        register(new NumericLiteralType());
        register(new OpType());
        register(new OperatorType());
        // note: PrimitiveStackMapping types are registered on a per-primitive basis as libraries are loaded
        register(new SesameListType());
        register(new StringType());

        ModelConnection mc = createConnection();
        try {
            // TODO: eliminate this temporary value map
            specialValues = new SpecialValueMap();
            specialValues = new LibraryLoader().load(mc);

            // At the moment, op needs to be a special value for the sake of the
            // evaluator.  This has the side-effect of making "op" a keyword.
            specialValues.add(Operator.OP, mc);

            // The nil list also needs to be special, so "nil" is also incidentally a keyword.
            specialValues.add(mc.list(), mc);

            mc.commit();
        } finally {
            mc.close();
        }
    }

    @Override
    public SpecialValueMap getSpecialValues() {
        return specialValues;
    }

    @Override
    public ModelConnection createConnection()
            throws RippleException {
        return new SesameModelConnection(this, null);
    }

    @Override
    public ModelConnection createConnection(final RDFDiffSink listener) throws RippleException {
        return new SesameModelConnection(this, listener);
    }

    @Override
    public void shutDown() throws RippleException {
        // Warn of any open connections, then close them
        synchronized (openConnections) {
            if (openConnections.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(openConnections.size()).append(" dangling connections: \"");
                boolean first = true;
                for (ModelConnection mc : openConnections) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(",");
                    }

                    sb.append(mc);
                }

                logger.warning(sb.toString());

                for (ModelConnection mc : openConnections) {
                    mc.reset(true);
                    mc.close();
                }
            }
        }
    }

    // Note: this method is not in the Model API
    public Sail getSail() {
        return sail;
    }

    @Override
    public void register(final RippleType type) {
        if (null == type || null == type.getInstanceClasses() || 0 == type.getInstanceClasses().size()) {
            throw new IllegalArgumentException();
        }

        for (Class c : (Iterable<Class>) type.getInstanceClasses()) {
            List<RippleType> types = registeredTypes.get(c);
            if (null == types) {
                types = new LinkedList<>();
                registeredTypes.put(c, types);
            }
            types.add(type);
        }
    }

    @Override
    public RippleType getTypeOf(final Object instance) {
        if (null == instance) {
            throw new IllegalArgumentException();
        }

        System.out.println("### a");
        System.out.println("class = " + instance.getClass());
        List<RippleType> types = registeredTypes.get(instance.getClass());
        System.out.println("### b");

        if (null == types) {
            return null;
        }

        // tie goes to the first matching type in order of registration
        for (RippleType type : types) {
            if (type.isInstance(instance)) {
                return type;
            }
        }

        return null;
    }
}
