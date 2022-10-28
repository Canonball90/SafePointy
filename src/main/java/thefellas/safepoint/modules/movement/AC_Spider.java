package thefellas.safepoint.modules.movement;

import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "Spider", category = Module.Category.Movement, description = "Climbs up walls")
public class AC_Spider extends Module {
    @Override
    public void onTick() {
        if(nullCheck()) return;
        if (!mc.player.collidedHorizontally) return;
        if (mc.player.motionY >= 0.2) return;
        mc.player.setVelocity(mc.player.motionX, 0.2, mc.player.motionZ);
    }
}
