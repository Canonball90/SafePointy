package thefellas.safepoint.modules.visual;

import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.IntegerSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.utils.RenderUtil;

@ModuleInfo(name = "SignNametags", description = "Shows the text on signs", category = Module.Category.Visual)
public class SignNametags extends Module {

    IntegerSetting scale = new IntegerSetting("Scale", 2, 1, 5, this);

    @SubscribeEvent
    public void onUpdate(RenderGameOverlayEvent.Text event) {
        if(nullCheck()) return;
        for (TileEntity tile : mc.world.loadedTileEntityList) {
            if (tile instanceof TileEntitySign) {
                TileEntitySign sign = (TileEntitySign) tile;
                // if (sign.getIsEditable()) continue;
                double deltaX = MathHelper.clampedLerp(sign.getPos().getX(), sign.getPos().getX(), event.getPartialTicks());
                double deltaY = MathHelper.clampedLerp(sign.getPos().getY(), sign.getPos().getY(), event.getPartialTicks());
                double deltaZ = MathHelper.clampedLerp(sign.getPos().getZ(), sign.getPos().getZ(), event.getPartialTicks());
                Vec3d projection = RenderUtil.toScaledScreenPos(new Vec3d(deltaX, deltaY, deltaZ).add(0, 0.5f, -1));
                int yOffset = 0;
                GlStateManager.pushMatrix();
                GlStateManager.translate(projection.x, projection.y, 0);
                GlStateManager.scale(scale.getValue(), scale.getValue(), 0);
                for(ITextComponent component : sign.signText) {
                    String line = component.getFormattedText();
                    if (line.equals("") || line.equals(" ")) continue;
                    mc.fontRenderer.drawStringWithShadow(line, -(mc.fontRenderer.getStringWidth(line) / 2f), -(mc.fontRenderer.FONT_HEIGHT) + yOffset, -1);
                    yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1;
                }
                GlStateManager.popMatrix();
            }
        }
    }
}
