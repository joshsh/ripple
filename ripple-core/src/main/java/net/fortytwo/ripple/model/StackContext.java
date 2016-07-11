package net.fortytwo.ripple.model;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class StackContext implements Cloneable {
    private final ModelConnection modelConnection;

    protected RippleList stack;

    protected StackContext(final RippleList stack, final ModelConnection mc) {
        this.stack = stack;
        this.modelConnection = mc;
    }

    public RippleList getStack() {
        return this.stack;
    }

    public ModelConnection getModelConnection() {
        return this.modelConnection;
    }

    public StackContext with(final RippleList s) {
        try {
            StackContext clone = (StackContext) this.clone();
            clone.stack = s;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
}
