package thefellas.safepoint.modules.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.utils.RenderUtil;

@ModuleInfo(name = "ItemESP", description = "Item ESP hack", category = Module.Category.Visual)
public class ItemESP extends Module {
    AxisAlignedBB box;

    @Override
    public void onWorldRender() {
        if(nullCheck()) return;
        if (!this.isEnabled()) return;
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityItem) {
                box = new AxisAlignedBB(
                        entity.getEntityBoundingBox().minX
                                - 0.05
                                - entity.posX
                                + ((float) ((double) ((float) entity.lastTickPosX) + (entity.posX - entity.lastTickPosX) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                .getRenderManager().viewerPosX),
                        entity.getEntityBoundingBox().minY
                                - entity.posY
                                + ((float) ((double) ((float) entity.lastTickPosY) + (entity.posY - entity.lastTickPosY) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                .getRenderManager().viewerPosY),
                        entity.getEntityBoundingBox().minZ
                                - 0.05
                                - entity.posZ
                                + ((float) ((double) ((float) entity.lastTickPosZ) + (entity.posZ - entity.lastTickPosZ) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                .getRenderManager().viewerPosZ),
                        entity.getEntityBoundingBox().maxX
                                + 0.05
                                - entity.posX
                                + ((float) ((double) ((float) entity.lastTickPosX) + (entity.posX - entity.lastTickPosX) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                .getRenderManager().viewerPosX),
                        entity.getEntityBoundingBox().maxY
                                + 0.1
                                - entity.posY
                                + ((float) ((double) ((float) entity.lastTickPosY) + (entity.posY - entity.lastTickPosY) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                .getRenderManager().viewerPosY),
                        entity.getEntityBoundingBox().maxZ
                                + 0.05
                                - entity.posZ
                                + ((float) ((double) ((float) entity.lastTickPosZ) + (entity.posZ - entity.lastTickPosZ) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                .getRenderManager().viewerPosZ));

                RenderUtil.FillLine(entity, box);
            }
        }
    }
}
