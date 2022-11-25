package thefellas.safepoint.impl.command.commands;

import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.command.Command;
import thefellas.safepoint.core.initializers.NotificationManager;
import thefellas.safepoint.impl.modules.Module;

import java.util.List;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        super("toggle", "Allows you to toggle modules", "toggle" + " " + "[module]");
    }

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 1) {
            for (Module module : Safepoint.moduleInitializer.getModuleList()) {
                if (module.getName().equalsIgnoreCase(args.get(0))) {
                    module.enableModule();
                }
            }
        } else {
            NotificationManager.sendMessage("Error", getSyntax());
        }
    }
}