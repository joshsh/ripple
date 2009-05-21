/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.media;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.util.HTTPUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.openrdf.model.URI;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import java.io.IOException;
import java.io.InputStream;

public class Play extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            MediaLibrary.NS_2007_08 + "play"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public Play()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "midiUri", null, true )};
    }

    public String getComment()
    {
        return "midiUri => midiUri  -- has the side-effect of playing the MIDI file";
    }

    public void apply( final StackContext arg,
                         final Sink<StackContext, RippleException> solutions )
            throws RippleException
	{
        ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();

        URI uri = mc.createURI( mc.toString( stack.getFirst() ) );
		//stack = stack.getRest();

		try
		{
			play( uri );
		}

		catch ( RippleException e )
		{
System.out.println( "error: " + e );
			e.logError();
		}

		// Pass the stack along, unaltered.
		solutions.put( arg.with( stack ) );
	}

	private void play( final URI uri ) throws RippleException
	{
		String[] mimeTypes = { "audio/midi" };
        HttpMethod method = HTTPUtils.createRdfGetMethod( uri.toString() );
        HTTPUtils.setAcceptHeader( method, mimeTypes );
        HTTPUtils.registerMethod( method );
		HttpClient client = HTTPUtils.createClient();

        InputStream is;

        try {
            client.executeMethod( method );
            is = method.getResponseBodyAsStream();
        } catch ( IOException e ) {
            throw new RippleException( e );
        }

        try {
            play( is );
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RippleException( e );
            }
        }
	}

	// Note: we won't try to play more than one MIDI at a time.
	private synchronized void play( final InputStream is )
		throws RippleException
	{
		// Play once
		try
		{
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.setSequence( MidiSystem.getSequence( is ) );
			sequencer.open();
			sequencer.start();

			// Break out when playback is finished.
			while( true )
			{
				if( sequencer.isRunning() )
				{
					try
					{
						Thread.sleep( 1000 ); // Check every second
					}

					catch( InterruptedException ignore)
					{
						break;
					}
				}

				else
					break;
			}

			// Close the MidiDevice & free resources
			sequencer.stop();
			sequencer.close();

			is.close();
		}

		catch( javax.sound.midi.MidiUnavailableException e )
		{
			throw new RippleException( e );
		}

		catch( javax.sound.midi.InvalidMidiDataException e )
		{
			throw new RippleException( e );
		}

		catch( java.io.IOException e )
		{
			throw new RippleException( e );
		}
	}
}
