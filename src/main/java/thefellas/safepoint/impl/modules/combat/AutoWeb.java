package thefellas.safepoint.impl.modules.combat;

import thefellas.safepoint.core.utils.*;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.settings.impl.ColorSetting;
import thefellas.safepoint.impl.settings.impl.FloatSetting;
import thefellas.safepoint.impl.settings.impl.IntegerSetting;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleInfo(name = "AutoWeb", description = "Automatically places webs around you", category = Module.Category.Combat)
public class AutoWeb extends Module {
    IntegerSetting targetRange = new IntegerSetting("Range", 5, 1, 15, this);
    IntegerSetting placeRange = new IntegerSetting("Delay", 6, 1, 6, this);
    FloatSetting predictTicks = new FloatSetting("Predict Ticks", 1, 0, 3, this);
    BooleanSetting predict = new BooleanSetting("Predict", true, this);
    BooleanSetting packet = new BooleanSetting("Packet", false, this);
    BooleanSetting rotate = new BooleanSetting("Rotate", false, this);
    BooleanSetting onGroundOnly = new BooleanSetting("OnGround", false, this);
    BooleanSetting onMoveCancel = new BooleanSetting("MoveCancel", false, this);

    @Override
    public void onTick() {
        EntityPlayer entityPlayer = EntityUtil.getTarget(targetRange.getValue());
        if (entityPlayer == null)
            return;
        if (predict.getValue())
            entityPlayer.setEntityBoundingBox(new AxisAlignedBB(new BlockPos(EntityUtil.getPlayerPos((EntityPlayer) EntityUtil.getPredictedPosition(entityPlayer, predictTicks.getValue())))));
        BlockPos pos = EntityUtil.getPlayerPos(entityPlayer);
        if (mc.player.getDistanceSq(pos) > (placeRange.getValue() * placeRange.getValue()) || !mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR))
            return;
        if ((onGroundOnly.getValue() && !mc.player.onGround) || (onMoveCancel.getValue() && EntityUtil.isMoving()))
            return;
        int slot = InventoryUtil.getItemFromHotbar(Item.getItemFromBlock(Blocks.WEB));
        if (slot == -1)
            return;
        BlockUtil.placeBlockWithSwitch(pos, rotate.getValue(), packet.getValue(), slot);
    }
}
