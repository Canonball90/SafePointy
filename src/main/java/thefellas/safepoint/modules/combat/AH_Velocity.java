package thefellas.safepoint.modules.combat;

import thefellas.safepoint.event.events.ReceivePacketEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.FloatSetting;
import net.minecraftforge.common.*;
import net.minecraft.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;

@ModuleInfo(name = "AH_Velocity", description = "Anti-Hit Velocity", category = Module.Category.Combat)
public class AH_Velocity extends Module {

    FloatSetting horizontal = new FloatSetting("Horizontal", 0.0f, 0.0f, 1.0f,this);
    FloatSetting vertical = new FloatSetting("Vertical", 0.0f, 0.0f, 1.0f,this);

    @Override
    public void onEnable() {
        super.onEnable();
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    @SubscribeEvent
    public void onReceivePacket(final ReceivePacketEvent event) {
        if (Minecraft.getMinecraft().player == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketEntityVelocity) {
            final SPacketEntityVelocity velPacket = (SPacketEntityVelocity)event.getPacket();
            if (velPacket.getEntityID() == Minecraft.getMinecraft().player.getEntityId()) {
                if (this.horizontal.getValue() != 0.0f) {
                    Minecraft.getMinecraft().player.motionX = velPacket.getMotionX() * (double)this.horizontal.getValue() / 8000.0;
                    Minecraft.getMinecraft().player.motionZ = velPacket.getMotionZ() * (double)this.horizontal.getValue() / 8000.0;
                }
                if (this.vertical.getValue() != 0.0f) {
                    Minecraft.getMinecraft().player.motionY = velPacket.getMotionY() * (double)this.vertical.getValue() / 8000.0;
                }
                event.setCanceled(true);
            }
        }
        if (event.getPacket() instanceof SPacketExplosion) {
            final SPacketExplosion expPacket = (SPacketExplosion)event.getPacket();
            final Explosion explosion = new Explosion((World)Minecraft.getMinecraft().world, (Entity)null, expPacket.getX(), expPacket.getY(), expPacket.getZ(), expPacket.getStrength(), expPacket.getAffectedBlockPositions());
            explosion.doExplosionB(true);
            if (this.horizontal.getValue() != 0.0f) {
                final EntityPlayerSP player = Minecraft.getMinecraft().player;
                player.motionX += expPacket.getMotionX() * this.horizontal.getValue();
                final EntityPlayerSP player2 = Minecraft.getMinecraft().player;
                player2.motionZ += expPacket.getMotionZ() * this.horizontal.getValue();
            }
            if (this.vertical.getValue() != 0.0f) {
                final EntityPlayerSP player3 = Minecraft.getMinecraft().player;
                player3.motionY += expPacket.getMotionY() * this.vertical.getValue();
            }
            event.setCanceled(true);
        }
    }
}
