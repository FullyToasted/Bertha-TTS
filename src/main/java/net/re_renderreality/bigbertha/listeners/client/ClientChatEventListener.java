package net.re_renderreality.bigbertha.listeners.client;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.re_renderreality.bigbertha.BigBertha;
import net.re_renderreality.bigbertha.text2speech.TextToSpeech;

public class ClientChatEventListener {
	@SubscribeEvent
    public void onClientChatEvent(ServerChatEvent event){
		BigBertha.logger.info(event.getMessage());
		TextToSpeech speech = new TextToSpeech();
		speech.speakText(event.getMessage(), TextToSpeech.getMaleVoice());
	}
}
