package thefellas.safepoint.ui.hud.hudcomponents;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.ui.hud.HudModule;
import thefellas.safepoint.modules.core.AC_ClickGui;
import thefellas.safepoint.utils.RenderUtil;

import java.awt.*;

public class HudWelcomerComponent extends HudModule {
    int dragX;
    int dragY;
    boolean isDragging;

    public HudWelcomerComponent() {
        super("Welcomer");
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
                RenderUtil.drawRect(renderX, renderY, renderX + Safepoint.mc.fontRenderer.getStringWidth("P-Client 0.1"), renderY + Safepoint.mc.fontRenderer.FONT_HEIGHT, new Color(0, 0, 0, 100).getRGB());
                RenderUtil.drawRect(renderX + Safepoint.mc.fontRenderer.getStringWidth("P-Client 0.1") + 3, renderY - 7, renderX + Safepoint.mc.fontRenderer.getStringWidth("P-Client 0.1") + 3 + Safepoint.mc.fontRenderer.getStringWidth("renderX: " + renderX + " renderY: " + renderY), renderY - 7 + Safepoint.mc.fontRenderer.FONT_HEIGHT, new Color(0, 0, 0, 100).getRGB());
                Safepoint.mc.fontRenderer.drawStringWithShadow("renderX: " + renderX + " renderY: " + renderY, renderX + Safepoint.mc.fontRenderer.getStringWidth("P-Client 0.1") + 3, renderY - 7, -1);
            }
            drawText();
        }
    }

    public void drawText() {
        Safepoint.mc.fontRenderer.drawStringWithShadow(ChatFormatting.BOLD + "Welcome to Safepoint.club, " + ChatFormatting.RESET + Minecraft.getMinecraft().player.getName().toString() + ":)", renderX, renderY, AC_ClickGui.getInstance().color.getColor().getRGB());
    }

    public boolean isInsideDragField(int mouseX, int mouseY) {
        return (mouseX > renderX && mouseX < renderX + Safepoint.mc.fontRenderer.getStringWidth("P-Client 0.1")) && (mouseY > renderY && mouseY < renderY + Safepoint.mc.fontRenderer.FONT_HEIGHT);
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
