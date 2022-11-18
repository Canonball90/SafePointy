package thefellas.safepoint.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "Sprint", category = Module.Category.Movement, description = "Auto Sprint")
public class AD_Sprint extends Module {


    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if(nullCheck()) return;
        if (mc.player.moveForward > 0 && !mc.player.collidedHorizontally) {
            mc.player.setSprinting(true);
        }
    }
}
