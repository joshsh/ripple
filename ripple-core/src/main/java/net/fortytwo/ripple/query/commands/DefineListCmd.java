package net.fortytwo.ripple.query.commands;

import net.fortytwo.flow.Collector;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class DefineListCmd extends Command {
    private final ListAST list;
    private final String name;

    public DefineListCmd(final String name, final ListAST list) {
        this.list = list;
        this.name = name;
    }

    public String getListName() {
        return name;
    }

    public ListAST getList() {
        return list;
    }

    public void execute(final QueryEngine qe, final ModelConnection mc)
            throws RippleException {
        Collector<RippleList> expressions = new Collector<RippleList>();
        list.evaluate(expressions, qe, mc);

        if (expressions.size() == 0) {
            qe.getErrorPrintStream().println(
                    "Warning: the given expression did not resolve to a value.");
        } else if (expressions.size() > 1) {
            qe.getErrorPrintStream().println(
                    "Warning: the given expression resolved to multiple values.");
        } else {
            // Note: the first element of the list will also be a list
            RippleList expr = (RippleList) expressions.iterator().next().getFirst();
//System.out.println( "exprList = " + exprList );

            Value id = mc.valueOf(java.net.URI.create(qe.getLexicon().getDefaultNamespace() + name));
            expr.setRDFEquivalent(id);
            mc.internalize(expr);
            mc.commit();

            qe.getLexicon().addURI((URI) id);
            mc.getModel().getSpecialValues().put(id, expr);
        }
    }

    public String getName() {
        return "list";
    }

    protected void abort() {
    }
}

