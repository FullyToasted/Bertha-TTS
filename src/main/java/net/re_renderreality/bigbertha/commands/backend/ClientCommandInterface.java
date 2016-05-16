package net.re_renderreality.bigbertha.commands.backend;

/**
 * An interface indicating that the command which implements this interface
 * should be processed on client side.
 * 
 * @author AlexHoff97
 */
public interface ClientCommandInterface {
    /**
     * @return Whether this command shall be registered if the server has this mod installed
     */
    public boolean registerIfServerModded();
}
