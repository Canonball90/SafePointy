package thefellas.safepoint.impl.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.settings.impl.EnumSetting;
import thefellas.safepoint.impl.settings.impl.IntegerSetting;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

import java.util.Arrays;

@ModuleInfo(name = "OldfagNoFall", description = "Prevents you from taking fall damage", category = Module.Category.Movement)
public class OldfagNoFall extends Module {
    EnumSetting mode = new EnumSetting("Mode","Predict", Arrays.asList("Predict", "Old"), this);
    BooleanSetting disconnect = new BooleanSetting("Disconnect", false, this);
    IntegerSetting fallDist = new IntegerSetting("FallDistance", 4, 3, 20, this);

    BlockPos n1;

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event)
    {
        if(nullCheck()) return;
        if(mode.getValue().equals("Predict"))
        {
            if(mc.player.fallDistance > fallDist.getValue() && predict(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)))
            {
                mc.player.motionY = 0;
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, n1.getY(), mc.player.posZ, false));
                mc.player.fallDistance = 0;
                if(disconnect.getValue()) mc.player.connection.getNetworkManager().closeChannel(new TextComponentString(ChatFormatting.GOLD + "NoFall"));
            }
        }
        if(mode.getValue().equals("Old"))
        {
            if(mc.player.fallDistance > fallDist.getValue())
            {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, 0, mc.player.posZ, false));
                mc.player.fallDistance = 0;
            }
        }
    }

    private boolean predict(BlockPos blockPos)
    {
        Minecraft mc = Minecraft.getMinecraft();
        n1 = blockPos.add(0, -fallDist.getValue(), 0);
        if (mc.world.getBlockState(n1).getBlock() != Blocks.AIR)
        {
            return true;
        }
        return false;
    }
}
