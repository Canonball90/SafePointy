package thefellas.safepoint.modules.movement;

import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.EnumSetting;
import thefellas.safepoint.settings.impl.FloatSetting;

import java.util.Arrays;

@ModuleInfo(name = "Flight", category = Module.Category.Movement, description = "Fly like a bird")
public class AF_Flight extends Module {
    public EnumSetting mode = new EnumSetting("Mode", "Static", Arrays.asList("Static", "Creative"), this);
    FloatSetting speed = new FloatSetting("Speed", 1, 1, 5, this);

    @Override
    public void onTick() {
        if(nullCheck()) return;
        if (mode.getValue().equals("Static")) {
            mc.player.noClip=true;
            mc.player.fallDistance=0;
            mc.player.onGround=false;
            mc.player.capabilities.isFlying=false;
            mc.player.motionX = 0;
            mc.player.motionY = 0;
            mc.player.motionZ = 0;
            mc.player.jumpMovementFactor = speed.getValue();
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY += speed.getValue();
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY -= speed.getValue();
            }
        }
        else if (mode.getValue().equals("Creative")) {
            mc.player.capabilities.isFlying = true;
        }
    }
    @Override
    public void onDisable() {
        mc.player.capabilities.isFlying=false;
    }
}
