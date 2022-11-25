package thefellas.safepoint.core.initializers;

import net.minecraft.client.Minecraft;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.modules.core.CustomFont;

import java.awt.*;

public class FontInitializer {

    public void draw(String text, float x, float y, int color){
        if(CustomFont.INSTANCE.isEnabled()){
            Safepoint.customFontRenderer.drawStringWithShadow(text, x, y, color);
        }else{
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x, y, color);
        }
    }

    public int getStringWidth(String text){
        int width = 0;
        if(CustomFont.INSTANCE.isEnabled()){
            Safepoint.customFontRenderer.getStringWidth(text);
        }else{
            Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
        }
        return width / 2;
    }
}
