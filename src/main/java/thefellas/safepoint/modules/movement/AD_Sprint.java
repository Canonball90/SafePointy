package thefellas.safepoint.modules.movement;

import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "Sprint", category = Module.Category.Movement, description = "Auto Sprint")
public class AD_Sprint extends Module {

    @Override
    public void onTick() {
        if(nullCheck()) return;
        if (mc.gameSettings.keyBindForward.isPressed() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown())
            mc.player.setSprinting(true);
    }
}
