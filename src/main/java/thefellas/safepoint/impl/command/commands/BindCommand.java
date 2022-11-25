package thefellas.safepoint.impl.command.commands;

import org.lwjgl.input.Keyboard;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.command.Command;
import thefellas.safepoint.core.initializers.NotificationManager;
import thefellas.safepoint.impl.modules.Module;

import java.util.List;

public class BindCommand extends Command {
    public BindCommand() {
        super("bind", "Allows You To Bind Modules To A Key", "bind" + " " + "[module]" + " " + "[key]");
    }

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 2) {
            for (Module module : Safepoint.moduleInitializer.getModuleList()) {
                if (module.getName().equalsIgnoreCase(args.get(0))) {
                    if (args.get(0).isEmpty()) {
                        NotificationManager.sendMessage("Error", "Please Only Enter One Character");
                        return;
                    }
                    String bind = args.get(1);
                    int key = Keyboard.getKeyIndex(bind.toUpperCase());
                    if (key == 0) {
                        NotificationManager.sendMessage("Error", "Unknown Keybind");
                        return;
                    }
                    module.setKeyBind(key);
                    NotificationManager.sendMessage("Module Binded", module.getName() + " Has Been Bound To " + Keyboard.getKeyName(module.getKeyBind()));
                }
            }
        } else {
            NotificationManager.sendMessage("Error", getSyntax());
        }
    }
}
