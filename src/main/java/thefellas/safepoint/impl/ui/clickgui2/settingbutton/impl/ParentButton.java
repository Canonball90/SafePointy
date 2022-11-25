package thefellas.safepoint.impl.ui.clickgui2.settingbutton.impl;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.modules.core.AC_ClickGui;
import thefellas.safepoint.impl.settings.Setting;
import thefellas.safepoint.impl.settings.impl.ParentSetting;
import thefellas.safepoint.impl.ui.clickgui2.settingbutton.Button;
import thefellas.safepoint.core.utils.RenderUtil;

import java.awt.*;

public class ParentButton extends Button {
    Setting setting;
    ParentSetting parentSetting;

    public ParentButton(Setting setting, ParentSetting parentSetting) {
        super(setting);
        this.setting = setting;
        this.parentSetting = parentSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(x - 2, y, x + width + 2, y + height, new Color(55,55,55, 255).getRGB());
        RenderUtil.drawRect(x, y, x + width, y + height, AC_ClickGui.getInstance().color.getColor().getRGB());
        if (isInside(mouseX, mouseY))
            RenderUtil.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 100).getRGB());
        Safepoint.mc.fontRenderer.drawStringWithShadow(parentSetting.isOpen ? "-" : "+", x + width - Safepoint.mc.fontRenderer.getStringWidth(parentSetting.isOpen ? "-" : "+") - 2, y + (height / 2f) - (Safepoint.mc.fontRenderer.FONT_HEIGHT / 2f), -1);
        Safepoint.mc.fontRenderer.drawStringWithShadow(parentSetting.getName(), x + 2, y + (height / 2f) - (Safepoint.mc.fontRenderer.FONT_HEIGHT / 2f), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 1 && isInside(mouseX, mouseY)) {
            parentSetting.isOpen = !parentSetting.isOpen;
            Safepoint.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }
}
