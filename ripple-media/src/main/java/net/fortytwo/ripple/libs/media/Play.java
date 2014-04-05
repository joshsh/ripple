package net.fortytwo.ripple.libs.media;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.rdf.HTTPUtils;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Play extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            MediaLibrary.NS_2013_03 + "play",
            MediaLibrary.NS_2008_08 + "play",
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

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RDFValue uri = mc.valueOf(URI.create(mc.toString(arg.getFirst())));
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
		solutions.put(arg);
	}

	private void play( final RDFValue uri ) throws RippleException
	{
		String[] mimeTypes = { "audio/midi" };
        HttpGet method = HTTPUtils.createRdfGetMethod(uri.toString());
        HTTPUtils.setAcceptHeader( method, mimeTypes );
        HTTPUtils.throttleHttpRequest(method);
		HttpClient client = HTTPUtils.createClient();

        InputStream is;

        try {
            HttpResponse response = client.execute(method);
            is = response.getEntity().getContent();
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
