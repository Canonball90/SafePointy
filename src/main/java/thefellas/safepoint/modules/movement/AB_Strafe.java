package thefellas.safepoint.modules.movement;

import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.utils.MathUtil;

@ModuleInfo(name = "Strafe", description = "Allows you to strafe while jumping", category = Module.Category.Movement)
public class AB_Strafe extends Module {

    int delay = 0;

    @Override
    public void onTick() {
        if(nullCheck()) return;
        AB_Strafe.mc.player.setSprinting(true);
        ++this.delay;
        AB_Strafe.mc.player.motionY *= 0.985;
        if (AB_Strafe.mc.player.onGround) {
            if (AB_Strafe.mc.gameSettings.keyBindForward.isKeyDown() || AB_Strafe.mc.gameSettings.keyBindBack.isKeyDown() || AB_Strafe.mc.gameSettings.keyBindLeft.isKeyDown() || AB_Strafe.mc.gameSettings.keyBindRight.isKeyDown()) {
                AB_Strafe.mc.player.jump();
                double[] dir = MathUtil.directionSpeed(0.6);
                AB_Strafe.mc.player.motionX = dir[0];
                AB_Strafe.mc.player.motionZ = dir[1];
            }
        } else {
            double[] dir = MathUtil.directionSpeed(0.26);
            AB_Strafe.mc.player.motionX = dir[0];
            AB_Strafe.mc.player.motionZ = dir[1];
        }
    }
}
