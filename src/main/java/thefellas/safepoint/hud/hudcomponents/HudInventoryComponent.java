package thefellas.safepoint.hud.hudcomponents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.hud.HudModule;
import thefellas.safepoint.modules.core.AC_ClickGui;
import thefellas.safepoint.utils.RenderUtil;

import java.awt.*;

public class HudInventoryComponent extends HudModule {
    int dragX;
    int dragY;
    boolean isDragging;

    public HudInventoryComponent() {
        super("Inventory");
        renderX = 0;
        renderY = 0;
    }

    public void dragScreen(int mouseX, int mouseY) {
        if (!isDragging)
            return;
        renderX = dragX + mouseX;
        renderY = dragY + mouseY;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        dragScreen(mouseX, mouseY);
        if (getValue()) {
            if (isInsideDragField(mouseX, mouseY)) {
                RenderUtil.drawRect(renderX, renderY, renderX + Safepoint.mc.fontRenderer.getStringWidth("SafePoint.club"), renderY + Safepoint.mc.fontRenderer.FONT_HEIGHT, new Color(0, 0, 0, 100).getRGB());
                RenderUtil.drawRect(renderX + Safepoint.mc.fontRenderer.getStringWidth("SafePoint.club") + 3, renderY - 7, renderX + Safepoint.mc.fontRenderer.getStringWidth("SafePoint.club") + 3 + Safepoint.mc.fontRenderer.getStringWidth("renderX: " + renderX + " renderY: " + renderY), renderY - 7 + Safepoint.mc.fontRenderer.FONT_HEIGHT, new Color(0, 0, 0, 100).getRGB());
                Safepoint.mc.fontRenderer.drawStringWithShadow("renderX: " + renderX + " renderY: " + renderY, renderX + Safepoint.mc.fontRenderer.getStringWidth("SafePoint.club") + 3, renderY - 7, -1);
            }
            drawText();
        }
    }

    public void drawText() {

        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        Gui.drawRect(renderX - 2, renderY - 2, renderX - 1, renderY + 58, new Color(64,41,213).getRGB());
        Gui.drawRect(renderX + 177, renderY - 1, renderX + 178, renderY+ 57, new Color(64,41,213, 255).getRGB());
        Gui.drawRect(renderX + 177, renderY - 2, renderX + 178, renderY + 58, new Color(124,9,77, 255).getRGB());
        RenderUtil.drawGradient(renderX -2, renderY - 3, renderX + 178, renderY - 2, new Color(64,41,213,255).getRGB(), new Color(124,9,77, 255).getRGB());
        RenderUtil.drawGradient(renderX - 1, renderY + 57, renderX + 177, renderY + 58, new Color(64,41,213,255).getRGB(), new Color(124,9,77, 255).getRGB());
        RenderUtil.drawGradient(renderX - 1, renderY - 2, renderX + 177, renderY + 57, new Color(10,10,10,155).getRGB(), new Color(10,10,10,55).getRGB());
        for (int i = 0; i < 27; i++)
        {
            ItemStack item_stack = Minecraft.getMinecraft().player.inventory.mainInventory.get(i + 9);
            int item_position_x = (int) renderX + (i % 9) * 20;
            int item_position_y = (int) renderY + (i / 9) * 20;
            Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(item_stack, item_position_x, item_position_y);
            Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, item_stack, item_position_x, item_position_y, null);
        }
        Minecraft.getMinecraft().getRenderItem().zLevel = - 5.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public boolean isInsideDragField(int mouseX, int mouseY) {
        return (mouseX > renderX && mouseX < renderX + Safepoint.mc.fontRenderer.getStringWidth("SafePoint.club")) && (mouseY > renderY && mouseY < renderY + Safepoint.mc.fontRenderer.FONT_HEIGHT);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isInsideDragField(mouseX, mouseY)) {
            dragX = renderX - mouseX;
            dragY = renderY - mouseY;
            isDragging = true;
        }
        if (mouseButton == 0 && isInside(mouseX, mouseY))
            value = !value;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0)
            isDragging = false;
    }



}
