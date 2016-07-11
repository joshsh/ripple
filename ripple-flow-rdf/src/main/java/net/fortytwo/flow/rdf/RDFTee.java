package net.fortytwo.flow.rdf;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.Tee;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RDFTee implements RDFSink {
    private final Sink<Statement> statementTee;
    private final Sink<Namespace> namespaceTee;
    private final Sink<String> commentTee;

    public RDFTee(final RDFSink sinkA, final RDFSink sinkB) {
        statementTee = new Tee<>(sinkA.statementSink(), sinkB.statementSink());
        namespaceTee = new Tee<>(sinkA.namespaceSink(), sinkB.namespaceSink());
        commentTee = new Tee<>(sinkA.commentSink(), sinkB.commentSink());
    }

    public Sink<Statement> statementSink() {
        return statementTee;
    }

    public Sink<Namespace> namespaceSink() {
        return namespaceTee;
    }

    public Sink<String> commentSink() {
        return commentTee;
    }
}

