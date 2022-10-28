package thefellas.safepoint.hud.hudcomponents;

import thefellas.safepoint.Safepoint;
import thefellas.safepoint.hud.HudModule;
import thefellas.safepoint.modules.core.AC_ClickGui;
import thefellas.safepoint.utils.RenderUtil;

import java.awt.*;

public class HudTargetComponent extends HudModule {
    int dragX;
    int dragY;
    boolean isDragging;

    public HudTargetComponent() {
        super("Target");
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
        Safepoint.mc.fontRenderer.drawStringWithShadow("SafePoint.club", renderX, renderY, AC_ClickGui.getInstance().color.getColor().getRGB());
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
