package thefellas.safepoint.impl.modules.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.EnumSetting;
import thefellas.safepoint.core.utils.BlockUtil;

import java.util.Arrays;

@ModuleInfo(name = "AntiVoid", category = Module.Category.Movement, description = "Prevents you from falling into the void")
public class AntiVoid extends Module {

    EnumSetting mode = new EnumSetting("Mode", "Nofall", Arrays.asList("Place", "Nofall", "Jump"), this);

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (mc.player == null || mc.world == null) return;

        if (isAboveVoid() && !mc.player.isSneaking())
        {

            if (mode.getValue().equals("Place"))
            {
                int slot = getObsidianSlot();
                if (slot != -1)
                {
                    BlockUtil.placeBlock(new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ), slot, true, true);
                }
            }
            else if (mode.getValue().equals("NoFall"))
            {
                mc.player.motionY = 0;
            }
            else
            {
                mc.player.moveVertical = 5.0f;
                mc.player.jump();
            }

        }
        else
        {
            mc.player.moveVertical = 0.0f;
        }

    }

    private boolean isAboveVoid()
    {
        if (mc.player.posY <= 3)
        {
            RayTraceResult trace = mc.world.rayTraceBlocks(mc.player.getPositionVector(), new Vec3d(mc.player.posX, 0, mc.player.posZ), false, false, false);

            return trace == null || trace.typeOfHit != RayTraceResult.Type.BLOCK;
        }

        return false;
    }

    public int getObsidianSlot()
    {
        int slot = -1;
        for (int i = 0; i < 9; i++)
        {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) continue;
            Block block = ((ItemBlock) stack.getItem()).getBlock();

            if (block instanceof BlockObsidian)
            {
                slot = i;
                break;
            }
        }
        return slot;
    }
}
