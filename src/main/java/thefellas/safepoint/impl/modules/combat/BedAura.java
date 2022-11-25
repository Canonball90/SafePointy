package thefellas.safepoint.impl.modules.combat;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thefellas.safepoint.core.utils.BlockUtil;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.settings.impl.IntegerSetting;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ModuleInfo(name = "BedAura", category = Module.Category.Combat, description = "Automatically places and breaks beds.")
public class BedAura extends Module {
    boolean moving;
    List<EntityPlayer> BedSaver;
    IntegerSetting range = new IntegerSetting("Range", 4, 0, 6, this);
    IntegerSetting minDamage = new IntegerSetting("Min Damage", 4, 0, 36, this);
    BooleanSetting rotate = new BooleanSetting("Rotate", true, this);
    BooleanSetting dimensionCheck = new BooleanSetting("DimensionCheck", true, this);
    BooleanSetting refill = new BooleanSetting("Ref", true, this);
    private ArrayList<BlockPos> placedPos = new ArrayList<>();

    public BedAura() {
        this.moving = false;
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.mc.player == null) {
            return;
        }
        if (this.refill.getValue()) {
            int slot = -1;
            for (int i = 0; i < 9; ++i) {
                if (this.mc.player.inventory.getStackInSlot(i) == ItemStack.EMPTY) {
                    slot = i;
                    break;
                }
            }
            if (this.moving && slot != -1) {
                this.mc.playerController.windowClick(0, slot + 36, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
                this.moving = false;
                slot = -1;
            }
            if (slot != -1 && !(this.mc.currentScreen instanceof GuiContainer) && this.mc.player.inventory.getItemStack().isEmpty()) {
                int t = -1;
                for (int j = 0; j < 45; ++j) {
                    if (this.mc.player.inventory.getStackInSlot(j).getItem() == Items.BED && j >= 9) {
                        t = j;
                        break;
                    }
                }
                if (t != -1) {
                    this.mc.playerController.windowClick(0, t, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
                    this.moving = true;
                }
            }
        }
        this.mc.world.loadedTileEntityList.stream().filter(e -> e instanceof TileEntityBed).filter(e -> this.mc.player.getDistance((double)e.getPos().getX(), (double)e.getPos().getY(), (double)e.getPos().getZ()) <= this.range.getValue()).sorted(Comparator.comparing(e -> this.mc.player.getDistance((double)e.getPos().getX(), (double)e.getPos().getY(), (double)e.getPos().getZ()))).forEach(bed -> {
            if (!this.dimensionCheck.getValue() || this.mc.player.dimension != 0) {
                placeBed();
                this.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(bed.getPos(), EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
        });
    }

    @Override
    public void onEnable() {
        placedPos.clear();
        super.onEnable();
    }

    private void placeBed() {
        for (EntityPlayer entityPlayer : findTargetEntities(mc.player)) {

            if (entityPlayer.isDead) {
                continue;
            }

            NonNullList<BlockPos> targetPos = findTargetPlacePos(entityPlayer);

            if (targetPos.size() < 1) {
                continue;
            }

            for (BlockPos blockPos : targetPos) {

                BlockPos targetPos1 = blockPos.up();

                if (targetPos1.getDistance((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ) > range.getValue()) {
                    continue;
                }

                if (mc.world.getBlockState(targetPos1).getBlock() != Blocks.AIR) {
                    continue;
                }

                if (entityPlayer.getPosition() == targetPos1) {
                    continue;
                }

                if (calculateDamage(targetPos1.getX(), targetPos1.getY(), targetPos1.getZ(), entityPlayer) < minDamage.getValue()) {
                    continue;
                }

                if (mc.world.getBlockState(targetPos1.east()).getBlock() == Blocks.AIR) {
                    placeBedFinal(targetPos1, 90, EnumFacing.DOWN);
                    return;
                }
                else if (mc.world.getBlockState(targetPos1.west()).getBlock() == Blocks.AIR) {
                    placeBedFinal(targetPos1, -90, EnumFacing.DOWN);
                    return;
                }
                else if (mc.world.getBlockState(targetPos1.north()).getBlock() == Blocks.AIR) {
                    placeBedFinal(targetPos1, 0, EnumFacing.DOWN);
                    return;
                }
                else if (mc.world.getBlockState(targetPos1.south()).getBlock() == Blocks.AIR) {
                    placeBedFinal(targetPos1, 180, EnumFacing.SOUTH);
                    return;
                }
            }
        }
    }

    private NonNullList<BlockPos> findTargetPlacePos(EntityPlayer entityPlayer) {
        NonNullList<BlockPos> targetPlacePos = NonNullList.create();

        targetPlacePos.addAll(getSphere(mc.player.getPosition(), range.getValue(), range.getValue(), false, true, 0)
                .stream()
                .filter(this::canPlaceBed)
                .sorted(Comparator.comparing(blockPos -> 1 - (calculateDamage(blockPos.up().getX(), blockPos.up().getY(), blockPos.up().getZ(), entityPlayer))))
                .collect(Collectors.toList()));

        return targetPlacePos;
    }

    private static float getDamageMultiplied(float damage) {
        int diff = mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0 : (diff == 2 ? 1 : (diff == 1 ? 0.5f : 1.5f)));
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
            finald = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6F, false, true));
        }
        return (float) finald;
    }

    public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

            int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            float f = MathHelper.clamp(k, 0.0F, 20.0F);
            damage *= 1.0F - f / 25.0F;

            if (entity.isPotionActive(Potion.getPotionById(11))) {
                damage = damage - (damage / 4);
            }
            damage = Math.max(damage, 0.0F);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plusY) {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plusY, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    private void placeBedFinal(BlockPos blockPos, int direction, EnumFacing enumFacing) {
        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(direction, 0, mc.player.onGround));

        if (mc.world.getBlockState(blockPos).getBlock() != Blocks.AIR) {
            return;
        }

        BlockPos neighbourPos = blockPos.offset(enumFacing);
        EnumFacing oppositeFacing = enumFacing.getOpposite();

        Vec3d vec3d = new Vec3d(neighbourPos).add(0.5, 0.5, 0.5).add(new Vec3d(oppositeFacing.getDirectionVec()).scale(0.5));

        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbourPos, oppositeFacing, vec3d, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        placedPos.add(blockPos);
    }

    private NonNullList<EntityPlayer> findTargetEntities(EntityPlayer entityPlayer) {
        NonNullList<EntityPlayer> targetEntities = NonNullList.create();

        mc.world.playerEntities.stream()
                .filter(entityPlayer1 -> entityPlayer1 != mc.player)
                .filter(entityPlayer1 -> !entityPlayer1.isDead)
                .filter(entityPlayer1 -> entityPlayer1.getDistance(entityPlayer) <= range.getValue())
                .sorted(Comparator.comparing(entityPlayer1 -> entityPlayer1.getDistance(entityPlayer)))
                .forEach(targetEntities::add);

        return targetEntities;
    }

    private boolean canPlaceBed(BlockPos blockPos) {
        if (mc.world.getBlockState(blockPos.up()).getBlock() != Blocks.AIR) {
            return false;
        }

        if (mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR) {
            return false;
        }

        if (!mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos)).isEmpty()) {
            return false;
        }

        return true;
    }
}
