package thefellas.safepoint.ui.hud;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.modules.core.AC_ClickGui;
import thefellas.safepoint.ui.clickgui.ClickGui;
import thefellas.safepoint.utils.RenderUtil;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;

public class HudWindow extends GuiScreen {
    static HudWindow INSTANCE = new HudWindow();
    int x;
    int y;
    int w;
    int h;
    boolean isDragging;
    int dragX;
    int dragY;

    ArrayList<HudModule> hudModules = new ArrayList<>();

    public HudWindow() {
        x = 10;
        y = 10;
        w = 150;
        h = 10;
        setInstance();
        load();
    }

    public void dragScreen(int mouseX, int mouseY) {
        if (!isDragging)
            return;
        x = dragX + mouseX;
        y = dragY + mouseY;
    }


    public static HudWindow getInstance() {
        if (INSTANCE == null)
            INSTANCE = new HudWindow();
        return INSTANCE;
    }

    void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        dragScreen(mouseX, mouseY);
        RenderUtil.drawRect(x, y, x + w, y + h, AC_ClickGui.getInstance().color.getColor().getRGB());
        Safepoint.mc.fontRenderer.drawStringWithShadow("Hud Editor", x + (w / 2f) - (Safepoint.mc.fontRenderer.getStringWidth("Hud Editor") / 2f), y + (h / 2f) - (Safepoint.mc.fontRenderer.FONT_HEIGHT / 2f), -1);
        if (isInsideCloseButton(mouseX, mouseY))
            RenderUtil.drawRect(x + w - Safepoint.mc.fontRenderer.getStringWidth("x") - 4, y, x + w, y + h, new Color(0, 0, 0, 100).getRGB());
        Safepoint.mc.fontRenderer.drawStringWithShadow("x", x + w - Safepoint.mc.fontRenderer.getStringWidth("x") - 2, y + (h / 2f) - (Safepoint.mc.fontRenderer.FONT_HEIGHT / 2f), -1);

        if (isInsideCloseButton(mouseX, mouseY)) {
            RenderUtil.drawRect(mouseX + 5, mouseY, mouseX + 5 + Safepoint.mc.fontRenderer.getStringWidth("Left Click: Close | Right Click: Return"), mouseY + 10, new Color(0, 0, 0, 100).getRGB());
            Safepoint.mc.fontRenderer.drawStringWithShadow("Left Click: Close | Right Click: Return", mouseX + 5, mouseY + 1, -1);
        }
        int yIncrease = y;
        for (HudModule hudModule : hudModules) {
            hudModule.setX(x);
            hudModule.setY(yIncrease += h);
            hudModule.setHeight(10);
            hudModule.setWidth(w);
            hudModule.drawScreen();
            if(hudModule.getValue())
                hudModule.render(mouseX, mouseY, partialTicks);
        }
    }

    public void load(){
        hudModules.addAll(HudComponentInitializer.getInstance().getHudModules());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isInside(mouseX, mouseY) && !isInsideCloseButton(mouseX, mouseY)) {
            dragX = x - mouseX;
            dragY = y - mouseY;
            isDragging = true;
        }
        if (mouseButton == 0 && isInsideCloseButton(mouseX, mouseY))
            mc.displayGuiScreen(null);

        if (mouseButton == 1 && isInsideCloseButton(mouseX, mouseY))
            mc.displayGuiScreen(ClickGui.getInstance());

        hudModules.forEach(hudModules -> hudModules.mouseClicked(mouseX, mouseY, mouseButton));
    }

    boolean isInsideCloseButton(int mouseX, int mouseY) {
        return (mouseX > x + w - Safepoint.mc.fontRenderer.getStringWidth("x") - 4 && mouseX < x + w) && (mouseY > y && mouseY < y + h);
    }

    boolean isInside(int mouseX, int mouseY) {
        return (mouseX > x && mouseX < x + w) && (mouseY > y && mouseY < y + h);
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0)
            isDragging = false;

        hudModules.forEach(hudModules -> hudModules.mouseReleased(mouseX, mouseY, releaseButton));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
