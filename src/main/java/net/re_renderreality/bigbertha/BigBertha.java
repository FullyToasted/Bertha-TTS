package net.re_renderreality.bigbertha;

import java.util.Random;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = BigBertha.MODID, name = BigBertha.MODNAME, version = BigBertha.VERSION)
public class BigBertha
{
    public static final String MODID = "bigbertha";
    public static final String MODNAME = "Big Bertha";
    public static final String VERSION = "0.0.1";
    
    @Instance(value = MODID)
    public static BigBertha instance;
    
    public static Random rnd = new Random();

    /* ------------------------------------------------------------ */

    public static final String PERM = "BB";
    public static final String PERM_CORE = PERM + ".core";
    public static final String PERM_INFO = PERM_CORE + ".info";
    public static final String PERM_RELOAD = PERM_CORE + ".reload";
    public static final String PERM_VERSIONINFO = PERM_CORE + ".versioninfo";

    /* ------------------------------------------------------------ */
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    }
}
