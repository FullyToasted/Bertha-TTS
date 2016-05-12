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


@Mod(modid = Reference.MODID, name = Reference.NAME, dependencies = "required-after:Forge@[" + Reference.MIN_FORGE_VER + ",)", version = Reference.VERSION)
public class BigBertha
{    
    @Mod.Instance(Reference.MODID)
    public static BigBertha INSTANCE;
    
    public static enum ServerType {INTEGRATED, DEDICATED, ALL}
    private final DynamicClassLoader commandClassLoader = new DynamicClassLoader(BigBertha.class.getClassLoader());
    
    @SidedProxy(clientSide = "net.re_renderreality.proxy.ClientProxy", serverSide = "net.re_renderreality.proxy.ServerProxy", modId = Reference.MODID)
    private static CommonProxy proxy;
    
    private final Pattern ipPattern = Pattern.compile("^(\\d{1,3}\\.){3}\\d{1,3}:\\d{1,5}[ \t]*\\{$");
    private Logger logger;
    
    public static Random rnd = new Random();
    
    private List<String> disabledCommands;
	private List<String> startupCommands;
	private List<String> startupMultiplayerCommands;
	private Map<String, List<String>> startupServerCommands;

    /* ------------------------------------------------------------ */

    public static final String PERM = "BB";
    public static final String PERM_CORE = PERM + ".core";
    public static final String PERM_INFO = PERM_CORE + ".info";
    public static final String PERM_RELOAD = PERM_CORE + ".reload";
    public static final String PERM_VERSIONINFO = PERM_CORE + ".versioninfo";

    /* ------------------------------------------------------------ */
    
    /**
	 * A factory method for forge to get the mod instance
	 * 
	 * @return the MoreCommands instance
	 */
	@InstanceFactory
	private static BigBertha getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @return The running proxy
	 */
	public static CommonProxy getProxy() {
		return BigBertha.proxy;
	}
	
	/**
	 * @return Whether the mod runs on a dedicated server
	 */
	public static boolean isServerSide() {
		return !(BigBertha.proxy instanceof ServerProxy);
	}
	
	/**
	 * @return Whether the mod runs client side (e.g. integrated server)
	 */
	public static boolean isClientSide() {
		return BigBertha.proxy instanceof ClientProxy;
	}
	
	/**
	 * @return The running environment (client or server)
	 */
	public static Side getEnvironment() {
		if (BigBertha.isClientSide()) return Side.CLIENT;
		else if (BigBertha.isServerSide()) return Side.SERVER;
		else return null;
	}
	
	/**
	 * @return The Server the player plays on (integrated or dedicated)
	 */
	public static ServerType getServerType() {
		return BigBertha.proxy.getRunningServerType();
	}
	
	/**
	 * @return A list of commands, which shall be disabled
	 */
	public List<String> getDisabledCommands() {
		return new ArrayList<String>(this.disabledCommands);
	}
	
	/**
	 * @return the dynamic class loader responsible for command class loading
	 */
	public DynamicClassLoader getCommandClassLoader() {
		return this.commandClassLoader;
	}
	
	/**
	 * @return The Mod Logger
	 */
	public Logger getLogger() {
		return this.logger;
	}
	
	/**
	 * @return Whether the command is enabled
	 */
	public boolean isCommandEnabled(String command) {
		return !this.disabledCommands.contains(command);
	}
	
	/**
	 * @return The current language
	 */
	public String getCurrentLang(ICommandSender sender) {
		return BigBertha.proxy.getLang(sender);
	}
	
    public static CreativeTabs tabRfTools = new CreativeTabs("Big Bertha") {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return new Item(); //TODO Replace with an actual item
        }
    };
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	this.logger = e.getModLog();
    	Reference.init(e);
    	//this.proxy.preInit(e); //activate proxy
        //MainCompatHandler.registerWaila(); //Waila needs to be activated here if we want to use it
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
    	//this.proxy.init(e); //activate proxy

        Achievements.init();

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
