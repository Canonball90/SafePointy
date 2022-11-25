package thefellas.safepoint.impl.modules.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.core.utils.BlockUtil;
import thefellas.safepoint.core.utils.InventoryUtil;
import thefellas.safepoint.core.utils.PlayerUtil;

import java.util.ArrayList;

@ModuleInfo(name = "Surround", description = "Surrounds you with obsidian", category = Module.Category.Combat)
public class AG_Surround extends Module {

    BooleanSetting rotate = new BooleanSetting("Rotate", true, this);
    BooleanSetting center = new BooleanSetting("Center", false, this);
    BooleanSetting smart = new BooleanSetting("Smart", false, this);

    private BlockPos startPos;
    private ArrayList<BlockPos> retryPos;
    double posY;
    private static final BlockPos[] surroundPos;

    @Override
    public void onEnable() {
        if (this.nullCheck()) {
            return;
        }
        this.startPos = PlayerUtil.getPlayerPos();
        this.retryPos = new ArrayList<BlockPos>();
        if (this.center.getValue()) {
            final double y = mc.player.getPosition().getY();
            double x = mc.player.getPosition().getX();
            double z = mc.player.getPosition().getZ();
            final Vec3d plusPlus = new Vec3d(x + 0.5, y, z + 0.5);
            final Vec3d plusMinus = new Vec3d(x + 0.5, y, z - 0.5);
            final Vec3d minusMinus = new Vec3d(x - 0.5, y, z - 0.5);
            final Vec3d minusPlus = new Vec3d(x - 0.5, y, z + 0.5);
            if (this.getDst(plusPlus) < this.getDst(plusMinus) && this.getDst(plusPlus) < this.getDst(minusMinus) && this.getDst(plusPlus) < this.getDst(minusPlus)) {
                x = mc.player.getPosition().getX() + 0.5;
                z = mc.player.getPosition().getZ() + 0.5;
                this.centerPlayer(x, y, z);
            }
            if (this.getDst(plusMinus) < this.getDst(plusPlus) && this.getDst(plusMinus) < this.getDst(minusMinus) && this.getDst(plusMinus) < this.getDst(minusPlus)) {
                x = mc.player.getPosition().getX() + 0.5;
                z = mc.player.getPosition().getZ() - 0.5;
                this.centerPlayer(x, y, z);
            }
            if (this.getDst(minusMinus) < this.getDst(plusPlus) && this.getDst(minusMinus) < this.getDst(plusMinus) && this.getDst(minusMinus) < this.getDst(minusPlus)) {
                x = mc.player.getPosition().getX() - 0.5;
                z = mc.player.getPosition().getZ() - 0.5;
                this.centerPlayer(x, y, z);
            }
            if (this.getDst(minusPlus) < this.getDst(plusPlus) && this.getDst(minusPlus) < this.getDst(plusMinus) && this.getDst(minusPlus) < this.getDst(minusMinus)) {
                x = mc.player.getPosition().getX() - 0.5;
                z = mc.player.getPosition().getZ() + 0.5;
                this.centerPlayer(x, y, z);
            }
        }
        this.posY = mc.player.posY;
    }

    @SubscribeEvent
    public void update(TickEvent.PlayerTickEvent event) {
        if (this.nullCheck()) {
            this.disableModule();
            return;
        }
        if (this.posY < mc.player.posY) {
            this.disableModule();
            return;
        }
        if (this.startPos != null && !this.startPos.equals((Object)PlayerUtil.getPlayerPos())) {
            this.disableModule();
            return;
        }
        if (!this.retryPos.isEmpty() && this.retryPos.size() < surroundPos.length && this.smart.getValue()) {
            for (final BlockPos pos : this.retryPos) {
                final BlockPos newPos = this.addPos(pos);
                if (BlockUtil.isPositionPlaceable(newPos, false) < 2) {
                    continue;
                }
                final int slot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
                if (slot == -1) {
                    this.disableModule();
                }
                if (!BlockUtil.placeBlock(newPos, slot, this.rotate.getValue(), this.rotate.getValue())) {
                    continue;
                }
                this.retryPos.remove(newPos);
            }
            return;
        }
        for (final BlockPos pos2 : surroundPos) {
            final BlockPos newPos2 = this.addPos(pos2);
            if (BlockUtil.isPositionPlaceable(newPos2, false) >= 2) {
                final int slot2 = InventoryUtil.findHotbarBlock(BlockObsidian.class);
                if (slot2 == -1) {
                    this.disableModule();
                }
                if (!BlockUtil.placeBlock(newPos2, slot2, this.rotate.getValue(), this.rotate.getValue())) {
                    this.retryPos.add(newPos2);
                }
            }
        }
    }

    private BlockPos addPos(final BlockPos pos) {
        final BlockPos pPos = PlayerUtil.getPlayerPos(0.2);
        return new BlockPos(pPos.getX() + pos.getX(), pPos.getY() + pos.getY(), pPos.getZ() + pos.getZ());
    }

    private double getDst(final Vec3d vec) {
        return mc.player.getPositionVector().distanceTo(vec);
    }

    private void centerPlayer(final double x, final double y, final double z) {
        mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y, z, true));
        mc.player.setPosition(x, y, z);
    }

    static {
        surroundPos = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(1, -1, 0), new BlockPos(-1, -1, 0), new BlockPos(0, -1, 1), new BlockPos(0, -1, -1), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
    }
}
