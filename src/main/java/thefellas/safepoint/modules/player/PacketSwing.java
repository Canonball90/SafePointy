package thefellas.safepoint.modules.player;

import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "PacketSwing", description = "Swings your arm without actually swinging it", category = Module.Category.Player)
public class PacketSwing extends Module {

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent e) {

    }
}
