package net.re_renderreality.bigbertha.proxy;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.re_renderreality.bigbertha.BigBertha;
import net.re_renderreality.bigbertha.BigBertha.ServerType;
import net.re_renderreality.bigbertha.commands.backend.MultipleCommands;
import net.re_renderreality.bigbertha.commands.backend.ServerCommand;
import net.re_renderreality.bigbertha.commands.backend.StandardCommand;
import net.re_renderreality.bigbertha.events.EventHandler;
import net.re_renderreality.bigbertha.utils.Reference;
import net.re_renderreality.bigbertha.utils.SettingsManager;
import net.re_renderreality.bigbertha.utils.Updater;
import net.re_renderreality.bigbertha.utils.GlobalSettings;
import net.re_renderreality.bigbertha.utils.NBTSettingsManager;

public class CommonProxy {
	
	protected BigBertha mod = BigBertha.INSTANCE;
	private ITextComponent updateText = null;
	protected boolean playerNotified = false;
	private String currentWorld = null;
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
	
	/**
	 * @return the update chat text component
	 */
	public ITextComponent getUpdateText() {
		return this.updateText;
	}
	
	/**
	 * @return whether the player was already notified about an update
	 */
	public boolean wasPlayerNotified() {
		return this.playerNotified;
	}
	
	/**
	 * Fetches the current world's folder name and sends it to the player
	 * 
	 * @param player the player to which the world's folder name is to be sent
	 * @see CommonProxy#setCurrentWorld(String)
	 */
	public void updateWorld(EntityPlayerMP player) {
		if (!player.getServer().getFolderName().equals(this.currentWorld)) {
			this.currentWorld = player.getServer().getFolderName();
			
			if (!(BigBertha.proxy instanceof ClientProxy)) {}
				//this.mod.getPacketDispatcher().sendS14ChangeWorld(player, currentWorld);
		}
	}
	
	/**
	 * @return The folder name of the current world
	 * @see CommonProxy#setCurrentWorld(String)
	 */
	public String getCurrentWorld() {
		return this.currentWorld;
	}
	
	/**
	 * Registers Event Handlers
	 */
	public void registerHandlers() {
		ModContainer container = Loader.instance().activeModContainer();
		
		if (container == null || !container.getModId().equals(Reference.MODID)){
			Reference.logger.warn("Error registering Event Handlers");
			return;
		}
		
		EventHandler.registerDefaultForgeHandlers(container, false);
		Reference.logger.info("Event Handlers registered");
	}
	
	/**
	 * Called from the main mod class to do pre initialization
	 */
	public void preInit(FMLPreInitializationEvent event) {
		
	}
	
	/**
	 * Called from the main mod class to do initialization
	 */
	public void init(FMLInitializationEvent event) {
		if (GlobalSettings.searchUpdates) findMoreCommandsUpdates();
	}
	
	/**
	 * Called from the main mod class to handle the server start
	 */
	public void serverInit(FMLServerStartingEvent event) {
		try {
			this.registerServerCommands(event.getServer());
			Reference.logger.info("Server Commands successfully registered");
		}
		catch (Exception ex) {Reference.logger.warn("Failed to register Server Command", ex);}
	}
	
	/**
	 * Starts a thread looking for MoreCommands updates
	 */
	private void findMoreCommandsUpdates() {
		Reference.logger.info("Searching for MoreCommands updates");
		
		new Thread(new Updater(Loader.MC_VERSION, new Updater.UpdateCallback() {
			@Override
			public void udpate(String version, String website, String download) {
				TextComponentString text = new TextComponentString(Reference.VERSION.equals(version) ? 
						"BigBertha update for this version found " : "new BigBertha version found: "); 
				text.getChatStyle().setColor(TextFormatting.BLUE);
				TextComponentString downloadVersion = new TextComponentString(version); downloadVersion.getChatStyle().setColor(TextFormatting.YELLOW);
				TextComponentString homepage = new TextComponentString("Minecraft Forum"); homepage.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, website)).setColor(TextFormatting.GREEN).setItalic(true).setUnderlined(true);
				TextComponentString downloadPage = new TextComponentString("Download"); downloadPage.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, download)).setColor(TextFormatting.GREEN).setItalic(true).setUnderlined(true);
				TextComponentString comma = new TextComponentString(", "); comma.getChatStyle().setColor(TextFormatting.DARK_GRAY);
				TextComponentString sep = new TextComponentString(" - "); sep.getChatStyle().setColor(TextFormatting.DARK_GRAY);
				
				String rawText = text.getUnformattedText() + (Reference.VERSION.equals(version) ? "" : downloadVersion.getUnformattedText()) + " - " + website + ", " + download;
				if (!Reference.VERSION.equals(version)) text.appendSibling(downloadVersion);
				text.appendSibling(sep).appendSibling(homepage).appendSibling(comma).appendSibling(downloadPage);
				
				Reference.logger.info(rawText);
				CommonProxy.this.updateText = text;
				
				if (BigBertha.proxy instanceof ClientProxy && net.minecraft.client.Minecraft.getMinecraft().thePlayer != null) {
					CommonProxy.this.playerNotified = true;
					net.minecraft.client.Minecraft.getMinecraft().thePlayer.addChatMessage(text);
				}
			}
		}), "MoreCommands Update Thread").start();
	}
	
	
	/**
	 * Registers all server commands
	 * 
	 * @return Whether the server commands were registered successfully
	 */
	private void registerServerCommands(MinecraftServer server) throws Exception {
		List<Class<? extends StandardCommand>> serverCommands = this.mod.getServerCommandClasses();
		if (serverCommands == null) throw new RuntimeException("Server Command Classes not loaded");
		ServerCommandManager commandManager = (ServerCommandManager) server.getCommandManager(); 
		
		for (Class<? extends StandardCommand> cmdClass : serverCommands) {
			try {
				StandardCommand cmd = cmdClass.newInstance();
				
				if (cmd instanceof MultipleCommands) {
					Constructor<? extends StandardCommand> ctr = cmdClass.getConstructor(int.class);
					
					for (int i = 0; i < ((MultipleCommands) cmd).getNames().length; i++)
						if (this.mod.isCommandEnabled(((MultipleCommands) cmd).getNames()[i]))
							commandManager.registerCommand(new ServerCommand(ServerCommand.upcast(ctr.newInstance(i))));
				}
				else if (this.mod.isCommandEnabled(cmd.getCommandName()))
					commandManager.registerCommand(new ServerCommand(ServerCommand.upcast(cmd)));
			}
			catch (Exception ex) {
				Reference.logger.warn("Skipping Server Command " + cmdClass.getName() + " due to the following exception during loading", ex);
			}
		}
	}
	
	
	/**
	 * Creates a {@link SettingsManager} for a player
	 * 
	 * @param player the player to whom the settings manager belongs to
	 * @return the settings manager
	 */
	public SettingsManager createSettingsManagerForPlayer(EntityPlayer player) {
		return new NBTSettingsManager(new File(Reference.getSettingsDirServer(), player.getUniqueID().toString() + ".dat"), true, false);
	}
	
}
