package thefellas.safepoint.impl.ui.clickgui.windows;

import net.minecraft.client.Minecraft;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.modules.core.AC_ClickGui;
import thefellas.safepoint.core.utils.RenderUtil;

import java.awt.*;

public class NotificationRenderer {
    public static final int HEIGHT = 40;
    static Minecraft mc = Minecraft.getMinecraft();

    public static void renderNotification(String title, String msg, float x, float y, int more) {
        int width = mc.fontRenderer.getStringWidth(msg) + 70;
        RenderUtil.drawRect(x, y, x + width, y + HEIGHT, new Color(0, 0, 0, 140).getRGB());
        RenderUtil.drawRect(x, y, x + 2, y + HEIGHT, AC_ClickGui.getInstance().color.getColor().getRGB());
        Safepoint.fontInitializer.draw(title, (int) ((float) x + 15), (int) (y + 9), new Color(255,255,255,255).getRGB());
        Safepoint.fontInitializer.draw(msg, (int) (x + 15), (int) (y + HEIGHT - mc.fontRenderer.FONT_HEIGHT - 8),  new Color(255,255,255,255).getRGB());
        if (more > 0) {
            Safepoint.fontInitializer.draw(more + " more", (int) (x + width - mc.fontRenderer.getStringWidth(more + " more") - 7), (int) (y + 10),  new Color(255,255,255,255).getRGB());
        }
    }

    public static float getNotificationWidth(String msg) {
        return mc.fontRenderer.getStringWidth(msg) + 70;
    }
}
