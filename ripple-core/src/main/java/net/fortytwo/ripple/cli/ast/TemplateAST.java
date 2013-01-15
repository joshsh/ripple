package net.fortytwo.ripple.cli.ast;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class TemplateAST extends ListAST
{
    public TemplateAST( final ListAST template, final ListAST expression )
    {
        ListAST expr = expression;

        ListAST params = template;//.invert();
        while ( !params.isNil() )
        {
//System.out.println("params = " + params);
//System.out.println("    expr = " + expr);
            AST p = params.getFirst();
//System.out.println("    p = " + p);
            if ( p instanceof ListAST )
            {
                expr = new TemplateAST( (ListAST) p, expr )
                        .push( new OperatorAST() );
            }

            else if ( p instanceof KeywordAST)
            {
                expr = new LambdaAST( ( (KeywordAST) p ).getName(), expr );
            }

            else
            {
                throw new IllegalArgumentException( "bad parameter AST: " + p );
            }

            params = params.getRest();
        }

        this.first = expr.getFirst();
        this.rest = expr.getRest();
    }
}
