package thefellas.safepoint.command;

import net.minecraft.util.text.*;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.initializers.NotificationManager;

import java.util.*;

public abstract class Command
{
    private final String name;
    private final String desc;
    private final String[] syntax;
    private final List<String> aliases;

    public Command(final String name, final String desc, final String... syntax) {
        this.aliases = new ArrayList<String>();
        this.name = name;
        this.desc = desc;
        this.syntax = syntax;
    }

    public void syntaxError() {
        NotificationManager.sendMessage("Error", "Incorrect Syntax: ");
        for (final String syntax : this.getSyntax()) {
            NotificationManager.sendMessage("",TextFormatting.YELLOW + Safepoint.commandManager.getPrefix() + syntax);
        }
    }

    public void addAliases(final String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
    }

    public abstract void onCommand(final String[] p0);

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.desc;
    }

    public String[] getSyntax() {
        return this.syntax;
    }

    public List<String> getAliases() {
        return this.aliases;
    }
}