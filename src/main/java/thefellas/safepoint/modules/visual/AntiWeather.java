package thefellas.safepoint.modules.visual;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;

@ModuleInfo(name = "AntiWeather", description = "Removes weather effects", category = Module.Category.Visual)
public class AntiWeather extends Module {

        @SubscribeEvent
        public void onTick(TickEvent.ClientTickEvent var1) {
            if(nullCheck()) return;
            if (Minecraft.getMinecraft().world.isRaining()) {
                Minecraft.getMinecraft().world.setRainStrength(0);
            }
        }
}
