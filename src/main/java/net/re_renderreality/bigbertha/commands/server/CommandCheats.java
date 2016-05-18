package net.re_renderreality.bigbertha.commands.server;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.re_renderreality.bigbertha.BigBertha.ServerType;
import net.re_renderreality.bigbertha.commands.backend.CommandBase;
import net.re_renderreality.bigbertha.commands.backend.CommandException;
import net.re_renderreality.bigbertha.commands.backend.CommandRequirement;
import net.re_renderreality.bigbertha.commands.backend.ServerCommandInterface;
import net.re_renderreality.bigbertha.commands.backend.StandardCommand;
import net.re_renderreality.bigbertha.wrapper.CommandSender;

@CommandBase(
		name = "cheats",
		description = "command.cheats.description",
		example = "command.cheats.example",
		syntax = "command.cheats.syntax",
		videoURL = "command.cheats.videoURL"
		)
public class CommandCheats extends StandardCommand implements ServerCommandInterface {
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {return true;}
	
	@Override
	public String getCommandName() {
		return "cheats";
	}

	@Override
	public String getCommandUsage() {
		return "command.cheats.syntax";
	}

	@Override
	public void execute(CommandSender sender, String[] params) throws CommandException {
		try {sender.getWorld().setCheats(parseTrueFalse(params, 0, sender.getWorld().isCheats()));}
		catch (IllegalArgumentException ex) {throw new CommandException("command.cheats.failure", sender);}
		
		sender.getWorld().setCheats(sender.getWorld().isCheats());
		sender.sendLangfileMessage(sender.getWorld().isCheats() ? "command.cheats.on" : "command.cheats.off");
	}
	
	@Override
	public CommandRequirement[] getRequirements() {
		return new CommandRequirement[0];
	}

	@Override
	public ServerType getAllowedServerType() {
		return ServerType.INTEGRATED;
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}
	
	@Override
	public boolean canSenderUse(String commandName, ICommandSender sender, String[] params) {
		return true;
	}
}
