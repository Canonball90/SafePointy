package thefellas.safepoint.impl.command.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.command.Command;

import java.util.List;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "Shows All The HockeyWare Commands", "help");
    }

    @Override
    public void runCommand(List<String> args) {
        try {
            for (Command command : Safepoint.commandManager.getCommands()) {
                Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.WHITE + command.getName() + TextFormatting.GRAY + " " + command.getDescription() + " syntax: " + command.getSyntax()));
            }
        } catch (Exception ignored) {
        }
    }
}
