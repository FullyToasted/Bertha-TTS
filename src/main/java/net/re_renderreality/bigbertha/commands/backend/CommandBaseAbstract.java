package net.re_renderreality.bigbertha.commands.backend;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;
import net.re_renderreality.bigbertha.BigBertha.ServerType;
import net.re_renderreality.bigbertha.wrapper.CommandSender;

/**
 * A wrapper class for {@link StandardCommand}s that delegates
 * all method calls to the wrapped {@link StandardCommand}
 * 
 * @author MrNobody98
 */
public abstract class CommandBaseAbstract<T extends StandardCommand> extends AbstractCommandBase {
	private final T delegate;
	
	protected CommandBaseAbstract(T delegate) {
		if (delegate == null) throw new NullPointerException("delegate == null");
		this.delegate = delegate;
	}
	
	public T getDelegate() {
		return this.delegate;
	}

	@Override
	public String getCommandName() {
		return this.delegate.getCommandName();
	}
	
	@Override
	public String getCommandUsage() {
		return this.delegate.getCommandUsage();
	}
	
	@Override
	public final void execute(MinecraftServer server, ICommandSender sender, String[] params) throws net.minecraft.command.CommandException {
    	if (this.checkRequirements(sender, params, this.getSide())) {
        	try {this.execute(new CommandSender(sender), params);}
        	catch (CommandException e) {
        		if (e.getCause() instanceof net.minecraft.command.CommandException)
    				throw (net.minecraft.command.CommandException) e.getCause();
        		else if (e.getMessage() != null) throw new net.minecraft.command.CommandException(e.getMessage());
        	}
    	}
    }
	
	@Override
	public void execute(CommandSender sender, String[] params) throws CommandException {
		this.delegate.execute(sender, params);
	}

	@Override
	public CommandRequirement[] getRequirements() {
		return this.delegate.getRequirements();
	}

	@Override
	public ServerType getAllowedServerType() {
		return this.delegate.getAllowedServerType();
	}

	@Override
	public int getDefaultPermissionLevel() {
		return this.delegate.getDefaultPermissionLevel();
	}
	
	/**
	 * @return the side on which the wrapped command should be executed
	 */
	public abstract Side getSide();
}
