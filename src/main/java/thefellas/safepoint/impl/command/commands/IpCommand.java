package thefellas.safepoint.impl.command.commands;

import net.minecraft.client.Minecraft;
import thefellas.safepoint.impl.command.Command;
import thefellas.safepoint.core.initializers.NotificationManager;

import java.util.List;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class IpCommand extends Command {
    public IpCommand() {
        super("ip", "Shows The Server IP", "ip");
    }

    @Override
    public void runCommand(List<String> args) {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.getCurrentServerData() != null) {
            final StringSelection contents = new StringSelection(mc.getCurrentServerData().serverIP);
            final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(contents, null);
            NotificationManager.sendMessage("Success", "Copied IP to clipboard");
        } else {
            NotificationManager.sendMessage("Error", "Join a server first");
        }
    }
}
