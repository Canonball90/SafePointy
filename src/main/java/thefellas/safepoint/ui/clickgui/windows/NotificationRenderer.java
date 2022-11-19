package thefellas.safepoint.ui.clickgui.windows;

import net.minecraft.client.Minecraft;
import thefellas.safepoint.modules.core.AC_ClickGui;
import thefellas.safepoint.utils.RenderUtil;

import java.awt.*;

public class NotificationRenderer {
    public static final int HEIGHT = 40;
    static Minecraft mc = Minecraft.getMinecraft();

    public static void renderNotification(String title, String msg, float x, float y, int more) {
        int width = mc.fontRenderer.getStringWidth(msg) + 70;
        RenderUtil.drawRect(x, y, x + width, y + HEIGHT, new Color(0, 0, 0, 140).getRGB());
        RenderUtil.drawRect(x, y, x + 2, y + HEIGHT, AC_ClickGui.getInstance().color.getColor().getRGB());
        mc.fontRenderer.drawString(title, (int) ((float) x + 15), (int) (y + 9), new Color(255,255,255,255).getRGB());
        mc.fontRenderer.drawString(msg, (int) (x + 15), (int) (y + HEIGHT - mc.fontRenderer.FONT_HEIGHT - 8),  new Color(255,255,255,255).getRGB());
        if (more > 0) {
            mc.fontRenderer.drawString(more + " more", (int) (x + width - mc.fontRenderer.getStringWidth(more + " more") - 7), (int) (y + 10),  new Color(255,255,255,255).getRGB());
        }
    }

    public static float getNotificationWidth(String msg) {
        return mc.fontRenderer.getStringWidth(msg) + 70;
    }
}
