/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.io.parser;

import org.openrdf.rio.helpers.RDFParserBase;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFHandlerException;

import java.io.Reader;
import java.io.IOException;
import java.io.InputStream;

import net.fortytwo.ripple.cli.*;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.cli.ast.URIAST;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.NullSink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.commands.DefineTermCmd;
import net.fortytwo.ripple.query.commands.DefinePrefixCmd;
import net.fortytwo.ripple.query.commands.RedefineTermCmd;

/**
 * Author: josh
 * Date: Feb 28, 2008
 * Time: 6:52:17 PM
 */
public class RippleRDFParser extends RDFParserBase
{
    private static RDFFormat RIPPLE_RDFFORMAT
            = new RDFFormat( "foo", "bar", null, "quux", false, false );

    private RecognizerAdapter adapter = null;

    public RippleRDFParser()
    {

    }

    public RDFFormat getRDFFormat()
    {
        return RIPPLE_RDFFORMAT;
    }

    public void parse( final InputStream in, final String baseUri )
            throws IOException, RDFParseException, RDFHandlerException
    {
        /* RESTOREME
        if ( null == adapter )
        {
            createAdapter();
        }

        RippleLexer lexer = new RippleLexer( in );
        lexer.initialize( adapter );
        RippleParser parser = new RippleParser( lexer );
        parser.initialize( adapter );

        try
        {
            parser.nt_Document();
        }

        catch (RecognitionException e)
        {
            throw new RDFParseException(e);
        }

        catch (TokenStreamException e)
        {
            throw new RDFParseException(e);
        }*/
    }

    public void parse( final Reader reader, final String baseUri )
            throws IOException, RDFParseException, RDFHandlerException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    ////////////////////////////////////////////////////////////////////////////

    private void createAdapter()
    {
        Sink<ListAST, RippleException> querySink = new Sink<ListAST, RippleException>()
        {
            public void put( final ListAST query ) throws RippleException
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        Sink<Command, RippleException> commandSink = new Sink<Command, RippleException>()
        {
            public void put( final Command command ) throws RippleException
            {
                // FIXME: using instanceof is a bit of a hack
                
                if ( command instanceof DefinePrefixCmd )
                {
                    String prefix = ( (DefinePrefixCmd) command ).getPrefix();
                    URIAST uri = ( (DefinePrefixCmd) command ).getUri();

                    //...

                }

                else if ( command instanceof DefineTermCmd || command instanceof RedefineTermCmd )
                {
                    String name;
                    ListAST list;

                    if ( command instanceof DefineTermCmd )
                    {
                        name = ( (DefineTermCmd) command ).getName();
                        list = ( (DefineTermCmd) command ).getList();
                    }

                    else
                    {
                        name = ( (RedefineTermCmd) command ).getName();
                        list = ( (RedefineTermCmd) command ).getList();
                    }

                    //...
                }
            }
        };

        Sink<RecognizerEvent, RippleException> eventSink = new NullSink<RecognizerEvent, RippleException>();

        adapter = new RecognizerAdapter(querySink, querySink, commandSink, eventSink, System.err);
    }
}
