package thefellas.safepoint.impl.modules.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "BlockObject", description = "Shows the blocks that you look at", category = Module.Category.Visual)
public class AK_BlockObject extends Module {
    public static BlockPos Block;
    AxisAlignedBB box = null;
    public static Entity entity;

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        RayTraceResult objectMouseOver = mc.objectMouseOver;
        try {
            if (objectMouseOver != null) {
                Block = objectMouseOver.getBlockPos();
                if (getBlock(Block) != Blocks.AIR) {
                    blockESP((Block));
                }
            }
        } catch (Exception ex) {return;}
    }

    public static void blockESP(BlockPos blockPos) {
        GL11.glPushMatrix();

        double x =
                blockPos.getX()
                        - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double y =
                blockPos.getY()
                        - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double z =
                blockPos.getZ()
                        - Minecraft.getMinecraft().getRenderManager().viewerPosZ;

        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glDepthMask(false);

        RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 66 / 255.0f, 245 / 255.0f, 218 / 255.0f, 10 / 255.0f);
        RenderGlobal.drawBoundingBox(x, y, z, x + 1.0, y + 1.0, z + 1.0, 66 / 255.0f, 245 / 255.0f, 218 / 255.0f, 255 / 255.0f);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glDepthMask(true);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void FillLine(Entity entity, AxisAlignedBB box) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);

        //RenderGlobal.renderFilledBox(box, 66 / 255.0f, 245 / 255.0f, 218 / 255.0f, 100 / 255.0f);
        RenderGlobal.drawSelectionBoundingBox(box, 66 / 255.0f, 245 / 255.0f, 218 / 255.0f, 255 / 255.0f);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static net.minecraft.block.Block getBlock(BlockPos pos) {
        try {
            return Minecraft.getMinecraft().world.getBlockState(pos).getBlock();
        } catch (NullPointerException e) {
            return null;
        }
    }
}
