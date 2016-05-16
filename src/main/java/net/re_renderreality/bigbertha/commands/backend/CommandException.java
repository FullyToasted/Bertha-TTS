package net.re_renderreality.bigbertha.commands.backend;


import net.minecraft.command.ICommandSender;
import net.re_renderreality.bigbertha.BigBertha;
import net.re_renderreality.bigbertha.utils.LanguageManager;
import net.re_renderreality.bigbertha.utils.Reference;
import net.re_renderreality.bigbertha.wrapper.CommandSender;

/**
 * Custom exception type for command exceptions
 * 
 * @author MrNobody98
 *
 */
public class CommandException extends Exception {
	public CommandException() {
		super();
	}
   
	/**
	 * Constructs a {@link CommandException} with an error message
	 * 
	 * @param message the error message
	 */
	public CommandException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a {@link CommandException}
	 * 
	 * @param message this string is assumed to be a key of a language file entry, if it is not, it will
	 *        be used as error message otherwise, the translation for the key is fetched and used as error message
	 * @param sender the command sender
	 * @param formatArgs the formatting arguments for a translation (used for {@link String#format(String, Object...)})
	 */
	public CommandException(String message, ICommandSender sender, Object... formatArgs) {
		this(LanguageManager.translate(Reference.getCurrentLang(sender), message, formatArgs));
	}
	
	/**
	 * Constructs a {@link CommandException}. Delegate to {@link CommandException#CommandException(String, ICommandSender, Object...)}
	 * 
	 * @param message this string is assumed to be a key of a language file entry, if it is not, it will
	 *        be used as error message otherwise, the translation for the key is fetched and used as error message
	 * @param sender the command sender
	 * @param formatArgs the formatting arguments for a translation (used for {@link String#format(String, Object...)})
	 */
	public CommandException(String message, CommandSender sender, Object... formatArgs) {
		this(message, sender.getMinecraftISender(), formatArgs);
	}
	
	/**
	 * Constructs a new {@link CommandException} with the given {@link Throwable} as cause.
	 * 
	 * @param t the {@link Throwable} causing this exception
	 */
	public CommandException(Throwable t) {
		super(t);
	}
}
