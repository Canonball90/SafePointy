package thefellas.safepoint;

import security.HWIDManger;
import security.JSON;
import security.WebhookUtils;
import thefellas.safepoint.event.EventListener;
import thefellas.safepoint.hud.HudComponentInitializer;
import thefellas.safepoint.initializers.CommandInitializer;
import thefellas.safepoint.initializers.ConfigInitializer;
import thefellas.safepoint.initializers.FriendInitializer;
import thefellas.safepoint.modules.ModuleInitializer;
import thefellas.safepoint.settings.SettingInitializer;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

public class Safepoint {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static Safepoint INSTANCE = new Safepoint();
    public static String HWIDUrl = "https://pastebin.com/raw/tiK60WnJ";

    public static ConfigInitializer configInitializer;
    public static EventListener eventListener;
    public static ModuleInitializer moduleInitializer;
    public static SettingInitializer settingInitializer;
    public static FriendInitializer friendInitializer;
    public static HudComponentInitializer hudComponentInitializer;
    public static CommandInitializer commandInitializer;
    public static HWIDManger hwidManager;

    public void init() {
        Display.setTitle("Safepoint 2.0");
        settingInitializer = new SettingInitializer();
        eventListener = new EventListener();
        eventListener.init(true);
        moduleInitializer = new ModuleInitializer();
        friendInitializer = new FriendInitializer();
        hudComponentInitializer = new HudComponentInitializer();
        configInitializer = new ConfigInitializer();
        configInitializer.init();
        commandInitializer = new CommandInitializer();
        hwidManager = new HWIDManger(HWIDUrl);
        JSON.parseJson();
    }
}
