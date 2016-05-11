package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.flow.Collector;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Source;
import net.fortytwo.ripple.ListNode;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StatementPatternQuery;
import net.fortytwo.ripple.util.ModelConnectionHelper;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SesameList<T> extends RippleList<T> {

    private static final RippleList NIL = new SesameList();

    private static Map<Value, Source<RippleList>> nativeLists = new HashMap<Value, Source<RippleList>>();
    private static Boolean memoize;

    private Value rdfEquivalent;

    public static RippleList nilList() {
        return NIL;
    }

    public SesameList(final T first) {
        super(first, NIL);
    }

    public SesameList(final T first,
                      final RippleList rest) {
        super(first, rest);
    }

    // FIXME: this is temporary
    private SesameList() {
        super(null, null);
        // TODO: beware of null pointer exceptions in the list memoizer.
        first = null;

        rdfEquivalent = RDF.NIL;
    }

    public boolean isNil() {
        return null == rest;
    }

    public RippleList push(final T first) throws RippleException {
        return new SesameList(first, this);
    }

    public RippleList invert() {
        ListNode<T> in = this;
        RippleList out = NIL;

        while (NIL != in) {
            out = new SesameList(in.getFirst(), out);
            in = in.getRest();
        }

        return out;
    }

    public Value getRDFEquivalent(final ModelConnection mc) throws RippleException {
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

    public static <T> void from(final T v,
                                final Sink<RippleList> sink,
                                final ModelConnection mc)
            throws RippleException {
        if (null == memoize) {
            memoize = Ripple.getConfiguration().getBoolean(Ripple.MEMOIZE_LISTS_FROM_RDF);
        }

        // If already a list...
        if (v instanceof RippleList) {
            sink.accept((RippleList) v);
        }

        // If the argument is an RDF value, try to convert it to a native list.
        else if (v instanceof Value) {
            if (memoize) {
                Value rdfVal = (Value) v;
                Source<RippleList> source = nativeLists.get(rdfVal);
                if (null == source) {
                    Collector<RippleList> coll = new Collector<RippleList>();

                    listsFromRdf(v, coll, mc);

                    source = coll;
                    nativeLists.put(rdfVal, source);
                }

                source.writeTo(sink);
            } else {
                listsFromRdf(v, sink, mc);
            }
        }

        // Towards a more general notion of lists
        else {
            createConceptualList(v, sink, mc);
        }
    }

    // TODO: find a better name
    private static void createConceptualList(final Object head,
                                             final Sink<RippleList> sink,
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

        sink.accept(new SesameList(Operator.OP).push(head));
    }

    // TODO: extend circular lists and other convergent structures
    private static <T> void listsFromRdf(final T head,
                                         final Sink<RippleList> sink,
                                         final ModelConnection mc)
            throws RippleException {
        if (mc.toRDF(head).equals(RDF.NIL)) {
            sink.accept(NIL);
        } else {
            final Collector<Object> firstValues = new Collector<Object>();

            final Sink<RippleList> restSink = new Sink<RippleList>() {
                public void accept(final RippleList rest) throws RippleException {
                    Sink<Object> firstSink = new Sink<Object>() {
                        public void accept(final Object first) throws RippleException {
                            SesameList list = new SesameList(first, rest);
                            list.rdfEquivalent = mc.toRDF(head);
                            sink.accept(list);
                        }
                    };

                    firstValues.writeTo(firstSink);
                }
            };

            Sink<Object> rdfRestSink = new Sink<Object>() {
                public void accept(final Object rest) throws RippleException {
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

            multiply(mc, head, RDF.FIRST, firstValues);

            if (firstValues.size() > 0 || mc.toRDF(head).equals(RDF.NIL)) {
                multiply(mc, head, RDF.REST, rdfRestSink);
            } else {
                createConceptualList(head, sink, mc);
            }
        }
    }

    private static void multiply(final ModelConnection mc,
                                 final Object subj,
                                 final Object pred,
                                 final Sink sink) throws RippleException {
        StatementPatternQuery query = new StatementPatternQuery(subj, pred, null);
        mc.query(query, sink, false);
    }

    public void setRDFEquivalent(final Value id) {
        rdfEquivalent = id;
    }
}
