package thefellas.safepoint.impl.modules.movement;

import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

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
