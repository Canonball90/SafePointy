package thefellas.safepoint.impl.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "ReverseStep", description = "Allows you to down step up blocks", category = Module.Category.Movement)
public class ReverseStep extends Module {

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event)
    {
        if(nullCheck() || mc.player.isDead || mc.player.collidedHorizontally || !mc.player.onGround || mc.player.isOnLadder() || mc.player.isInWater() || mc.player.isInLava() || mc.player.noClip) return;
        if (mc.player.onGround)
        {
            mc.player.motionY -= 1.0;
        }
    }
}
