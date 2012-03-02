/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.keyval.KeyValueMapping;
import net.fortytwo.ripple.util.ModelConnectionHelper;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;

public class Operator implements RippleValue {
    public static final Op OP = new Op();

    private static final RDFValue RDF_FIRST = new RDFValue(RDF.FIRST);

    private final StackMapping mapping;

    private Operator(final String key) {
        mapping = new KeyValueMapping(key);
    }

    // Note: only for URI values (Literals are handled in createOperator)
    private Operator(final RDFValue pred) throws RippleException {
        mapping = new RDFPredicateMapping(StatementPatternQuery.Pattern.SP_O, pred, null);
    }

    public Operator(final StackMapping mapping) {
//System.out.println( "Operator[" + this + "](" + function + ")" );
        this.mapping = mapping;
    }

    public Operator(final RippleList list) {
//System.out.println( "Operator[" + this + "](" + list + ")" );
        mapping = new ListDequotation(list);
    }

    // TODO: implement equals() and hashCode() for all StackMappings
    public boolean equals(final Object other) {
        return ((Operator) other).mapping.equals(mapping) && (other instanceof Operator);
    }

    public int hashCode() {
        return 630572943 + mapping.hashCode();
    }

    public String toString() {
        return "Operator(" + mapping + ")";
    }

    public void printTo(final RipplePrintStream p)
            throws RippleException {
        p.print("[Operator: ");
        p.print(mapping);
        p.print("]");
    }

    public StackMapping getMapping() {
        return mapping;
    }

    public RDFValue toRDF(final ModelConnection mc)
            throws RippleException {
        return null;
    }

    /**
     * Finds the type of a value and creates an appropriate "active" wrapper.
     */
    public static void createOperator(final RippleValue v,
                                      final Sink<Operator> opSink,
                                      final ModelConnection mc)
            throws RippleException {
        //System.out.println("creating operator from: " + v);
        //System.out.println("\tsesameValue: " + v.toRDF(mc).sesameValue());
        // A function becomes active.
        if (v instanceof StackMapping) {
            opSink.put(new Operator((StackMapping) v));
            return;
        }

        // A list is dequoted.
        else if (v instanceof RippleList) {
            opSink.put(new Operator((RippleList) v));
            return;
        }

        // This is the messy part.  Attempt to guess the type of the object from
        // the available RDF statements, and create the appropriate object.
        if (v instanceof RDFValue) {
            //System.out.println("it's RDF");
            if (isRDFList((RDFValue) v, mc)) {
                //System.out.println("it IS a list");
                Sink<RippleList> listSink = new Sink<RippleList>() {
                    public void put(final RippleList list)
                            throws RippleException {
                        opSink.put(new Operator(list));
                    }
                };

                mc.toList(v, listSink);
                return;
            } else {
                //System.out.println("not a list");
                Value sv = ((RDFValue) v).sesameValue();

                if (sv instanceof Literal) {
                    //System.out.println("literal predicate: " + sv);
                    opSink.put(new Operator(((Literal) sv).getLabel()));
                    return;
                }

                // An RDF resource not otherwise recognizable becomes a predicate filter.
                else if (sv instanceof Resource) {
                    opSink.put(new Operator((RDFValue) v));
                    return;
                }
            }
        }

        // Anything else becomes an active nullary filter with no output.
        opSink.put(new Operator(new NullStackMapping()));
    }

    // TODO: replace this with something a little more clever
    public static boolean isRDFList(final RDFValue v, final ModelConnection mc)
            throws RippleException {
        // TODO: this is a bit of a hack
        if (v.sesameValue() instanceof Literal) {
            return false;
        }

        ModelConnectionHelper h = new ModelConnectionHelper(mc);
        return (v.sesameValue().equals(RDF.NIL)
                || null != h.findSingleObject(v, RDF_FIRST));
    }

    public Type getType() {
        return Type.OPERATOR;
    }
}

