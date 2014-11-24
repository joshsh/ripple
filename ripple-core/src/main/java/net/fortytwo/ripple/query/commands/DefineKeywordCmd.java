package net.fortytwo.ripple.query.commands;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.ast.KeywordAST;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class DefineKeywordCmd extends Command {
    private final KeywordAST name;
    private final Object value;

    public DefineKeywordCmd(final KeywordAST name,
                            final Object value) {
        this.name = name;
        this.value = value;
    }

    public void execute(final QueryEngine qe, final ModelConnection mc)
            throws RippleException {
        qe.getLexicon().putTemporaryValue(name.getName(), value);
    }

    public String getName() {
        // Note: never used
        return "keyword";
    }

    protected void abort() {
    }
}

