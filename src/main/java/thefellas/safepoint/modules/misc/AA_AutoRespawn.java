package thefellas.safepoint.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "AutoRespawn", description = "Automatioally respawns you",category = Module.Category.Misc)
public class AA_AutoRespawn extends Module {

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent e) {
        if(nullCheck()) return;
        if (mc.player.isDead){
            mc.player.respawnPlayer();
        }
    }
}

