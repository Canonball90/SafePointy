package thefellas.safepoint.command.commands;

import net.minecraft.client.*;
import thefellas.safepoint.command.Command;

public class SayCommand extends Command
{
    public SayCommand() {
        super("say", "Sends a message (that can include the prefix)", new String[] { "say <message>" });
        this.addAliases(new String[] { "s" });
    }

    public void onCommand(final String[] args) {
        final StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; ++i) {
            message.append(args[i]);
            if (i != args.length - 1) {
                message.append(" ");
            }
        }
        Minecraft.getMinecraft().player.sendChatMessage(message.toString());
    }
}
