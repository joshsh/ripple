header
{
package net.fortytwo.ripple.cli;

import java.util.Properties;
import java.util.List;
import java.util.LinkedList;

import net.fortytwo.ripple.cli.ast.AST;
import net.fortytwo.ripple.cli.ast.BooleanAST;
import net.fortytwo.ripple.cli.ast.BlankNodeAST;
import net.fortytwo.ripple.cli.ast.DoubleAST;
import net.fortytwo.ripple.cli.ast.IntegerAST;
import net.fortytwo.ripple.cli.ast.KeywordAST;
import net.fortytwo.ripple.cli.ast.LambdaAST;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.cli.ast.OperatorAST;
import net.fortytwo.ripple.cli.ast.AnnotatedAST;
import net.fortytwo.ripple.cli.ast.QNameAST;
import net.fortytwo.ripple.cli.ast.StringAST;
import net.fortytwo.ripple.cli.ast.TemplateAST;
import net.fortytwo.ripple.cli.ast.TypedLiteralAST;
import net.fortytwo.ripple.cli.ast.URIAST;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.commands.CountStatementsCmd;
import net.fortytwo.ripple.query.commands.DefinePrefixCmd;
import net.fortytwo.ripple.query.commands.DefineTermCmd;
import net.fortytwo.ripple.query.commands.ExportCmd;
import net.fortytwo.ripple.query.commands.QuitCmd;
import net.fortytwo.ripple.query.commands.RedefineTermCmd;
import net.fortytwo.ripple.query.commands.ShowContextsCmd;
import net.fortytwo.ripple.query.commands.ShowNamespacesCmd;
import net.fortytwo.ripple.query.commands.UndefineTermCmd;
}


////////////////////////////////////////////////////////////////////////////////


class RippleLexer extends Lexer;

options
{
	k = 3;

	// Use custom error recovery.
	defaultErrorHandler = false;
}

{
	RecognizerAdapter adapter = null;

	public void initialize( final RecognizerAdapter i )
	{
		adapter = i;
	}

	void matchEndOfLine()
	{
		adapter.putEvent( RecognizerEvent.NEWLINE );
	}

/*
	void matchEscapeCharacter()
	{
System.out.println( "matchEscapeCharacter" );
		adapter.putEvent( RecognizerEvent.ESCAPE );
	}
*/
}


protected
WS_CHAR
	: ' ' | '\t' | '\r'
	| '\n'  { newline(); matchEndOfLine(); }
	;

WS
	: (WS_CHAR)+
	;

/*
ESC
	: //(('\0'..'\8') | ('\11'..'\12') | ('\14'..'\27'))
	  (('\u0000'..'\u0008') | ('\u000b'..'\u000c') | ('\u000e'..'\u001b'))
		{ matchEscapeCharacter(); $setType(Token.SKIP); }
	;
*/

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
	: (' '..';') | '=' | ('?'..'[')  // excludes: '>', '\\' and '<' (the lAST of which is not excluded by Turtle)
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

NODEID_PREFIX : "_:" ;

// Note: the '+' prefix (e.g. in +42) is excluded, as it interferes with the '+'
// operator.
NUMBER
	: ('-' /*| '+'*/)? ( DIGIT )+
		(('.' DIGIT ) => ( '.' ( DIGIT )+ )
		| ())
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

SEMI : ';' ;
PERIOD : '.' ;
COMMA : ',';

COLON : ':' ;

EQUAL : '=';

OP_APPLY_PRE : '/' ;
OP_APPLY_POST : ">>";
OP_INVERSE_APPLY : "<<";
OP_SUFFIX_OPTIONAL : "?" ;
OP_SUFFIX_STAR : "*";
OP_SUFFIX_PLUS : "+";

protected
DRCTV : '@' ;

DRCTV_COUNT     : DRCTV ( "count"         | "c" ) ;
DRCTV_DEFINE    : DRCTV ( "define"        | "d" ) ;
DRCTV_EXPORT    : DRCTV ( "export"        | "e" ) ;
DRCTV_HELP      : DRCTV ( "help"          | "h" ) ;
DRCTV_LIST      : DRCTV ( "list"          | "l" ) ;
DRCTV_PREFIX    : DRCTV ( "prefix"        | "p" ) ;
DRCTV_QUIT      : DRCTV ( "quit"          | "q" ) ;
DRCTV_REDEFINE  : DRCTV ( "redefine"      | "r" ) ;
DRCTV_UNDEFINE  : DRCTV ( "undefine"      | "u" ) ;


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

	public void matchCommand( final Command cmd )
	{
		adapter.putCommand( cmd );
	}

	public void matchQuery( final ListAST AST )
	{
		adapter.putQuery( AST );
	}

	public void matchContinuingQuery( final ListAST AST )
	{
		adapter.putContinuingQuery( AST );
	}

	public void matchQuit()
	{
		adapter.putEvent( RecognizerEvent.QUIT );
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
        | nt_Statement nt_Statements )
    ;

nt_Ws
	// Note: consecutive WS tokens occur when the lexer matches a COMMENT
	//       between them.
	: (WS)+
	;


nt_Statement
{
	ListAST r;
}
	// A directive is executed as soon as PERIOD is matched in the individual
	// rule.
	: nt_Directive

	// Query statements are always lists.
	| r=nt_List (
		PERIOD { matchQuery( r ); }
 		| SEMI { matchContinuingQuery( r ); } )

	// Empty statements are effectively ignored.
	| PERIOD { matchQuery( new ListAST() ); }
	| SEMI { matchContinuingQuery( new ListAST() ); }
	;


nt_List returns [ ListAST list ]
{
	AST first;
	ListAST rest = null;
	list = null;
	boolean modified = false;
}
		// Optional slash operator.
	:	( OP_APPLY_PRE (WS)? { modified = true; } )?

		// Head of the list.
		first = nt_Node

		(	(WS) => ( nt_Ws
				( (~(PERIOD | SEMI | R_PAREN )) => rest = nt_List
				| {}
				) )

			// Tail of the list.
		|	(~(WS | PERIOD | SEMI | R_PAREN)) => rest = nt_List

			// End of the list.
		|	()
		)
			{
				if ( null == rest )
				{
					rest = new ListAST();
				}

				if ( modified )
				{
					rest = new ListAST( new OperatorAST(), rest );
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

		(	(WS) => ( nt_Ws
				( (~(R_PAREN)) => rest = nt_TemplateList
				| {}
				) )

			// Tail of the list.
		|	(~(WS | R_PAREN)) => rest = nt_TemplateList

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
		

nt_Node returns [ AST r ]
{
	r = null;
	Properties props;
}
	: ( r=nt_Resource
		| r=nt_Literal
		| r=nt_ParenthesizedList
		| r=nt_Operator
		)
	  (( (WS)? L_BRACKET ) => ( (WS)? props=nt_Properties { r = new AnnotatedAST( r, props ); } )
	  | ())
	;


nt_Properties returns [ Properties props ]
{
	props = new Properties();
}
	: L_BRACKET (WS)? nt_PropertyList[props] R_BRACKET
	;

	
nt_PropertyList[ Properties props ]
{
	String name;
}
	: name=nt_PropertyName EQUAL value:STRING (WS)? { props.setProperty( name, value.getText() ); }
		( COMMA (WS)? nt_PropertyList[props] )?
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

		/* Note: for agreement with Turtle, the grammar allows any resource
				reference as the data type of a literal (i.e. a URI or a blank
				node).  However, the Sesame back end will only accept a URI. */
		( DOUBLE_HAT dataType=nt_Resource )?
	)
		{
			r = ( null == dataType )
				? new StringAST( t.getText(), adapter.getLanguageTag() )
				: new TypedLiteralAST( t.getText(), dataType );
		}
	| u:NUMBER
		{
			// Note: number format exceptions are handled at a higher level.
			String s = u.getText();

			if ( s.contains( "." ) )
			{
				r = new DoubleAST( ( new Double( s ) ).doubleValue() );
			}

			else
			{
				r = new IntegerAST( ( new Integer( s ) ).intValue() );
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
	| r=nt_BNodeRef
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


nt_Keyword returns [ AST r ]
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
//		( localName=nt_Name
//		| (~(NAME_OR_PREFIX | NAME_NOT_PREFIX)) => {} )
	  )
		{
			r = new QNameAST( nsPrefix, localName );
		}
	;


nt_BNodeRef returns [ AST r ]
{
	r = null;
	String localName = null;
}
	: NODEID_PREFIX localName=nt_Name
		{
			r = new BlankNodeAST( localName );
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
	boolean inverse = false;
}
	: (OP_APPLY_POST { AST = new OperatorAST(); }
	    | OP_INVERSE_APPLY {inverse = true; AST = new OperatorAST( OperatorAST.Type.InverseApply ); }
	    )
	  (OP_SUFFIX_OPTIONAL { AST = new OperatorAST( inverse ? OperatorAST.Type.InverseOption : OperatorAST.Type.Option ); }
	    | OP_SUFFIX_STAR { AST = new OperatorAST( inverse ? OperatorAST.Type.InverseStar : OperatorAST.Type.Star ); }
	    | OP_SUFFIX_PLUS { AST = new OperatorAST( inverse ? OperatorAST.Type.InversePlus : OperatorAST.Type.Plus ); }
	    | L_CURLY (nt_Ws)? min:NUMBER (nt_Ws)? ( COMMA (nt_Ws)? max:NUMBER (nt_Ws)? )? R_CURLY
		  {
			// Note: floating-point values are syntactically valid, but will be
			// truncated to integer values.
			int minVal = new Double( min.getText() ).intValue();

			if ( null == max )
			{
				AST = new OperatorAST( minVal, inverse );
			}

			else
			{
				int maxVal = new Double( max.getText() ).intValue();
				AST = new OperatorAST( minVal, maxVal, inverse );
			}
	      }
	    )?
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
	: DRCTV_COUNT nt_Ws "statements" (nt_Ws)? PERIOD
		{
			matchCommand( new CountStatementsCmd() );
		}

    // FIXME: this syntax allows a parenthesized expression in place of the definition name
	| ( DRCTV_DEFINE || DRCTV_REDEFINE { redefine = true; } )
	        nt_Ws lhs=nt_TemplateList /*(nt_Ws)?*/ COLON (nt_Ws)? rhs=nt_List /*(nt_Ws)?*/ PERIOD
		{
            lhs = lhs.invert();
            keyword = ( (KeywordAST) lhs.getFirst() ).getName();
            lhs = lhs.getRest().invert();

		    rhs = new TemplateAST( lhs, rhs );

			matchCommand( redefine
			        ? new RedefineTermCmd( keyword, rhs )
			        : new DefineTermCmd( keyword, rhs ) );
		}

	| DRCTV_EXPORT ( nt_Ws ( nsPrefix=nt_PrefixName (nt_Ws)? )? )? COLON (nt_Ws)? exFile:STRING (nt_Ws)? PERIOD
		{
			matchCommand( new ExportCmd( nsPrefix, exFile.getText() ) );
		}

	| DRCTV_HELP (nt_Ws)? PERIOD
		{
			System.out.println( "\nSorry, the @help directive is just a placeholder for now.\n" );
		}

	| DRCTV_LIST nt_Ws
		( "contexts" (nt_Ws)? PERIOD
			{
				matchCommand( new ShowContextsCmd() );
			}
		| "prefixes" (nt_Ws)? PERIOD
			{
				matchCommand( new ShowNamespacesCmd() );
			}
		)

	| DRCTV_PREFIX nt_Ws ( nsPrefix=nt_PrefixName (nt_Ws)? )? COLON (nt_Ws)? ns=nt_URIRef (nt_Ws)? PERIOD
		{
			matchCommand( new DefinePrefixCmd( nsPrefix, ns ) );
		}

	| DRCTV_QUIT (nt_Ws)? PERIOD
		{
			matchQuit();
//			matchCommand( new QuitCmd() );
		}

	| DRCTV_UNDEFINE nt_Ws keyword=nt_Name (nt_Ws)? PERIOD
		{
			matchCommand( new UndefineTermCmd( keyword ) );
		}
	;


// kate: tab-width 4
