package thefellas.safepoint.core.initializers;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.impl.command.Command;
import thefellas.safepoint.impl.command.commands.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    public static String prefix = ".";
    private final ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {

        commands.add(new PrefixCommand());
        commands.add(new FriendCommand());
        commands.add(new HelpCommand());
        commands.add(new ToggleCommand());
        commands.add(new BindCommand());
        commands.add(new ReloadSoundCommand());
        commands.add(new BookCommand());
        commands.add(new IpCommand());
    }

    public static void setPrefix(String prefix) {
        CommandManager.prefix = prefix;
    }

    @SubscribeEvent
    public void chatEvent(ClientChatEvent event) {
        if (event.getMessage().startsWith(prefix)) {
            event.setCanceled(true);

            List<String> args = Arrays.asList(event.getMessage().substring(prefix.length()).trim().split(" "));
            if (args.isEmpty()) {
                return;
            }

            for (Command command : commands) {
                if (command.getName().equalsIgnoreCase(args.get(0))) {
                    command.runCommand(args.subList(1, args.size()));
                    return;
                }
            }

            NotificationManager.sendMessage("Error", ChatFormatting.RED + "Invalid Command");
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}