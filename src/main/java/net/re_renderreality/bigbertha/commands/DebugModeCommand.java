package net.re_renderreality.bigbertha.commands;


import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.re_renderreality.bigbertha.commands.backend.AbstractCommand;
import net.re_renderreality.bigbertha.utils.Logging;

public class DebugModeCommand extends AbstractCommand {
    @Override
    public String getHelp() {
        return "";
    }

    @Override
    public String getCommand() {
        return "toggle";
    }

    @Override
    public int getPermissionLevel() {
        return 1;
    }

    @Override
    public boolean isClientSide() {
        return false;
    }

    @Override
    public void execute(ICommandSender sender, String[] args) {
        Logging.debugMode = !Logging.debugMode;
        if (Logging.debugMode) {
            sender.addChatMessage(new TextComponentString(TextFormatting.YELLOW + "RFTools Debug Mode enabled!"));
        } else {
            sender.addChatMessage(new TextComponentString(TextFormatting.YELLOW + "RFTools Debug Mode disabled!"));
        }
    }
}
