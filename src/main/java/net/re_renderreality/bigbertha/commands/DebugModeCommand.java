package net.re_renderreality.bigbertha.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.re_renderreality.bigbertha.BigBertha.ServerType;
import net.re_renderreality.bigbertha.commands.backend.AbstractCommandBase;
import net.re_renderreality.bigbertha.commands.backend.ClientCommand;
import net.re_renderreality.bigbertha.commands.backend.ClientCommandInterface;
import net.re_renderreality.bigbertha.commands.backend.CommandBase;
import net.re_renderreality.bigbertha.commands.backend.CommandException;
import net.re_renderreality.bigbertha.commands.backend.CommandRequirement;
import net.re_renderreality.bigbertha.commands.backend.StandardCommand;
import net.re_renderreality.bigbertha.utils.Logging;
import net.re_renderreality.bigbertha.wrapper.CommandSender;

@CommandBase(
		name = "defuse",
		description = "command.defuse.description",
		example = "command.defuse.example",
		syntax = "command.defuse.syntax",
		videoURL = "command.defuse.videoURL"
)
public class DebugModeCommand extends StandardCommand implements ClientCommandInterface {
    @Override
    public String getCommandUsage() {
        return "command.debugmode.syntax";
    }

    @Override
    public String getCommandName() {
        return "DebugMode";
    }

    @Override
	public CommandRequirement[] getRequirements() {
		return new CommandRequirement[0];
	}

    @Override
	public ServerType getAllowedServerType() {
		return ServerType.ALL;
	}
    
    @Override
	public boolean registerIfServerModded() {
		return true;
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}
	
	private String[] getInfo(ClientCommand<?> cmd) {
		StandardCommand delegate = cmd.getDelegate();
		
		/*
		if (delegate instanceof MultipleCommands && delegate.getClass().isAnnotationPresent(Command.MultipleCommand.class)) {
			MultipleCommands command = (MultipleCommands) delegate;
			Command.MultipleCommand info = delegate.getClass().getAnnotation(Command.MultipleCommand.class);
			
			try {return new String[] {info.name()[command.getTypeIndex()], info.description()[command.getTypeIndex()],
				info.syntax()[command.getTypeIndex()], info.example()[command.getTypeIndex()], info.videoURL()[command.getTypeIndex()]};}
			catch (ArrayIndexOutOfBoundsException ex) {return null;}
		}
		else */
		if (delegate.getClass().isAnnotationPresent(CommandBase.class)) {
			CommandBase info = delegate.getClass().getAnnotation(CommandBase.class);
			return new String[] {info.name(), info.description(), info.syntax(), info.example(), info.videoURL()};
		}
		else return null;
	}

    @Override
    public void execute(CommandSender sender, String[] params) throws CommandException {
        Logging.debugMode = !Logging.debugMode;
        if (Logging.debugMode) {
            sender.sendChatComponent(new TextComponentString(TextFormatting.YELLOW + "BigBertha Debug Mode enabled!"));
        } else {
            sender.sendChatComponent(new TextComponentString(TextFormatting.YELLOW + "BigBertha Debug Mode disabled!"));
        }
    }
}
