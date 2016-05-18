package net.re_renderreality.bigbertha;

import java.io.File;
import java.util.Date;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Reference Class to be used by other classes that need access to basic mod information
 * 
 * @author AlexHoff97
 */
public final class Reference {
	/** the mod id */
	public static final String MODID = "BigBertha";
	/** the mod version */
	public static final String VERSION = "0.0.1";
	/** the mod name */
	public static final String NAME = "Big Bertha";
	/** the mod network channel name */
	public static final String CHANNEL = "bb";
	/** The website URL */
	public static final String WEBSITE = "http://http://re-renderreality.net/";
	/** The build date */
    public static final Date BUILD = new Date(System.currentTimeMillis()); //gets replaced during build process
    
    public static final String CLIENT_PROXY_CLASS = "net.re_renderreality.bigbertha.proxy.ClientProxy";
    
    public static final String SERVER_PROXY_CLASS = "net.re_renderreality.bigbertha.proxy.CommonProxy";
	
    /** @return the mod configuration directory */
	public static final File getModDir() {return Reference.INSTANCE.MOD_DIR;}
	/** @return the directory where the mod saves server player data */
	public static final File getSettingsDirServer() {return Reference.INSTANCE.SETTINGS_DIR_SERVER;}
	
	private static Reference INSTANCE;
	
	public static final boolean ForceDebug = false;
	
	private final File SETTINGS_DIR_SERVER;
	private final File MOD_DIR;
	
	private Reference(FMLPreInitializationEvent event) {
		this.MOD_DIR = new File(event.getModConfigurationDirectory(), "BigBertha");
		if (!this.MOD_DIR.exists()) {
			this.MOD_DIR.mkdirs();
		}
		
		this.SETTINGS_DIR_SERVER = new File(this.MOD_DIR, "serversettings");
		if (!this.SETTINGS_DIR_SERVER.exists()) {
			this.SETTINGS_DIR_SERVER.mkdirs();
		}
	}
	/**
	 * Initializes the the configuration directories from
	 * {@link FMLPreInitializationEvent#getModConfigurationDirectory()}
	 */
	public static final void init(FMLPreInitializationEvent event) {
		if (INSTANCE == null) {
			Reference.INSTANCE = new Reference(event);
		}
	}
}