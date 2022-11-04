package thefellas.safepoint.modules.visual;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.IntegerSetting;

@ModuleInfo(name = "SmallHand", category = Module.Category.Visual, description = "Makes your hand smaller.")
public class SmallHand extends Module {
    IntegerSetting multiplier = new IntegerSetting("Multiplier", 90, 1, 360, this);

    @SubscribeEvent
    public void up(TickEvent.PlayerTickEvent event) {
        if(nullCheck()) return;
        mc.player.renderArmPitch = multiplier.getValue();
    }
}
