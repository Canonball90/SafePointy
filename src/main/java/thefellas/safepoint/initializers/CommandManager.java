package thefellas.safepoint.initializers;

import thefellas.safepoint.command.Command;
import thefellas.safepoint.command.commands.SayCommand;

import java.util.*;

public class CommandManager
{
    private String prefix;
    public final List<Command> COMMAND_LIST;

    public CommandManager() {
        this.prefix = ".";
        this.COMMAND_LIST = new ArrayList<Command>();
    }

    public void close() {
        this.COMMAND_LIST.clear();
    }

    public void init() {
        COMMAND_LIST.add(new SayCommand());
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }
}