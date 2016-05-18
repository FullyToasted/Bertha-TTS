package net.re_renderreality.bigbertha.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.re_renderreality.bigbertha.listeners.client.ClientChatEventListener;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderers() {
    }

    @Override
    public void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(new ClientChatEventListener());
    }
}
