package thefellas.safepoint;

import net.minecraftforge.common.MinecraftForge;
import thefellas.safepoint.core.event.EventListener;
import thefellas.safepoint.core.event.events.onGuiOpenEvent;
import thefellas.safepoint.core.initializers.*;
import thefellas.safepoint.core.utils.font.CustomFont;
import thefellas.safepoint.core.utils.font.CustomFontRenderer;
import thefellas.safepoint.impl.ui.hud.HudComponentInitializer;
import thefellas.safepoint.impl.modules.ModuleInitializer;
import thefellas.safepoint.impl.settings.SettingInitializer;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import thefellas.safepoint.core.utils.Shaders.Shaders;

import java.awt.*;

public class Safepoint {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static Safepoint INSTANCE = new Safepoint();

    public static ConfigInitializer configInitializer;
    public static EventListener eventListener;
    public static ModuleInitializer moduleInitializer;
    public static SettingInitializer settingInitializer;
    public static FriendInitializer friendInitializer;
    public static HudComponentInitializer hudComponentInitializer;
    public static NotificationManager notificationManager;
    public static CommandManager commandManager;
    public static Shaders shaders;
    public static ThreadInitializer threadInitializer;
    public static CustomFontRenderer customFontRenderer;
    public static FontInitializer fontInitializer;

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
        MinecraftForge.EVENT_BUS.register(commandManager);
        shaders = new Shaders( );
        threadInitializer = new ThreadInitializer();
        fontInitializer = new FontInitializer();
        customFontRenderer = new CustomFontRenderer(new Font("Arial", Font.PLAIN, 19), true, false);
        MinecraftForge.EVENT_BUS.register(new onGuiOpenEvent());
    }
}
