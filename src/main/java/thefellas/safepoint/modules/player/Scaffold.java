package thefellas.safepoint.modules.player;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.BooleanSetting;
import thefellas.safepoint.utils.BlockUtil;
import thefellas.safepoint.utils.InventoryUtil;
import thefellas.safepoint.utils.MathUtil;
import thefellas.safepoint.utils.TimerUtil;

@ModuleInfo(name = "Scaffold", description = "Automatically places blocks under you", category = Module.Category.Player)
public class Scaffold extends Module {
    private final TimerUtil timer = new TimerUtil();
    BooleanSetting rotation = new BooleanSetting("Rotate", true, this);

    public Scaffold() {
    }

    @Override
    public void onEnable() {
        this.timer.reset();
    }

    @SubscribeEvent
    public void Update(TickEvent.PlayerTickEvent e) {
        BlockPos floored_pos = new BlockPos(Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY)-1, Math.floor(mc.player.posZ) + 0.5);
        if(mc.world.getBlockState(floored_pos).getBlock()== Blocks.AIR)
        {
            if(BlockUtil.canPlaceBlock(floored_pos))
                BlockUtil.placeBlock(floored_pos, true);


        }
    }


    public void place(BlockPos posI, EnumFacing face) {
        Block block;
        BlockPos pos = posI;
        if (face == EnumFacing.UP) {
            pos = pos.add(0, -1, 0);
        } else if (face == EnumFacing.NORTH) {
            pos = pos.add(0, 0, 1);
        } else if (face == EnumFacing.SOUTH) {
            pos = pos.add(0, 0, -1);
        } else if (face == EnumFacing.EAST) {
            pos = pos.add(-1, 0, 0);
        } else if (face == EnumFacing.WEST) {
            pos = pos.add(1, 0, 0);
        }
        int oldSlot = Scaffold.mc.player.inventory.currentItem;
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Scaffold.mc.player.inventory.getStackInSlot(i);
            if (!(stack.getItem() instanceof ItemBlock) || !Block.getBlockFromItem(stack.getItem()).getDefaultState().isFullBlock())
                continue;
            newSlot = i;
            break;
        }
        if (newSlot == -1) {
            return;
        }
        boolean crouched = false;
        if (!Scaffold.mc.player.isSneaking() && BlockUtil.blackList.contains(block = Scaffold.mc.world.getBlockState(pos).getBlock())) {
            Scaffold.mc.player.connection.sendPacket(new CPacketEntityAction(Scaffold.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            crouched = true;
        }
        if (!(Scaffold.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
            Scaffold.mc.player.connection.sendPacket(new CPacketHeldItemChange(newSlot));
            Scaffold.mc.player.inventory.currentItem = newSlot;
            Scaffold.mc.playerController.updateController();
        }
        if (Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
            Scaffold.mc.player.motionX *= 0.3;
            Scaffold.mc.player.motionZ *= 0.3;
            Scaffold.mc.player.jump();
            if (this.timer.passedMs(1500L)) {
                Scaffold.mc.player.motionY = -0.28;
                this.timer.reset();
            }
        }
        if (this.rotation.getValue().booleanValue()) {
            float[] angle = MathUtil.calcAngle(Scaffold.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() - 0.5f, (float) pos.getZ() + 0.5f));
            Scaffold.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(angle[0], (float) MathHelper.normalizeAngle((int) angle[1], 360), Scaffold.mc.player.onGround));
        }
        Scaffold.mc.playerController.processRightClickBlock(Scaffold.mc.player, Scaffold.mc.world, pos, face, new Vec3d(0.5, 0.5, 0.5), EnumHand.MAIN_HAND);
        Scaffold.mc.player.swingArm(EnumHand.MAIN_HAND);
        Scaffold.mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
        Scaffold.mc.player.inventory.currentItem = oldSlot;
        Scaffold.mc.playerController.updateController();
        if (crouched) {
            Scaffold.mc.player.connection.sendPacket(new CPacketEntityAction(Scaffold.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }

}
