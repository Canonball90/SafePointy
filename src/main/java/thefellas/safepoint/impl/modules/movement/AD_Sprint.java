package thefellas.safepoint.impl.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;

@ModuleInfo(name = "Sprint", category = Module.Category.Movement, description = "Auto Sprint")
public class AD_Sprint extends Module {

    BooleanSetting rage = new BooleanSetting("Rage", false, this);

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent e) {
        if (nullCheck()) return;
        if (rage.getValue()) {
            if (!AD_Sprint.mc.gameSettings.keyBindForward.isKeyDown() && !AD_Sprint.mc.gameSettings.keyBindBack.isKeyDown() && !AD_Sprint.mc.gameSettings.keyBindLeft.isKeyDown() && !AD_Sprint.mc.gameSettings.keyBindRight.isKeyDown() || AD_Sprint.mc.player.isSneaking() || AD_Sprint.mc.player.collidedHorizontally || (float) AD_Sprint.mc.player.getFoodStats().getFoodLevel() <= 6.0f)
                ;
            AD_Sprint.mc.player.setSprinting(true);
        } else {
            if (!AD_Sprint.mc.gameSettings.keyBindForward.isKeyDown() || AD_Sprint.mc.player.isSneaking() || AD_Sprint.mc.player.isHandActive() || AD_Sprint.mc.player.collidedHorizontally || (float) AD_Sprint.mc.player.getFoodStats().getFoodLevel() <= 6.0f || AD_Sprint.mc.currentScreen != null)
                ;
            AD_Sprint.mc.player.setSprinting(true);
        }
    }
}
