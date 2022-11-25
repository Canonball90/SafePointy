package thefellas.safepoint.impl.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import thefellas.safepoint.impl.command.Command;
import thefellas.safepoint.core.initializers.CommandManager;
import thefellas.safepoint.core.initializers.NotificationManager;

import java.util.List;

public class PrefixCommand extends Command {
    public PrefixCommand() {
        super("prefix", "Allows You To Change The HockeyWare Chat Prefix", "prefix [prefix]");
    }

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 1) {
            CommandManager.setPrefix(args.get(0));
            NotificationManager.sendMessage("Success", ChatFormatting.GREEN + "Set prefix to " + args.get(0));
        } else {
            NotificationManager.sendMessage("Error", getSyntax());
        }
    }
}
