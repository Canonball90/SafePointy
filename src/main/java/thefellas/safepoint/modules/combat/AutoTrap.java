package thefellas.safepoint.modules.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.BooleanSetting;
import thefellas.safepoint.settings.impl.IntegerSetting;
import thefellas.safepoint.utils.BlockUtil;
import thefellas.safepoint.utils.PlayerUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "AutoTrap", description = "Traps enemies in obsidian", category = Module.Category.Combat)
public class AutoTrap extends Module {
    BooleanSetting disable = new BooleanSetting("Disable", true, this);
    IntegerSetting bpt = new IntegerSetting("Blocks Per Tick", 1, 1, 10, this);
    BooleanSetting packet = new BooleanSetting("Packet", true, this);

    private final List<Vec3d> positions = new ArrayList<>(Arrays.asList(
            new Vec3d(0, -1, -1),
            new Vec3d(1, -1, 0),
            new Vec3d(0, -1, 1),
            new Vec3d(-1, -1, 0),
            new Vec3d(0, 0, -1),
            new Vec3d(1, 0, 0),
            new Vec3d(0, 0, 1),
            new Vec3d(-1, 0, 0),
            new Vec3d(0, 1, -1),
            new Vec3d(1, 1, 0),
            new Vec3d(0, 1, 1),
            new Vec3d(-1, 1, 0),
            new Vec3d(0, 2, -1),
            new Vec3d(0, 2, 1),
            new Vec3d(0, 2, 0)
    ));

    private boolean finished;

    @Override
    public void onEnable()
    {
        finished = false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (nullCheck()) return;

        if (finished && disable.getValue()) this.disableModule();

        int blocksPlaced = 0;

        for (Vec3d position : positions)
        {
            EntityPlayer closestPlayer = getClosestPlayer();
            if (closestPlayer != null)
            {
                BlockPos pos = new BlockPos(position.add(getClosestPlayer().getPositionVector()));

                if (mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR))
                {
                    int oldSlot = mc.player.inventory.currentItem;
                    mc.player.inventory.currentItem = PlayerUtil.getSlot(Blocks.OBSIDIAN);
                    if(packet.getValue()) {
                        BlockUtil.placeBlock(pos, true);
                    }else{
                        BlockUtil.placeBlock(pos, false);
                    }
                    mc.player.inventory.currentItem = oldSlot;
                    blocksPlaced++;

                    if (blocksPlaced == bpt.getValue()) return;
                }
            }
        }
        if (blocksPlaced == 0) finished = true;
    }

    private EntityPlayer getClosestPlayer()
    {
        EntityPlayer closestPlayer = null;
        double range = 1000;
        for (EntityPlayer playerEntity : mc.world.playerEntities)
        {
            if (!playerEntity.equals(mc.player))
            {
                double distance = mc.player.getDistance(playerEntity);
                if (distance < range)
                {
                    closestPlayer = playerEntity;
                    range = distance;
                }
            }
        }
        return closestPlayer;
    }
}
