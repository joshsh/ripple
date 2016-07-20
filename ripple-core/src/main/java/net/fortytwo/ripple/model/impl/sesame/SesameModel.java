package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.flow.rdf.diff.RDFDiffSink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleType;
import net.fortytwo.ripple.model.SpecialValueMap;
import net.fortytwo.ripple.model.types.BNodeType;
import net.fortytwo.ripple.model.types.BooleanType;
import net.fortytwo.ripple.model.types.IRIType;
import net.fortytwo.ripple.model.types.JSONObjectType;
import net.fortytwo.ripple.model.types.LanguageTaggedLiteralType;
import net.fortytwo.ripple.model.types.NonNumericLiteralType;
import net.fortytwo.ripple.model.types.NumberType;
import net.fortytwo.ripple.model.types.NumericLiteralType;
import net.fortytwo.ripple.model.types.OpType;
import net.fortytwo.ripple.model.types.OperatorType;
import net.fortytwo.ripple.model.types.SPARQLValueType;
import net.fortytwo.ripple.model.types.SesameListType;
import net.fortytwo.ripple.model.types.StackMappingType;
import net.fortytwo.ripple.model.types.StackMappingWrapperType;
import net.fortytwo.ripple.model.types.StringLiteralType;
import net.fortytwo.ripple.model.types.StringType;
import org.openrdf.IsolationLevel;
import org.openrdf.sail.Sail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * A Ripple <code>Model</code> implementation using the Sesame RDF toolkit.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SesameModel implements Model {
    private static final Logger logger = LoggerFactory.getLogger(SesameModel.class);

    private final Map<Class, List<RippleType>> registeredTypes;
    private final Set<Class> unmappedTypes;

    final Sail sail;
    final Set<ModelConnection> openConnections = new LinkedHashSet<>();
    SpecialValueMap specialValues;

    public SesameModel(final Sail sail) throws RippleException {
        this.sail = sail;

        logger.info("instantiating SesameModel");

        unmappedTypes = new HashSet<>();
        registeredTypes = new HashMap<>();

        // register string-typed literals before other literal types
        register(new StringLiteralType());
        register(new LanguageTaggedLiteralType());
        register(new NonNumericLiteralType());
        register(new NumericLiteralType());

        register(new BNodeType());
        register(new BooleanType());
        register(new JSONObjectType());
        register(new NumberType());
        register(new OperatorType());
        register(new OpType());
        // note: PrimitiveStackMapping types are registered on a per-primitive basis as libraries are loaded
        register(new SesameListType());
        register(new SPARQLValueType());
        register(new StackMappingType());
        register(new StackMappingWrapperType());
        register(new StringType());
        register(new IRIType());

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

                logger.warn(sb.toString());

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

        Class c = instance.getClass();
        List<RippleType> types = registeredTypes.get(c);

        if (null == types) {
            // quickly check whether this class has been marked as non mapped...
            if (unmappedTypes.contains(c)) {
                return null;
            }

            // ...before attempting to discover a mapped superclass or inherited interface
            types = findTypes(c, new Stack<>());
            if (null == types) {
                return null;
            }
        }

        // tie goes to the first matching type in order of registration
        for (RippleType type : types) {
            if (type.isInstance(instance)) {
                return type;
            }
        }

        return null;
    }

    private List<RippleType> findTypes(final Class c,
                                       final Stack<Class> hierarchy) {
        if (unmappedTypes.contains(c)) {
            return null;
        }

        hierarchy.push(c);

        List<RippleType> types = registeredTypes.get(c);
        if (null != types) {
            for (Class d : hierarchy) {
                List<RippleType> newTypes = new LinkedList<>();
                newTypes.addAll(types);
                registeredTypes.put(d, newTypes);
            }

            return types;
        }

        Class sc = c.getSuperclass();
        if (null != sc) {
            types = findTypes(sc, hierarchy);
            if (null != types) {
                return types;
            } else {
                unmappedTypes.add(sc);
            }
        }

        for (Class ic : c.getInterfaces()) {
            types = findTypes(ic, hierarchy);
            if (null != types) {
                return types;
            } else {
                unmappedTypes.add(ic);
            }
        }

        hierarchy.pop();
        unmappedTypes.add(c);
        return null;
    }

    @Override
    public List<IsolationLevel> getSupportedIsolationLevels() {
        return sail.getSupportedIsolationLevels();
    }

    @Override
    public IsolationLevel getDefaultIsolationLevel() {
        return sail.getDefaultIsolationLevel();
    }
}
