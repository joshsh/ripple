package net.fortytwo.ripple.libs.media;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Speak extends PrimitiveStackMapping
{
	private Voice singleVoice = null;

    private static final String[] IDENTIFIERS = {
            MediaLibrary.NS_2013_03 + "speak",
            MediaLibrary.NS_2008_08 + "speak",
            MediaLibrary.NS_2007_08 + "speak"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Speak()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "text", null, true )};
    }

    public String getComment()
    {
        return "text => text  -- has the side-effect of speaking the text";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        String s = mc.toString( arg.getFirst() );
		//stack = stack.getRest();

		try
		{
			speak( s );
		}

		catch ( RippleException e )
		{
System.out.println( "error: " + e );
			e.logError();
		}

		// Pass the stack along, unaltered.
		solutions.put( arg );
	}

	// Note: we won't try to speak more than one expression at a time.
	synchronized void speak( final String s )
		throws RippleException
	{
		if ( null == singleVoice )
		{
			createVoice();
		}

		singleVoice.speak( s );
	}

	private void createVoice()
		throws RippleException
	{
		String voiceName = "kevin";

		try
		{
			VoiceManager voiceManager = VoiceManager.getInstance();
			singleVoice = voiceManager.getVoice( voiceName );
		}

		catch ( Throwable t )
		{
			throw new RippleException( t );
		}

		if ( null == singleVoice )
		{
			throw new RippleException(
				"Cannot find a voice named " + voiceName );
		}

		try
		{
			singleVoice.allocate();
		}

		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
	}

	// Note: never called.
	private synchronized void end() throws RippleException
	{
		try
		{
			if ( null != singleVoice )
			{
				singleVoice.deallocate();
			}
		}

		catch ( Throwable t )
		{
			throw new RippleException( t );
		}
	}
}
