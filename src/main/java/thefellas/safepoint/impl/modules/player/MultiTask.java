package thefellas.safepoint.impl.modules.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "MultiTask", description = "Allows you to do multiple things at once", category = Module.Category.Player)
public class MultiTask extends Module {
    @SubscribeEvent
    public void onMouseInput(final InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButtonState() && this.mc.player != null && this.mc.objectMouseOver.typeOfHit.equals((Object) RayTraceResult.Type.ENTITY) && this.mc.player.isHandActive() && (this.mc.gameSettings.keyBindAttack.isPressed() || Mouse.getEventButton() == this.mc.gameSettings.keyBindAttack.getKeyCode())) {
            this.mc.playerController.attackEntity((EntityPlayer)this.mc.player, this.mc.objectMouseOver.entityHit);
            this.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }
}
