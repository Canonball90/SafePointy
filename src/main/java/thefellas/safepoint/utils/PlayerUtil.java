package thefellas.safepoint.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.*;

public class PlayerUtil {
    static Minecraft mc = Minecraft.getMinecraft();

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(PlayerUtil.mc.player.posX), Math.floor(PlayerUtil.mc.player.posY), Math.floor(PlayerUtil.mc.player.posZ));
    }

    public static BlockPos getPlayerPos(final double pY) {
        return new BlockPos(Math.floor(PlayerUtil.mc.player.posX), Math.floor(PlayerUtil.mc.player.posY + pY), Math.floor(PlayerUtil.mc.player.posZ));
    }
}
