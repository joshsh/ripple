/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.ast;

import java.util.List;

/**
 * Author: josh
 * Date: Apr 8, 2008
 * Time: 12:28:41 PM
 */
public class LambdaAST extends ListAST
{
    private static final String
            CONS = "cons",
            DIP = "dip",
            DUP = "dup",
            POP = "pop";

    public LambdaAST( final String boundVariable, final ListAST expression )
    {
        ListAST expr = toConcatenativeExpression( boundVariable, expression );
        this.first = expr.getFirst();
        this.rest = expr.getRest();
    }

    public LambdaAST( List<String> boundVars, ListAST expression )
    {
        ListAST expr = expression;
//System.out.println( "original: " + expr );

        for ( int i = 0; i < boundVars.size(); i++ )
        {
//System.out.println( "replacing variable " + boundVars.get( i ));
            String boundVariable = boundVars.get( i );
            expr = toConcatenativeExpression( boundVariable, expr );
//System.out.println( "    intermediate: " + expr );
        }

        this.first = expr.getFirst();
        this.rest = expr.getRest();
    }

    private static ListAST toConcatenativeExpression(final String boundVariable, final ListAST expression)
    {
        int fo = firstOccurrence( boundVariable, expression );
 //System.out.println("(1) first occurrence of " + boundVariable + " in " + expression + " is " + fo );
 //        int count = countOccurrences( boundVariable, expression );

        if ( 0 > fo )
        {
            return expression
                    .push( new OperatorAST() )
                    .push( new KeywordAST( POP ) );
        }

        else
        {
           ListAST expr = expression;

           /*for ( int i = 0; i < count; i++ )
           {
               int fo = firstOccurrence( boundVariable, expr );
               expr = replaceFirstOccurrence( boundVariable, expr, fo );
           }

           for ( int i = 1; i < count; i++ )
           {
               expr = expr
                       .push( new OperatorAst() )
                       .push( new KeywordAst( "dup" ) );
           }*/

            int i = 0;
            while ( fo >= 0 )
            {
                expr = replaceFirstOccurrence( boundVariable, expr, fo );
                if ( i > 0 ) {
                    expr = expr
                            .push( new OperatorAST() )
                            .push( new KeywordAST( DUP ) );
                }
                i++;
                fo = firstOccurrence(boundVariable, expr);
//     System.out.println("(2) first occurrence of " + boundVariable + " in " + expr + " is " + fo );
            }

            return expr;
        }
    }

    private static ListAST replaceFirstOccurrence( final String name, ListAST list, final int i )
    {
        // If the to-be-replaced node is not at the head of the list, transform
        // the list so that it is at the head.
        if ( i > 0 )
        {
            ListAST head = new ListAST();
            for ( int j = 0; j < i; j++ )
            {
                head = new ListAST( list.getFirst(), head );
                list = list.getRest();
            }

            head = head.invert();
            AST first = list.getFirst();
            list = list.getRest()
                    .push( new OperatorAST() )
                    .push( new KeywordAST( DIP ) )
                    .push( head )
                    .push( first );
        }

        AST first = list.getFirst();

        if ( first instanceof ListAST)
        {
            return list.getRest()
                    .push( new OperatorAST() )
                    .push( new KeywordAST( CONS ) )
                    .push( toConcatenativeExpression( name, (ListAST) first ) );
        }

        // Note: assumed to be instanceof KeywordAst
        else
        {
            return list.getRest();
        }
    }

     /*private Ast replaceName( final String oldName, final String newName, final Ast ast )
     {
         if ( ast instanceof KeywordAst )
         {
             if ( ( (KeywordAst) ast ).getName().equals( oldName ) )
             {
                 return new KeywordAst( newName );
             }

             else
             {
                 return ast;
             }
         }

         else if ( ast instanceof ListAst )
         {
             ListAst list = (ListAst) ast;
             if ( ListAst.isNil( list ) )
             {
                 return list;
             }

             else
             {
                 return new ListAst( replaceName( oldName, newName, list.getFirst() ),
                         (ListAst) replaceName( oldName, newName, list.getRest() ) );
             }
         }

         else
         {
             return ast;
         }
     }

     private Ast el( ListAst list, final int i )
     {
         for ( int j = 0; j < i; j++ )
         {
             list = list.getRest();
         }

         return list.getFirst();
     }*/

    private static int countOccurrences( final String name, ListAST list )
    {
        int count = 0;
        while ( !list.isNil() )
        {
            AST a = list.getFirst();
            if ( a instanceof KeywordAST)
            {
                if ( ( (KeywordAST) a ).getName().equals( name ) )
                {
                    count++;
                }
            }

            else if ( a instanceof ListAST)
            {
                int j = countOccurrences( name, (ListAST) a );
                if ( j >= 0 )
                {
                    count++;
                }
            }

            list = list.getRest();
        }

        return count;
    }

    private static int firstOccurrence( final String name, ListAST list )
    {
        int i = 0;
        while ( !list.isNil() )
        {
            AST a = list.getFirst();
            if ( a instanceof KeywordAST)
            {
                if ( ( (KeywordAST) a ).getName().equals( name ) )
                {
                    return i;
                }
            }

            else if ( a instanceof ListAST)
            {
                int j = firstOccurrence( name, (ListAST) a );
                if ( j >= 0 )
                {
                    return i;
                }
            }

            list = list.getRest();
            i++;
        }

        return -1;
    }
}
