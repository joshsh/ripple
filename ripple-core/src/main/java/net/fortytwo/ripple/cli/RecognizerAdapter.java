package net.fortytwo.ripple.cli;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.ast.KeywordAST;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.query.Command;

import java.io.PrintStream;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class RecognizerAdapter {
    private final PrintStream errorStream;

    // A helper variable for the lexer and parser.
    private String languageTag;

    public RecognizerAdapter(final PrintStream errorStream) {
        this.errorStream = errorStream;
    }

    protected abstract void handleQuery(ListAST query) throws RippleException;

    protected abstract void handleCommand(Command command) throws RippleException;

    protected abstract void handleEvent(RecognizerEvent event) throws RippleException;

    protected abstract void handleAssignment(KeywordAST name) throws RippleException;

    public void putQuery(final ListAST query) {
        try {
            handleQuery(query);
        } catch (RippleException e) {
            errorStream.println("\nQuery error: " + e + "\n");
            e.logError();
        }
    }

    public void putCommand(final Command cmd) {
        try {
            handleCommand(cmd);
        } catch (RippleException e) {
            errorStream.println("\nCommand error: " + e + "\n");
            e.logError();
        }
    }

    public void putEvent(final RecognizerEvent event) {
//System.out.println("putting event: " + event);
        try {
            handleEvent(event);
        } catch (RippleException e) {
            errorStream.println("\nEvent error: " + e + "\n");
            e.logError();
        }
    }


    public void putAssignment(final KeywordAST name) {
        try {
            handleAssignment(name);
        } catch (RippleException e) {
            errorStream.println("\nAssignment failed: " + e + "\n");
        }
    }

    public String getLanguageTag() {
        return languageTag;
    }

    public void setLanguageTag(final String tag) {
        languageTag = tag;
    }
}

