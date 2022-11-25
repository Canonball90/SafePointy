package thefellas.safepoint.impl.modules.core;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "BetterFPS", description = "Better FPS", category = Module.Category.Core)
public class BetterFPS extends Module {
    private boolean focused = true;

    @Override
    public void onTick() {
        super.onTick();
        if (Minecraft.getMinecraft().world == null) {
            return;
        }
        if (!Display.isActive() && this.focused) {
            this.focused = false;
            Thread th = new Thread(new Runnable(){

                @Override
                public void run() {
                    try {
                        Thread.sleep(500L);
                        Minecraft.getMinecraft().gameSettings.limitFramerate = 3;
                        Display.setTitle("[Unfocused] " + Display.getTitle());
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            th.run();
        } else if (Display.isActive() && Minecraft.getMinecraft().gameSettings.limitFramerate == 3) {
            this.focused = true;
            Minecraft.getMinecraft().gameSettings.limitFramerate = 260;
            Display.setTitle(Display.getTitle().replace("[Unfocused] ", ""));
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.getMinecraft().gameSettings.limitFramerate = 260;
    }
}
