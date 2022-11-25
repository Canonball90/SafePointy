package thefellas.safepoint.impl.modules.player.scaffold;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
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
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.core.utils.BlockUtil;
import thefellas.safepoint.core.utils.MathUtil;
import thefellas.safepoint.core.utils.TimerUtil;
import thefellas.safepoint.impl.settings.impl.DoubleSetting;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "Scaffold", description = "Automatically places blocks under you", category = Module.Category.Player)
public class Scaffold extends Module {

    BooleanSetting rotate = new BooleanSetting("Rotate", true, this);
    BooleanSetting swing = new BooleanSetting("Swing", true, this);
    BooleanSetting Switch = new BooleanSetting("Switch", true, this);
    BooleanSetting Tower = new BooleanSetting("Tower", true, this);
    DoubleSetting speed = new DoubleSetting("Speed", 0.7, 0.0, 1.0, this);

    private List<ScaffoldBlock> blocksToRender = new ArrayList<ScaffoldBlock>();
    private BlockPos pos;
    private boolean packet = false;

    @Override
    public void onTick() {
        super.onTick();
        this.pos = new BlockPos(this.mc.player.posX, this.mc.player.posY - 1.0, this.mc.player.posZ);
        if (this.isAir(this.pos)) {
            BlockUtil.placeBlock(this.pos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet, this.mc.player.isSneaking());
            this.blocksToRender.add(new ScaffoldBlock(BlockUtil.posToVec3d(this.pos)));
        }
        if (this.swing.getValue().booleanValue()) {
            this.mc.player.isSwingInProgress = false;
            this.mc.player.swingProgressInt = 0;
            this.mc.player.swingProgress = 0.0f;
            this.mc.player.prevSwingProgress = 0.0f;
        }
        if (this.mc.player == null || this.mc.world == null) {
            return;
        }
        double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
        this.mc.player.motionX = calc[0];
        this.mc.player.motionZ = calc[1];
        if (this.Switch.getValue().booleanValue() && (this.mc.player.getHeldItemMainhand().getItem() == null || !(this.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock))) {
            for (int j = 0; j < 9; ++j) {
                if (this.mc.player.inventory.getStackInSlot(j) == null || this.mc.player.inventory.getStackInSlot(j).getCount() == 0 || !(this.mc.player.inventory.getStackInSlot(j).getItem() instanceof ItemBlock)) continue;
                this.mc.player.inventory.currentItem = j;
                break;
            }
        }
        if (this.Tower.getValue().booleanValue() && this.mc.gameSettings.keyBindJump.isKeyDown() && this.mc.player.moveForward == 0.0f && this.mc.player.moveStrafing == 0.0f && !this.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
            this.mc.player.motionY = 0.2444441;
            this.mc.player.motionZ = 0.0;
            this.mc.player.motionX = 0.0;
        }
    }

    private boolean isAir(BlockPos pos) {
        return this.mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.blocksToRender.clear();
    }
}
