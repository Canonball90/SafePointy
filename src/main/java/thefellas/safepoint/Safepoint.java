package thefellas.safepoint;

import net.minecraftforge.common.MinecraftForge;
import security.api.KeyAuth;
import security.util.HWID;
import thefellas.safepoint.event.EventListener;
import thefellas.safepoint.event.events.onGuiOpenEvent;
import thefellas.safepoint.ui.hud.HudComponentInitializer;
import thefellas.safepoint.initializers.CommandManager;
import thefellas.safepoint.initializers.ConfigInitializer;
import thefellas.safepoint.initializers.FriendInitializer;
import thefellas.safepoint.initializers.NotificationManager;
import thefellas.safepoint.modules.ModuleInitializer;
import thefellas.safepoint.settings.SettingInitializer;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import thefellas.safepoint.utils.Shaders.Shaders;

public class Safepoint {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static Safepoint INSTANCE = new Safepoint();
    private static String url = "https://keyauth.win/api/1.1/";

    private static String ownerid = "eN68K5qktw"; // You can find out the owner id in the profile settings keyauth.com
    private static String appname = "Safepoint"; // Application name
    private static String version = "1.0"; // Application version

    public static ConfigInitializer configInitializer;
    public static EventListener eventListener;
    public static ModuleInitializer moduleInitializer;
    public static SettingInitializer settingInitializer;
    public static FriendInitializer friendInitializer;
    public static HudComponentInitializer hudComponentInitializer;
    public static NotificationManager notificationManager;
    public static CommandManager commandManager;
    public static Shaders shaders;
    private static KeyAuth keyAuth = new KeyAuth(appname, ownerid, version, url);
    private static HWID hwid;

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
        notificationManager = new NotificationManager();
        commandManager = new CommandManager();
        shaders = new Shaders( );
        MinecraftForge.EVENT_BUS.register(new onGuiOpenEvent());
        keyAuth.init();
    }
}
