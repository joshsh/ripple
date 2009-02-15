/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.audio;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

/**
* Simple program to demonstrate the use of the FreeTTS speech
* synthesizer.  This simple program shows how to use FreeTTS
* without requiring the Java Speech API (JSAPI).
*/
public class Test {

	/**
	* Example of how to list all the known voices.
	*/
	public static void listAllVoices() {
		System.out.println();
		System.out.println("All voices available:");
		VoiceManager voiceManager = VoiceManager.getInstance();
		Voice[] voices = voiceManager.getVoices();
		for (int i = 0; i < voices.length; i++) {
			System.out.println("    " + voices[i].getName()
							+ " (" + voices[i].getDomain() + " domain)");
		}
	}

	public static void main(String[] args) {

		listAllVoices();
		
		String voiceName = (args.length > 0)
			? args[0]
			: "kevin16";
		
		System.out.println();
		System.out.println("Using voice: " + voiceName);
		
		/* The VoiceManager manages all the voices for FreeTTS.
		*/
		VoiceManager voiceManager = VoiceManager.getInstance();
		Voice helloVoice = voiceManager.getVoice(voiceName);

		if (helloVoice == null) {
			System.err.println(
				"Cannot find a voice named "
				+ voiceName + ".  Please specify a different voice.");
			System.exit(1);
		}
		
		/* Allocates the resources for the voice.
		*/
		helloVoice.allocate();
		
		/* Synthesize speech.
		*/
// 		helloVoice.speak("Thank you for giving me a voice. "
// 						+ "I'm so glad to say hello to this world.");
		helloVoice.speak( "How much wood could a woodchuck chuck if a woodchuck could chuck wood?" );
		helloVoice.speak( "The RDF Schema vocabulary (RDFS)" );

		/* Clean up and leave.
		*/
		helloVoice.deallocate();
		System.exit(0);
	}
}

