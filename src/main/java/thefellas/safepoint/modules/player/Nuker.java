package thefellas.safepoint.modules.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.EnumSetting;
import thefellas.safepoint.settings.impl.IntegerSetting;

import java.util.Arrays;

@ModuleInfo(name = "Nuker", description = "", category = Module.Category.Misc)
public class Nuker extends Module {
    IntegerSetting rad = new IntegerSetting("Radius", 4, 0, 6, this);
    EnumSetting breakMode = new EnumSetting("BreakMode", "Packet", Arrays.asList("Packet", "Creative"),this);

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e) {
        int dist = (int) rad.getValue();
        for (int x = -dist; x < dist; x++) {
            for (int y = dist + 1; y > -dist + 1; y--) {
                for (int z = -dist; z < dist; z++) {

                    double xBlock = (mc.player.posX + x);
                    double yBlock = (mc.player.posY + y);
                    double zBlock = (mc.player.posZ + z);

                    BlockPos blockPos = new BlockPos(xBlock, yBlock, zBlock);
                    Block block = mc.world.getBlockState(blockPos).getBlock();

                    if (block != Blocks.AIR && block.getBlockHardness(block.getBlockState().getBaseState(), mc.world, blockPos) != -1) {

                        if (breakMode.getValue().equals("Packet")) {
                            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        } else {
                            // only works in creative
                            mc.playerController.clickBlock(blockPos, EnumFacing.NORTH);
                            mc.playerController.updateController();
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                        }
                    }
                }
            }
        }
    }
}
