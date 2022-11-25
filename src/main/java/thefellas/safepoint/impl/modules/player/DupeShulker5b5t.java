package thefellas.safepoint.impl.modules.player;

import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import thefellas.safepoint.impl.settings.impl.BooleanSetting;
import thefellas.safepoint.impl.settings.impl.IntegerSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "DupeShulker5b5t", description = "Dupe", category = Module.Category.Player)
public class DupeShulker5b5t extends Module {

    BooleanSetting hopperCheck = new BooleanSetting("HopperCheck", false, this);
    BooleanSetting dontPlace = new BooleanSetting("DontPlace", true, this);
    BooleanSetting stopOnFirstCycle = new BooleanSetting("HopperCheck", true, this);
    IntegerSetting itemCrafting = new IntegerSetting("Item Crafting", 60, 0, 100, this);
    IntegerSetting maxStackWait = new IntegerSetting("Max Stack Wait", 60, 0, 100, this);
    IntegerSetting waitPlace = new IntegerSetting("Wait Place", 60, 0, 100, this);

    private EntityPlayerSP player;
    private PlayerControllerMP playerController;
    private BlockPos shulkerPos = null;
    private BlockPos wbPos = null;
    private int slotPick = 0;
    private int slotShulk = 0;
    private int stage = 0;
    private int slotWood = 0;
    private int tickPutItem = 0;
    private boolean beforePlaced = false;

    Block[] shulkerList = {
            Blocks.WHITE_SHULKER_BOX,
            Blocks.ORANGE_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX,
            Blocks.LIGHT_BLUE_SHULKER_BOX,
            Blocks.YELLOW_SHULKER_BOX,
            Blocks.LIME_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX,
            Blocks.GRAY_SHULKER_BOX,
            Blocks.SILVER_SHULKER_BOX,
            Blocks.CYAN_SHULKER_BOX,
            Blocks.PURPLE_SHULKER_BOX,
            Blocks.BLUE_SHULKER_BOX,
            Blocks.BROWN_SHULKER_BOX,
            Blocks.GREEN_SHULKER_BOX,
            Blocks.RED_SHULKER_BOX,
            Blocks.BLACK_SHULKER_BOX
    };

    Block[] blockBlacklist = {
            Blocks.ENDER_CHEST,
            Blocks.CHEST,
            Blocks.TRAPPED_CHEST,
            Blocks.CRAFTING_TABLE,
            Blocks.ANVIL,
            Blocks.BREWING_STAND,
            Blocks.HOPPER,
            Blocks.DROPPER,
            Blocks.DISPENSER,
            Blocks.TRAPDOOR,
            Blocks.ENCHANTING_TABLE,
            Blocks.STANDING_SIGN,
            Blocks.WALL_SIGN,
            Blocks.WHITE_SHULKER_BOX,
            Blocks.ORANGE_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX,
            Blocks.LIGHT_BLUE_SHULKER_BOX,
            Blocks.YELLOW_SHULKER_BOX,
            Blocks.LIME_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX,
            Blocks.GRAY_SHULKER_BOX,
            Blocks.SILVER_SHULKER_BOX,
            Blocks.CYAN_SHULKER_BOX,
            Blocks.PURPLE_SHULKER_BOX,
            Blocks.BLUE_SHULKER_BOX,
            Blocks.BROWN_SHULKER_BOX,
            Blocks.GREEN_SHULKER_BOX,
            Blocks.RED_SHULKER_BOX,
            Blocks.BLACK_SHULKER_BOX
    };

    @Override
    public void onEnable() {
        initValues();
        super.onEnable();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        switch (stage) {
            case 0: {
                // Check if the slot is not empty
                if (player.inventory.getStackInSlot(slotShulk + 36).isEmpty()) {
                    // If it is, look for another shulker
                    slotShulk = player.inventory.currentItem;
                    if (slotShulk == -1) {
                        // If not found, disable
                        disableModule();
                    }
                }
                // Drop the shulker
                playerController.windowClick(0, slotShulk + 36, 0, ClickType.THROW, player);
                if (player.isSneaking())
                    player.connection.sendPacket(new CPacketEntityAction(player, CPacketEntityAction.Action.STOP_SNEAKING));
                // Right Click the wb
                if (wbPos != null) {
                    playerController.processRightClickBlock(player, mc.world, wbPos, EnumFacing.UP, new Vec3d(wbPos), EnumHand.MAIN_HAND);
                    if (player.isSneaking())
                        player.connection.sendPacket(new CPacketEntityAction(player, CPacketEntityAction.Action.START_SNEAKING));
                    // Go to the other stage
                    stage = 1;
                    tickPutItem = 0;
                }
                break;
            }
            case 1: {
                // If we are in the wb
                if (mc.currentScreen instanceof GuiCrafting) {
                    // Wait for crafting
                    if (tickPutItem++ >= itemCrafting.getValue().intValue()) {
                        // We split the wood and take it
                        playerController.windowClick(player.openContainer.windowId, slotWood < 9 ? slotWood + 37 : slotWood + 1, 1, ClickType.PICKUP, player);
                        // And then put on the wb
                        playerController.windowClick(player.openContainer.windowId, 1, 0, ClickType.PICKUP, player);
                        // Update controller for wb output
                        playerController.updateController();
                        // Next stage
                        stage = 2;
                        tickPutItem = 0;
                        if(dontPlace.getValue()) {
                            stage = 0;
                            disableModule();
                        }
                    }
                }
                break;
            }
            case 2: {
                // Iterate whole hotbar
                int i = 0;
                while (i < 9) {
                    // If it's block, and shulker, and it is > 1
                    if (player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock
                            && ((ItemBlock) player.inventory.getStackInSlot(i).getItem()).getBlock() instanceof BlockShulkerBox && player.inventory.getStackInSlot(i).getCount() > 1) {
                        // Close and place it
                        player.closeScreen();
                        player.inventory.currentItem = i;
                        if (!player.isSneaking()) player.connection.sendPacket(new CPacketEntityAction(player, CPacketEntityAction.Action.START_SNEAKING));
                        if (shulkerPos != null) place(shulkerPos, EnumHand.MAIN_HAND, false);
                        if (!player.isSneaking()) player.connection.sendPacket(new CPacketEntityAction(player, CPacketEntityAction.Action.STOP_SNEAKING));
                        // Ready for the other stage
                        stage = 3;
                        tickPutItem = 0;
                        beforePlaced = false;
                        break;
                    }
                    i++;
                }
                // In case of error and it has not found a shulker
                if (tickPutItem++ > maxStackWait.getValue().intValue()) {
                    stage = 0;
                    player.closeScreen();
                    tickPutItem = 0;
                }
                break;
            }
            case 3: {
                // If that block is BlockShulker
                // If beforePlaced == true and this is not blockShulker (so we have mined it)
                // Or we run out of time
                if (getBlock(shulkerPos) instanceof BlockShulkerBox) {
                    // Continue mining it
                    beforePlaced = true;
                    player.inventory.currentItem = slotPick;
                    player.swingArm(EnumHand.MAIN_HAND);
                    if (shulkerPos != null) playerController.onPlayerDamageBlock(shulkerPos, EnumFacing.UP);
                } else if (beforePlaced || tickPutItem++ > waitPlace.getValue().intValue()) stage = 0;
                if(stopOnFirstCycle.getValue()) {
                    stage = 0;
                    disableModule();
                }
                break;
            }
        }
    }


    private void initValues() {
        player = mc.player;
        playerController = mc.playerController;
        // Check for the crafting table
        wbPos = new BlockPos(player.posX, player.posY, player.posZ).add(0.5, -1.0, 0.5);
        // Check for the hopper
        shulkerPos = null;
        for (Vec3d surround : Arrays.asList( // -2 Because the hopper must be down
                new Vec3d(1.0, -2.0, 0.0),
                new Vec3d(-1.0, -2.0, 0.0),
                new Vec3d(0.0, -2.0, 1.0),
                new Vec3d(0.0, -2.0, -1.0)
        )) {
            // If we have to check for a hopper
            if (hopperCheck.getValue()) {
                // Pos hopper
                BlockPos pos = new BlockPos(player.posX + surround.x, player.posY + surround.y, player.posZ + surround.z);
                // Is hopper
                if (getBlock(pos) instanceof BlockHopper) {
                    shulkerPos = new BlockPos(player.posX + surround.x, player.posY, player.posZ + surround.z);
                    break;
                }
            } else {
                // Else, the block must be air
                BlockPos pos = new BlockPos(player.posX + surround.x, player.posY, player.posZ + surround.z);
                if (getBlock(pos) instanceof BlockAir) {
                    shulkerPos = pos;
                    break;
                }
            }
        }
        if (shulkerPos == null) {
            disableModule();
            return;
        }
        slotPick = findFirstItemSlot(Items.DIAMOND_PICKAXE);
        if (slotPick == -1) {
            disableModule();
            return;
        }
        slotWood = findFirstBlockSlot(Blocks.PLANKS);
        if (slotWood == -1) {
            disableModule();
            return;
        }
        slotShulk = findFirstShulker();
        if (slotShulk == -1) {
            disableModule();
            return;
        }
        stage = 0;
    }

    private int findFirstShulker() {
        int slot = -1;
        List<ItemStack> mainInventory = player.inventory.mainInventory;
        for (int i = 0; i < 8; i++) {
            ItemStack stack = mainInventory.get(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) continue;
            if (((ItemBlock) stack.getItem()).getBlock() instanceof BlockShulkerBox) {
                slot = i;
                return slot;
            }
        }
        return slot;
    }

    private int findFirstItemSlot(Item itemToFind) {
        int slot = -1;
        List<ItemStack> mainInventory = player.inventory.mainInventory;
        for (int i = 0; i < 8; i++) {
            ItemStack stack = mainInventory.get(i);
            if (stack == ItemStack.EMPTY || itemToFind != stack.getItem()) {
                continue;
            }
            if (itemToFind == stack.getItem()) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private int findFirstBlockSlot(Block blockToFind) {
        int slot = -1;
        List<ItemStack> mainInventory = player.inventory.mainInventory;
        for (int i = 0; i < 35; i++) {
            ItemStack stack = mainInventory.get(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) {
                continue;
            }
            if (blockToFind == ((ItemBlock) stack.getItem()).getBlock()) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    private IBlockState getState(BlockPos pos) {
        return mc.world.getBlockState(pos);
    }

    private boolean place(BlockPos blockPos, EnumHand hand, boolean rotate) {
        return placeBlock(blockPos, hand, rotate, null);
    }

    private boolean placeBlock(BlockPos blockPos, EnumHand hand, Boolean checkAction, ArrayList<EnumFacing> forceSide) {
        if (!mc.world.getBlockState(blockPos).getMaterial().isReplaceable()) {
            return false;
        }
        EnumFacing side = forceSide != null ? placeableSideExclude(blockPos, forceSide) : getPlaceableSide(blockPos);
        if(side == null) return false;
        BlockPos neighbour = blockPos.offset(side);
        EnumFacing opposite = side.getOpposite();
        if (!canBeClicked(neighbour)) {
            return false;
        }
        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
        if (!player.isSneaking() && Arrays.asList(blockBlacklist).contains(neighbourBlock) || Arrays.asList(shulkerList).contains(neighbourBlock)) {
            player.connection.sendPacket(new CPacketEntityAction(player, CPacketEntityAction.Action.START_SNEAKING));
        }
        EnumActionResult action = playerController.processRightClickBlock(player, mc.world, neighbour, opposite, hitVec, hand);
        if (!checkAction || action == EnumActionResult.SUCCESS) {
            player.swingArm(hand);
            //Client.rightClickDelayTimer.setRightClickDelayTimer(4);
        }
        return action == EnumActionResult.SUCCESS;
    }

    private boolean canBeClicked(BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }

    private EnumFacing getPlaceableSide(BlockPos pos) {
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = pos.offset(side);
            if (!mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
                continue;
            }
            IBlockState blockState = mc.world.getBlockState(neighbour);
            if (!blockState.getMaterial().isReplaceable()) {
                return side;
            }
        }
        return null;
    }

    private EnumFacing placeableSideExclude(BlockPos pos, ArrayList<EnumFacing> excluding) {
        for (EnumFacing side : EnumFacing.values()) {
            if (!excluding.contains(side)) {
                BlockPos neighbour = pos.offset(side);
                if (!mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
                    continue;
                }
                IBlockState blockState = mc.world.getBlockState(neighbour);
                if (!blockState.getMaterial().isReplaceable()) {
                    return side;
                }
            }
        }
        return null;
    }
}
