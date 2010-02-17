// Warning: in porting this grammar from ANTLR 2 to ANTLR 3, lexer and parser "options" have been discarded.

grammar Ripple;

@rulecatch { }

@header
{
package net.fortytwo.ripple.cli;

import java.util.Properties;
import java.util.List;
import java.util.LinkedList;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.cli.ast.AST;
import net.fortytwo.ripple.cli.ast.AnnotatedAST;
import net.fortytwo.ripple.cli.ast.BlankNodeAST;
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
import net.fortytwo.ripple.query.commands.CountStatementsCmd;
import net.fortytwo.ripple.query.commands.DefinePrefixCmd;
import net.fortytwo.ripple.query.commands.DefineTermCmd;
import net.fortytwo.ripple.query.commands.ExportCmd;
import net.fortytwo.ripple.query.commands.QuitCmd;
import net.fortytwo.ripple.query.commands.RedefineTermCmd;
import net.fortytwo.ripple.query.commands.ShowContextsCmd;
import net.fortytwo.ripple.query.commands.ShowNamespacesCmd;
import net.fortytwo.ripple.query.commands.UndefinePrefixCmd;
import net.fortytwo.ripple.query.commands.UndefineTermCmd;
}

@lexer::header{
package net.fortytwo.ripple.cli;
}

@members {
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

@lexer::members {
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

document
@init {
	// Request a first line of input from the interface (the lexer will request
	// additional input as it matches newlines).
System.out.println("putting a newline");
	adapter.putEvent( RecognizerEvent.NEWLINE );
}
//	: ( (whitespace)? statement )* EOF
    : statements
	;

statements
    : (whitespace)?
      ( (EOF) => EOF
        | statement statements )
    ;

whitespace
	// Note: consecutive WS tokens occur when the lexer matches a COMMENT
	//       between them.
	: (WS)+
	;

statement
@init {
	ListAST resource;
}
	// A directive is executed as soon as PERIOD is matched in the individual
	// rule.
	: directive

	// Query statements are always lists.
	| r=list { resource = $r.result; } (
		PERIOD { matchQuery( $r.result ); }
 		| SEMI { matchContinuingQuery( $r.result ); } )

	// Empty statements are effectively ignored.
	| PERIOD { matchQuery( new ListAST() ); }
	| SEMI { matchContinuingQuery( new ListAST() ); }
	;


list returns [ ListAST result ]
@init {
	AST first = null;
	ListAST rest = null;
	result = null;
	boolean modified = false;
}
		// Optional slash operator.
	:	( OP_APPLY_PRE (WS)? { modified = true; } )?

		// Head of the list.
		f = node { first = $f.result; }

		(	(WS) => ( whitespace
				( (~(PERIOD | SEMI | R_PAREN )) => r = list { rest = $r.result; }
				| {}
				) )

			// Tail of the list.
		|	(~(WS | PERIOD | SEMI | R_PAREN)) => r = list { rest = $r.result; }

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

				result = new ListAST( first, rest );
			}
	;


templateList returns [ ListAST result ]
@init {
	AST first = null;
	ListAST rest = null;
	result = null;
}
	:	// Head of the list.
		f = templatenode { first = $f.result; }

		(	(WS) => ( whitespace
				( (~(R_PAREN)) => r = templateList { rest = $r.result; }
				| {}
				) )

			// Tail of the list.
		|	(~(WS | R_PAREN)) => r = templateList { rest = $r.result; }

			// End of the list.
		|	()
		)
			{
				if ( null == rest )
				{
					rest = new ListAST();
				}

				result = new ListAST( first, rest );
			}
	;
	
parenthesizedTemplateList returns [ ListAST result ]
@init {
	result = null;
}
	:	L_PAREN (whitespace)? (
		( r = templateList { result = $r.result; } /*(whitespace)?*/ R_PAREN )
		| R_PAREN { result = new ListAST(); } )
	;
	
templatenode returns [ AST result ]
@init {
	result = null;
}
	:	( keyword { result = $keyword.result; }
		| parenthesizedTemplateList { result = $parenthesizedTemplateList.result; }
		)
	;
		

node returns [ AST result ]
@init {
	result = null;
}
	: ( resource { result = $resource.result; }
		| literal { result = $literal.result; }
		| parenthesizedList { result = $parenthesizedList.result; }
		| operator { result = $operator.result; }
		)
	  (( (WS)? L_BRACKET ) => ( (WS)? props=properties { result = new AnnotatedAST( result, $props.result ); } )
	  | ())
	;


properties returns [ Properties result ]
@init {
	result = new Properties();
}
	: L_BRACKET (WS)? propertyList[result] R_BRACKET
	;

	
propertyList[ Properties props ]
@init {
	String name = null;
}
	: n=propertyName { name = $n.result; } EQUAL STRING (WS)? { props.setProperty( name, $STRING.text ); }
		( COMMA (WS)? propertyList[props] )?
	;
	
	
propertyName returns [ String result ]
@init {
	String rest;
}
	:	n=name { result = $n.result; }
		( PERIOD r=propertyName { rest = $r.result; result += rest; } )?
	;
	
	
parenthesizedList returns [ ListAST result ]
@init {
	result = null;
}
	:	L_PAREN (whitespace)? (
		( r = list { result = $r.result; } /*(whitespace)?*/ R_PAREN )
		| R_PAREN { r = new ListAST(); } )
	;


literal returns [ AST result ]
@init {
	result = null;
	AST dataType = null;
}
	:	( STRING

		/* Note: for agreement with Turtle, the grammar allows any resource
				reference as the data type of a literal (i.e. a URI or a blank
				node).  However, the Sesame back end will only accept a URI. */
		( DOUBLE_HAT d=resource { dataType = $d.result; } )?
	)
		{
		    String label = unescape( $STRING.text );

			result = ( null == dataType )
				? new PlainLiteralAST( label, adapter.getLanguageTag() )
				: new TypedLiteralAST( label, dataType );
		}
	| r=number { result = $r.result; }
	;


number returns [ NumberAST result ]
@init {
    result = null;
}
    :	NUMBER
		{
			// Note: number format exceptions are handled at a higher level.
			String s = $NUMBER.text;

            // Numbers with an exponent portion are considered to be xsd:double
            // values.  A decimal point is optional, but must be followed by at
            // least one digit if present.
            if ( s.contains( "e" ) || s.contains( "E" ) )
            {
                result = new DoubleAST( s );
            }

            // Numbers with no exponent portion but with a decimal point are
            // considered to be xsd:decimal values.
			else if ( s.contains( "." ) )
			{
    		    result = new DecimalAST( s );
			}

            // All other numbers (with neither an exponent portion nor a decimal
            // point) are considered to be xsd:integer values.
			else
			{
				result = new IntegerAST( s );
			}
		}
	;


resource returns [ AST result ]
@init {
	result = null;
}
	: r=uriReference { result = $r.result; }
	| ( (NAME_OR_PREFIX)? COLON ) => r=qName { result = $r.result; }
	| r=keywordOrBoolean { result = $r.result; }
	| r=bnodeReference { result = $r.result; }
	;


uriReference returns [ URIAST result ]
@init {
	result = null;
}
	:	URIREF
		{
			result = new URIAST( $URIREF.text );
		}
	;


prefixName returns [ String result ]
@init {
	result = null;
}
	: NAME_OR_PREFIX { result = $NAME_OR_PREFIX.text; }
	;


keyword returns [ AST result ]
@init {
	String keyword;
	result = null;
}
	: k=name { result = new KeywordAST( $k.result ); }
	;


keywordOrBoolean returns [ AST result ]
@init {
	String keyword;
	result = null;
}
	: k=name {
		keyword = $k.result;
	    result = keyword.equals( "true" )
	            ? new BooleanAST( true )
	            : keyword.equals( "false" )
	                    ? new BooleanAST( false )
	                    : new KeywordAST( keyword ); }
	;


qName returns [ AST result ]
@init {
	String nsPrefix = "", localName = "";
	result = null;
}
	: ( ( n=prefixName { nsPrefix = $n.result; } )?
		COLON
		( l=name { localName = $l.result; } )?
//		( localName=Name
//		| (~(NAME_OR_PREFIX | NAME_NOT_PREFIX)) => {} )
	  )
		{
			result = new QNameAST( nsPrefix, localName );
		}
	;


bnodeReference returns [ AST result ]
@init {
	result = null;
	String localName = null;
}
	:	NODEID_PREFIX l=name { localName = $l.result; }
		{
			result = new BlankNodeAST( localName );
		}
	;


name returns [ String result ]
@init {
	result = null;
}
	: NAME_OR_PREFIX { result = $NAME_OR_PREFIX.text; }
	| NAME_NOT_PREFIX { result = $NAME_NOT_PREFIX.text; }
	;


operator returns [ OperatorAST result ]
@init {
	result = null;
	boolean inverse = false;
	OperatorAST.Type type = OperatorAST.Type.Apply;
	NumberAST minTimes = null, maxTimes = null;
}
	: ((OP_MOD_OPTION { type = OperatorAST.Type.Option; }
	    | OP_MOD_STAR { type = OperatorAST.Type.Star; }
//	    | OP_MOD_PLUS { type = OperatorAST.Type.Plus; }
	    | L_CURLY (whitespace)? min=number { minTimes = $min.result; } (whitespace)? ( COMMA (whitespace)? max=number { maxTimes = $max.result; } (whitespace)? )? R_CURLY
		  {
		    type = ( null == maxTimes )
			        ? OperatorAST.Type.Times
			        : OperatorAST.Type.Range;
	      }
	    ) (whitespace)? )?

	  (OP_APPLY_FORWARD
	    | OP_APPLY_BACKWARD { inverse = true; }

	    // Special-case to avoid lexical conflict with "+" prefix for numbers.
	    | OP_PLUS_FORWARD { type = OperatorAST.Type.Plus; }
	    )
        {
            switch (type)
            {
                case Apply:
                    result = new OperatorAST( type, inverse );
                    break;
                case Option:
                    result = new OperatorAST( type, inverse );
                    break;
                case Star:
                    result = new OperatorAST( type, inverse );
                    break;
                case Plus:
                    result = new OperatorAST( type, inverse );
                    break;
                case Times:
                    result = new OperatorAST( minTimes, inverse );
                    break;
                case Range:
                    result = new OperatorAST( minTimes, maxTimes, inverse );
                    break;
            }
        }
	;


directive
@init {
	URIAST ns = null;

	// Default to the empty (but non-null) prefix.
	String nsPrefix = "";

	String keyword = null;
	List<String> names = new LinkedList<String>();
	
	// Note: it is not possible to define a term with a nil AST.  If this were
	//       allowed, this would be equivalent to redefining rdf:nil in a new
	//       namespace, which is strange and probably not what the programmer
	//       intended.
	// TODO: however, in combination with template parameters, an empty expression may have a meaning other than rdf:nil
	ListAST rhs = null;

	ListAST lhs = new ListAST();
	boolean redefine = false;
}
	:	DRCTV_COUNT whitespace 'statements' (whitespace)? PERIOD
		{
			matchCommand( new CountStatementsCmd() );
		}

    // FIXME: this syntax allows a parenthesized expression in place of the definition name
	| ( DRCTV_DEFINE || DRCTV_REDEFINE { redefine = true; } )
	        whitespace templateList { lhs = $templateList.result; } /*(whitespace)?*/ COLON (whitespace)? list { rhs = $list.result; } /*(whitespace)?*/ PERIOD
		{
            lhs = lhs.invert();
            keyword = ( (KeywordAST) lhs.getFirst() ).getName();
            lhs = lhs.getRest().invert();

		    rhs = new TemplateAST( lhs, rhs );

			matchCommand( redefine
			        ? new RedefineTermCmd( keyword, rhs )
			        : new DefineTermCmd( keyword, rhs ) );
		}

	| DRCTV_EXPORT ( whitespace ( prefixName { nsPrefix = $prefixName.result; } (whitespace)? )? )? COLON (whitespace)? STRING (whitespace)? PERIOD
		{
			matchCommand( new ExportCmd( nsPrefix, $STRING.text ) );
		}

	| DRCTV_HELP (whitespace)? PERIOD
		{
			System.out.println( "\nSorry, the @help directive is just a placeholder for now.\n" );
		}

	| DRCTV_LIST whitespace
		( 'contexts' (whitespace)? PERIOD
			{
				matchCommand( new ShowContextsCmd() );
			}
		| 'prefixes' (whitespace)? PERIOD
			{
				matchCommand( new ShowNamespacesCmd() );
			}
		)

	| DRCTV_PREFIX whitespace ( prefixName { nsPrefix = $prefixName.result; } (whitespace)? )? COLON (whitespace)? uriReference { ns = $uriReference.result; } (whitespace)? PERIOD
		{
			matchCommand( new DefinePrefixCmd( nsPrefix, ns ) );
		}

	| DRCTV_QUIT (whitespace)? PERIOD
		{
			matchQuit();
//			matchCommand( new QuitCmd() );
		}

	| DRCTV_UNDEFINE whitespace name (whitespace)? PERIOD
		{
			matchCommand( new UndefineTermCmd( $name.result ) );
		}

	| DRCTV_UNPREFIX whitespace prefixName (whitespace)? PERIOD
		{
			matchCommand( new UndefinePrefixCmd( $prefixName.result ) );
		}
	;

fragment
WS_CHAR
	: ' ' | '\t' | '\r'
	| '\n'  { /*newline();*/ matchEndOfLine(); }
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

fragment
HEX
	: ('0'..'9')
	| ('A'..'F')
	;

fragment
SCHARACTER
	: ' ' | '!' | ('#'..'[')  // excludes: '\"', '\\'
	| (']'..'\uFFFF')  // Note: '\u10FFFF' in Turtle
	| '\\u' HEX HEX HEX HEX
	| '\\U' HEX HEX HEX HEX HEX HEX HEX HEX
	| '\\' ('\\' | '\"' | 't' | 'n' | 'r' )
	;

fragment
UCHARACTER
	: (' '..';') | '=' | ('?'..'[')  // excludes: '>', '\\' and '<' (the lAST of which is not excluded by Turtle)
	| (']'..'\uFFFF')  // Note: '\u10FFFF' in Turtle
	| '\\u' HEX HEX HEX HEX
	| '\\U' HEX HEX HEX HEX HEX HEX HEX HEX
	| '\\' ('\\' | '>' | '<')
	;

fragment
LANGUAGE
	: ( '@' ('a'..'z')+ ('-' (('a'..'z') | ('0'..'9'))+)* )
		{ adapter.setLanguageTag( getText().substring(1) ); }
	;

fragment
SCHARACTERS
	: ( SCHARACTER )*
	;
	
STRING
	: '\"'
		{ adapter.setLanguageTag( null ); }
		chars=SCHARACTERS '\"' ( LANGUAGE )?
		{setText($chars.text);}
	;

/*
LONG_STRING
	: "\"\"\""!
		{ adapter.setLanguageTag( null ); }
		( SCHARACTER )* "\"\"\""! ( LANGUAGE! )?
	;
*/

fragment
UCHARACTERS
	: UCHARACTER *
	;
	
URIREF
	: '<' ( UCHARACTER )* '>'
	;

fragment
DIGIT
	: ('0' .. '9')
	;

fragment
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

fragment
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

NODEID_PREFIX : '_:' ;

NUMBER
	: ('-' | '+')? ( DIGIT )+
		(('.' DIGIT ) => ( '.' ( DIGIT )+ )
		| ())
		(('e' | 'E') ('-' | '+')? ( DIGIT )+ )?
	;

// Ignore comments.
COMMENT
	: ( '#' ( ~('\n') )* ) { $channel=HIDDEN; }
	;

/*
MULTI_LINE_COMMENT
	: "(:" ((~':') | (':' ~')'))* ":)"
		{ $setType( Token.SKIP ); }
	;
*/

DOUBLE_HAT : '^^' ;

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
OP_APPLY_FORWARD : '>>';
OP_APPLY_BACKWARD : '<<';
OP_MOD_OPTION : '?' ;
OP_MOD_STAR : '*';
//OP_MOD_PLUS : "+";
// Special-case to avoid lexical conflict with "+" prefix for numbers.
OP_PLUS_FORWARD : '+>>';

fragment
DRCTV : '@' ;

DRCTV_COUNT     : DRCTV ( 'count'         | 'c' ) ;
DRCTV_DEFINE    : DRCTV ( 'define'        | 'd' ) ;
DRCTV_EXPORT    : DRCTV ( 'export'        | 'e' ) ;
DRCTV_HELP      : DRCTV ( 'help'          | 'h' ) ;
DRCTV_LIST      : DRCTV ( 'list'          | 'l' ) ;
DRCTV_PREFIX    : DRCTV ( 'prefix'        | 'p' ) ;
DRCTV_QUIT      : DRCTV ( 'quit'          | 'q' | 'exit' ) ;
DRCTV_REDEFINE  : DRCTV ( 'redefine'      | 'r' ) ;
DRCTV_UNDEFINE  : DRCTV ( 'undefine'      | 'u' ) ;
DRCTV_UNPREFIX  : DRCTV ( 'unprefix'            ) ;


////////////////////////////////////////////////////////////////////////////////



