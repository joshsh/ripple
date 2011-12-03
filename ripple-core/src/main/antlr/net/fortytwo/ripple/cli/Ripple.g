header
{
package net.fortytwo.ripple.cli;

import java.util.Properties;
import java.util.List;
import java.util.LinkedList;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.cli.ast.AST;
import net.fortytwo.ripple.cli.ast.AnnotatedAST;
import net.fortytwo.ripple.cli.ast.BooleanAST;
import net.fortytwo.ripple.cli.ast.DecimalAST;
import net.fortytwo.ripple.cli.ast.DoubleAST;
import net.fortytwo.ripple.cli.ast.IntegerAST;
import net.fortytwo.ripple.cli.ast.KeywordAST;
import net.fortytwo.ripple.cli.ast.LambdaAST;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.cli.ast.NumberAST;
import net.fortytwo.ripple.cli.ast.OperatorAST;
import net.fortytwo.ripple.cli.ast.PlainLiteralAST;
import net.fortytwo.ripple.cli.ast.QNameAST;
import net.fortytwo.ripple.cli.ast.TemplateAST;
import net.fortytwo.ripple.cli.ast.TypedLiteralAST;
import net.fortytwo.ripple.cli.ast.URIAST;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.commands.DefinePrefixCmd;
import net.fortytwo.ripple.query.commands.DefineListCmd;
import net.fortytwo.ripple.query.commands.QuitCmd;
import net.fortytwo.ripple.query.commands.RedefineListCmd;
import net.fortytwo.ripple.query.commands.ShowContextsCmd;
import net.fortytwo.ripple.query.commands.ShowPrefixesCmd;
import net.fortytwo.ripple.query.commands.UndefinePrefixCmd;
import net.fortytwo.ripple.query.commands.UndefineListCmd;
}


////////////////////////////////////////////////////////////////////////////////


class RippleLexer extends Lexer;

options
{
    // Note the @unprefix and @undefine commands, which share the same three-character prefix.
	k = 4;

	// Use custom error recovery.
	defaultErrorHandler = false;
}

{
	RecognizerAdapter adapter = null;

	public void initialize( final RecognizerAdapter i )
	{
		adapter = i;
	}

	/*void matchEndOfLine()
	{
		adapter.putEvent( RecognizerEvent.NEWLINE );
	}*/
}


protected
WS_CHAR
	: ' ' | '\t' | '\r'
	;

WS
	: (WS_CHAR)+
	;


LINE_CONT
    : '\\' (WS_CHAR)* '\n' //{ newline(); }
    ;


NEWLINE
	: '\n' { newline(); /*matchEndOfLine();*/ }
    ;

protected
HEX
	: ('0'..'9')
	| ('A'..'F')
	;

protected
SCHARACTER
	: ' ' | '!' | ('#'..'[')  // excludes: '\"', '\\'
	| (']'..'\uFFFF')  // Note: '\u10FFFF' in Turtle
	| "\\u" HEX HEX HEX HEX
	| "\\U" HEX HEX HEX HEX HEX HEX HEX HEX
	| '\\' ('\\' | '\"' | 't' | 'n' | 'r' )
	;

protected
UCHARACTER
	: (' '..';') | '=' | ('?'..'[')  // excludes: '>', '\\' and '<' (the LAST of which is not excluded by Turtle)
	| (']'..'\uFFFF')  // Note: '\u10FFFF' in Turtle
	| "\\u" HEX HEX HEX HEX
	| "\\U" HEX HEX HEX HEX HEX HEX HEX HEX
	| '\\' ('\\' | '>' | '<')
	;

protected
LANGUAGE
	: ( '@'! ('a'..'z')+ ('-' (('a'..'z') | ('0'..'9'))+)* )
		{ adapter.setLanguageTag( $getText ); }
	;

STRING
	: '\"'!
		{ adapter.setLanguageTag( null ); }
		( SCHARACTER )* '\"'! ( LANGUAGE! )?
	;

/*
LONG_STRING
	: "\"\"\""!
		{ adapter.setLanguageTag( null ); }
		( SCHARACTER )* "\"\"\""! ( LANGUAGE! )?
	;
*/

URIREF
	: '<'! ( UCHARACTER )* '>'!
	;

protected
DIGIT
	: ('0' .. '9')
	;

protected
NAME_START_CHAR_NOUSC
	: ('A' .. 'Z') | ('a' .. 'z')
	| ('\u00C0'..'\u00D6')
	| ('\u00D8'..'\u00F6')
	| ('\u00F8'..'\u02FF')
	| ('\u0370'..'\u037D')
	| ('\u037F'..'\u1FFF')
	| ('\u200C'..'\u200D')
	| ('\u2070'..'\u218F')
	| ('\u2C00'..'\u2FEF')
	| ('\u3001'..'\uD7FF')
	| ('\uF900'..'\uFDCF')
	| ('\uFDF0'..'\uFFFD')
//	| ('\u10000'..'\uEFFFF')
	;

protected
NAME_CHAR
	: ( NAME_START_CHAR_NOUSC | '_' )
	| '-' | DIGIT
	| '\u00B7'
	| ('\u0300'..'\u036F')
	| ('\u203F'..'\u2040')
	;

// Could be a name or a prefixName.
NAME_OR_PREFIX
	: NAME_START_CHAR_NOUSC ( NAME_CHAR )*
	;

// Can only be a name.
NAME_NOT_PREFIX
	: '_' (NAME_CHAR)*
	;

UNSIGNED_NUMBER
	: //('-' | '+')?
	  ( DIGIT )+
		(('.' DIGIT ) => ( '.' ( DIGIT )+ )
		 | ())
		(('e' | 'E') ('-' | '+')? ( DIGIT )+ )?
	;

// Ignore comments.
COMMENT
	: ( '#' ( ~('\n') )* ) { $setType( Token.SKIP ); }
	;

/*
MULTI_LINE_COMMENT
	: "(:" ((~':') | (':' ~')'))* ":)"
		{ $setType( Token.SKIP ); }
	;
*/

DOUBLE_HAT : "^^" ;

L_PAREN : '(' ;
R_PAREN : ')' ;

L_BRACKET : '[' ;
R_BRACKET : ']' ;

L_CURLY : '{' ;
R_CURLY : '}' ;

COLON : ':' ;
COMMA : ',';
EQUAL : '=';
MINUS : '-';
PERIOD : '.' ;
PLUS : '+';
QUESTION : '?';
SEMI : ';' ;
STAR : '*';
TILDE : '~';

protected
DRCTV : '@' ;

DRCTV_HELP      : DRCTV ( "help"          | "h" ) ;
DRCTV_LIST      : DRCTV ( "list"          | "l" ) ;
DRCTV_PREFIX    : DRCTV ( "prefix"        | "p" ) ;
DRCTV_QUIT      : DRCTV ( "quit"          | "q" | "exit" ) ;
DRCTV_RELIST    : DRCTV ( "relist"        | "r" ) ;
DRCTV_SHOW      : DRCTV ( "show"          | "s" ) ;
DRCTV_UNLIST    : DRCTV ( "unlist"        | "ul" ) ;
DRCTV_UNPREFIX  : DRCTV ( "unprefix"      | "up" ) ;


////////////////////////////////////////////////////////////////////////////////


class RippleParser extends Parser;
options
{
	k = 1;
	buildAST = false;

	// Use custom error recovery.
	defaultErrorHandler = false;
}

{
	private RecognizerAdapter adapter = null;

	public void initialize( final RecognizerAdapter i )
	{
		adapter = i;
	}

	void matchEndOfLine()
	{
		adapter.putEvent( RecognizerEvent.NEWLINE );
	}

	public void matchCommand( final Command cmd )
	{
		adapter.putCommand( cmd );
	}

	public void matchQuery( final ListAST query )
	{
		adapter.putQuery( query );
	}

    public void matchAssignment( final KeywordAST name )
    {
        adapter.putAssignment( name );
    }

	public void matchQuit()
	{
		adapter.putEvent( RecognizerEvent.QUIT );
	}

	private String unescape( final String s )
	{
	    try
	    {
	        return StringUtils.unescapeString( s );
        }

        catch ( RippleException e )
        {
            e.logError();
            System.exit( 1 );
            return null;
        }
    }
}


nt_Document
{
	// Request a first line of input from the interface (the lexer will request
	// additional input as it matches newlines).
	adapter.putEvent( RecognizerEvent.NEWLINE );
}
//	: ( (nt_Ws)? nt_Statement )* EOF
    : nt_Statements
	;


nt_Statements
    : (nt_Ws)?
      ( (EOF) => EOF
        | nt_Statement { matchEndOfLine(); } nt_Statements )
    ;

nt_Ws
	// Note: consecutive WS tokens occur when the lexer matches a COMMENT
	//       between them.
	: (WS | LINE_CONT)+
	;


nt_Statement
{
	ListAST r;
	KeywordAST k = null;
}
	// A directive is executed as soon as PERIOD is matched in the individual rule.
	: nt_Directive (nt_Ws)? NEWLINE

	// Query statements are always lists.
	| (r=nt_List
	  (EQUAL (nt_Ws)? k=nt_Keyword (nt_Ws)?)?
	  NEWLINE
	) {
	    matchQuery( r );
	    if (null != k) { matchAssignment( k ); }
	  }

    | (EQUAL (nt_Ws)? k=nt_Keyword (nt_Ws)? NEWLINE ) { matchAssignment( k ); }

	// Empty statements are effectively ignored.
	| NEWLINE { matchQuery( new ListAST() ); }
	;


nt_List returns [ ListAST list ]
{
	AST first;
	ListAST rest = null;
	list = null;
}
	:	// The head of the list.
	    first = nt_Node

		(	(WS | LINE_CONT) => ( nt_Ws
				( (~(R_PAREN)) => rest = nt_List
				| {}
				) )

        // Only operators may follow other nodes without being separated by whitespace.
		|	(PERIOD | QUESTION | STAR | TILDE | L_CURLY) => rest = nt_List

		|   (PLUS (~(UNSIGNED_NUMBER))) => rest = nt_List

			// End of the list.
		|	()
		)
			{
				if ( null == rest )
				{
					rest = new ListAST();
				}

				list = new ListAST( first, rest );
			}
	;


nt_TemplateList returns [ ListAST list ]
{
	AST first;
	ListAST rest = null;
	list = null;
}
	:   // Head of the list.
		first = nt_TemplateNode

		(	(WS | LINE_CONT) => ( nt_Ws
				( (~(R_PAREN)) => rest = nt_TemplateList
				| {}
				) )

			// Tail of the list.
		|	(~(WS | LINE_CONT | R_PAREN)) => rest = nt_TemplateList

			// End of the list.
		|	()
		)
			{
				if ( null == rest )
				{
					rest = new ListAST();
				}

				list = new ListAST( first, rest );
			}
	;


nt_ParenthesizedTemplateList returns [ ListAST r ]
{
	r = null;
}
	: L_PAREN (nt_Ws)? (
		( r = nt_TemplateList /*(nt_Ws)?*/ R_PAREN )
		| R_PAREN { r = new ListAST(); } )
	;


nt_TemplateNode returns [ AST r ]
{
	r = null;
}
	: ( r=nt_Keyword
		| r=nt_ParenthesizedTemplateList
		)
	;
		

// Operators are not "normal nodes" in that they can't have annotations.
nt_Node returns [ AST r ]
{
}
    // Note: this disambiguation is necessary because of the '+' symbol, which is either an operator or the beginning of a number.
    //       It is more straightforward to do this disambiguation here than in the lexer.
    : (PERIOD | QUESTION | STAR | TILDE | L_CURLY) => r=nt_Operator
    | (PLUS UNSIGNED_NUMBER) => r=nt_NormalNode
    | (PLUS (~(UNSIGNED_NUMBER))) => r=nt_Operator
    | r=nt_NormalNode
    ;


nt_NormalNode returns [ AST r ]
{
	Properties props;
}
	: ( r=nt_Resource
		| r=nt_Literal
		| r=nt_ParenthesizedList
		)
	  (( (WS | LINE_CONT)? L_BRACKET ) => ( (WS | LINE_CONT)? props=nt_Properties { r = new AnnotatedAST( r, props ); } )
	  | ())
	;


nt_Properties returns [ Properties props ]
{
	props = new Properties();
}
	: L_BRACKET (WS | LINE_CONT)? nt_PropertyList[props] R_BRACKET
	;

	
nt_PropertyList[ Properties props ]
{
	String name;
}
	: name=nt_PropertyName EQUAL value:STRING (WS | LINE_CONT)? { props.setProperty( name, value.getText() ); }
		( COMMA (WS | LINE_CONT)? nt_PropertyList[props] )?
	;
	
	
nt_PropertyName returns [ String name ]
{
	String rest;
}
	: name=nt_Name
		( PERIOD rest=nt_PropertyName { name += rest; } )?
	;
	
	
nt_ParenthesizedList returns [ ListAST r ]
{
	r = null;
}
	: L_PAREN (nt_Ws)? (
		( r = nt_List /*(nt_Ws)?*/ R_PAREN )
		| R_PAREN { r = new ListAST(); } )
	;


nt_Literal returns [ AST r ]
{
	r = null;
	AST dataType = null;
}
	: ( t:STRING

		/* Note: only a URI reference is accepted as a datatype, in contrast to Turtle,
		 which also allows blank-node datatypes */
		( DOUBLE_HAT dataType=nt_Resource )?
	)
		{
		    String label = unescape( t.getText() );

			r = ( null == dataType )
				? new PlainLiteralAST( label, adapter.getLanguageTag() )
				: new TypedLiteralAST( label, dataType );
		}
	| r=nt_Number
	;


nt_Number returns [ NumberAST r ]
{
    boolean negative = false;
    r = null;
}
    :   (PLUS | MINUS { negative = true; })?
        u:UNSIGNED_NUMBER
		{
			// Note: number format exceptions are handled at a higher level.
			String s = u.getText();
			if (negative) {
			    s = "-" + s;
			}

            // Numbers with an exponent portion are considered to be xsd:double
            // values.  A decimal point is optional, but must be followed by at
            // least one digit if present.
            if ( s.contains( "e" ) || s.contains( "E" ) )
            {
                r = new DoubleAST( s );
            }

            // Numbers with no exponent portion but with a decimal point are
            // considered to be xsd:decimal values.
			else if ( s.contains( "." ) )
			{
    		    r = new DecimalAST( s );
			}

            // All other numbers (with neither an exponent portion nor a decimal
            // point) are considered to be xsd:integer values.
			else
			{
				r = new IntegerAST( s );
			}
		}
	;


nt_Resource returns [ AST r ]
{
	r = null;
}
	: r=nt_URIRef
	| ( (NAME_OR_PREFIX)? COLON ) => r=nt_QName
	| r=nt_KeywordOrBoolean
	;


nt_URIRef returns [ URIAST r ]
{
	r = null;
}
	: uri:URIREF
		{
			r = new URIAST( uri.getText() );
		}
	;


nt_PrefixName returns [ String prefix ]
{
	prefix = null;
}
	: t:NAME_OR_PREFIX { prefix = t.getText(); }
	;


nt_Keyword returns [ KeywordAST r ]
{
	String keyword;
	r = null;
}
	: keyword=nt_Name { r = new KeywordAST( keyword ); }
	;


nt_KeywordOrBoolean returns [ AST r ]
{
	String keyword;
	r = null;
}
	: keyword=nt_Name {
	    r = keyword.equals( "true" )
	            ? new BooleanAST( true )
	            : keyword.equals( "false" )
	                    ? new BooleanAST( false )
	                    : new KeywordAST( keyword ); }
	;


nt_QName returns [ AST r ]
{
	String nsPrefix = "", localName = "";
	r = null;
}
	: ( ( nsPrefix=nt_PrefixName )?
		COLON
		( localName=nt_Name )?
	  )
		{
			r = new QNameAST( nsPrefix, localName );
		}
	;


nt_Name returns [ String name ]
{
	name = null;
}
	: t1:NAME_OR_PREFIX { name = t1.getText(); }
	| t2:NAME_NOT_PREFIX { name = t2.getText(); }
	;


nt_Operator returns [ OperatorAST AST ]
{
	AST = null;
	OperatorAST.Type type = OperatorAST.Type.Apply;
	NumberAST minTimes = null, maxTimes = null;
}
	: ( PERIOD { AST = new OperatorAST(OperatorAST.Type.Apply); }
	  | QUESTION { AST = new OperatorAST(OperatorAST.Type.Option); }
	  | STAR { AST = new OperatorAST(OperatorAST.Type.Star); }
	  | PLUS { AST = new OperatorAST(OperatorAST.Type.Plus); }
	  | TILDE { AST = new OperatorAST(OperatorAST.Type.Inverse); }
	  | L_CURLY (nt_Ws)? minTimes=nt_Number (nt_Ws)? ( COMMA (nt_Ws)? maxTimes=nt_Number (nt_Ws)? )? R_CURLY
		  {
		    AST = null == maxTimes
		            ? new OperatorAST(minTimes)
		            : new OperatorAST(minTimes, maxTimes);
	      }
	  )
	;


nt_Directive
{
	URIAST ns;

	// Default to the empty (but non-null) prefix.
	String nsPrefix = "";

    String keyword = null;
	List<String> names = new LinkedList<String>();
	
	// Note: it is not possible to define a term with a nil AST.  If this were
	//       allowed, this would be equivalent to redefining rdf:nil in a new
	//       namespace, which is strange and probably not what the programmer
	//       intended.
	// TODO: however, in combination with template parameters, an empty expression may have a meaning other than rdf:nil
	ListAST rhs;

	ListAST lhs = new ListAST();
	boolean redefine = false;
}
    // FIXME: this syntax allows a parenthesized expression in place of the definition name
	: ( DRCTV_LIST || DRCTV_RELIST { redefine = true; } )
	        nt_Ws lhs=nt_TemplateList /*(nt_Ws)?*/ COLON (nt_Ws)? rhs=nt_List
		{
            lhs = lhs.invert();
            keyword = ( (KeywordAST) lhs.getFirst() ).getName();
            lhs = lhs.getRest().invert();

		    rhs = new TemplateAST( lhs, rhs );

			matchCommand( redefine
			        ? new RedefineListCmd( keyword, rhs )
			        : new DefineListCmd( keyword, rhs ) );
		}

	| DRCTV_HELP
		{
			System.out.println( "\nSorry, the @help directive is just a placeholder for now.\n" );
		}

	| DRCTV_SHOW nt_Ws
		( "contexts"
			{
				matchCommand( new ShowContextsCmd() );
			}
		| "prefixes"
			{
				matchCommand( new ShowPrefixesCmd() );
			}
		)

	| DRCTV_PREFIX nt_Ws ( nsPrefix=nt_PrefixName (nt_Ws)? )? COLON (nt_Ws)? ns=nt_URIRef ((nt_Ws)? PERIOD)?
		{
			matchCommand( new DefinePrefixCmd( nsPrefix, ns ) );
		}

	| DRCTV_QUIT
		{
			matchQuit();
//			matchCommand( new QuitCmd() );
		}

	| DRCTV_UNLIST nt_Ws keyword=nt_Name
		{
			matchCommand( new UndefineListCmd( keyword ) );
		}

	| DRCTV_UNPREFIX nt_Ws nsPrefix=nt_PrefixName
		{
			matchCommand( new UndefinePrefixCmd( nsPrefix ) );
		}
	;
