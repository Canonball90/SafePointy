package thefellas.safepoint.core.utils;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.*;
import thefellas.safepoint.Safepoint;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BlockUtil {
    static Minecraft mc = Minecraft.getMinecraft();
    public static List<Block> emptyBlocks;
    public static List<Block> rightclickableBlocks;
    public static final List<Block> blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER, Blocks.TRAPDOOR, Blocks.ENCHANTING_TABLE);
    public static final List<Block> shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);

    public enum AirMode {
        NoAir,
        AirOnly,
        Ignored
    }

    public static List<BlockPos> getBlocksInRadius(double radius, AirMode airMode) {
        ArrayList<BlockPos> posList = new ArrayList<>();
        BlockPos pos = new BlockPos(Math.floor(Safepoint.mc.player.posX), Math.floor(Safepoint.mc.player.posY), Math.floor(Safepoint.mc.player.posZ));
        for (int x = pos.getX() - (int) radius; x <= pos.getX() + radius; ++x) {
            for (int y = pos.getY() - (int) radius; y < pos.getY() + radius; ++y) {
                for (int z = pos.getZ() - (int) radius; z <= pos.getZ() + radius; ++z) {
                    double distance = (pos.getX() - x) * (pos.getX() - x) + (pos.getZ() - z) * (pos.getZ() - z) + (pos.getY() - y) * (pos.getY() - y);
                    BlockPos position = new BlockPos(x, y, z);
                    if (distance < radius * radius) {
                        if (airMode.equals(AirMode.NoAir) && Safepoint.mc.world.getBlockState(position).getBlock().equals(Blocks.AIR))
                            continue;
                        if (airMode.equals(AirMode.AirOnly) && !Safepoint.mc.world.getBlockState(position).getBlock().equals(Blocks.AIR))
                            continue;
                        posList.add(position);
                    }
                }
            }
        }
        return posList;
    }

    public static void placeBlock(BlockPos blockPos, boolean packet) {
        for (EnumFacing enumFacing : EnumFacing.values()) {
            if (!(Safepoint.mc.world.getBlockState(blockPos.offset(enumFacing)).equals(Blocks.AIR))) {
                for (Entity entity : Safepoint.mc.world.loadedEntityList)
                    if (new AxisAlignedBB(blockPos).intersects(entity.getEntityBoundingBox()))
                        return;

                Safepoint.mc.player.connection.sendPacket(new CPacketEntityAction(Safepoint.mc.player, CPacketEntityAction.Action.START_SNEAKING));

                if (packet)
                    Safepoint.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos.offset(enumFacing), enumFacing.getOpposite(), EnumHand.MAIN_HAND, 0, 0, 0));
                else
                    Safepoint.mc.playerController.processRightClickBlock(Safepoint.mc.player, Safepoint.mc.world, blockPos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d(blockPos), EnumHand.MAIN_HAND);

                Safepoint.mc.player.connection.sendPacket(new CPacketEntityAction(Safepoint.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                return;
            }
        }
    }

    public static int isPositionPlaceable(final BlockPos pos, final boolean rayTrace) {
        return isPositionPlaceable(pos, rayTrace, true);
    }

    public static int isPositionPlaceable(final BlockPos pos, final boolean rayTrace, final boolean entityCheck) {
        final Block block = Minecraft.getMinecraft().world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow)) {
            return 0;
        }
        if (!rayTracePlaceCheck(pos, rayTrace, 0.0f)) {
            return -1;
        }
        if (entityCheck) {
            for (final Object entity : Minecraft.getMinecraft().world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(pos))) {
                if (!(entity instanceof EntityItem)) {
                    if (entity instanceof EntityXPOrb) {
                        continue;
                    }
                    return 1;
                }
            }
        }
        for (final EnumFacing side : getPossibleSides(pos)) {
            if (!canBeClicked(pos.offset(side))) {
                continue;
            }
            return 3;
        }
        return 2;
    }

    public static boolean rayTracePlaceCheck(final BlockPos pos, final boolean shouldCheck, final float height) {
        return !shouldCheck || Minecraft.getMinecraft().world.rayTraceBlocks(new Vec3d(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY + Minecraft.getMinecraft().player.getEyeHeight(), Minecraft.getMinecraft().player.posZ), new Vec3d((double)pos.getX(), (double)(pos.getY() + height), (double)pos.getZ()), false, true, false) == null;
    }

    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }

    private static Block getBlock(final BlockPos pos) {
        return getState(pos).getBlock();
    }

    private static IBlockState getState(final BlockPos pos) {
        return Minecraft.getMinecraft().world.getBlockState(pos);
    }

    public static List<EnumFacing> getPossibleSides(final BlockPos pos) {
        final List<EnumFacing> facings = new ArrayList<EnumFacing>();
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.offset(side);
            if (Minecraft.getMinecraft().world.getBlockState(neighbour) == null) {
                return facings;
            }
            if (Minecraft.getMinecraft().world.getBlockState(neighbour).getBlock() == null) {
                return facings;
            }
            if (Minecraft.getMinecraft().world.getBlockState(neighbour).getBlock().canCollideCheck(Minecraft.getMinecraft().world.getBlockState(neighbour), false)) {
                final IBlockState blockState = Minecraft.getMinecraft().world.getBlockState(neighbour);
                if (!blockState.getMaterial().isReplaceable()) {
                    facings.add(side);
                }
            }
        }
        return facings;
    }

    public static boolean placeBlock(final BlockPos pos, final int slot, final boolean rotate, final boolean rotateBack) {
        if (isBlockEmpty(pos)) {
            int old_slot = -1;
            if (slot != Minecraft.getMinecraft().player.inventory.currentItem) {
                old_slot = Minecraft.getMinecraft().player.inventory.currentItem;
                Minecraft.getMinecraft().player.inventory.currentItem = slot;
            }
            final EnumFacing[] values;
            final EnumFacing[] facings = values = EnumFacing.values();
            for (final EnumFacing f : values) {
                final Block neighborBlock = Minecraft.getMinecraft().world.getBlockState(pos.offset(f)).getBlock();
                final Vec3d vec = new Vec3d(pos.getX() + 0.5 + f.getXOffset() * 0.5, pos.getY() + 0.5 + f.getYOffset() * 0.5, pos.getZ() + 0.5 + f.getZOffset() * 0.5);
                if (!BlockUtil.emptyBlocks.contains(neighborBlock) && Minecraft.getMinecraft().player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()).distanceTo(vec) <= 4.25) {
                    final float[] rot = { Minecraft.getMinecraft().player.rotationYaw, Minecraft.getMinecraft().player.rotationPitch };
                    if (rotate) {
                        rotatePacket(vec.x, vec.y, vec.z);
                    }
                    if (BlockUtil.rightclickableBlocks.contains(neighborBlock)) {
                        Minecraft.getMinecraft().player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Minecraft.getMinecraft().player, CPacketEntityAction.Action.START_SNEAKING));
                    }
                    Minecraft.getMinecraft().playerController.processRightClickBlock(Minecraft.getMinecraft().player, Minecraft.getMinecraft().world, pos.offset(f), f.getOpposite(), new Vec3d((Vec3i)pos), EnumHand.MAIN_HAND);
                    if (BlockUtil.rightclickableBlocks.contains(neighborBlock)) {
                        Minecraft.getMinecraft().player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Minecraft.getMinecraft().player, CPacketEntityAction.Action.STOP_SNEAKING));
                    }
                    if (rotateBack) {
                        Minecraft.getMinecraft().player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rot[0], rot[1], Minecraft.getMinecraft().player.onGround));
                    }
                    if (old_slot != -1) {
                        Minecraft.getMinecraft().player.inventory.currentItem = old_slot;
                    }
                    return true;
                }
            }
            if (old_slot != -1) {
                Minecraft.getMinecraft().player.inventory.currentItem = old_slot;
            }
        }
        return false;
    }

    public static void rotatePacket(final double x, final double y, final double z) {
        final double diffX = x - Minecraft.getMinecraft().player.posX;
        final double diffY = y - (Minecraft.getMinecraft().player.posY + Minecraft.getMinecraft().player.getEyeHeight());
        final double diffZ = z - Minecraft.getMinecraft().player.posZ;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        Minecraft.getMinecraft().player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(yaw, pitch, Minecraft.getMinecraft().player.onGround));
    }

    public static boolean isBlockEmpty(final BlockPos pos) {
        try {
            if (BlockUtil.emptyBlocks.contains(Minecraft.getMinecraft().world.getBlockState(pos).getBlock())) {
                final AxisAlignedBB box = new AxisAlignedBB(pos);
                for (final Entity e : Minecraft.getMinecraft().world.loadedEntityList) {
                    if (e instanceof EntityLivingBase && box.intersects(e.getEntityBoundingBox())) {
                        return false;
                    }
                }
                return true;
            }
        }
        catch (Exception ex) {}
        return false;
    }

    static {
        BlockUtil.emptyBlocks = Arrays.asList(Blocks.AIR, (Block)Blocks.FLOWING_LAVA, (Block)Blocks.LAVA, (Block)Blocks.FLOWING_WATER, (Block)Blocks.WATER, Blocks.VINE, Blocks.SNOW_LAYER, (Block)Blocks.TALLGRASS, (Block)Blocks.FIRE);
        BlockUtil.rightclickableBlocks = Arrays.asList((Block)Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.ANVIL, Blocks.WOODEN_BUTTON, Blocks.STONE_BUTTON, (Block)Blocks.UNPOWERED_COMPARATOR, (Block)Blocks.UNPOWERED_REPEATER, (Block)Blocks.POWERED_REPEATER, (Block)Blocks.POWERED_COMPARATOR, Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.BREWING_STAND, Blocks.DISPENSER, Blocks.DROPPER, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.JUKEBOX, (Block)Blocks.BEACON, Blocks.BED, Blocks.FURNACE, (Block)Blocks.OAK_DOOR, (Block)Blocks.SPRUCE_DOOR, (Block)Blocks.BIRCH_DOOR, (Block)Blocks.JUNGLE_DOOR, (Block)Blocks.ACACIA_DOOR, (Block)Blocks.DARK_OAK_DOOR, Blocks.CAKE, Blocks.ENCHANTING_TABLE, Blocks.DRAGON_EGG, (Block)Blocks.HOPPER, Blocks.REPEATING_COMMAND_BLOCK, Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.CRAFTING_TABLE);
    }

    public static boolean canPlaceBlock(BlockPos pos)
    {
        if (isBlockEmpty(pos)) {
            EnumFacing[] facings = EnumFacing.values();

            for (EnumFacing f : facings) {
                if (!emptyBlocks.contains(Minecraft.getMinecraft().world.getBlockState(pos.offset(f)).getBlock()) && Minecraft.getMinecraft().player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()).distanceTo(new Vec3d(pos.getX() + 0.5D + (double) f.getXOffset() * 0.5D, pos.getY() + 0.5D + (double) f.getYOffset() * 0.5D, pos.getZ() + 0.5D + (double) f.getZOffset() * 0.5D)) <= 4.25D) {
                    return true;
                }
            }

        }
        return false;
    }

    public static void placeBlockWithSwitch(BlockPos pos, boolean rotate, boolean packet, int slot) {
        int currentItem = mc.player.inventory.currentItem;
        InventoryUtil.switchToSlot(slot);
        placeBlock(pos, slot, rotate, packet);
        mc.player.inventory.currentItem = currentItem;
        mc.playerController.updateController();
    }


    public static EnumFacing getFirstFacing(BlockPos pos) {
        Iterator<EnumFacing> iterator = BlockUtil.getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            EnumFacing facing = iterator.next();
            return facing;
        }
        return null;
    }

    public static boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = BlockUtil.getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = Minecraft.getMinecraft().world.getBlockState(neighbour).getBlock();
        if (!Minecraft.getMinecraft().player.isSneaking() && (blackList.contains(neighbourBlock) || shulkerList.contains(neighbourBlock))) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketEntityAction(Minecraft.getMinecraft().player, CPacketEntityAction.Action.START_SNEAKING));
            Minecraft.getMinecraft().player.setSneaking(true);
            sneaking = true;
        }
        if (rotate) {
            faceVector(hitVec, true);
        }
        BlockUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
        return sneaking || isSneaking;
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float)(vec.x - (double)pos.getX());
            float f1 = (float)(vec.y - (double)pos.getY());
            float f2 = (float)(vec.z - (double)pos.getZ());
            BlockUtil.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, pos, direction, vec, hand);
        }
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    public static Vec3d posToVec3d(BlockPos pos) {
        return new Vec3d(pos);
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(mc.player.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ);
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{mc.player.rotationYaw + MathHelper.wrapDegrees((float)(yaw - mc.player.rotationYaw)), mc.player.rotationPitch + MathHelper.wrapDegrees((float)(pitch - mc.player.rotationPitch))};
    }

    public static void faceVector(Vec3d vec, boolean normalizeAngle) {
        float[] rotations = getLegitRotations(vec);
        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? (float) MathHelper.normalizeAngle((int)((int)rotations[1]), (int)360) : rotations[1], mc.player.onGround));
    }

}
