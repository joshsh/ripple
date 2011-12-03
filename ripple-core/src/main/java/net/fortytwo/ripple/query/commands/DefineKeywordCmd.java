/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.query.commands;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.ast.KeywordAST;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;

public class DefineKeywordCmd extends Command {
    private final KeywordAST name;
    private final RippleValue value;

    public DefineKeywordCmd(final KeywordAST name,
                            final RippleValue value) {
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

