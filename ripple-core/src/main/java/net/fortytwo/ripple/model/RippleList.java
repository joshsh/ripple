package net.fortytwo.ripple.model;

import net.fortytwo.ripple.ListNode;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import org.openrdf.model.Value;

import java.util.LinkedList;
import java.util.List;

/**
 * The head of a simple linked-list data structure
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class RippleList<T> extends ListNode<T> {

    protected T first;
    protected final RippleList<T> rest;

    protected RippleList(final T first,
                         final RippleList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    @Override
    public T getFirst() {
        return first;
    }

    @Override
    public RippleList getRest() {
        return rest;
    }

    public abstract void setRDFEquivalent(final Value id);

    /**
     * @return the number of items in this list.
     * This is purely a convenience method.
     */
    public int length() {
        int l = 0;

        ListNode<T> cur = this;
        while (!cur.isNil()) {
            l++;
            cur = cur.getRest();
        }

        return l;
    }

    /**
     * Gets the item at the specified index in the list.
     * This is purely a convenience method.
     *
     * @param i an index into the list, where the index 0 corresponds to the first item in the list
     * @return the corresponding list item
     * @throws RippleException if retrieval from the list fails
     */
    public T get(final int i)
            throws RippleException {
        if (i < 0) {
            throw new RippleException("list index out of bounds: " + i);
        }

        ListNode<T> cur = this;
        for (int j = 0; j < i; j++) {
            if (cur.isNil()) {
                throw new RippleException("list index out of bounds: " + i);
            }

            cur = cur.getRest();
        }

        return cur.getFirst();
    }

    public abstract RippleList push(T v) throws RippleException;

    public abstract RippleList invert();

    public abstract RippleList concat(final RippleList tail);

    public String toString() {
        StringBuilder sb = new StringBuilder();

        ListNode<T> cur = this;

        sb.append("(");

        boolean isFirst = true;
        while (!cur.isNil()) {
            T val = cur.getFirst();

            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(" ");
            }

            if (Operator.OP == val) {
                sb.append("op");
            } else {
                sb.append(val);
            }

            cur = cur.getRest();
        }

        sb.append(")");

        return sb.toString();
    }

    // Note: assumes diagrammatic order
    public void printTo(final RipplePrintStream p,
                        final ModelConnection mc,
                        final boolean includeParentheses)
            throws RippleException {
        ListNode<T> cur = this;

        if (includeParentheses) {
            p.print("(");
        }

        boolean isFirst = true;
        while (!cur.isNil()) {
            T val = cur.getFirst();

            if (Operator.OP == val) {
                p.print(".");
            } else {
                if (!isFirst) {
                    p.print(" ");

                }

                mc.print(val, p);
            }

            if (isFirst) {
                isFirst = false;
            }

            cur = cur.getRest();
        }

        if (includeParentheses) {
            p.print(")");
        }
    }

    /**
     * @return a Java list of the items in this list.  This is purely a convenience method.
     */
    public List<T> toJavaList() {
        LinkedList<T> javaList = new LinkedList<>();

        ListNode<T> cur = this;
        while (!cur.isNil()) {
            javaList.addLast(cur.getFirst());
            cur = cur.getRest();
        }

        return javaList;
    }

    public boolean equals(final Object other) {
        if (other instanceof RippleList) {
            ListNode<T> cur = this;
            ListNode<T> cur2 = (RippleList) other;

            while (!cur.isNil()) {
                if (cur2.isNil()) {
                    return false;
                }

                if (!cur.getFirst().equals(cur2.getFirst())) {
                    return false;
                }

                cur = cur.getRest();
                cur2 = cur2.getRest();
            }

            return cur2.isNil();
        } else {
            return false;
        }
    }

    public int hashCode() {
        int code = 1320672831;
        int pow = 2;

        ListNode<T> cur = this;
        while (!cur.isNil()) {
            code += pow * cur.getFirst().hashCode();
            pow *= 2;
            cur = cur.getRest();
        }

        return code;
    }
}
