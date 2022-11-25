package thefellas.safepoint.core.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import thefellas.safepoint.Safepoint;

public class EntityUtil {
    static Minecraft mc = Minecraft.getMinecraft();
    public static final Vec3d[] antiDropOffsetList = new Vec3d[] { new Vec3d(0.0D, -2.0D, 0.0D) };

    public static final Vec3d[] platformOffsetList = new Vec3d[] { new Vec3d(0.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, -1.0D), new Vec3d(0.0D, -1.0D, 1.0D), new Vec3d(-1.0D, -1.0D, 0.0D), new Vec3d(1.0D, -1.0D, 0.0D) };

    public static final Vec3d[] legOffsetList = new Vec3d[] { new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 0.0D, 1.0D) };

    public static final Vec3d[] OffsetList = new Vec3d[] { new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(0.0D, 1.0D, -1.0D), new Vec3d(0.0D, 2.0D, 0.0D) };

    public static final Vec3d[] antiStepOffsetList = new Vec3d[] { new Vec3d(-1.0D, 2.0D, 0.0D), new Vec3d(1.0D, 2.0D, 0.0D), new Vec3d(0.0D, 2.0D, 1.0D), new Vec3d(0.0D, 2.0D, -1.0D) };

    public static final Vec3d[] antiScaffoldOffsetList = new Vec3d[] { new Vec3d(0.0D, 3.0D, 0.0D) };

    public static void attackEntity(Entity entity, boolean packet, boolean swingArm) {
        if (packet) {
            mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
        } else {
            mc.playerController.attackEntity((EntityPlayer)mc.player, entity);
        }
        if (swingArm)
            mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    public static Vec3d interpolateEntity(Entity entity, float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }

    public static Vec3d getInterpolatedPos(Entity entity, float partialTicks) {
        return (new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ)).add(getInterpolatedAmount(entity, partialTicks));
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec) {
        return getInterpolatedAmount(entity, vec.x, vec.y, vec.z);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, float partialTicks) {
        return getInterpolatedAmount(entity, partialTicks, partialTicks, partialTicks);
    }

    public static boolean isPassive(Entity entity) {
        if (entity instanceof EntityWolf && ((EntityWolf)entity).isAngry())
            return false;
        if (entity instanceof net.minecraft.entity.EntityAgeable || entity instanceof net.minecraft.entity.passive.EntityAmbientCreature || entity instanceof net.minecraft.entity.passive.EntitySquid)
            return true;
        return (entity instanceof EntityIronGolem && ((EntityIronGolem)entity).getRevengeTarget() == null);
    }

    public static boolean isSafe(Entity entity, int height, boolean floor) {
        return (getUnsafeBlocks(entity, height, floor).size() == 0);
    }

    public static boolean stopSneaking(boolean isSneaking) {
        if (isSneaking && mc.player != null)
            mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        return false;
    }

    public static boolean isSafe(Entity entity) {
        return isSafe(entity, 0, false);
    }

    public static BlockPos getPlayerPos(EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    public static List<Vec3d> getUnsafeBlocks(Entity entity, int height, boolean floor) {
        return getUnsafeBlocksFromVec3d(entity.getPositionVector(), height, floor);
    }

    public static boolean isMobAggressive(Entity entity) {
        if (entity instanceof EntityPigZombie) {
            if (((EntityPigZombie)entity).isArmsRaised() || ((EntityPigZombie)entity).isAngry())
                return true;
        } else {
            if (entity instanceof EntityWolf)
                return (((EntityWolf)entity).isAngry() && !mc.player.equals(((EntityWolf)entity).getOwner()));
            if (entity instanceof EntityEnderman)
                return ((EntityEnderman)entity).isScreaming();
        }
        return isHostileMob(entity);
    }

    public static boolean isNeutralMob(Entity entity) {
        return (entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman);
    }

    public static boolean isProjectile(Entity entity) {
        return (entity instanceof net.minecraft.entity.projectile.EntityShulkerBullet || entity instanceof net.minecraft.entity.projectile.EntityFireball);
    }

    public static boolean isVehicle(Entity entity) {
        return (entity instanceof net.minecraft.entity.item.EntityBoat || entity instanceof net.minecraft.entity.item.EntityMinecart);
    }

    public static boolean isFriendlyMob(Entity entity) {
        return ((entity.isCreatureType(EnumCreatureType.CREATURE, false) && !isNeutralMob(entity)) || entity.isCreatureType(EnumCreatureType.AMBIENT, false) || entity instanceof net.minecraft.entity.passive.EntityVillager || entity instanceof EntityIronGolem || (isNeutralMob(entity) && !isMobAggressive(entity)));
    }

    public static boolean isHostileMob(Entity entity) {
        return (entity.isCreatureType(EnumCreatureType.MONSTER, false) && !isNeutralMob(entity));
    }

    public static List<Vec3d> getUnsafeBlocksFromVec3d(Vec3d pos, int height, boolean floor) {
        ArrayList<Vec3d> vec3ds = new ArrayList<>();
        for (Vec3d vector : getOffsets(height, floor)) {
            BlockPos targetPos = (new BlockPos(pos)).add(vector.x, vector.y, vector.z);
            Block block = mc.world.getBlockState(targetPos).getBlock();
            if (block instanceof net.minecraft.block.BlockAir || block instanceof net.minecraft.block.BlockLiquid || block instanceof net.minecraft.block.BlockTallGrass || block instanceof net.minecraft.block.BlockFire || block instanceof net.minecraft.block.BlockDeadBush || block instanceof net.minecraft.block.BlockSnow)
                vec3ds.add(vector);
        }
        return vec3ds;
    }

    public static boolean isInHole(Entity entity) {
        return isBlockValid(new BlockPos(entity.posX, entity.posY, entity.posZ));
    }

    public static boolean isBlockValid(BlockPos blockPos) {
        return (isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos));
    }

    public static boolean isObbyHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        BlockPos[] arrayOfBlockPos1;
        int i;
        byte b;
        for (arrayOfBlockPos1 = touchingBlocks = new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }, i = arrayOfBlockPos1.length, b = 0; b < i; ) {
            BlockPos pos = arrayOfBlockPos1[b];
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && touchingState.getBlock() == Blocks.OBSIDIAN) {
                b++;
                continue;
            }
            return false;
        }
        return true;
    }

    public static boolean isBedrockHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        BlockPos[] arrayOfBlockPos1;
        int i;
        byte b;
        for (arrayOfBlockPos1 = touchingBlocks = new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }, i = arrayOfBlockPos1.length, b = 0; b < i; ) {
            BlockPos pos = arrayOfBlockPos1[b];
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && touchingState.getBlock() == Blocks.BEDROCK) {
                b++;
                continue;
            }
            return false;
        }
        return true;
    }

    public static boolean isBothHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        BlockPos[] arrayOfBlockPos1;
        int i;
        byte b;
        for (arrayOfBlockPos1 = touchingBlocks = new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }, i = arrayOfBlockPos1.length, b = 0; b < i; ) {
            BlockPos pos = arrayOfBlockPos1[b];
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && (touchingState.getBlock() == Blocks.BEDROCK || touchingState.getBlock() == Blocks.OBSIDIAN)) {
                b++;
                continue;
            }
            return false;
        }
        return true;
    }

    public static Vec3d[] getUnsafeBlockArray(Entity entity, int height, boolean floor) {
        List<Vec3d> list = getUnsafeBlocks(entity, height, floor);
        Vec3d[] array = new Vec3d[list.size()];
        return list.<Vec3d>toArray(array);
    }

    public static Vec3d[] getUnsafeBlockArrayFromVec3d(Vec3d pos, int height, boolean floor) {
        List<Vec3d> list = getUnsafeBlocksFromVec3d(pos, height, floor);
        Vec3d[] array = new Vec3d[list.size()];
        return list.<Vec3d>toArray(array);
    }

    public static double getDst(Vec3d vec) {
        return mc.player.getPositionVector().distanceTo(vec);
    }

    public static boolean isTrapped(EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
        return (getUntrappedBlocks(player, antiScaffold, antiStep, legs, platform, antiDrop).size() == 0);
    }

//    public static boolean isTrappedExtended(int extension, EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace) {
//        return (getUntrappedBlocksExtended(extension, player, antiScaffold, antiStep, legs, platform, antiDrop, raytrace).size() == 0);
//    }

    public static List<Vec3d> getUntrappedBlocks(EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
        ArrayList<Vec3d> vec3ds = new ArrayList<>();
        if (!antiStep && getUnsafeBlocks((Entity)player, 2, false).size() == 4)
            vec3ds.addAll(getUnsafeBlocks((Entity)player, 2, false));
        for (int i = 0; i < (getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop)).length; i++) {
            Vec3d vector = getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop)[i];
            BlockPos targetPos = (new BlockPos(player.getPositionVector())).add(vector.x, vector.y, vector.z);
            Block block = mc.world.getBlockState(targetPos).getBlock();
            if (block instanceof net.minecraft.block.BlockAir || block instanceof net.minecraft.block.BlockLiquid || block instanceof net.minecraft.block.BlockTallGrass || block instanceof net.minecraft.block.BlockFire || block instanceof net.minecraft.block.BlockDeadBush || block instanceof net.minecraft.block.BlockSnow)
                vec3ds.add(vector);
        }
        return vec3ds;
    }

    public static boolean isInWater(Entity entity) {
        if (entity == null)
            return false;
        double y = entity.posY + 0.01D;
        for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); x++) {
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ) {
                BlockPos pos = new BlockPos(x, (int)y, z);
                if (!(mc.world.getBlockState(pos).getBlock() instanceof net.minecraft.block.BlockLiquid)) {
                    z++;
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isDrivenByPlayer(Entity entityIn) {
        return (mc.player != null && entityIn != null && entityIn.equals(mc.player.getRidingEntity()));
    }

    public static boolean isPlayer(Entity entity) {
        return entity instanceof EntityPlayer;
    }

    public static boolean isAboveWater(Entity entity) {
        return isAboveWater(entity, false);
    }

    public static boolean isAboveWater(Entity entity, boolean packet) {
        if (entity == null)
            return false;
        double y = entity.posY - (packet ? 0.03D : (isPlayer(entity) ? 0.2D : 0.5D));
        for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); x++) {
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ) {
                BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                if (!(mc.world.getBlockState(pos).getBlock() instanceof net.minecraft.block.BlockLiquid)) {
                    z++;
                    continue;
                }
                return true;
            }
        }
        return false;
    }

//    public static List<Vec3d> getUntrappedBlocksExtended(int extension, EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace) {
//        ArrayList<Vec3d> placeTargets = new ArrayList<>();
//        if (extension == 1) {
//            placeTargets.addAll(targets(player.getPositionVector(), antiScaffold, antiStep, legs, platform, antiDrop, raytrace));
//        } else {
//            int extend = 1;
//            for (Vec3d vec3d : MathUtil.getBlockBlocks((Entity)player)) {
//                if (extend > extension)
//                    break;
//                placeTargets.addAll(targets(vec3d, antiScaffold, antiStep, legs, platform, antiDrop, raytrace));
//                extend++;
//            }
//        }
//        ArrayList<Vec3d> removeList = new ArrayList<>();
//        for (Vec3d vec3d : placeTargets) {
//            BlockPos pos = new BlockPos(vec3d);
//            if (BlockUtil.isPositionPlaceable(pos, raytrace) != -1)
//                continue;
//            removeList.add(vec3d);
//        }
//        for (Vec3d vec3d : removeList)
//            placeTargets.remove(vec3d);
//        return placeTargets;
//    }

//    public static List<Vec3d> targets(Vec3d vec3d, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace) {
//        ArrayList<Vec3d> placeTargets = new ArrayList<>();
//        if (antiDrop)
//            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiDropOffsetList));
//        if (platform)
//            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, platformOffsetList));
//        if (legs)
//            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, legOffsetList));
//        Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, OffsetList));
//        if (antiStep) {
//            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiStepOffsetList));
//        } else {
//            List<Vec3d> vec3ds = getUnsafeBlocksFromVec3d(vec3d, 2, false);
//            if (vec3ds.size() == 4)
//                for (Vec3d vector : vec3ds) {
//                    BlockPos position = (new BlockPos(vec3d)).add(vector.x, vector.y, vector.z);
//                    switch (BlockUtil.isPositionPlaceable(position, raytrace)) {
//                        case 0:
//                            break;
//                        case -1:
//                        case 1:
//                        case 2:
//                            continue;
//                        case 3:
//                            placeTargets.add(vec3d.add(vector));
//                            break;
//                        default:
//                            // Byte code: goto -> 234
//                    }
//                    if (antiScaffold)
//                        Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiScaffoldOffsetList));
//                    return placeTargets;
//                }
//        }
//        if (antiScaffold)
//            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiScaffoldOffsetList));
//        return placeTargets;
//    }

    public static List<Vec3d> getOffsetList(int y, boolean floor) {
        ArrayList<Vec3d> offsets = new ArrayList<>();
        offsets.add(new Vec3d(-1.0D, y, 0.0D));
        offsets.add(new Vec3d(1.0D, y, 0.0D));
        offsets.add(new Vec3d(0.0D, y, -1.0D));
        offsets.add(new Vec3d(0.0D, y, 1.0D));
        if (floor)
            offsets.add(new Vec3d(0.0D, (y - 1), 0.0D));
        return offsets;
    }

    public static Vec3d[] getOffsets(int y, boolean floor) {
        List<Vec3d> offsets = getOffsetList(y, floor);
        Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.<Vec3d>toArray(array);
    }

    public static Vec3d[] getTrapOffsets(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
        List<Vec3d> offsets = getTrapOffsetsList(antiScaffold, antiStep, legs, platform, antiDrop);
        Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.<Vec3d>toArray(array);
    }

    public static List<Vec3d> getTrapOffsetsList(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
        ArrayList<Vec3d> offsets = new ArrayList<>(getOffsetList(1, false));
        offsets.add(new Vec3d(0.0D, 2.0D, 0.0D));
        if (antiScaffold)
            offsets.add(new Vec3d(0.0D, 3.0D, 0.0D));
        if (antiStep)
            offsets.addAll(getOffsetList(2, false));
        if (legs)
            offsets.addAll(getOffsetList(0, false));
        if (platform) {
            offsets.addAll(getOffsetList(-1, false));
            offsets.add(new Vec3d(0.0D, -1.0D, 0.0D));
        }
        if (antiDrop)
            offsets.add(new Vec3d(0.0D, -2.0D, 0.0D));
        return offsets;
    }

    public static Vec3d[] getHeightOffsets(int min, int max) {
        ArrayList<Vec3d> offsets = new ArrayList<>();
        for (int i = min; i <= max; i++)
            offsets.add(new Vec3d(0.0D, i, 0.0D));
        Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.<Vec3d>toArray(array);
    }

    public static BlockPos getRoundedBlockPos(Entity entity) {
        return new BlockPos(MathUtil.roundVec(entity.getPositionVector(), 0));
    }

    public static boolean isLiving(Entity entity) {
        return entity instanceof EntityLivingBase;
    }

    public static boolean isAlive(Entity entity) {
        return (isLiving(entity) && !entity.isDead && ((EntityLivingBase)entity).getHealth() > 0.0F);
    }

    public static boolean isDead(Entity entity) {
        return !isAlive(entity);
    }

    public static float getHealth(Entity entity) {
        if (isLiving(entity)) {
            EntityLivingBase livingBase = (EntityLivingBase)entity;
            return livingBase.getHealth() + livingBase.getAbsorptionAmount();
        }
        return 0.0F;
    }

    public static float getHealth(Entity entity, boolean absorption) {
        if (isLiving(entity)) {
            EntityLivingBase livingBase = (EntityLivingBase)entity;
            return livingBase.getHealth() + (absorption ? livingBase.getAbsorptionAmount() : 0.0F);
        }
        return 0.0F;
    }

    public static boolean canEntityFeetBeSeen(Entity entityIn) {
        return (mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posX + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ), false, true, false) == null);
    }

//    public static boolean isntValid(Entity entity, double range) {
//        return (entity == null || isDead(entity) || entity.equals(mc.player) || (entity instanceof EntityPlayer && OyVey.friendManager.isFriend(entity.getName())) || mc.player.getDistanceSq(entity) > MathUtil.square(range));
//    }

//    public static boolean isValid(Entity entity, double range) {
//        return !isntValid(entity, range);
//    }

    public static boolean holdingWeapon(EntityPlayer player) {
        return (player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemSword || player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemAxe);
    }

    public static double getMaxSpeed() {
        double maxModifier = 0.2873D;
        if (mc.player.isPotionActive(Objects.<Potion>requireNonNull(Potion.getPotionById(1))))
            maxModifier *= 1.0D + 0.2D * (((PotionEffect)Objects.<PotionEffect>requireNonNull(mc.player.getActivePotionEffect(Objects.<Potion>requireNonNull(Potion.getPotionById(1))))).getAmplifier() + 1);
        return maxModifier;
    }

    public static void mutliplyEntitySpeed(Entity entity, double multiplier) {
        if (entity != null) {
            entity.motionX *= multiplier;
            entity.motionZ *= multiplier;
        }
    }

    public static boolean isEntityMoving(Entity entity) {
        if (entity == null)
            return false;
        if (entity instanceof EntityPlayer)
            return (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown());
        return (entity.motionX != 0.0D || entity.motionY != 0.0D || entity.motionZ != 0.0D);
    }

    public static double getEntitySpeed(Entity entity) {
        if (entity != null) {
            double distTraveledLastTickX = entity.posX - entity.prevPosX;
            double distTraveledLastTickZ = entity.posZ - entity.prevPosZ;
            double speed = MathHelper.sqrt(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ);
            return speed * 20.0D;
        }
        return 0.0D;
    }

    public static boolean is32k(ItemStack stack) {
        return (EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack) >= 1000);
    }

    public static void moveEntityStrafe(double speed, Entity entity) {
        if (entity != null) {
            MovementInput movementInput = mc.player.movementInput;
            double forward = movementInput.moveForward;
            double strafe = movementInput.moveStrafe;
            float yaw = mc.player.rotationYaw;
            if (forward == 0.0D && strafe == 0.0D) {
                entity.motionX = 0.0D;
                entity.motionZ = 0.0D;
            } else {
                if (forward != 0.0D) {
                    if (strafe > 0.0D) {
                        yaw += ((forward > 0.0D) ? -45 : 45);
                    } else if (strafe < 0.0D) {
                        yaw += ((forward > 0.0D) ? 45 : -45);
                    }
                    strafe = 0.0D;
                    if (forward > 0.0D) {
                        forward = 1.0D;
                    } else if (forward < 0.0D) {
                        forward = -1.0D;
                    }
                }
                entity.motionX = forward * speed * Math.cos(Math.toRadians((yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((yaw + 90.0F)));
                entity.motionZ = forward * speed * Math.sin(Math.toRadians((yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((yaw + 90.0F)));
            }
        }
    }

    public static boolean rayTraceHitCheck(Entity entity, boolean shouldCheck) {
        return (!shouldCheck || mc.player.canEntityBeSeen(entity));
    }

//    public static Color getColor(Entity entity, int red, int green, int blue, int alpha, boolean colorFriends) {
//        Color color = new Color(red / 255.0F, green / 255.0F, blue / 255.0F, alpha / 255.0F);
//        if (entity instanceof EntityPlayer && colorFriends && OyVey.friendManager.isFriend((EntityPlayer)entity))
//            color = new Color(0.33333334F, 1.0F, 1.0F, alpha / 255.0F);
//        return color;
//    }

    public static boolean isMoving() {
        return (mc.player.moveForward != 0.0D || mc.player.moveStrafing != 0.0D);
    }

//    public static EntityPlayer getClosestEnemy(double distance) {
//        EntityPlayer closest = null;
//        for (EntityPlayer player : mc.world.playerEntities) {
//            if (isntValid((Entity)player, distance))
//                continue;
//            if (closest == null) {
//                closest = player;
//                continue;
//            }
//            if (mc.player.getDistanceSq((Entity)player) >= mc.player.getDistanceSq((Entity)closest))
//                continue;
//            closest = player;
//        }
//        return closest;
//    }

    public static boolean checkCollide() {
        if (mc.player.isSneaking())
            return false;
        if (mc.player.getRidingEntity() != null && (mc.player.getRidingEntity()).fallDistance >= 3.0F)
            return false;
        return (mc.player.fallDistance < 3.0F);
    }

    public static BlockPos getPlayerPosWithEntity() {
        return new BlockPos((mc.player.getRidingEntity() != null) ? (mc.player.getRidingEntity()).posX : mc.player.posX, (mc.player.getRidingEntity() != null) ? (mc.player.getRidingEntity()).posY : mc.player.posY, (mc.player.getRidingEntity() != null) ? (mc.player.getRidingEntity()).posZ : mc.player.posZ);
    }

    public static double[] forward(double speed) {
        float forward = mc.player.movementInput.moveForward;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0F) {
            if (side > 0.0F) {
                yaw += ((forward > 0.0F) ? -45 : 45);
            } else if (side < 0.0F) {
                yaw += ((forward > 0.0F) ? 45 : -45);
            }
            side = 0.0F;
            if (forward > 0.0F) {
                forward = 1.0F;
            } else if (forward < 0.0F) {
                forward = -1.0F;
            }
        }
        double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
        double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
        double posX = forward * speed * cos + side * speed * sin;
        double posZ = forward * speed * sin - side * speed * cos;
        return new double[] { posX, posZ };
    }

    public static Map<String, Integer> getTextRadarPlayers() {
        Map<String, Integer> output = new HashMap<>();
        DecimalFormat dfHealth = new DecimalFormat("#.#");
        dfHealth.setRoundingMode(RoundingMode.CEILING);
        DecimalFormat dfDistance = new DecimalFormat("#.#");
        dfDistance.setRoundingMode(RoundingMode.CEILING);
        StringBuilder healthSB = new StringBuilder();
        StringBuilder distanceSB = new StringBuilder();
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player.isInvisible() || player.getName().equals(mc.player.getName()))
                continue;
            int hpRaw = (int)getHealth((Entity)player);
            String hp = dfHealth.format(hpRaw);
            healthSB.append("Â§");
            if (hpRaw >= 20) {
                healthSB.append("a");
            } else if (hpRaw >= 10) {
                healthSB.append("e");
            } else if (hpRaw >= 5) {
                healthSB.append("6");
            } else {
                healthSB.append("c");
            }
            healthSB.append(hp);
            int distanceInt = (int)mc.player.getDistance((Entity)player);
            String distance = dfDistance.format(distanceInt);
            distanceSB.append("Â§");
            if (distanceInt >= 25) {
                distanceSB.append("a");
            } else if (distanceInt > 10) {
                distanceSB.append("6");
            } else {
                distanceSB.append("c");
            }
            distanceSB.append(distance);
            output.put(healthSB.toString() + " " + (Safepoint.friendInitializer.isFriend(player.getName()) ? ChatFormatting.AQUA : ChatFormatting.RED) + player.getName() + " " + distanceSB.toString() + " Â§f0", Integer.valueOf((int)mc.player.getDistance((Entity)player)));
            healthSB.setLength(0);
            distanceSB.setLength(0);
        }
        if (!output.isEmpty())
            output = MathUtil.sortByValue(output, false);
        return output;
    }

    public static boolean isntValid(Entity entity, double range) {
        return entity == null || isDead(entity) || entity.equals(mc.player) || entity instanceof EntityPlayer && Safepoint.friendInitializer.isFriend(entity.getName()) || mc.player.getDistanceSq(entity) > (range * range);
    }

    public static Entity getPredictedPosition(Entity entity, double x) {
        if (x == 0) return entity;
        EntityPlayer e = null;
        double motionX = entity.posX - entity.lastTickPosX;
        double motionY = entity.posY - entity.lastTickPosY;
        double motionZ = entity.posZ - entity.lastTickPosZ;
        boolean shouldPredict = false;
        boolean shouldStrafe = false;
        double motion = Math.sqrt(Math.pow(motionX, 2) + Math.pow(motionZ, 2) + Math.pow(motionY, 2));
        if (motion > 0.1) {
            shouldPredict = true;
        }
        if (!shouldPredict) {
            return entity;
        }
        if (motion > 0.31) {
            shouldStrafe = true;
        }
        for (int i = 0; i < x; i++) {
            if (e == null) {
                if (isOnGround(0, 0, 0, entity)) {
                    motionY = shouldStrafe ? 0.4 : -0.07840015258789;
                } else {
                    motionY -= 0.08;
                    motionY *= 0.9800000190734863D;
                }
                e = placeValue(motionX, motionY, motionZ, (EntityPlayer) entity);
            } else {
                if (isOnGround(0, 0, 0, e)) {
                    motionY = shouldStrafe ? 0.4 : -0.07840015258789;
                } else {
                    motionY -= 0.08;
                    motionY *= 0.9800000190734863D;
                }
                e = placeValue(motionX, motionY, motionZ, e);
            }
        }
        return e;
    }

    public static EntityPlayer placeValue(double x, double y, double z, EntityPlayer entity) {
        List<AxisAlignedBB> list1 = mc.world.getCollisionBoxes(entity, entity.getEntityBoundingBox().expand(x, y, z));

        if (y != 0.0D) {
            int k = 0;
            for (int l = list1.size(); k < l; ++k) {
                y = (list1.get(k)).calculateYOffset(entity.getEntityBoundingBox(), y);
            }
            if (y != 0.0D) {
                entity.setEntityBoundingBox(entity.getEntityBoundingBox().offset(0.0D, y, 0.0D));
            }
        }

        if (x != 0.0D) {
            int j5 = 0;
            for (int l5 = list1.size(); j5 < l5; ++j5) {
                x = calculateXOffset(entity.getEntityBoundingBox(), x, list1.get(j5));
            }
            if (x != 0.0D) {
                entity.setEntityBoundingBox(entity.getEntityBoundingBox().offset(x, 0.0D, 0.0D));
            }
        }

        if (z != 0.0D) {
            int k5 = 0;
            for (int i6 = list1.size(); k5 < i6; ++k5) {
                z = calculateZOffset(entity.getEntityBoundingBox(), z, list1.get(k5));
            }
            if (z != 0.0D) {
                entity.setEntityBoundingBox(entity.getEntityBoundingBox().offset(0.0D, 0.0D, z));
            }
        }
        return entity;
    }

    public static double calculateXOffset(AxisAlignedBB other, double offsetX, AxisAlignedBB this1) {
        if (other.maxY > this1.minY && other.minY < this1.maxY && other.maxZ > this1.minZ && other.minZ < this1.maxZ) {
            if (offsetX > 0.0D && other.maxX <= this1.minX) {
                double d1 = (this1.minX - 0.3) - other.maxX;

                if (d1 < offsetX) {
                    offsetX = d1;
                }
            } else if (offsetX < 0.0D && other.minX >= this1.maxX) {
                double d0 = (this1.maxX + 0.3) - other.minX;

                if (d0 > offsetX) {
                    offsetX = d0;
                }
            }
        }
        return offsetX;
    }

    public static double calculateZOffset(AxisAlignedBB other, double offsetZ, AxisAlignedBB this1) {
        if (other.maxX > this1.minX && other.minX < this1.maxX && other.maxY > this1.minY && other.minY < this1.maxY) {
            if (offsetZ > 0.0D && other.maxZ <= this1.minZ) {
                double d1 = (this1.minZ - 0.3) - other.maxZ;
                if (d1 < offsetZ) {
                    offsetZ = d1;
                }
            } else if (offsetZ < 0.0D && other.minZ >= this1.maxZ) {
                double d0 = (this1.maxZ + 0.3) - other.minZ;
                if (d0 > offsetZ) {
                    offsetZ = d0;
                }
            }

        }
        return offsetZ;
    }

    public static boolean isOnGround(double x, double y, double z, Entity entity) {
        try {
            double d3 = y;
            List<AxisAlignedBB> list1 = mc.world.getCollisionBoxes(entity, entity.getEntityBoundingBox().expand(x, y, z));
            if (y != 0.0D) {
                int k = 0;
                for (int l = list1.size(); k < l; ++k) {
                    y = (list1.get(k)).calculateYOffset(entity.getEntityBoundingBox(), y);
                }
            }
            return d3 != y && d3 < 0.0D;
        } catch (Exception ignored) {
            return false;
        }
    }


    public static boolean isAboveBlock(Entity entity, BlockPos blockPos) {
        return (entity.posY >= blockPos.getY());
    }

    public static EntityPlayer getTarget(float range) {
        EntityPlayer currentTarget = null;
        for (int size = mc.world.playerEntities.size(), i = 0; i < size; ++i) {
            EntityPlayer player = mc.world.playerEntities.get(i);
            if (!EntityUtil.isntValid(player, range)) {
                if (currentTarget == null) {
                    currentTarget = player;
                } else if (mc.player.getDistanceSq(player) < mc.player.getDistanceSq(currentTarget)) {
                    currentTarget = player;
                }
            }
        }
        return currentTarget;
    }

}
