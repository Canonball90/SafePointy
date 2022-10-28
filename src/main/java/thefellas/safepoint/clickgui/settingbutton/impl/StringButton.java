package thefellas.safepoint.clickgui.settingbutton.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.clickgui.settingbutton.Button;
import thefellas.safepoint.modules.core.AC_ClickGui;
import thefellas.safepoint.settings.Setting;
import thefellas.safepoint.settings.impl.StringSetting;
import thefellas.safepoint.utils.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ChatAllowedCharacters;

import java.awt.*;

public class StringButton extends Button {
    Setting setting;
    StringSetting stringSetting;

    public StringButton(Setting setting, StringSetting stringSetting) {
        super(setting);
        this.setting = setting;
        this.stringSetting = stringSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(x - 2, y, x + width + 2, y + height, AC_ClickGui.getInstance().backgroundColor.getColor().getRGB());
        if (isInside(mouseX, mouseY))
            RenderUtil.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 100).getRGB());
        Safepoint.mc.fontRenderer.drawStringWithShadow(stringSetting.isOpen() ? stringSetting.getName() + " " + ChatFormatting.GRAY + stringSetting.getValue() + "..." : stringSetting.getName() + " " + ChatFormatting.GRAY + stringSetting.getValue(), x + 2, y, -1);

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY) && mouseButton == 0) {
            stringSetting.setOpen(!stringSetting.isOpen());
            Safepoint.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (!stringSetting.isOpen())
            return;
        if (keyCode == 14) {
            if (stringSetting.getValue() != null && stringSetting.getValue().length() > 0)
                stringSetting.setValue(stringSetting.getValue().substring(0, stringSetting.getValue().length() - 1));
        } else if (keyCode == 28)
            stringSetting.setOpen(false);
        else if (keyCode == 27)
            stringSetting.setOpen(false);
        else if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
            stringSetting.setValue(stringSetting.getValue() + "" + typedChar);
    }
}


