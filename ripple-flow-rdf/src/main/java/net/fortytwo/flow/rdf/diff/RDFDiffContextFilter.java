package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.diff.DiffSink;
import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public final class RDFDiffContextFilter implements RDFDiffSink {
    private final Map<Resource, RDFDiffCollector> contextToCollectorMap;
    private final RDFSink addSink;
    private final RDFSink subSink;
    private final DiffSink<Statement> stSink;
    private final DiffSink<Namespace> nsSink;
    private final DiffSink<String> cmtSink;

    public RDFDiffContextFilter() {
        contextToCollectorMap = new HashMap<>();

        final Sink<Statement> stAddSink = st -> {
            Resource context = st.getContext();

            RDFDiffCollector sink = contextToCollectorMap.get(context);
            if (null == sink) {
                sink = new RDFDiffCollector();
                contextToCollectorMap.put(context, sink);
            }

            sink.adderSink().statementSink().accept(st);
        };

        final Sink<Statement> stSubSink = st -> {
            Resource context = st.getContext();

            RDFDiffCollector sink = contextToCollectorMap.get(context);
            if (null == sink) {
                sink = new RDFDiffCollector();
                contextToCollectorMap.put(context, sink);
            }

            sink.subtractorSink().statementSink().accept(st);
        };

        final Sink<Namespace> nsAddSink = ns -> contextToCollectorMap.get(null).adderSink().namespaceSink().accept(ns);

        final Sink<Namespace> nsSubSink = ns -> contextToCollectorMap.get(null).subtractorSink().namespaceSink().accept(ns);

        final Sink<String> cmtAddSink = comment -> contextToCollectorMap.get(null).adderSink().commentSink().accept(comment);

        final Sink<String> cmtSubSink = comment -> contextToCollectorMap.get(null).subtractorSink().commentSink().accept(comment);

        addSink = new RDFSink() {
            public Sink<Statement> statementSink() {
                return stAddSink;
            }

            public Sink<Namespace> namespaceSink() {
                return nsAddSink;
            }

            public Sink<String> commentSink() {
                return cmtAddSink;
            }
        };

        subSink = new RDFSink() {
            public Sink<Statement> statementSink() {
                return stSubSink;
            }

            public Sink<Namespace> namespaceSink() {
                return nsSubSink;
            }

            public Sink<String> commentSink() {
                return cmtSubSink;
            }
        };

        stSink = new DiffSink<Statement>() {
            public Sink<Statement> getPlus() {
                return stAddSink;
            }

            public Sink<Statement> getMinus() {
                return stSubSink;
            }
        };

        nsSink = new DiffSink<Namespace>() {
            public Sink<Namespace> getPlus() {
                return nsAddSink;
            }

            public Sink<Namespace> getMinus() {
                return nsSubSink;
            }
        };

        cmtSink = new DiffSink<String>() {
            public Sink<String> getPlus() {
                return cmtAddSink;
            }

            public Sink<String> getMinus() {
                return cmtSubSink;
            }
        };

        clear();
    }

    public Iterator<Resource> contextIterator() {
        return contextToCollectorMap.keySet().iterator();
    }

    public RDFDiffSource sourceForContext(final Resource context) {
        return contextToCollectorMap.get(context);
    }

    private void clear() {
        contextToCollectorMap.clear();

        RDFDiffCollector nullCollector = new RDFDiffCollector();
        contextToCollectorMap.put(null, nullCollector);
    }

    public void clearContext(final Resource context) {
        contextToCollectorMap.remove(context);
    }

    public RDFSink adderSink() {
        return addSink;
    }

    public RDFSink subtractorSink() {
        return subSink;
    }

    public DiffSink<Statement> statementSink() {
        return stSink;
    }

    public DiffSink<Namespace> namespaceSink() {
        return nsSink;
    }

    public DiffSink<String> commentSink() {
        return cmtSink;
    }
}

