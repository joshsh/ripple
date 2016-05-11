package net.fortytwo.ripple.model;

import net.fortytwo.flow.rdf.diff.RDFDiffSink;
import net.fortytwo.ripple.RippleException;
import org.openrdf.IsolationLevel;

import java.util.List;

/**
 * A gateway between an RDF data store and the native Ripple environment of stacks and streams.
 * Most of the things you can do in Ripple involve a <code>ModelConnection</code>,
 * which provides transactional access to a <code>Model</code>.
 * <p>
 * Native Ripple values are totally ordered in a Model,
 * and each one maps to exactly one RDF resource.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface Model {
    /**
     * @return a new, transactional connection to this model
     * @throws RippleException if creation of the connection fails
     */
    ModelConnection createConnection() throws RippleException;

    /**
     * @param listener a handler for RDF diffs of data added or removed from the model through the returned connection
     * @return a new, transactional connection to this model, to which the given listener is attached
     * @throws RippleException if creation of the connection fails
     */
    ModelConnection createConnection(RDFDiffSink listener) throws RippleException;

    /**
     * Adds support for a given data type to this model
     *
     * @param type a Ripple data type
     */
    void register(RippleType type);

    /**
     * Finds the Ripple type, if any, for the given object in this model
     *
     * @param instance the object to check
     * @return the associated Ripple type, if any
     */
    RippleType getTypeOf(Object instance);

    /**
     * @return a mapping of RDF space into Ripple space
     */
    SpecialValueMap getSpecialValues();

    /**
     * Shuts down this model, releasing its resources.
     *
     * @throws RippleException if anything goes wrong
     */
    void shutDown() throws RippleException;

    // TODO: JavaDoc
    List<IsolationLevel> getSupportedIsolationLevels();

    // TODO: Javadoc
    IsolationLevel getDefaultIsolationLevel();
}
