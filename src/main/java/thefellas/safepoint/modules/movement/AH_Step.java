package thefellas.safepoint.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.IntegerSetting;

@ModuleInfo(name = "Step", description = "Makes you step higher", category = Module.Category.Movement)
public class AH_Step extends Module {

    IntegerSetting height = new IntegerSetting("Height", 2, 1, 5, this);

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent e){
        if(nullCheck()) return;
        step(height.getValue());
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.stepHeight = 0.6f;
        }
        super.onDisable();
    }

    public static void step(float height) {
        if (mc.player.isOnLadder()) {
            return;
        }
        mc.player.stepHeight = height;
    }

}
