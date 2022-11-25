package thefellas.safepoint.impl.modules.movement;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "TunnelSpeed", description = "TunnelSpeed", category = Module.Category.Movement)
public class TunnelSpeed extends Module {

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY + 2.0, mc.player.posZ);
        BlockPos pos2 = new BlockPos(mc.player.posX, mc.player.posY - 1.0, mc.player.posZ);
        if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR && mc.world.getBlockState(pos).getBlock() != Blocks.PORTAL && mc.world.getBlockState(pos).getBlock() != Blocks.END_PORTAL && mc.world.getBlockState(pos).getBlock() != Blocks.WATER && mc.world.getBlockState(pos).getBlock() != Blocks.FLOWING_WATER && mc.world.getBlockState(pos).getBlock() != Blocks.LAVA && mc.world.getBlockState(pos).getBlock() != Blocks.FLOWING_LAVA && mc.world.getBlockState(pos2).getBlock() != Blocks.ICE && mc.world.getBlockState(pos2).getBlock() != Blocks.FROSTED_ICE && mc.world.getBlockState(pos2).getBlock() != Blocks.PACKED_ICE && !mc.player.isInWater()) {
            float yaw = (float)Math.toRadians(mc.player.rotationYaw);
            if (mc.gameSettings.keyBindForward.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown() && mc.player.onGround) {
                mc.player.motionX -= Math.sin(yaw) * 0.15;
                mc.player.motionZ += Math.cos(yaw) * 0.15;
            }
        }
    }
}
