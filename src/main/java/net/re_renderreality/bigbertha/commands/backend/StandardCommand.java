package net.re_renderreality.bigbertha.commands.backend;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;
import net.re_renderreality.bigbertha.wrapper.CommandSender;

/**
 * The base class for a standard command. A standard command is a regular
 * command which has only one name and provides only one functionality
 * corresponding to that name
 * 
 * @author MrNobody98
 */
public abstract class StandardCommand extends AbstractCommandBase {
	@Override
	public final void execute(MinecraftServer server, ICommandSender sender, String[] params) throws net.minecraft.command.CommandException {
    	if (this.checkRequirements(sender, params, this instanceof ClientCommandInterface ? Side.CLIENT : Side.SERVER)) {
        	try {this.execute(new CommandSender(sender), params);}
        	catch (CommandException e) {throw new net.minecraft.command.CommandException(e.getMessage());}
    	}
	}
}
