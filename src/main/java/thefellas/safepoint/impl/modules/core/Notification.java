package thefellas.safepoint.impl.modules.core;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.core.event.events.DeathEvent;
import thefellas.safepoint.core.event.events.ModuleToggleEvent;
import thefellas.safepoint.core.initializers.NotificationManager;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.settings.impl.IntegerSetting;

@ModuleInfo(name = "Notification", description = "Displays a notification when a module is toggled", category = Module.Category.Core)
public class Notification extends Module {
    static Notification INSTANCE = new Notification();
    public IntegerSetting speed = new IntegerSetting("Speed", 1, 1, 10, this);
    public IntegerSetting time = new IntegerSetting("Time", 1, 1, 10, this);
    BooleanSetting deaths = new BooleanSetting("Deaths", true, this);

    @SubscribeEvent
    public void onModuleEnableEvent(ModuleToggleEvent.Enable event){
        NotificationManager.sendMessage(event.getModule().name, ChatFormatting.GREEN + "Enabled");
    }

    @SubscribeEvent
    public void onModuleDisableEvent(ModuleToggleEvent.Disable event){
        NotificationManager.sendMessage(event.getModule().name, ChatFormatting.RED + "Disabled");
    }

    @SubscribeEvent
    public void onDeathEvent(DeathEvent event){

    }


    @Override
    public void initializeModule() {
        setInstance();
    }

    public static Notification getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Notification();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

}
