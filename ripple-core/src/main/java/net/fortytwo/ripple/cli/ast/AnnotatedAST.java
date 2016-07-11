package net.fortytwo.ripple.cli.ast;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.QueryEngine;

import java.util.Properties;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class AnnotatedAST implements AST<RippleList> {
    private final AST innerAst;

    public AnnotatedAST(final AST inner, final Properties props) {
        innerAst = inner;
    }

    public void evaluate(Sink<RippleList> sink,
                         QueryEngine qe,
                         ModelConnection mc)
            throws RippleException {
// TODO: create a PropertyAnnotatedValue class and translate into it
        innerAst.evaluate(sink, qe, mc);
    }
}


