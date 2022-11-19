package thefellas.safepoint;

import net.minecraftforge.common.MinecraftForge;
import security.HWIDManger;
import security.JSON;
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
    public static String HWIDUrl = "https://pastebin.com/raw/9n0dgckT";

    public static ConfigInitializer configInitializer;
    public static EventListener eventListener;
    public static ModuleInitializer moduleInitializer;
    public static SettingInitializer settingInitializer;
    public static FriendInitializer friendInitializer;
    public static HudComponentInitializer hudComponentInitializer;
    public static HWIDManger hwidManager;
    public static NotificationManager notificationManager;
    public static CommandManager commandManager;
    public static Shaders shaders;

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
        hwidManager = new HWIDManger(HWIDUrl);
        notificationManager = new NotificationManager();
        commandManager = new CommandManager();
        shaders = new Shaders( );
        MinecraftForge.EVENT_BUS.register(new onGuiOpenEvent());
        JSON.parseJson();
    }
}
