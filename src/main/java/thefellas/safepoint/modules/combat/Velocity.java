package thefellas.safepoint.modules.combat;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.FloatSetting;

@ModuleInfo(name = "Velocity", description = "Prevents knockback", category = Module.Category.Combat)
public class Velocity extends Module {

    FloatSetting horizontal = new FloatSetting("Horizontal", 0.0f, 0.0f, 100.0f, this);
    FloatSetting vertical = new FloatSetting("Vertical", 0.0f, 0.0f, 100.0f, this);

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
        if (Velocity.mc.player.hurtTime == Velocity.mc.player.maxHurtTime && Velocity.mc.player.maxHurtTime > 0) {
            Velocity.mc.player.motionX *= (double)(horizontal.getValue() / 100.0f);
            Velocity.mc.player.motionY *= (double)(vertical.getValue() / 100.0f);
            Velocity.mc.player.motionZ *= (double)(horizontal.getValue() / 100.0f);
        }
    }
}
