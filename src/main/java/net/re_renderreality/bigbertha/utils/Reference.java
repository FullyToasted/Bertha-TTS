package net.re_renderreality.bigbertha.utils;

import java.io.File;
import java.util.Date;

import org.apache.logging.log4j.Logger;

import net.re_renderreality.bigbertha.BigBertha;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * A class containing information on the mod, such as mod id, version <br>
 * paths, etc.
 * 
 * @author AlexHoff
 *
 */
public final class Reference {
	/** the mod id */
	public static final String MODID = "bigbertha";
	/** the mod version */
	public static final String VERSION = "0.0.1";
	/** the mod name */
	public static final String NAME = "Big Bertha";
	/** the mod network channel name */
	public static final String CHANNEL = "BigBertha_mod";
	/** The website URL */
	public static final String WEBSITE = "";
	/** The build date */
    public static final Date BUILD = new Date(System.currentTimeMillis()); //gets replaced during build process
	/** Minimum Forge Version */
    public static final String MIN_FORGE_VER = "12.16.1.1891";
    
    /** @return the mod configuration directory */
	public static final File getModDir() {return Reference.INSTANCE.MOD_DIR;}
	/** @return the directory where the mod saves server player data */
	public static final File getSettingsDirServer() {return Reference.INSTANCE.SETTINGS_DIR_SERVER;}
	
	/** @return the serverLogger */
	public static Logger logger;
	
	private static Reference INSTANCE;
	
	private final File SETTINGS_DIR_SERVER;
	private final File MOD_DIR;
	
	private Reference(FMLPreInitializationEvent event) {
		this.MOD_DIR = new File(event.getModConfigurationDirectory(), "BigBertha");
		if (!this.MOD_DIR.exists()) this.MOD_DIR.mkdirs();
		
		this.SETTINGS_DIR_SERVER = new File(this.MOD_DIR, "Bertha_CoreSettings");
		if (!this.SETTINGS_DIR_SERVER.exists()) this.SETTINGS_DIR_SERVER.mkdirs();
	}
	
	/**
	 * Initializes the the configuration directories from
	 * {@link FMLPreInitializationEvent#getModConfigurationDirectory()}
	 */
	public static final void init(FMLPreInitializationEvent event) {
		if (INSTANCE == null) Reference.INSTANCE = new Reference(event);
		
		if (logger == null) Reference.logger = event.getModLog();
	}
	
	/**
	 * @return The current language
	 */
	public static String getCurrentLang(ICommandSender sender) {
		return BigBertha.proxy.getLang(sender);
	}
}
