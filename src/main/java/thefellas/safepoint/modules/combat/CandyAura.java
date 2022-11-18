package thefellas.safepoint.modules.combat;

import thefellas.safepoint.Safepoint;
import thefellas.safepoint.event.events.ReceivePacketEvent;
import thefellas.safepoint.event.events.SendPacketEvent;
import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.*;
import thefellas.safepoint.utils.EntityUtil;
import thefellas.safepoint.utils.MathUtil;
import thefellas.safepoint.utils.TimerUtil;
import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleInfo(name = "CandyAura", description = "CandyAura", category = Module.Category.Combat)
public class CandyAura extends Module {
    private final TimerUtil placeTimer = new TimerUtil();

    private final TimerUtil breakTimer = new TimerUtil();

    private final TimerUtil preditTimer = new TimerUtil();

    private final TimerUtil manualTimer = new TimerUtil();

    IntegerSetting attackFactor = new IntegerSetting("PredictDelay", 0, 0, 200, this);
    ColorSetting color = new ColorSetting("Color", new Color(140, 90, 140), this);

    BooleanSetting place = new BooleanSetting("Place", true, this);
    BooleanSetting explode = new BooleanSetting("Explode", true, this);
    FloatSetting placeRange = new FloatSetting("PlaceRange", 4.5F, 0.0F, 6.0F, this);
    FloatSetting breakRange = new FloatSetting("BreakRange", 4.5F, 0.0F, 6.0F, this);
    FloatSetting placeDelay = new FloatSetting("PlaceDelay", 0.0F, 0.0F, 1000.0F, this);
    FloatSetting breakDelay = new FloatSetting("BreakDelay", 0.0F, 0.0F, 1000.0F, this);
    BooleanSetting packetBreak = new BooleanSetting("PacketBreak", true, this);
    BooleanSetting predicts = new BooleanSetting("Predict", true, this);
    BooleanSetting rotate = new BooleanSetting("Rotate", true, this);
    BooleanSetting opPlace = new BooleanSetting("1.13 Place", true, this);
    BooleanSetting suicide = new BooleanSetting("AntiSuicide", true, this);
    BooleanSetting autoswitch = new BooleanSetting("AutoSwitch", true, this);
    BooleanSetting ignoreUseAmount = new BooleanSetting("IgnoreUseAmount", true, this);
    BooleanSetting facePlaceSword = new BooleanSetting("FacePlaceSword", true, this);
    BooleanSetting antiKick = new BooleanSetting("AntiKick", true, this);
    FloatSetting brakWallRange = new FloatSetting("BreakWallRange", 4.0F, 0.1f, 7.0f, this);
    FloatSetting targetRange = new FloatSetting("TargetRange", 4.0F, 0.1f, 12.0f, this);
    FloatSetting minDamage = new FloatSetting("MinDamage", 4.0F, 0.1f, 20.0f, this);
    FloatSetting facePlace = new FloatSetting("FacePlaceHP", 4.0F, 0.1f, 36.0f, this);
    FloatSetting breakMaxSelfDamage = new FloatSetting("BreakMaxSelf", 4.0F, 0.1f, 12.0f, this);
    FloatSetting breakMinDmg = new FloatSetting("BreakMinDmg", 4.0F, 0.1f, 7.0f, this);
    FloatSetting minArmor = new FloatSetting("MinArmor", 4.0F, 0.1f, 80.0f, this);
    IntegerSetting wasteAmount = new IntegerSetting("WasteAmount", 4, 0, 64, this);
    EnumSetting swingMode = new EnumSetting("Swing", "Mainhand", Arrays.asList( "Mainhand", "Offhand", "None" ), this);

    EntityEnderCrystal crystal;

    private EntityLivingBase target;

    private BlockPos pos;

    private int hotBarSlot;

    private boolean armor;

    private boolean armorTarget;

    private int crystalCount;

    private int predictWait;

    private int predictPackets;

    private boolean packetCalc;

    private float yaw = 0.0F;

    private EntityLivingBase realTarget;

    private int predict;

    private float pitch = 0.0F;

    private boolean rotating = false;

    public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        int x = cx - (int)r;
        while (x <= cx + r) {
            int z = cz - (int)r;
            while (z <= cz + r) {
                int y = sphere ? (cy - (int)r) : cy;
                while (true) {
                    float f = sphere ? (cy + r) : (cy + h), f2 = f;
                    if (y >= f)
                        break;
                    double dist = ((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0));
                    if (dist < (r * r) && (!hollow || dist >= ((r - 1.0F) * (r - 1.0F)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    y++;
                }
                z++;
            }
            x++;
        }
        return circleblocks;
    }

    @SubscribeEvent
    public void onPacketSend(SendPacketEvent event) {
        if (((Boolean)this.rotate.getValue()).booleanValue() && this.rotating && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.getYaw(yaw);
            packet.getPitch(pitch);
            this.rotating = false;
        }
        if (((Boolean)this.antiKick.getValue()).booleanValue() && event.getPacket() instanceof net.minecraft.network.play.client.CPacketAnimation)
            event.setCanceled(true);
    }

    private void rotateTo(Entity entity) {
        if (((Boolean)this.rotate.getValue()).booleanValue()) {
            float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }

    private void rotateToPos(BlockPos pos) {
        if (((Boolean)this.rotate.getValue()).booleanValue()) {
            float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((pos.getX() + 0.5F), (pos.getY() - 0.5F), (pos.getZ() + 0.5F)));
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }

    public void onEnable() {
        this.placeTimer.reset();
        this.breakTimer.reset();
        this.predictWait = 0;
        this.hotBarSlot = -1;
        this.pos = null;
        this.crystal = null;
        this.predict = 0;
        this.predictPackets = 1;
        this.target = null;
        this.packetCalc = false;
        this.realTarget = null;
        this.armor = false;
        this.armorTarget = false;
    }

    public void onDisable() {
        this.rotating = false;
    }

    public String getDisplayInfo() {
        if (this.realTarget != null)
            return this.realTarget.getName();
        return null;
    }

    public void onCrystal() {
        if (mc.world == null || mc.player == null)
            return;
        this.realTarget = null;
        manualBreaker();
        this.crystalCount = 0;
        if (!((Boolean)this.ignoreUseAmount.getValue()).booleanValue())
            for (Entity crystal : mc.world.loadedEntityList) {
                if (!(crystal instanceof EntityEnderCrystal) || !IsValidCrystal(crystal))
                    continue;
                boolean count = false;
                double damage = calculateDamage(this.target.getPosition().getX() + 0.5D, this.target.getPosition().getY() + 1.0D, this.target.getPosition().getZ() + 0.5D, (Entity)this.target);
                if (damage >= ((Float)this.minDamage.getValue()).floatValue())
                    count = true;
                if (!count)
                    continue;
                this.crystalCount++;
            }
        this.hotBarSlot = -1;
        if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            int crystalSlot = (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? mc.player.inventory.currentItem : -1, n = crystalSlot;
            if (crystalSlot == -1)
                for (int l = 0; l < 9; ) {
                    if (mc.player.inventory.getStackInSlot(l).getItem() != Items.END_CRYSTAL) {
                        l++;
                        continue;
                    }
                    crystalSlot = l;
                    this.hotBarSlot = l;
                }
            if (crystalSlot == -1) {
                this.pos = null;
                this.target = null;
                return;
            }
        }
        if (mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) {
            this.pos = null;
            this.target = null;
            return;
        }
        if (this.target == null)
            this.target = (EntityLivingBase)getTarget();
        if (this.target == null) {
            this.crystal = null;
            return;
        }
        if (this.target.getDistance((Entity)mc.player) > 12.0F) {
            this.crystal = null;
            this.target = null;
        }
        this.crystal = mc.world.loadedEntityList.stream().filter(this::IsValidCrystal).map(p_Entity -> (EntityEnderCrystal)p_Entity).min(Comparator.comparing(p_Entity -> Float.valueOf(this.target.getDistance((Entity)p_Entity)))).orElse(null);
        if (this.crystal != null && ((Boolean)this.explode.getValue()).booleanValue() && this.breakTimer.passedMs(((Float)this.breakDelay.getValue()).longValue())) {
            this.breakTimer.reset();
            if (((Boolean)this.packetBreak.getValue()).booleanValue()) {
                rotateTo((Entity)this.crystal);
                mc.player.connection.sendPacket((Packet)new CPacketUseEntity((Entity)this.crystal));
            } else {
                rotateTo((Entity)this.crystal);
                mc.playerController.attackEntity((EntityPlayer)mc.player, (Entity)this.crystal);
            }
            if (this.swingMode.getValue().equalsIgnoreCase("Mainhand")) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
            } else if (this.swingMode.getValue().equalsIgnoreCase("Offhand")) {
                mc.player.swingArm(EnumHand.OFF_HAND);
            }
        }
        if (this.placeTimer.passedMs(((Float)this.placeDelay.getValue()).longValue()) && ((Boolean)this.place.getValue()).booleanValue()) {
            this.placeTimer.reset();
            double damage = 0.5D;
            for (BlockPos blockPos : placePostions(((Float)this.placeRange.getValue()).floatValue())) {
                double targetRange;
                if (blockPos == null || this.target == null || !mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos)).isEmpty() || (targetRange = this.target.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ())) > ((Float)this.targetRange.getValue()).floatValue() || this.target.isDead || this.target.getHealth() + this.target.getAbsorptionAmount() <= 0.0F)
                    continue;
                double targetDmg = calculateDamage(blockPos.getX() + 0.5D, blockPos.getY() + 1.0D, blockPos.getZ() + 0.5D, (Entity)this.target);
                this.armor = false;
                for (ItemStack is : this.target.getArmorInventoryList()) {
                    float green = (is.getMaxDamage() - is.getItemDamage()) / is.getMaxDamage();
                    float red = 1.0F - green;
                    int dmg = 100 - (int)(red * 100.0F);
                    if (dmg > ((Float)this.minArmor.getValue()).floatValue())
                        continue;
                    this.armor = true;
                }
                if (targetDmg < ((Float)this.minDamage.getValue()).floatValue() && (((Boolean)this.facePlaceSword.getValue()).booleanValue() ? (this.target.getAbsorptionAmount() + this.target.getHealth() > ((Float)this.facePlace.getValue()).floatValue()) : (mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemSword || this.target.getAbsorptionAmount() + this.target.getHealth() > ((Float)this.facePlace.getValue()).floatValue())) && (((Boolean)this.facePlaceSword.getValue()).booleanValue() ? !this.armor : (mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemSword || !this.armor)))
                    continue;
                double selfDmg;
                if (((selfDmg = calculateDamage(blockPos.getX() + 0.5D, blockPos.getY() + 1.0D, blockPos.getZ() + 0.5D, (Entity)mc.player)) + (((Boolean)this.suicide.getValue()).booleanValue() ? 2.0D : 0.5D) >= (mc.player.getHealth() + mc.player.getAbsorptionAmount()) && selfDmg >= targetDmg && targetDmg < (this.target.getHealth() + this.target.getAbsorptionAmount())) || damage >= targetDmg)
                    continue;
                this.pos = blockPos;
                damage = targetDmg;
            }
            if (damage == 0.5D) {
                this.pos = null;
                this.target = null;
                this.realTarget = null;
                return;
            }
            this.realTarget = this.target;
            if (this.hotBarSlot != -1 && ((Boolean)this.autoswitch.getValue()).booleanValue() && !mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                mc.player.inventory.currentItem = this.hotBarSlot;
                mc.playerController.updateController();
            }
            if (!((Boolean)this.ignoreUseAmount.getValue()).booleanValue()) {
                int crystalLimit = ((Integer)this.wasteAmount.getValue()).intValue();
                if (this.crystalCount >= crystalLimit)
                    return;
                if (damage < ((Float)this.minDamage.getValue()).floatValue())
                    crystalLimit = 1;
                if (this.crystalCount < crystalLimit && this.pos != null) {
                    rotateToPos(this.pos);
                    mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, EnumFacing.UP, (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
                }
            } else if (this.pos != null) {
                rotateToPos(this.pos);
                mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, EnumFacing.UP, (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onPacketReceive(ReceivePacketEvent event) {
        SPacketSpawnObject packet;
        if (event.getPacket() instanceof SPacketSpawnObject && (packet = (SPacketSpawnObject)event.getPacket()).getType() == 51 && ((Boolean)this.predicts.getValue()).booleanValue() && this.preditTimer.passedMs(((Integer)this.attackFactor.getValue()).longValue()) && ((Boolean)this.predicts.getValue()).booleanValue() && ((Boolean)this.explode.getValue()).booleanValue() && ((Boolean)this.packetBreak.getValue()).booleanValue() && this.target != null) {
            if (!isPredicting(packet))
                return;
            CPacketUseEntity predict = new CPacketUseEntity();
            //predict.entityId = packet.getEntityID();
            //predict.action = CPacketUseEntity.Action.ATTACK;
            mc.player.connection.sendPacket((Packet)predict);
        }
    }

//    public void onRender3D(Render3DEvent event) {
//        if (this.pos != null && ((Boolean)this.render.getValue()).booleanValue() && this.target != null) {
//            RenderUtil.drawBoxESP(this.pos, ((Boolean)(ClickGui.getInstance()).rainbow.getValue()).booleanValue() ? ColorUtil.rainbow(((Integer)(ClickGui.getInstance()).rainbowHue.getValue()).intValue()) : new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)(ClickGui.getInstance()).rainbow.getValue()).booleanValue() ? ColorUtil.rainbow(((Integer)(ClickGui.getInstance()).rainbowHue.getValue()).intValue()) : new Color(((Integer)this.cRed.getValue()).intValue(), ((Integer)this.cGreen.getValue()).intValue(), ((Integer)this.cBlue.getValue()).intValue(), ((Integer)this.cAlpha.getValue()).intValue()), ((Float)this.lineWidth.getValue()).floatValue(), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)this.box.getValue()).booleanValue(), ((Integer)this.boxAlpha.getValue()).intValue(), true);
//            if (((Boolean)this.renderDmg.getValue()).booleanValue()) {
//                double renderDamage = calculateDamage(this.pos.getX() + 0.5D, this.pos.getY() + 1.0D, this.pos.getZ() + 0.5D, (Entity)this.target);
//                RenderUtil.drawText(this.pos, ((Math.floor(renderDamage) == renderDamage) ? Integer.valueOf((int)renderDamage) : String.format("%.1f", new Object[] { Double.valueOf(renderDamage) })) + "");
//            }
//        }
//    }

    private boolean isPredicting(SPacketSpawnObject packet) {
        BlockPos packPos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
        if (mc.player.getDistance(packet.getX(), packet.getY(), packet.getZ()) > ((Float)this.breakRange.getValue()).floatValue())
            return false;
        if (!canSeePos(packPos) && mc.player.getDistance(packet.getX(), packet.getY(), packet.getZ()) > ((Float)this.brakWallRange.getValue()).floatValue())
            return false;
        double targetDmg = calculateDamage(packet.getX() + 0.5D, packet.getY() + 1.0D, packet.getZ() + 0.5D, (Entity)this.target);
        if (EntityUtil.isInHole((Entity)mc.player) && targetDmg >= 1.0D)
            return true;
        double selfDmg = calculateDamage(packet.getX() + 0.5D, packet.getY() + 1.0D, packet.getZ() + 0.5D, (Entity)mc.player);
        double d = ((Boolean)this.suicide.getValue()).booleanValue() ? 2.0D : 0.5D;
        if (selfDmg + d < (mc.player.getHealth() + mc.player.getAbsorptionAmount()) && targetDmg >= (this.target.getAbsorptionAmount() + this.target.getHealth()))
            return true;
        this.armorTarget = false;
        for (ItemStack is : this.target.getArmorInventoryList()) {
            float green = (is.getMaxDamage() - is.getItemDamage()) / is.getMaxDamage();
            float red = 1.0F - green;
            int dmg = 100 - (int)(red * 100.0F);
            if (dmg > ((Float)this.minArmor.getValue()).floatValue())
                continue;
            this.armorTarget = true;
        }
        if (targetDmg >= ((Float)this.breakMinDmg.getValue()).floatValue() && selfDmg <= ((Float)this.breakMaxSelfDamage.getValue()).floatValue())
            return true;
        return (EntityUtil.isInHole((Entity)this.target) && this.target.getHealth() + this.target.getAbsorptionAmount() <= ((Float)this.facePlace.getValue()).floatValue());
    }

    private boolean IsValidCrystal(Entity p_Entity) {
        if (p_Entity == null)
            return false;
        if (!(p_Entity instanceof EntityEnderCrystal))
            return false;
        if (this.target == null)
            return false;
        if (p_Entity.getDistance((Entity)mc.player) > ((Float)this.breakRange.getValue()).floatValue())
            return false;
        if (!mc.player.canEntityBeSeen(p_Entity) && p_Entity.getDistance((Entity)mc.player) > ((Float)this.brakWallRange.getValue()).floatValue())
            return false;
        if (this.target.isDead || this.target.getHealth() + this.target.getAbsorptionAmount() <= 0.0F)
            return false;
        double targetDmg = calculateDamage(p_Entity.getPosition().getX() + 0.5D, p_Entity.getPosition().getY() + 1.0D, p_Entity.getPosition().getZ() + 0.5D, (Entity)this.target);
        if (EntityUtil.isInHole((Entity)mc.player) && targetDmg >= 1.0D)
            return true;
        double selfDmg = calculateDamage(p_Entity.getPosition().getX() + 0.5D, p_Entity.getPosition().getY() + 1.0D, p_Entity.getPosition().getZ() + 0.5D, (Entity)mc.player);
        double d = ((Boolean)this.suicide.getValue()).booleanValue() ? 2.0D : 0.5D;
        if (selfDmg + d < (mc.player.getHealth() + mc.player.getAbsorptionAmount()) && targetDmg >= (this.target.getAbsorptionAmount() + this.target.getHealth()))
            return true;
        this.armorTarget = false;
        for (ItemStack is : this.target.getArmorInventoryList()) {
            float green = (is.getMaxDamage() - is.getItemDamage()) / is.getMaxDamage();
            float red = 1.0F - green;
            int dmg = 100 - (int)(red * 100.0F);
            if (dmg > ((Float)this.minArmor.getValue()).floatValue())
                continue;
            this.armorTarget = true;
        }
        if (targetDmg >= ((Float)this.breakMinDmg.getValue()).floatValue() && selfDmg <= ((Float)this.breakMaxSelfDamage.getValue()).floatValue())
            return true;
        return (EntityUtil.isInHole((Entity)this.target) && this.target.getHealth() + this.target.getAbsorptionAmount() <= ((Float)this.facePlace.getValue()).floatValue());
    }

    EntityPlayer getTarget() {
        EntityPlayer closestPlayer = null;
        for (EntityPlayer entity : mc.world.playerEntities) {
            if (mc.player == null || mc.player.isDead || entity.isDead || entity == mc.player || Safepoint.friendInitializer.isFriend(entity.getName()) || entity.getDistance((Entity)mc.player) > 12.0F)
                continue;
            this.armorTarget = false;
            for (ItemStack is : entity.getArmorInventoryList()) {
                float green = (is.getMaxDamage() - is.getItemDamage()) / is.getMaxDamage();
                float red = 1.0F - green;
                int dmg = 100 - (int)(red * 100.0F);
                if (dmg > ((Float)this.minArmor.getValue()).floatValue())
                    continue;
                this.armorTarget = true;
            }
            if (EntityUtil.isInHole((Entity)entity) && entity.getAbsorptionAmount() + entity.getHealth() > ((Float)this.facePlace.getValue()).floatValue() && !this.armorTarget && ((Float)this.minDamage.getValue()).floatValue() > 2.2F)
                continue;
            if (closestPlayer == null) {
                closestPlayer = entity;
                continue;
            }
            if (closestPlayer.getDistance((Entity)mc.player) <= entity.getDistance((Entity)mc.player))
                continue;
            closestPlayer = entity;
        }
        return closestPlayer;
    }

    private void manualBreaker() {
        RayTraceResult result;
        if (this.manualTimer.passedMs(200L) && mc.gameSettings.keyBindUseItem.isKeyDown() && mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE && mc.player.inventory.getCurrentItem().getItem() != Items.GOLDEN_APPLE && mc.player.inventory.getCurrentItem().getItem() != Items.BOW && mc.player.inventory.getCurrentItem().getItem() != Items.EXPERIENCE_BOTTLE && (result = mc.objectMouseOver) != null)
            if (result.typeOfHit.equals(RayTraceResult.Type.ENTITY)) {
                Entity entity = result.entityHit;
                if (entity instanceof EntityEnderCrystal) {
                    if (((Boolean)this.packetBreak.getValue()).booleanValue()) {
                        mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
                    } else {
                        mc.playerController.attackEntity((EntityPlayer)mc.player, entity);
                    }
                    this.manualTimer.reset();
                }
            } else if (result.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
                BlockPos mousePos = new BlockPos(mc.objectMouseOver.getBlockPos().getX(), mc.objectMouseOver.getBlockPos().getY() + 1.0D, mc.objectMouseOver.getBlockPos().getZ());
                for (Entity target : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(mousePos))) {
                    if (!(target instanceof EntityEnderCrystal))
                        continue;
                    if (((Boolean)this.packetBreak.getValue()).booleanValue()) {
                        mc.player.connection.sendPacket((Packet)new CPacketUseEntity(target));
                    } else {
                        mc.playerController.attackEntity((EntityPlayer)mc.player, target);
                    }
                    this.manualTimer.reset();
                }
            }
    }

    private boolean canSeePos(BlockPos pos) {
        return (mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), false, true, false) == null);
    }

    private NonNullList<BlockPos> placePostions(float placeRange) {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll((Collection)getSphere(new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> canPlaceCrystal(pos, true)).collect(Collectors.toList()));
        return positions;
    }

    private boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        try {
            if (!((Boolean)this.opPlace.getValue()).booleanValue()) {
                if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN)
                    return false;
                if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR || mc.world.getBlockState(boost2).getBlock() != Blocks.AIR)
                    return false;
                if (!specialEntityCheck)
                    return (mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty());
                for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal)
                        continue;
                    return false;
                }
                for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2))) {
                    if (entity instanceof EntityEnderCrystal)
                        continue;
                    return false;
                }
            } else {
                if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN)
                    return false;
                if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR)
                    return false;
                if (!specialEntityCheck)
                    return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty();
                for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal)
                        continue;
                    return false;
                }
            }
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }

    private float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0F;
        double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0D;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0D;
        try {
            blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        } catch (Exception exception) {}
        double v = (1.0D - distancedsize) * blockDensity;
        float damage = (int)((v * v + v) / 2.0D * 7.0D * 12.0D + 1.0D);
        double finald = 1.0D;
        if (entity instanceof EntityLivingBase)
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)mc.world, null, posX, posY, posZ, 6.0F, false, true));
        return (float)finald;
    }

    private float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer)entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int k = 0;
            try {
                k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            } catch (Exception exception) {}
            float f = MathHelper.clamp(k, 0.0F, 20.0F);
            damage *= 1.0F - f / 25.0F;
            if (entity.isPotionActive(MobEffects.RESISTANCE))
                damage -= damage / 4.0F;
            damage = Math.max(damage, 0.0F);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private float getDamageMultiplied(float damage) {
        int diff = mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0F : ((diff == 2) ? 1.0F : ((diff == 1) ? 0.5F : 1.5F)));
    }

}
