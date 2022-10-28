package thefellas.safepoint.modules.movement;

import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.utils.MathUtil;

@ModuleInfo(name = "Strafe", description = "Allows you to strafe while jumping", category = Module.Category.Movement)
public class Strafe extends Module {

    int delay = 0;

    @Override
    public void onTick() {
        Strafe.mc.player.setSprinting(true);
        ++this.delay;
        Strafe.mc.player.motionY *= 0.985;
        if (Strafe.mc.player.onGround) {
            if (Strafe.mc.gameSettings.keyBindForward.isKeyDown() || Strafe.mc.gameSettings.keyBindBack.isKeyDown() || Strafe.mc.gameSettings.keyBindLeft.isKeyDown() || Strafe.mc.gameSettings.keyBindRight.isKeyDown()) {
                Strafe.mc.player.jump();
                double[] dir = MathUtil.directionSpeed(0.6);
                Strafe.mc.player.motionX = dir[0];
                Strafe.mc.player.motionZ = dir[1];
            }
        } else {
            double[] dir = MathUtil.directionSpeed(0.26);
            Strafe.mc.player.motionX = dir[0];
            Strafe.mc.player.motionZ = dir[1];
        }
    }
}
