package net.re_renderreality.bigbertha;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = BigBertha.MODID, version = BigBertha.VERSION)
public class BigBertha
{
    public static final String MODID = "bigbertha";
    public static final String VERSION = "0.0.1";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
}
