/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;
import net.fortytwo.ripple.ListNode;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.StatementPatternQuery;
import net.fortytwo.ripple.util.ModelConnectionHelper;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;

import java.util.HashMap;
import java.util.Map;

/**
 * A
 */
public class SesameList extends RippleList {
    private static final RDFValue RDF_FIRST = new RDFValue(RDF.FIRST);
    private static final RDFValue RDF_REST = new RDFValue(RDF.REST);

    private static final RippleList NIL = new SesameList();

    private static Map<Value, Source<RippleList, RippleException>> nativeLists = new HashMap<Value, Source<RippleList, RippleException>>();
    private static Boolean memoize;

    private  RDFValue rdfEquivalent;

    public static RippleList nilList() {
        return NIL;
    }

    public SesameList(final RippleValue first) {
        super(first, NIL);
    }

    public SesameList(final RippleValue first,
                      final RippleList rest) {
        super(first, rest);
    }

    // FIXME: this is temporary
    private SesameList() {
        super(null, null);
        // Note: this is a trick to avoid null pointer exceptions in the list memoizer.
        first = this;

        rdfEquivalent = new RDFValue(RDF.NIL);
    }

    public boolean isNil() {
        return null == rest;
    }

    public RippleList push(final RippleValue first) throws RippleException {
        return new SesameList(first, this);
    }

    public RippleList invert() {
        ListNode<RippleValue> in = this;
        RippleList out = NIL;

        while (NIL != in) {
            out = new SesameList(in.getFirst(), out);
            in = in.getRest();
        }

        return out;
    }

    public StackMapping getMapping() {
        return null;
    }

    public RDFValue toRDF(final ModelConnection mc)
            throws RippleException {
        if (null == rdfEquivalent) {
            rdfEquivalent = new ModelConnectionHelper(mc).createRandomURI();
        }

        return rdfEquivalent;
    }

    public RippleList concat(final RippleList tail) {
        return (NIL == this)
                ? tail
                : new SesameList(this.getFirst(), concat(this.getRest(), tail));
    }

    public static RippleList concat(final RippleList head, final RippleList tail) {
        return (NIL == head)
                ? tail
                : new SesameList(head.getFirst(), concat(head.getRest(), tail));
    }

    public static void from(final RippleValue v,
                            final Sink<RippleList, RippleException> sink,
                            final ModelConnection mc)
            throws RippleException {
        if (null == memoize) {
            memoize = Ripple.getConfiguration().getBoolean(Ripple.MEMOIZE_LISTS_FROM_RDF);
        }

        // If already a list...
        if (v instanceof RippleList) {
            sink.put((RippleList) v);
        }

        // If the argument is an RDF value, try to convert it to a native list.
        else if (v instanceof RDFValue) {
            if (memoize) {
//System.out.println("looking for source for list: " + v);
                Value rdfVal = ((RDFValue) v).toRDF(mc).sesameValue();
                Source<RippleList, RippleException> source = nativeLists.get(rdfVal);
                if (null == source) {
                    Collector<RippleList, RippleException> coll = new Collector<RippleList, RippleException>();

                    listsFromRdf(v, coll, mc);

                    source = coll;
                    nativeLists.put(rdfVal, source);
                }
//else System.out.println("   found source for list");

                source.writeTo(sink);
            } else {
                listsFromRdf(v, sink, mc);
            }
        }

        // Towards a more general notion of lists
        else {
            createConceptualList(v, sink, mc);
        }

        /*
          // Otherwise, fail.
          else
          {
              throw new RippleException( "expecting " + RippleList.class + ", found " + v );
          }*/
    }

    // TODO: find a better name
    private static void createConceptualList(final RippleValue head,
                                             final Sink<RippleList, RippleException> sink,
                                             final ModelConnection mc)
            throws RippleException {
        /*
          Sink<Operator> opSink = new Sink<Operator>()
          {
              public void put( final Operator op )
                  throws RippleException
              {
                  sink.put( mc.list( op ) );
              }
          };
  
          Operator.createOperator( head, opSink, mc );
          */

        sink.put(new SesameList(Operator.OP).push(head));
    }

    // TODO: extend circular lists and other convergent structures
    private static void listsFromRdf(final RippleValue head,
                                     final Sink<RippleList, RippleException> sink,
                                     final ModelConnection mc)
            throws RippleException {
        if (head.toRDF(mc).sesameValue().equals(RDF.NIL)) {
            sink.put(NIL);
        } else {
            final Collector<RippleValue, RippleException> firstValues = new Collector<RippleValue, RippleException>();

            final Sink<RippleList, RippleException> restSink = new Sink<RippleList, RippleException>() {
                public void put(final RippleList rest) throws RippleException {
                    Sink<RippleValue, RippleException> firstSink = new Sink<RippleValue, RippleException>() {
                        public void put(final RippleValue first) throws RippleException {
                            SesameList list = new SesameList(first, rest);
                            list.rdfEquivalent = head.toRDF(mc);
                            sink.put(list);
                        }
                    };

                    firstValues.writeTo(firstSink);
                }
            };

            Sink<RippleValue, RippleException> rdfRestSink = new Sink<RippleValue, RippleException>() {
                public void put(final RippleValue rest) throws RippleException {
                    // Recurse.
                    listsFromRdf(rest, restSink, mc);
                }
            };

            /*
               Sink<RdfValue> rdfFirstSink = new Sink<RdfValue>()
               {
                   public void put( final RdfValue first ) throws RippleException
                   {
                       // Note: it might be more efficient to use ModelBridge only
                       //       lazily, binding RDF to generic RippleValues on an
                       //       as-needed basis.  However, for now there is no better
                       //       place to do this when we're coming from an rdf:List.
                       //       Consider a list containing operators.
                       firstValues.put( mc.getModel().getBridge().get( first ) );
                   }
               };*/

            multiply(mc, head, RDF_FIRST, firstValues);

            if (firstValues.size() > 0 || head.toRDF(mc).sesameValue().equals(RDF.NIL)) {
                multiply(mc, head, RDF_REST, rdfRestSink);
            } else {
                createConceptualList(head, sink, mc);
            }
        }
    }

    private static void multiply(final ModelConnection mc,
                                 final RippleValue subj,
                                 final RippleValue pred,
                                 final Sink<RippleValue, RippleException> sink) throws RippleException {
        StatementPatternQuery query = new StatementPatternQuery(subj, pred, null);
        mc.query(query, sink, false);
    }

    public void setRDF(final RDFValue id) {
        rdfEquivalent = id;
    }
}
