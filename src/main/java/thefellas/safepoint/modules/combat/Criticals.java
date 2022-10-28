package thefellas.safepoint.modules.combat;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

import java.util.Objects;

@ModuleInfo(name = "Criticals", description = "Automatically deals criticals hit", category = Module.Category.Combat)
public class Criticals extends Module {

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if(nullCheck()) return;
        if (Criticals.mc.player.isInWater() || Criticals.mc.player.isInLava()) {
            return;
        }
        if (Criticals.mc.player.onGround) {
            Criticals.mc.player.connection.sendPacket((Packet) new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.1625, Criticals.mc.player.posZ, false));
            Criticals.mc.player.connection.sendPacket((Packet) new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
            Criticals.mc.player.onCriticalHit(event.getTarget());

        }
    }
}
