package thefellas.safepoint.initializers;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import thefellas.safepoint.command.Command;
import thefellas.safepoint.command.commands.SaveConfig;

import java.util.ArrayList;

public class CommandInitializer {
    private final ArrayList<Command> commands = new ArrayList<>();
    private String prefix = ",";

    public CommandInitializer()
    {
        commands.add(new SaveConfig("Config", new String[]{"save"}, "Save config"));
    }

    public void runCommand(String args)
    {
        boolean found = false;
        String[] split = args.split(" ");
        String startCommand = split[0];
        String arguments = args.substring(startCommand.length()).trim();

        for (Command command : getCommands())
        {
            for (String alias : command.getAlias())
            {
                if (startCommand.equals(getPrefix() + alias))
                {
                    command.onTrigger(arguments);
                    found = true;
                }
            }
        }

        if (!found)
        {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(ChatFormatting.GOLD +  "Unknown command"));
        }
    }

    public ArrayList<Command> getCommands()
    {
        return commands;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }
}
