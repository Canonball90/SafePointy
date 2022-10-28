package thefellas.safepoint.modules.core;

import com.mojang.realmsclient.gui.ChatFormatting;
import thefellas.safepoint.event.events.DeathEvent;
import thefellas.safepoint.event.events.ModuleToggleEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.BooleanSetting;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleInfo(name = "Chat Notifications", category = Module.Category.Core, description = "Send Notifications in chat when certain things happen.")
public class AA_ChatNotifications extends Module {
    public BooleanSetting modules = new BooleanSetting("Modules", false, this);
 //   public BooleanSetting totemPops = new BooleanSetting("Totem Pops", false, this);
    public BooleanSetting deaths = new BooleanSetting("Deaths", false, this);

    @SubscribeEvent
    public void onModuleEnableEvent(ModuleToggleEvent.Enable event){
        if(modules.getValue())
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString("[SafePoint] " + ChatFormatting.BOLD + event.getModule().getName() + ChatFormatting.RESET + " has been " + ChatFormatting.GREEN + "Enabled" + ChatFormatting.RESET + "."), 1);
    }

    @SubscribeEvent
    public void onModuleDisableEvent(ModuleToggleEvent.Disable event){
        if(modules.getValue())
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString("[SafePoint] " + ChatFormatting.BOLD + event.getModule().getName() + ChatFormatting.RESET + " has been " + ChatFormatting.RED + "Disabled" + ChatFormatting.RESET + "."), 1);
    }

    @SubscribeEvent
    public void onDeathEvent(DeathEvent event){
        if(deaths.getValue())
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString("[SafePoint] " + ChatFormatting.BOLD + event.getPlayer().getName() + ChatFormatting.RESET + " has just died."), 1);

    }
}
