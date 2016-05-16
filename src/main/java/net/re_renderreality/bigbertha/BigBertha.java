package net.re_renderreality.bigbertha;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.Logger;

import net.minecraft.command.ICommandSender;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.InstanceFactory;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.re_renderreality.bigbertha.achievements.Achievements;
import net.re_renderreality.bigbertha.commands.DebugModeCommand;
import net.re_renderreality.bigbertha.proxy.*;
import net.re_renderreality.bigbertha.utils.DynamicClassLoader;
import net.re_renderreality.bigbertha.utils.Reference;

// dependencies = "required-after:Forge@[" + Reference.MIN_FORGE_VER + ",)",
@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class BigBertha
{    
    @Mod.Instance(Reference.MODID)
    public static BigBertha INSTANCE;
    
    @SidedProxy(modId = Reference.MODID, clientSide = "net.re_renderreality.bigbertha.proxy.ClientProxy", serverSide = "net.re_renderreality.bigbertha.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    public static enum ServerType {INTEGRATED, DEDICATED, ALL}
    private final DynamicClassLoader commandClassLoader = new DynamicClassLoader(BigBertha.class.getClassLoader());
    
    private final Pattern ipPattern = Pattern.compile("^(\\d{1,3}\\.){3}\\d{1,3}:\\d{1,5}[ \t]*\\{$");
    
    public static Random rnd = new Random();
    
    private List<String> disabledCommands;
	private List<String> startupCommands;
	private List<String> startupMultiplayerCommands;
	private Map<String, List<String>> startupServerCommands;
	
	/**
	 * @return the dynamic class loader responsible for command class loading
	 */
	public DynamicClassLoader getCommandClassLoader() {
		return this.commandClassLoader;
	}
	
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	Reference.init(e);
    	//this.proxy.preInit(e); //activate proxy
        //MainCompatHandler.registerWaila(); //Waila needs to be activated here if we want to use it
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
    	//this.proxy.init(e); //activate proxy

        //Achievements.init();

        // boolean MODLOADED = Loader.isModLoaded("MOD"); used to check if mods are loaded
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        //event.registerServerCommand(new DebugModeCommand()); register commands here
    }
    
    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
    	
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    	//this.proxy.postInit(e); //activate proxy
    }
}
