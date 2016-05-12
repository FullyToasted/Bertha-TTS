package net.re_renderreality.bigbertha.proxy;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.re_renderreality.bigbertha.BigBertha.ServerType;

public class CommonProxy {
	
	/**
	 * @return The Server Type the player plays on
	 */
	public ServerType getRunningServerType() {
		return ServerType.DEDICATED;
	}
	
	/**
	 * Called from the main mod class to get the language
	 */
	public String getLang(ICommandSender sender) {
		return "TODO";
	}
}
