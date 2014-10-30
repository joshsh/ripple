package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.diff.DiffSink;
import net.fortytwo.flow.diff.NullDiffSink;
import net.fortytwo.flow.rdf.RDFCollector;
import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.flow.rdf.RDFSource;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RDFDiffCollector implements RDFDiffSource, RDFDiffSink {
    private final RDFCollector adderCollector, subtractorCollector;

    private final DiffSink<Statement> stSink;
    private final DiffSink<Namespace> nsSink;
    private final DiffSink<String> cmtSink;

    public RDFDiffCollector() {
        adderCollector = new RDFCollector();
        subtractorCollector = new RDFCollector();

        stSink = new DiffSink<Statement>() {
            public Sink<Statement> getPlus() {
                return adderCollector.statementSink();
            }

            public Sink<Statement> getMinus() {
                return subtractorCollector.statementSink();
            }
        };

        nsSink = new DiffSink<Namespace>() {
            public Sink<Namespace> getPlus() {
                return adderCollector.namespaceSink();
            }

            public Sink<Namespace> getMinus() {
                return subtractorCollector.namespaceSink();
            }
        };

        cmtSink = new NullDiffSink<String>();
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

    public RDFSink adderSink() {
        return adderCollector;
    }

    public RDFSink subtractorSink() {
        return subtractorCollector;
    }

    public RDFSource adderSource() {
        return adderCollector;
    }

    public RDFSource subtractorSource() {
        return subtractorCollector;
    }

    public void writeTo(final RDFDiffSink sink) throws RippleException {
        adderCollector.writeTo(sink.adderSink());
        subtractorCollector.writeTo(sink.subtractorSink());
    }

    public void clear() {
        adderCollector.clear();
        subtractorCollector.clear();
    }
}

