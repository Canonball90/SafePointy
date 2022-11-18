package thefellas.safepoint.clickgui2.windows;

import thefellas.safepoint.Safepoint;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.core.AC_ClickGui;
import thefellas.safepoint.settings.Setting;
import thefellas.safepoint.settings.impl.ColorSetting;
import thefellas.safepoint.settings.impl.EnumSetting;
import thefellas.safepoint.utils.RenderUtil;

import java.awt.*;
import java.util.ArrayList;

public class Window {

    String name;
    int x;
    int y;
    int width;
    int height;
    Module.Category category;

    boolean isDragging;
    int dragX;
    int dragY;
    boolean isOpened;

    ArrayList<ModuleWindow> modules = new ArrayList<>();

    public Window(String name, int x, int y, int width, int height, Module.Category category) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.category = category;
        isOpened = true;
    }

    public void dragScreen(int mouseX, int mouseY) {
        if (!isDragging)
            return;
        x = dragX + mouseX;
        y = dragY + mouseY;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        dragScreen(mouseX, mouseY);
        RenderUtil.drawRect(x, y - 3, x + width, y + height, new Color(35,35,35,255).getRGB());
        Safepoint.mc.fontRenderer.drawStringWithShadow(name, x + (width / 2f) - (Safepoint.mc.fontRenderer.getStringWidth(name) / 2f), y + (height / 2f) - (Safepoint.mc.fontRenderer.FONT_HEIGHT / 2f) - 1, -1);
        if (isOpened) {
            modules.clear();
            int y = this.y;
            assert Safepoint.moduleInitializer != null;
            for (Module module : Safepoint.moduleInitializer.getCategoryModules(category)) {
                int openedHeight = 0;
                if (module.isOpened) {
                    assert Safepoint.settingInitializer != null;
                    for (Setting settingsRewrite : module.getSettings()) {
                        if (settingsRewrite.getName().equals("Enabled"))
                            continue;
                        if (settingsRewrite.isVisible())
                            openedHeight += 10;

                        if (settingsRewrite instanceof ColorSetting && settingsRewrite.isVisible()) {
                            if (((ColorSetting) settingsRewrite).isOpen()) {
                                openedHeight += 112;
                                if (((ColorSetting) settingsRewrite).isSelected())
                                    openedHeight += 10;
                            }
                        }
                        if (settingsRewrite instanceof EnumSetting)
                            if (((EnumSetting) settingsRewrite).droppedDown)
                                openedHeight += ((EnumSetting) settingsRewrite).getModes().size() * 10;
                    }
                }
                modules.add(new ModuleWindow(module.getName(), x, y += height, width, height, AC_ClickGui.getInstance().backgroundColor.getColor(), AC_ClickGui.getInstance().color.getColor(), module));
                y += openedHeight;
            }
            RenderUtil.drawOutlineRect(x, this.y + height, x + width, y + height, AC_ClickGui.getInstance().color.getColor(), 1f);
        }
        RenderUtil.drawOutlineRect(x, this.y - 3, x + width, this.y + height, AC_ClickGui.getInstance().color.getColor(), 1f);
        if (isOpened)
            modules.forEach(modules -> modules.drawScreen(mouseX, mouseY, partialTicks));
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isInside(mouseX, mouseY)) {
            dragX = x - mouseX;
            dragY = y - mouseY;
            isDragging = true;
        }
        if (mouseButton == 1 && isInside(mouseX, mouseY)) {
            isOpened = !isOpened;
            //if (AC_ClickGui.getInstance().clickSound.getValue()) Safepoint.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
           // else Safepoint.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_NOTE_FLUTE, 1.0f));
        }
        if (isOpened)
            modules.forEach(modules -> modules.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void onKeyTyped(char typedChar, int keyCode) {
        if (isOpened)
            modules.forEach(modules -> modules.onKeyTyped(typedChar, keyCode));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0)
            isDragging = false;
    }

    public void initGui() {
        if (isOpened)
            modules.forEach(ModuleWindow::initGui);
    }

    public boolean isInside(int mouseX, int mouseY) {
        return (mouseX > x && mouseX < x + width) && (mouseY > y && mouseY < y + height);
    }
}
