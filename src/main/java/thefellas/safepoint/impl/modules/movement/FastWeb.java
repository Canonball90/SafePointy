package thefellas.safepoint.impl.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

@ModuleInfo(name = "FastWeb", description = "Allows you to move faster on webs", category = Module.Category.Movement)
public class FastWeb extends Module {
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (mc.player == null || mc.world == null) return;

        if (isInWeb()) mc.player.motionY--;
    }

    private boolean isInWeb()
    {
        Vec3d vec = interpolateEntity(mc.player, mc.getRenderPartialTicks());
        return mc.world.getBlockState(new BlockPos(vec.x, vec.y, vec.z)).getBlock().equals(Blocks.WEB) || mc.world.getBlockState(new BlockPos(vec.x, vec.y - 1, vec.z)).getBlock().equals(Blocks.WEB);
    }

    private Vec3d interpolateEntity(EntityPlayerSP entity, float time)
    {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time,
                entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time,
                entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }
}
