package thefellas.safepoint.impl.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.core.utils.MathUtil;

@ModuleInfo(name = "Strafe", description = "Allows you to strafe while jumping", category = Module.Category.Movement)
public class AB_Strafe extends Module {

    int delay = 0;
/*
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
*/
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
     if(nullCheck()) return;

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
