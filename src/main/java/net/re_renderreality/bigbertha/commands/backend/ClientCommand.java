package net.re_renderreality.bigbertha.commands.backend;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.re_renderreality.bigbertha.BigBertha;
import net.re_renderreality.bigbertha.utils.LanguageManager;
import net.re_renderreality.bigbertha.utils.Reference;

/**
 * A wrapper class for commands which are intended to be used
 * on client side. Delegates all method calls to the wrapped command.
 * The wrapped command must be of type {@link StandardCommand} and {@link ClientCommandProperties}
 * 
 * @author MrNobody98
 */
public final class ClientCommand<T extends StandardCommand & ClientCommandInterface> extends CommandBaseAbstract<T> implements ClientCommandInterface {	
	/** Set by the "clientcommands" command. Indicates whether client commands are enabled/disabled */
	public static boolean clientCommandsEnabled = true;
	
	/**
	 * Checks if an object <i>o</i> is of type {@link StandardCommand} and {@link ClientCommandProperties}.<br>
	 * Returns null if not, else a generic type with those two types as bounds.
	 * 
	 * @param o the object to check/cast
	 * @return a generic type having those two types as bounds
	 * @throws IllegalArgumentException if the <i>o</i> is not of type {@link StandardCommand} and {@link ClientCommandProperties}
	 */
	//possible to do with "(A & B) obj" multiple bounds cast in java 8, not used in order to be able to use older java versions
	public static final <T extends StandardCommand & ClientCommandInterface> T upcast(Object o) throws IllegalArgumentException {
		if (o instanceof StandardCommand && o instanceof ClientCommandInterface) return (T) o;
		else throw new IllegalArgumentException("argument is not of type StandardCommand & ClientCommandProperties");
	}
	
	private final ClientCommandInterface delegate;
	
	public ClientCommand(T delegate) {
		super(delegate);
		this.delegate = delegate;
	}
	
    @Override
    public final boolean checkRequirements(ICommandSender sender, String[] params, Side side) {
		String lang = Reference.getCurrentLang(sender);
		
    	if (!(sender instanceof net.minecraft.client.entity.EntityPlayerSP)) {
        	TextComponentString text = new TextComponentString(LanguageManager.translate(lang, "command.generic.cantUse"));
        	text.getChatStyle().setColor(TextFormatting.RED);
        	sender.addChatMessage(text);
        	return false;
    	}
    	
    	return super.checkRequirements(sender, params, side);
    }

	@Override
	public boolean registerIfServerModded() {
		return this.delegate.registerIfServerModded();
	}
	
	@Override
	public Side getSide() {
		return Side.CLIENT;
	}
}
