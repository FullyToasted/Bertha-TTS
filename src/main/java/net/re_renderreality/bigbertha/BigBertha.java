package net.re_renderreality.bigbertha;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.re_renderreality.bigbertha.proxy.CommonProxy;

// dependencies = "required-after:Forge@[" + Reference.MIN_FORGE_VER + ",)",
@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class BigBertha
{    
	public static Logger logger;
	
    @Mod.Instance(Reference.MODID)
    public static BigBertha INSTANCE;
    
    @SidedProxy(modId = Reference.MODID, clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;
	
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	this.logger = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
    	proxy.registerRenderers();
        proxy.registerEventHandlers();
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
    	
    }
    
    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
    	
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    	
    }
}
