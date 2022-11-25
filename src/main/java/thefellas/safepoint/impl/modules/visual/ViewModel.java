package thefellas.safepoint.impl.modules.visual;


import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.settings.impl.DoubleSetting;

@ModuleInfo(name = "ViewModel", description = "Changes the position of your hand", category = Module.Category.Visual)
public class ViewModel extends Module {
    //Right
    DoubleSetting right_x = new DoubleSetting("Right_X", 0, -2, 2, this);
    DoubleSetting right_y = new DoubleSetting("Right_Y", 0, -2, 2, this);
    DoubleSetting right_z = new DoubleSetting("Right_Z", 0, -2, 2, this);
    //Left
    DoubleSetting left_x = new DoubleSetting("Left_X", 0, -2, 2, this);
    DoubleSetting left_y = new DoubleSetting("Left_Y", 0, -2, 2, this);
    DoubleSetting left_z = new DoubleSetting("Left_Z", 0, -2, 2, this);

    BooleanSetting offhand = new BooleanSetting("Offhand", true, this);

    @SubscribeEvent
    public void transform(RenderSpecificHandEvent var1) {
        if (var1.getHand() == EnumHand.MAIN_HAND) {
            GlStateManager.translate(right_x.getValue(), right_y.getValue(), right_z.getValue());
        }

        if (var1.getHand() == EnumHand.OFF_HAND) {
            GlStateManager.translate(-left_x.getValue(), left_y.getValue(), left_z.getValue());
        }

        if(offhand.getValue()){
            mc.player.swingingHand = EnumHand.OFF_HAND;
        }

    }
}
