package thefellas.safepoint.ui.hud.hudcomponents;

import thefellas.safepoint.Safepoint;
import thefellas.safepoint.SafepointMod;
import thefellas.safepoint.ui.hud.HudModule;
import thefellas.safepoint.modules.core.AC_ClickGui;
import thefellas.safepoint.utils.RenderUtil;
import thefellas.safepoint.utils.TimerUtil;

import java.awt.*;

public class HudWatermarkComponent extends HudModule {
    int dragX;
    int dragY;
    boolean isDragging;
    TimerUtil timer = new TimerUtil();

    public HudWatermarkComponent() {
        super("Watermark");
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
        String wate = " Safepoint.club | ";
    if(AC_ClickGui.getInstance().Sta.getValue()) {
        if (timer.hasTimeElapsed(0, false)) {
            wate = " S | ";
        }
        if (timer.hasTimeElapsed(500, false)) {
            wate = " Sa | ";
        }
        if (timer.hasTimeElapsed(1000, false)) {
            wate = " Saf | ";
        }
        if (timer.hasTimeElapsed(1500, false)) {
            wate = " Safe | ";
        }
        if (timer.hasTimeElapsed(2000, false)) {
            wate = " Safep | ";
        }
        if (timer.hasTimeElapsed(2500, false)) {
            wate = " Safepo | ";
        }
        if (timer.hasTimeElapsed(3000, false)) {
            wate = " Safepoi | ";
        }
        if (timer.hasTimeElapsed(3500, false)) {
            wate = " Safepoin | ";
        }
        if (timer.hasTimeElapsed(4000, false)) {
            wate = " Safepoint | ";
        }
        if (timer.hasTimeElapsed(5000, false)) {
            wate = " Safepoint. | ";
        }
        if (timer.hasTimeElapsed(5500, false)) {
            wate = " Safepoint.c | ";
        }
        if (timer.hasTimeElapsed(6000, false)) {
            wate = " Safepoint.cl | ";
        }
        if (timer.hasTimeElapsed(6500, false)) {
            wate = " Safepoint.clu | ";
        }
        if (timer.hasTimeElapsed(7000, false)) {
            wate = " Safepoint.club | ";
            timer.reset();
        }
    }
        else
        {
            timer.reset();
        }
        String water = wate + " v" + SafepointMod.VERSION;

        Safepoint.mc.fontRenderer.drawStringWithShadow(water, renderX, renderY, -1);
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
