package thefellas.safepoint.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.DoubleSetting;
import thefellas.safepoint.settings.impl.FloatSetting;

@ModuleInfo(name = "ElytraFly", description = "Makes flying with an elytra easier", category = Module.Category.Movement)
public class ElytraFly extends Module {

    FloatSetting speed = new FloatSetting("Speed", 1, 1, 3, this);
    DoubleSetting Upspeed = new DoubleSetting("Up-Speed", 1.0, 0.1, 3.0, this);
    DoubleSetting Downspeed = new DoubleSetting("Down-Speed", 1.0, 0.1, 3.0, this);

    public static void strafe(float f) {
        if (!isMoving()) {
            return;
        }
        double d = getDirection();
        mc.player.motionX = -Math.sin(d) * (double)f;
        mc.player.motionZ = Math.cos(d) * (double)f;
    }
    public static double getDirection() {
        float f = mc.player.rotationYaw;
        if (mc.player.moveForward < 0.0f) {
            f += 180.0f;
        }
        float f2 = 1.0f;
        if (mc.player.moveForward < 0.0f) {
            f2 = -0.5f;
        } else if (mc.player.moveForward > 0.0f) {
            f2 = 0.5f;
        }
        if (mc.player.moveStrafing > 0.0f) {
            f -= 90.0f * f2;
        }
        if (mc.player.moveStrafing < 0.0f) {
            f += 90.0f * f2;
        }
        return Math.toRadians(f);
    }
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
        if (mc.player.isElytraFlying()) {
            float yaw = (float) Math.toRadians(ElytraFly.mc.player.rotationYaw);
            double sped = 10 / 10.0;

            mc.player.setVelocity(0, 0, 0);
            if (mc.player.movementInput.forwardKeyDown || mc.player.movementInput.leftKeyDown || mc.player.movementInput.rightKeyDown || mc.player.movementInput.backKeyDown) {
                //   mc.player.motionX = (double) MathHelper.sin((float) yaw) * -sped;
                //   mc.player.motionZ = (double) MathHelper.cos((float) yaw) * sped;
                strafe(speed.getValue());
            }
            if (mc.player.movementInput.jump) {
                mc.player.motionY = Upspeed.getValue();
            } else if (mc.player.movementInput.sneak) {
                mc.player.motionY = -Downspeed.getValue();
            } else {
                mc.player.motionY = 0;
            }

        }
    }

    public static boolean isMoving() {
        return mc.player != null && (mc.player.movementInput.moveForward != 0.0f || mc.player.movementInput.moveStrafe != 0.0f);
    }
}
