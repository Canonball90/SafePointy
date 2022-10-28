package thefellas.safepoint.modules.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.FloatSetting;

@ModuleInfo(name = "Hitbox", description = "Bigger hitbox", category = Module.Category.Combat)
public class AD_Hitbox extends Module {
    FloatSetting size = new FloatSetting("Size", 1f, 0.1f, 4f, this);
    @Override
    public void onTick() {
        if(nullCheck()) return;
        if (this.isEnabled()) {

            for (EntityPlayer player : mc.world.playerEntities) {
                if (player != null && player != mc.player) {
                    player.setEntityBoundingBox(new AxisAlignedBB(
                            player.posX - size.getValue(),
                            player.getEntityBoundingBox().minY,
                            player.posZ - size.getValue(),
                            player.posX + size.getValue(),
                            player.getEntityBoundingBox().maxY,
                            player.posZ + size.getValue()
                    ));
                }
            }
        } else {
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player != null && player != mc.player) {
                    player.setEntityBoundingBox(new AxisAlignedBB(
                            player.posX - 0.3F,
                            player.getEntityBoundingBox().minY,
                            player.posZ - 0.3F,
                            player.posX + 0.3F,
                            player.getEntityBoundingBox().maxY,
                            player.posZ + 0.3F
                    ));
                }
            }
        }
    }
}
