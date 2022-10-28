package thefellas.safepoint.event;

import thefellas.safepoint.Safepoint;
import thefellas.safepoint.event.events.DeathEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class EventListener {

    public void init(boolean load) {
        if (load)
            MinecraftForge.EVENT_BUS.register(this);
        else MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Safepoint.moduleInitializer.onTick();
        if (Safepoint.mc.world == null || Safepoint.mc.player == null)
            return;
        Safepoint.mc.world.playerEntities.stream().filter(player -> player != null && !(player.getHealth() > 0.0f)).map(DeathEvent::new).forEach(MinecraftForge.EVENT_BUS::post);
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState())
            Safepoint.moduleInitializer.onKey(Keyboard.getEventKey());
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        Safepoint.moduleInitializer.onWorldRender();
    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (!Safepoint.hudComponentInitializer.getHudModules().isEmpty())
            Safepoint.hudComponentInitializer.drawText();
    }
}
