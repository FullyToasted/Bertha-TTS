package net.re_renderreality.bigbertha;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;

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
import net.re_renderreality.bigbertha.commands.backend.ClientCommandInterface;
import net.re_renderreality.bigbertha.commands.backend.ServerCommandInterface;
import net.re_renderreality.bigbertha.commands.backend.StandardCommand;
import net.re_renderreality.bigbertha.commands.client.DebugModeCommand;
import net.re_renderreality.bigbertha.proxy.*;
import net.re_renderreality.bigbertha.utils.DynamicClassLoader;
import net.re_renderreality.bigbertha.utils.GlobalSettings;
import net.re_renderreality.bigbertha.utils.LanguageManager;
import net.re_renderreality.bigbertha.utils.PlayerSettings;
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
    
    private final String clientCommandsPackage = "net.re_renderreality.bigbertha.commands.client";
	private List<Class<? extends StandardCommand>> clientCommandClasses = new ArrayList<Class<? extends StandardCommand>>();
	
	private final String serverCommandsPackage = "net.re_renderreality.bigbertha.commands.server";
	private List<Class<? extends StandardCommand>> serverCommandClasses = new ArrayList<Class<? extends StandardCommand>>();
	
    private List<String> disabledCommands;
	private List<String> startupCommands;
	private List<String> startupMultiplayerCommands;
	private Map<String, List<String>> startupServerCommands;
	
	/**
	 * @return Whether the command is enabled
	 */
	public boolean isCommandEnabled(String command) {
		return !this.disabledCommands.contains(command);
	}
	
	/**
	 * Loads Command Classes
	 * 
	 * @return Whether the commands were loaded successfully
	 */
	private boolean loadCommands() {
		List<Class<?>> commandClasses = this.commandClassLoader.getCommandClasses(this.clientCommandsPackage, true);
		commandClasses.addAll(this.commandClassLoader.getCommandClasses(this.serverCommandsPackage, false));
		
		for (Class<?> commandClass : commandClasses) {
			try {
				if (StandardCommand.class.isAssignableFrom(commandClass) && ClientCommandInterface.class.isAssignableFrom(commandClass))
					this.clientCommandClasses.add(commandClass.asSubclass(StandardCommand.class));
				else if (StandardCommand.class.isAssignableFrom(commandClass) && ServerCommandInterface.class.isAssignableFrom(commandClass))
					this.serverCommandClasses.add(commandClass.asSubclass(StandardCommand.class));
			}
			catch (Exception ex) {ex.printStackTrace(); return false;}
		}
		
		return true;
	}
	
	/**
	 * @return The Server Command Classes
	 */
	public List<Class<? extends StandardCommand>> getServerCommandClasses() {
		return this.serverCommandClasses;
	}
	
	/**
	 * @return the dynamic class loader responsible for command class loading
	 */
	public DynamicClassLoader getCommandClassLoader() {
		return this.commandClassLoader;
	}
	
	/**
	 * @return A List of disabled commands
	 */
	private List<String> readDisabledCommands() {
		ImmutableList.Builder<String> builder = ImmutableList.builder();
		File file = new File(Reference.getModDir(), "disable.cfg");

	    try {
			if (!file.exists() || !file.isFile()) file.createNewFile();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) builder.add(line);
			br.close();
		}
		catch (IOException ex) {ex.printStackTrace(); Reference.logger.info("Could not read disable.cfg");}
		
		return builder.build();
	}
	
	/**
	 * Reads the files containing the command that shall be executed on startup
	 * 
	 * @return A {@link Triple} of which the left value is the commands that should be run after server startup,
	 * 		   the middle value is the command that should be run on every server the player joins and the right
	 * 		   value is the map of commands which should only be run on certain servers
	 */
	private Triple<List<String>, List<String>, Map<String, List<String>>> parseStartupFiles() {
		final List<String> startupCommands = new ArrayList<String>();
		final List<String> startupCommandsM = new ArrayList<String>();
		final Map<String, List<String>> startupCommandsMap = new HashMap<String, List<String>>();
		
		try {
			File startup = new File(Reference.getModDir(), "startup.cfg");
			File startupMultiplayer = new File(Reference.getModDir(), "startup_multiplayer.cfg");
			if (!startup.exists() || !startup.isFile()) startup.createNewFile();
			if (!startupMultiplayer.exists() || !startupMultiplayer.isFile()) startupMultiplayer.createNewFile();
		
			BufferedReader br = new BufferedReader(new FileReader(startup));
			String line; while ((line = br.readLine()) != null) {if (!line.startsWith("#")) startupCommands.add(line.trim());}
			br.close();
			
			br = new BufferedReader(new FileReader(startupMultiplayer));
			boolean bracketOpen = false;
			String addr = null;
			
			while ((line = br.readLine()) != null) {
				if (!bracketOpen && ipPattern.matcher(line.trim()).matches()) {bracketOpen = true; addr = line.split("\\{")[0].trim();}
				else if (bracketOpen && line.trim().equals("}")) {bracketOpen = false;}
				else if (!bracketOpen && !line.startsWith("#")) startupCommandsM.add(line.trim());
				else if (bracketOpen && !line.startsWith("#")) {
					if (!startupCommandsMap.containsKey(addr)) startupCommandsMap.put(addr, new ArrayList<String>());
					startupCommandsMap.get(addr).add(line.trim());
				}
			}
			
			br.close();
		}
		catch (IOException ex) {ex.printStackTrace(); Reference.logger.info("Startup commands file could not be read");}
		
		return ImmutableTriple.of(startupCommands, startupCommandsM, startupCommandsMap);
	}
	
	
	
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	Reference.init(event);
    	LanguageManager.readTranslations();
    	
    	GlobalSettings.init();
		GlobalSettings.readSettings();
		
		PlayerSettings.registerCapabilities();
		
		if (this.loadCommands()) Reference.logger.info("Command Classes successfully loaded");
		
		this.disabledCommands = this.readDisabledCommands();
		Reference.logger.info("Following commands were disabled: " + this.disabledCommands);
		
		//Triple<List<String>, List<String>, Map<String, List<String>>> startupCommands = this.parseStartupFiles();
		//this.startupCommands = startupCommands.getLeft();
		//this.startupMultiplayerCommands = startupCommands.getMiddle();
		//this.startupServerCommands = startupCommands.getRight();
		
		BigBertha.proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
    	//this.proxy.init(e); //activate proxy

        //Achievements.init();

        // boolean MODLOADED = Loader.isModLoaded("MOD"); used to check if mods are loaded
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        this.proxy.serverInit(event);
    	
    }
    
    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
    	
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    	//this.proxy.postInit(e); //activate proxy
    }
}
