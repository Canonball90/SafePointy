package thefellas.safepoint.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import thefellas.safepoint.command.Command;

public class SaveConfig extends Command {
    public SaveConfig(String name, String[] alias, String usage) {
        super(name, alias, usage);
    }

    @Override
    public void onTrigger(String arguments) {
        String[] split = arguments.split(" ");
        try {
            if (split[0].equals("") || split[1].equals("")) {
                printUsage();
                return;
            }
        } catch (Exception ignored) {
            printUsage();
            return;
        }
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(ChatFormatting.DARK_GRAY + "[Safepoint] "));

    }
}