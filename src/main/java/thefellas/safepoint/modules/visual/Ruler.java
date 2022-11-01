package thefellas.safepoint.modules.visual;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.ColorSetting;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;
import thefellas.safepoint.utils.RenderUtil;

import java.awt.*;

@ModuleInfo(name = "Ruler", description = "Draws a ruler on your screen", category = Module.Category.Visual)
public class Ruler extends Module {

    ColorSetting boxColor = new ColorSetting("Color", new Color(255, 255, 255, 255), this);
    public BlockPos rulerBlock = null;

    @SubscribeEvent
    public void onClick(InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButtonState()) {
            if (Mouse.isButtonDown(0)) {
                if (!mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.BLOCK)) return;
                if (mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock() == Blocks.AIR) return;
                if (rulerBlock == null) {
                    rulerBlock = mc.objectMouseOver.getBlockPos();
                    mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString("[SafePoint] " + "Block set!"), 1);
                } else {
                    int blocksBetween = (int) rulerBlock.getDistance(mc.objectMouseOver.getBlockPos().getX(), mc.objectMouseOver.getBlockPos().getY(), mc.objectMouseOver.getBlockPos().getZ());
                    mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString("[SafePoint] " + "distance: " + blocksBetween), 1);
                    rulerBlock = null;
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (nullCheck()) return;
        if (rulerBlock == null) return;
        AxisAlignedBB box = mc.world.getBlockState(rulerBlock).getSelectedBoundingBox(mc.world, rulerBlock).offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
        RenderUtil.prepare();
        RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, boxColor.getValue().getRed() / 255f, boxColor.getValue().getGreen() / 255f, boxColor.getValue().getBlue() / 255f, boxColor.getValue().getAlpha() / 255f);
        RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, boxColor.getValue().getRed() / 255f, boxColor.getValue().getGreen() / 255f, boxColor.getValue().getBlue() / 255f, 1f);
        RenderUtil.release();
    }
}
