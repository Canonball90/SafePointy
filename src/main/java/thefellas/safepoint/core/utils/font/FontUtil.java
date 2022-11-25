package thefellas.safepoint.core.utils.font;

import java.awt.GraphicsEnvironment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import thefellas.safepoint.Safepoint;

public class FontUtil
{
    private static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

    public static int getStringWidth(String text)
    {
        return customFont() ? (Safepoint.customFontRenderer.getStringWidth(text) + 3) : fontRenderer.getStringWidth(text);
    }

    public static void drawString(String text, double x, double y, int color)
    {
        if (customFont())
        {
            Safepoint.customFontRenderer.drawString(text, x, y - 1, color, false);
        }
        else
        {
            fontRenderer.drawString(text, (int) (x), (int) (y), color);
        }
    }

    public static void drawStringWithShadow(String text, double x, double y, int color)
    {
        if (customFont())
        {
            Safepoint.customFontRenderer.drawStringWithShadow(text, x, y - 1, color);
        }
        else
        {
            fontRenderer.drawStringWithShadow(text, (float) (x), (float) (y), color);
        }
    }

    public static void drawCenteredStringWithShadow(String text, float x, float y, int color)
    {
        if (customFont())
        {
            Safepoint.customFontRenderer.drawCenteredStringWithShadow(text, x, y - 1, color);
        }
        else
        {
            fontRenderer.drawStringWithShadow(text, x - fontRenderer.getStringWidth(text) / 2f, y, color);
        }
    }

    public static void drawCenteredString(String text, float x, float y, int color)
    {
        if (customFont())
        {
            Safepoint.customFontRenderer.drawCenteredString(text, x, y - 1, color);
        }
        else
        {
            fontRenderer.drawString(text, (int) (x - getStringWidth(text) / 2), (int) (y), color);
        }
    }

    public static int getFontHeight()
    {
        return customFont() ? (Safepoint.customFontRenderer.fontHeight / 2) - 1 : fontRenderer.FONT_HEIGHT;
    }

    public static boolean validateFont(String font)
    {
        for (String s : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
        {
            if (s.equals(font))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean customFont()
    {
        return true;
    }
}
