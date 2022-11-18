package thefellas.safepoint.initializers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.clickgui.windows.NotificationRenderer;
import thefellas.safepoint.modules.core.Notification;

import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager {
    public static final CopyOnWriteArrayList<Notif> notif = new CopyOnWriteArrayList();
    private int y = 0;
    private int tick = 0;

    public NotificationManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void sendMessage(String title, String msg) {
        notif.add(new Notif(title, msg));
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text event) {
        if (notif.size() > 0) {
            ScaledResolution sr = event.getResolution();

            Notif n = notif.get(0);
            NotificationRenderer.renderNotification(n.title, n.msg,
                    sr.getScaledWidth() - NotificationRenderer.getNotificationWidth(n.msg) - 10,
                    sr.getScaledHeight() - y, notif.size() - 1);

            float speed = (5.0F - (Notification.getInstance().speed.getValue() - 1.5F));
            int ticks = Minecraft.getDebugFPS() * Notification.getInstance().time.getValue().intValue();
            tick++;
            if (tick > ticks) {
                if (y > 0.05) {
                    y += -y / speed;
                } else {
                    notif.remove(0);
                    tick = 0;
                    y = 0;
                }
            } else {
                y += ((NotificationRenderer.HEIGHT + 10) - y) / speed;
            }
        }
    }

    private static class Notif {
        public String title, msg;

        public Notif(String title, String msg) {
            this.title = title;
            this.msg = msg;
        }
    }
}