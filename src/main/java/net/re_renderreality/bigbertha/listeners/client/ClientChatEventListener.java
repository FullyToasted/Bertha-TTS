package net.re_renderreality.bigbertha.listeners.client;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.re_renderreality.bigbertha.BigBertha;
import net.re_renderreality.bigbertha.tts.TextToSpeech;

public class ClientChatEventListener {
	@SubscribeEvent
    public void onClientChatEvent(ClientChatReceivedEvent event){
		BigBertha.logger.info(event.getMessage().getUnformattedText());
        new TextToSpeech().speakText(event.getMessage().getUnformattedText(), TextToSpeech.getMaleVoice());
    }
}
