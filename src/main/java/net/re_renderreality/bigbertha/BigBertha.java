package net.re_renderreality.bigbertha;

import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
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


@Mod(modid = BigBertha.MODID, name = "Big Bertha", dependencies = "required-after:Forge@[" + BigBertha.MIN_FORGE_VER + ",)", version = BigBertha.VERSION)
public class BigBertha
{
    public static final String MODID = "bigbertha";
    public static final String VERSION = "0.0.1";
    public static final String MIN_FORGE_VER = "12.16.1.1891";
    
    @Mod.Instance(MODID)
    public static BigBertha instance;
    
    public static Random rnd = new Random();

    /* ------------------------------------------------------------ */

    public static final String PERM = "BB";
    public static final String PERM_CORE = PERM + ".core";
    public static final String PERM_INFO = PERM_CORE + ".info";
    public static final String PERM_RELOAD = PERM_CORE + ".reload";
    public static final String PERM_VERSIONINFO = PERM_CORE + ".versioninfo";

    /* ------------------------------------------------------------ */
    
    public static CreativeTabs tabRfTools = new CreativeTabs("Big Bertha") {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return new Item(); //TODO Replace with an actual item
        }
    };
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
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
