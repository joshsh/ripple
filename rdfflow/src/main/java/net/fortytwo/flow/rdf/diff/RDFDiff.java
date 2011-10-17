/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/main/java/net/fortytwo/flow/rdf/diff/RDFDiff.java $
 * $Revision: 135 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow.rdf.diff;

import net.fortytwo.flow.diff.DiffSink;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.diff.NullDiffSink;
import net.fortytwo.flow.rdf.RDFCollector;
import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.flow.rdf.RDFSource;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Statement;
import org.openrdf.model.Namespace;

public class RDFDiff implements RDFDiffSink, RDFDiffSource {
    private final RDFCollector added, subtracted;

    private final DiffSink<Statement> stSink;
    private final DiffSink<Namespace> nsSink;
    private final DiffSink<String> cmtSink;

    public RDFDiff() {
        added = new RDFCollector();
        subtracted = new RDFCollector();

        stSink = new DiffSink<Statement>() {
            public Sink<Statement> getPlus() {
                return added.statementSink();
            }

            public Sink<Statement> getMinus() {
                return subtracted.statementSink();
            }
        };

        nsSink = new DiffSink<Namespace>() {
            public Sink<Namespace> getPlus() {
                return added.namespaceSink();
            }

            public Sink<Namespace> getMinus() {
                return subtracted.namespaceSink();
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
        return added;
    }

    public RDFSink subtractorSink() {
        return subtracted;
    }

    public RDFSource adderSource() {
        return added;
    }

    public RDFSource subtractorSource() {
        return subtracted;
    }

    public void writeTo(final RDFDiffSink sink) throws RippleException {
        added.writeTo(sink.adderSink());
        subtracted.writeTo(sink.subtractorSink());
    }
}

