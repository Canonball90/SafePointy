package thefellas.safepoint.impl.modules.player;

import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.settings.impl.ColorSetting;
import thefellas.safepoint.impl.settings.impl.EnumSetting;
import thefellas.safepoint.impl.settings.impl.FloatSetting;
import thefellas.safepoint.core.utils.NumUtils;
import thefellas.safepoint.core.utils.RenderUtil;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;


@ModuleInfo(name = "PacketMine", description = "Mine blocks without mining animation", category = Module.Category.Player)
public class AD_PacketMine extends Module {
    BooleanSetting pickaxeOnly = new BooleanSetting("PickaxeOnly", true, this);
    FloatSetting lineWidth = new FloatSetting("LineWidth", 2f, 0f, 4f, this);
    EnumSetting renderMode = new EnumSetting("Render Mode", "Both", Arrays.asList("Both", "Outline", "Fill"), this);
    ColorSetting color = new ColorSetting("Color", new Color(244,2,2,120), this);

    public BlockPos targetPos = null;
    public NumUtils timeBreaking = new NumUtils(0);

    @SubscribeEvent
    public void onClick(PlayerInteractEvent.LeftClickBlock event) {
        if(canBreak(event.getPos())) {
            if (pickaxeOnly.getValue() && mc.player.getHeldItemMainhand().getItem() != Items.DIAMOND_PICKAXE) return;
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), Objects.requireNonNull(event.getFace())));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
            timeBreaking.increase(1);
            targetPos = event.getPos();
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(targetPos != null) {
            AxisAlignedBB box = new AxisAlignedBB(targetPos).offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
            RenderUtil.prepare();
            GL11.glLineWidth(lineWidth.getValue());
            if(renderMode.getValue().equalsIgnoreCase("fill") || renderMode.getValue().equalsIgnoreCase("both"))
                RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, color.getValue().getRed() / 255f, color.getValue().getGreen()  / 255f, color.getValue().getBlue() / 255f, color.getValue().getAlpha() / 255f);
            if(renderMode.getValue().equalsIgnoreCase("outline") || renderMode.getValue().equalsIgnoreCase("both"))
                RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, color.getValue().getRed() / 255f, color.getValue().getGreen() / 255f, color.getValue().getBlue() / 255f, 1f);
            RenderUtil.release();
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if(targetPos != null && mc.world.getBlockState(targetPos).getBlock() == Blocks.AIR) {
            timeBreaking.zero();
            targetPos = null;
        }
    }

    @Override
    public void onDisable() {
        if(targetPos != null) {
            targetPos = null;
            timeBreaking.zero();
        }
    }

    public boolean canBreak(BlockPos pos) {
        if(mc.world.getBlockState(pos).getBlock() == Blocks.AIR) return false;
        return mc.world.getBlockState(pos).getBlockHardness(mc.world, pos) != -1;
    }
}
