package net.re_renderreality.bigbertha.text2speech;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.re_renderreality.bigbertha.BerthaTTS;

//Import freeTTS for TTS capability

public class TextToSpeech{
	// This stores the voice to be used and its properties
	private Voice voice = null;
	// This stores the text to be read
	private String text = "";

	// Define a TextReader class to use as a thread to playback the sound
	private class TextReader implements Runnable {
     public void run() {
         try {
         	// Allocate any required data for the voice
     		voice.allocate();
     		
     		float volume = Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.VOICE);
     		voice.setVolume(volume);
     		if(volume < 0.65 ) 
     			voice.setVolume(0.65f);
     		if(volume < 0.1)
     			voice.setVolume(0f);
 		 	
     		// This part actually reads the text
     		voice.startBatch();
     		BerthaTTS.logger.info("Attempting Speech!");
     		voice.speak(text);
     		voice.endBatch();
     		
     		// Deallocate the data
     		voice.deallocate();
         } catch (Exception e) { }
         BerthaTTS.logger.info("Speech Failed!");
     }
 }
	
	// Gets a male voice to use for playback
	public static Voice getMaleVoice() {
		// TODO: Make this interchangeable
		// This sets the voice to use for playback 
		// There are three options: "kevin" (low quality), "kevin16" (medium quality), and "alan" (high quality, requires additional code)
		String voiceName = "kevin16";
     
		// This instantializes the voiceManager
		VoiceManager voiceManager = VoiceManager.getInstance();
		// This gets the specified voice from the voiceManager
		Voice voice = voiceManager.getVoice(voiceName);

		return voice;
	}
	
	// Actually reads the text
	public void speakText(String text, Voice voice)
	{
		parseText(text);
		this.voice = voice;
		BerthaTTS.logger.info(Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.VOICE));
		
		Thread t = new Thread(new TextReader());
		t.start();
	}
	
	// Parses the text, removing the player's name
	public void parseText(String text)
	{
		// Sets the char from which to begin TTS conversion (everything before this is deleted)
		char endChar = '>';
		// The String containing the text to be played
		String outputText = text;
		
		for (int i=0; i<text.length(); i++) {
			if (text.charAt(i) == endChar){
				outputText = text.substring(i+1);
			}
		}
		
		this.text = outputText;
	}
}