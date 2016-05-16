package net.re_renderreality.bigbertha.commands.backend;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.re_renderreality.bigbertha.BigBertha;
import net.re_renderreality.bigbertha.BigBertha.ServerType;
import net.re_renderreality.bigbertha.core.AppliedPatches.PlayerPatches;
import net.re_renderreality.bigbertha.utils.LanguageManager;
import net.re_renderreality.bigbertha.utils.Reference;
import net.re_renderreality.bigbertha.wrapper.CommandSender;

public abstract class AbstractCommandBase extends net.minecraft.command.CommandBase {
	/**
	 * @return whether the command sender can use this command
	 */
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if (this.getRequiredPermissionLevel() == 0) return true;
		else return sender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
	}
	
	/**
     * Gets the sender as an entity. {@link #isSenderOfEntityType(ICommandSender, Class)} must be true!
     * 
     * @param sender the command sender
     * @param entityType the entity type class
     * @return the sender as entity type <b>entityType</b> or null if  {@link #isSenderOfEntityType(ICommandSender, Class)} is false
     */
    public static <T extends Entity> T getSenderAsEntity(ICommandSender sender, Class<T> entityType) {
    	if (!isSenderOfEntityType(sender, entityType)) return null;
    	else return entityType.isInstance(sender) ? entityType.cast(sender) : entityType.cast(sender.getCommandSenderEntity());
    }
    
    /**
	 * @return The requirements for a command to be executed
	 */
    public abstract CommandRequirement[] getRequirements();
    
	/**
	 * Checks the command sender whether he can use this command with the given parameters
	 * 
	 * @param sender the command sender
	 * @param params the command parameters
	 * @param side the side on which this command is executed
	 * 
	 * @return whether the command requirements are satisfied
	 */
    public boolean checkRequirements(ICommandSender sender, String[] params, Side side) {
    	String lang = Reference.getCurrentLang(sender);
    	
    	if (!(this.getAllowedServerType() == ServerType.ALL || this.getAllowedServerType() == BigBertha.proxy.getRunningServerType())) {
    		if (this.getAllowedServerType() == ServerType.INTEGRATED)
    			sendChatMsg(sender, LanguageManager.translate(lang, "command.generic.notIntegrated"));
    		if (this.getAllowedServerType() == ServerType.DEDICATED) 
    			sendChatMsg(sender, LanguageManager.translate(lang, "command.generic.notDedicated"));
    		return false;
    	}
    	
    	PlayerPatches clientInfo = isSenderOfEntityType(sender, EntityPlayerMP.class) ? 
    	getSenderAsEntity(sender, EntityPlayerMP.class).getCapability(PlayerPatches.PATCHES_CAPABILITY, null) : null;
    	    	
    	CommandRequirement[] requierements = this.getRequirements();
    	if (clientInfo == null) {
    	   clientInfo = PlayerPatches.PATCHES_CAPABILITY.getDefaultInstance();
    	    		
    	   if (isSenderOfEntityType(sender, EntityPlayerMP.class)) {}
    		   //clientInfo.setServerPlayHandlerPatched(getSenderAsEntity(sender, EntityPlayerMP.class);
    	   //TODO: playerNetServerHandler instanceof com.mrnobody.morecommands.patch.NetHandlerPlayServer);
    	}
    	
    	for (CommandRequirement requierement : requierements) {
    		if (!requierement.isSatisfied(sender, clientInfo, side)) {
    			sendChatMsg(sender, LanguageManager.translate(lang, requierement.getLangfileMsg(side)));
    			return false;
    		}
    	}
    	
    	return true;
    }
    	    	
	
	/**
	 * @return The required permission level
	 */
	@Override
    public int getRequiredPermissionLevel() {
		return this.getDefaultPermissionLevel();
	}
	
	/**
	 * @return The default permission level
	 */
    public abstract int getDefaultPermissionLevel();
    
    /**
	 * @return The command usage
	 */
    public final String getCommandUsage(ICommandSender sender) {return this.getCommandUsage();}
    
	/**
	 * processes the command
	 */
    public abstract void execute(MinecraftServer server, ICommandSender sender, String[] params) throws net.minecraft.command.CommandException;
    
	/**
	 * @return The command name
	 */
    public abstract String getCommandName();
    
	/**
	 * @return The command usage
	 */
    public abstract String getCommandUsage();
    
    /**
	 * Executes the command
	 * 
	 * @param sender the command sender
	 * @param params the command parameters
	 * @throws CommandException if the command couldn't be processed for some reason
	 */
    public abstract void execute(CommandSender sender, String[] params) throws CommandException;
    
	/**
	 * @return The Server Type on which this command can be executed
	 */
    public abstract ServerType getAllowedServerType();
    
    private final void sendChatMsg(ICommandSender sender, String msg) {
    	TextComponentString text = new TextComponentString(msg);
    	text.getChatStyle().setColor(TextFormatting.RED);
    	sender.addChatMessage(text);
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o == this) return true;
    	else if (!(o instanceof AbstractCommandBase)) return false;
    	else return ((AbstractCommandBase) o).getCommandName() != null && ((AbstractCommandBase) o).getCommandName().equals(this.getCommandName());
    }
    
    @Override
    public int hashCode() {
    	return this.getCommandName() == null ? 0 : this.getCommandName().hashCode();
    }
    
    private static final Joiner spaceJoiner = Joiner.on(" ");
    
    /**
     * Rejoins the space-splitted parameters to a string
     * 
     * @param params
     * @return the rejoined parameters
     */
    public static final String rejoinParams(String[] params) {
    	return spaceJoiner.join(params);
    }
    
    /**
     * returns true if the value of <b>params[index]</b> is <b>"enable"</b>, <b>"on"</b>, <b>"1"</b> or <b>"true"</b>,<br>
     * false if this value is <b>"disable"</b>, <b>"off"</b>, <b>"0"</b> or <b>"false"</b>.<br>
     * If the index does not exist, it will return <b>default_</b> NEGATED.
     * 
     * @param params the command arguments
     * @param index the index to be parsed
     * @param default_ the negated default value
     * @return the parsed boolean argument
     * 
     * @throws IllegalArgumentException if <b>params[index]</b> is none of the values named above
     */
    public static boolean parseTrueFalse(String[] params, int index, boolean default_) throws IllegalArgumentException {
        if (params.length > index) {
        	if (params[index].equalsIgnoreCase("enable") || params[index].equalsIgnoreCase("1")
            	|| params[index].equalsIgnoreCase("on") || params[index].equalsIgnoreCase("true")) {
        		return true;
            }
            else if (params[index].equalsIgnoreCase("disable") || params[index].equalsIgnoreCase("0")
            		|| params[index].equalsIgnoreCase("off") || params[index].equalsIgnoreCase("false")) {
            	return false;
            }
            else throw new IllegalArgumentException("Invalid Argument");
        }
        else return !default_;
    }
    
    /**
     * The default opening <-> closing char mapping. Contains:<br>
     * '{' -> '}'<br>
     * '[' -> ']'<br>
     */
    public static final ImmutableMap<Character, Character> DEFAULT_JSON_OPENING_CLOSING_CHARS = ImmutableMap.of('{', '}', '[', ']');
    
    /**
     * Searches a splitted string from a starting index which contains an opening char<br>
     * for the closing index which contains the respective closing char. If the opening char occurs<br>
     * again, the next closing char is NOT considered as the final closing char and the<br>
     * search continues. <b>Note that the opening and closing char must be the first/last<br>
     * char of their respective index!</b>
     * 
     * @param params the the splitted string array to be searched
     * @param openingIndex the index from where the closing index should be searched
     * @param openingClosingChars the opening char <-> closing char map
     * 
     * @return <b>-1</b> if the first char of <b>openingIndex is not any of openingClosingChars.keySet()</b><br>
     * or if the <b>closing index does not exist</b> or if the <b>last char of the closing index<br>
     * is not the closing char</b>. Otherwise the <b>closing index</b> is returned
     * 
     * @throws ArrayIndexOutOfBoundsException if openingIndex >= params.length
     */
    public static int getClosingIndex(String[] params, int openingIndex, Map<Character, Character> openingClosingChars) {
    	if (openingIndex >= params.length) throw new ArrayIndexOutOfBoundsException("openingIndex out of bounds");
    	
    	if (params[openingIndex].isEmpty() || !startsWithAnyChar(params[openingIndex], 
    		openingClosingChars.keySet().toArray(new Character[openingClosingChars.size()]))) return -1;
    	
    	char openingChar = params[openingIndex].charAt(0), closingChar = openingClosingChars.get(openingChar);
    	int depthLevel = 0; int index = openingIndex;
    	
    	for (String param : Arrays.copyOfRange(params, openingIndex, params.length)) {
    		char[] chars = param.toCharArray();
    		
    		for (char ch : chars) {
    			if (ch == openingChar) depthLevel++;
    			else if (ch == closingChar) depthLevel--;
    		}
    		
    		if (depthLevel == 0) 
    			return chars[chars.length - 1] == closingChar ? index : -1;
    		
    		index++;
    	}
    	
    	return -1;
    }
    
    private static boolean startsWithAnyChar(String s, Character[] chars) {
    	char ch = s.charAt(0);
    	
    	for (char character : chars)
    		if (character == ch) return true;
    	
    	return false;
    }
    
    private static final Pattern isTargetSeletorWithArguments = Pattern.compile("^@[pareb]\\[");
    
    /**
     * Get's a player by its name
     * 
     * @return the player or null if it wasn't found
     */
    public static EntityPlayerMP getPlayer(MinecraftServer server, String param) {
    	return server.getPlayerList().getPlayerByUsername(param);
    }
    
    /**
     * checks whether the command sender is of a certain entity type
     * 
     * @param sender the command sender
     * @param entityType the entity type class
     * @return whether the sender is of the type <b>entityType</b>
     */
    public static <T extends Entity> boolean isSenderOfEntityType(ICommandSender sender, Class<T> entityType) {
    	return entityType.isInstance(sender) ? true : entityType.isInstance(sender.getCommandSenderEntity());
    }
}
