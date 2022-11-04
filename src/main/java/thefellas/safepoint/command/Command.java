package thefellas.safepoint.command;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import thefellas.safepoint.Safepoint;

public class Command
{
    private String name;
    private String[] alias;
    private String usage;

    public Command(String name, String[] alias, String usage)
    {
        setName(name);
        setAlias(alias);
        setUsage(usage);
    }

    public void onTrigger(String arguments) {}

    public void printUsage()
    {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(ChatFormatting.GOLD +  "Usage: " + Safepoint.commandInitializer.getPrefix() + usage));
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String[] getAlias()
    {
        return alias;
    }

    public void setAlias(String[] alias)
    {
        this.alias = alias;
    }

    public String getUsage()
    {
        return usage;
    }

    public void setUsage(String usage)
    {
        this.usage = usage;
    }
}
