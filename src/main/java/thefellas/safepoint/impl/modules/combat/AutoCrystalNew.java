package thefellas.safepoint.impl.modules.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.*;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.core.utils.BlockUtil;
import thefellas.safepoint.core.utils.RenderUtil;
import thefellas.safepoint.core.utils.TimerUtil;
import thefellas.safepoint.core.utils.WorldUtil;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ModuleInfo(name = "AutoCrystalNew", description = "AutoCrystalNew", category = Module.Category.Combat)
public class AutoCrystalNew extends Module {

    //place
    ParentSetting Pplace = new ParentSetting("Place", false, this);
    BooleanSetting place = new BooleanSetting("Place", true, this).setParent(Pplace);
    BooleanSetting predict = new BooleanSetting("Predict", true, this).setParent(Pplace);
    IntegerSetting faceplace = new IntegerSetting("FacePlace",  8, 0, 36, this).setParent(Pplace);
    //break
    ParentSetting Bbreak = new ParentSetting("Break", false, this);
    IntegerSetting hitDelay = new IntegerSetting("HitDelay", 0, -6, 600, this).setParent(Bbreak);
    BooleanSetting ak47 = new BooleanSetting("Ak47", true, this).setParent(Bbreak);
    //render
    ParentSetting rendor = new ParentSetting("Render", false, this);
    ColorSetting color = new ColorSetting("Color", new Color(255,0,0,100), this).setParent(rendor);
    BooleanSetting slab = new BooleanSetting("Slab", false, this).setParent(rendor);
    FloatSetting height = new FloatSetting("Height", 0.8f, -1.5f, 3, this, v -> slab.getValue()).setParent(rendor);
    BooleanSetting pulse = new BooleanSetting("Pulse", true, this).setParent(rendor);
    FloatSetting pulseMax = new FloatSetting("Pulse Max", 1f, 0.0f, 1.5f, this, v -> pulse.getValue()).setParent(rendor);
    FloatSetting pulseMin = new FloatSetting("Pulse Min", 0.5f, 0.0f, 1.5f, this, v -> pulse.getValue()).setParent(rendor);
    FloatSetting pulseSpeed = new FloatSetting("Pulse Speed", 4.0f, 0.0f, 5.0f, this, v -> pulse.getValue()).setParent(rendor);
    FloatSetting rollingWidth = new FloatSetting("Pulse W", 8.0f, 0.0f, 20.0f, this, v -> pulse.getValue()).setParent(rendor);
    //other
    ParentSetting other = new ParentSetting("Other", false, this);
    EnumSetting logic = new EnumSetting("Logic", "BREAKPLACE",  Arrays.asList("BREAKPLACE", "PLACEBREAK"), this).setParent(other);
    BooleanSetting rotate = new BooleanSetting("Rotate", true, this).setParent(other);
    BooleanSetting spoofRotations = new BooleanSetting("SpoofRotations", true, this).setParent(other);
    BooleanSetting autoSwitch = new BooleanSetting("AutoSwitch", true, this).setParent(other);
    IntegerSetting range = new IntegerSetting("Range",  5, 0, 6, this).setParent(other);
    IntegerSetting walls = new IntegerSetting("WallRange", 3, 0, 4, this).setParent(other);
    IntegerSetting enemyRange = new IntegerSetting("EnemyRange",  12, 5, 15, this).setParent(other);
    IntegerSetting placeRange = new IntegerSetting("PlaceRange", 5, 0, 6, this).setParent(other);
    IntegerSetting maxSelfDmg = new IntegerSetting("MaxSeldDMG",  8, 0, 36, this).setParent(other);
    IntegerSetting minDmg = new IntegerSetting("MinDMG", 8, 0, 20, this).setParent(other);

    public static EntityPlayer target2;
    BlockPos render;
    BlockPos pos = null;
    String damageString;
    TimerUtil breakTimer = new TimerUtil();
    boolean mainhand = false;
    boolean offhand = false;

    public static void placeCrystalOnBlock(BlockPos pos, EnumHand hand) {
        RayTraceResult result = Minecraft.getMinecraft().world
                .rayTraceBlocks(
                        new Vec3d(
                                Minecraft.getMinecraft().player.posX,
                                Minecraft.getMinecraft().player.posY
                                        + (double) Minecraft.getMinecraft().player.getEyeHeight(),
                                Minecraft.getMinecraft().player.posZ),
                        new Vec3d((double) pos.getX() + 0.5, (double) pos.getY() - 0.5, (double) pos.getZ() + 0.5));
        EnumFacing facing = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
        BlockUtil.rotatePacket(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5);
        Minecraft.getMinecraft().player.connection
                .sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0f, 0.0f, 0.0f));
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(Minecraft.getMinecraft().player.posX),
                Math.floor(Minecraft.getMinecraft().player.posY), Math.floor(Minecraft.getMinecraft().player.posZ));
    }

    public static List<BlockPos> possiblePlacePositions(float placeRange, boolean specialEntityCheck) {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(WorldUtil.getSphere(getPlayerPos(), placeRange, (int) placeRange, false, true, 0).stream()
                .filter(pos -> canPlaceCrystal(pos, specialEntityCheck)).collect(Collectors.toList()));
        return positions;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck) {
        block7: {
            BlockPos boost = blockPos.add(0, 1, 0);
            BlockPos boost2 = blockPos.add(0, 2, 0);
            try {
                if (Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK
                        && Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (Minecraft.getMinecraft().world.getBlockState(boost).getBlock() != Blocks.AIR
                        || Minecraft.getMinecraft().world.getBlockState(boost2).getBlock() != Blocks.AIR) {
                    return false;
                }
                if (specialEntityCheck) {
                    for (Entity entity : Minecraft.getMinecraft().world.getEntitiesWithinAABB(Entity.class,
                            new AxisAlignedBB(boost))) {
                        if (entity instanceof EntityEnderCrystal)
                            continue;
                        return false;
                    }
                    for (Entity entity : Minecraft.getMinecraft().world.getEntitiesWithinAABB(Entity.class,
                            new AxisAlignedBB(boost2))) {
                        if (entity instanceof EntityEnderCrystal)
                            continue;
                        return false;
                    }
                    break block7;
                }
                return Minecraft.getMinecraft().world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))
                        .isEmpty()
                        && Minecraft.getMinecraft().world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2))
                        .isEmpty();
            } catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.player == null || mc.world == null)
            return;
        dologic();
    }

    void dologic() {
        if(logic.getValue().equalsIgnoreCase("BREAKPLACE")) {
            logic();
            gloop();
        } else if(logic.getValue().equalsIgnoreCase("PLACEBREAK")) {
            gloop();
            logic();
        }
    }

    void logic() {
        final EntityEnderCrystal crystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityEnderCrystal)
                .min(Comparator.comparing(c -> mc.player.getDistance(c))).orElse(null);
        if (crystal != null && mc.player.getDistance(crystal) <= range.getValue()) {
            if (ak47.getValue()) {
                crystal.setDead();
            }

            if (breakTimer.passedMs(hitDelay.getValue())) {
            	if (predict.getValue()) {
                  final CPacketUseEntity attackPacket = new CPacketUseEntity();
                  mc.player.connection.sendPacket((Packet)attackPacket);
           	    }
                mc.playerController.attackEntity(mc.player, crystal);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                breakTimer.reset();
            }
        }
    }

    void gloop() {
        int crystalSlot = mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL
                ? mc.player.inventory.currentItem
                : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }

        boolean offhand = false;
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        } else if (crystalSlot == -1) {
            return;
        }
        double dmg = .5;
        mainhand = (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL);
        offhand = (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL);
        final List<EntityPlayer> entities = mc.world.playerEntities.stream()
                .filter(entityPlayer -> entityPlayer != mc.player && !Safepoint.friendInitializer.isFriend(entityPlayer.getName()))
                .collect(Collectors.toList());
        if (!offhand && mc.player.inventory.currentItem != crystalSlot) {
            if (autoSwitch.getValue()) {
                mc.player.inventory.currentItem = crystalSlot;
            }
            return;
        }
        for (EntityPlayer entity2 : entities) {
            if (entity2.getHealth() <= 0.0f || mc.player.getDistance(entity2) > enemyRange.getValue())
                continue;
            for (final BlockPos blockPos : possiblePlacePositions((float) placeRange.getValue(), true)) {
                final double d = calcDmg(blockPos, entity2);
                final double self = calcDmg(blockPos, mc.player);
                if (d < minDmg.getValue()
                        && entity2.getHealth() + entity2.getAbsorptionAmount() > faceplace.getValue()
                        || maxSelfDmg.getValue() <= self || d <= dmg)
                    continue;
                dmg = d;
                pos = blockPos;
                target2 = entity2;
            }
        }

        if (dmg == .5) {
            render = null;
            return;
        }

        if (place.getValue()) {
            if (offhand || mainhand) {
                render = pos;
                placeCrystalOnBlock(pos, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                damageString = String.valueOf(String.format("%.1f", dmg));
            }
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (this.render != null && target2 != null) {
            RenderUtil.renderBox(pos, color.getValue(), getRolledHeight(4));
        }
    }

    public float calcDmg(BlockPos b, EntityPlayer target) {
        return calculateDamage(b.getX() + .5, b.getY() + 1, b.getZ() + .5, target);
    }

    public void onDisable() {
        render = null;
        target2 = null;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0F;
        double distancedsize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        double v = (1.0D - distancedsize) * blockDensity;
        float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * (double) doubleExplosionSize + 1.0D));
        double finald = 1.0D;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage),
                    new Explosion(Minecraft.getMinecraft().world, null, posX, posY, posZ, 6.0F, false, true));
        }

        return (float) finald;
    }

    private static float getDamageMultiplied(float damage) {
        int diff = Minecraft.getMinecraft().world.getDifficulty().getId();
        return damage * (diff == 0 ? 0.0F : (diff == 2 ? 1.0F : (diff == 1 ? 0.5F : 1.5F)));
    }

    public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer) entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(),
                    (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            final int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            final float f = MathHelper.clamp((float) k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(Objects.requireNonNull(Potion.getPotionById(11)))) {
                damage -= damage / 4.0f;
            }
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(),
                (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private float getRolledHeight(float offset) {
        double s = (System.currentTimeMillis() * pulseSpeed.getValue()) + (offset * rollingWidth.getValue() * 100.0f);
        s %= 300.0;
        s = (150.0f * Math.sin(((s - 75.0f) * Math.PI) / 150.0f)) + 150.0f;
        return pulseMax.getValue() + ((float)s * ((pulseMin.getValue() - pulseMax.getValue()) / 300.0f));
    }

}