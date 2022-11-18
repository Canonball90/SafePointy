package thefellas.safepoint.modules.visual;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "NoBob", description = "Removes bobbing from the camera", category = Module.Category.Visual)
public class NoBob extends Module {
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.player == null)
            return;

        mc.gameSettings.viewBobbing = false;

    }
}
