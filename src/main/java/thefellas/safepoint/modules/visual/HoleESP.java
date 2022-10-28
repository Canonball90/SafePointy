package thefellas.safepoint.modules.visual;

import thefellas.safepoint.modules.Module;
import thefellas.safepoint.modules.ModuleInfo;
import thefellas.safepoint.settings.impl.BooleanSetting;
import thefellas.safepoint.settings.impl.ColorSetting;
import thefellas.safepoint.settings.impl.FloatSetting;
import thefellas.safepoint.settings.impl.ParentSetting;
import thefellas.safepoint.utils.BlockUtil;
import thefellas.safepoint.utils.RenderUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.HashSet;

@ModuleInfo(name = "Hole ESP", category = Module.Category.Visual, description = "Draws safe spots.")
public class HoleESP extends Module {

    HashSet<BlockPos> obsidianHoles = new HashSet<>();
    HashSet<BlockPos> bedrockHoles = new HashSet<>();

    public FloatSetting holeRadius = new FloatSetting("Hole Radius", 8.0f, 0.0f, 20.0f, this);

    public ParentSetting obsidianParent = new ParentSetting("Obsidian Holes", false, this);
    public BooleanSetting obsidianBox = new BooleanSetting("Obsidian Box", false, this).setParent(obsidianParent);
    public ColorSetting obsidianBoxColor = new ColorSetting("Obsidian Box Color", new Color(255, 0, 0, 100), this, v -> obsidianBox.getValue()).setParent(obsidianParent);
    public BooleanSetting obsidianOutline = new BooleanSetting("Obsidian Outline", false, this).setParent(obsidianParent);
    public ColorSetting obsidianOutlineColor = new ColorSetting("Obsidian Outline Color", new Color(255, 0, 0, 100), this, v -> obsidianOutline.getValue()).setParent(obsidianParent);
    public FloatSetting obsidianOutlineWidth = new FloatSetting("Obsidian Outline Width", 1.0f, 0.0f, 5.0f, this, v -> obsidianOutline.getValue()).setParent(obsidianParent);

    public ParentSetting bedrockParent = new ParentSetting("Bedrock Holes", false, this);
    public BooleanSetting bedrockBox = new BooleanSetting("Bedrock Box", false, this).setParent(bedrockParent);
    public ColorSetting bedrockBoxColor = new ColorSetting("Bedrock Box Color", new Color(0, 255, 0, 100), this, v -> bedrockBox.getValue()).setParent(bedrockParent);
    public BooleanSetting bedrockOutline = new BooleanSetting("Bedrock Outline", false, this).setParent(bedrockParent);
    public ColorSetting bedrockOutlineColor = new ColorSetting("Bedrock Outline Color", new Color(0, 255, 0, 100), this, v -> bedrockOutline.getValue()).setParent(bedrockParent);
    public FloatSetting bedrockOutlineWidth = new FloatSetting("Bedrock Outline Width", 1.0f, 0.0f, 5.0f, this, v -> bedrockOutline.getValue()).setParent(bedrockParent);


    @Override
    public void onWorldRender() {
        if (!obsidianHoles.isEmpty())
            obsidianHoles.forEach(pos -> RenderUtil.drawBoxESPFlat(pos, obsidianBox.getValue(), obsidianOutline.getValue(), obsidianBoxColor.getColor(), obsidianOutlineColor.getColor(), obsidianOutlineWidth.getValue()));

        if (!bedrockHoles.isEmpty())
            bedrockHoles.forEach(pos -> RenderUtil.drawBoxESPFlat(pos, bedrockBox.getValue(), bedrockOutline.getValue(), bedrockBoxColor.getColor(), bedrockOutlineColor.getColor(), bedrockOutlineWidth.getValue()));

        if (!obsidianHoles.isEmpty())
            obsidianHoles.clear();
        if (!bedrockHoles.isEmpty())
            bedrockHoles.clear();
        searchHoles();
    }

    public void searchHoles() {
        for (BlockPos pos : BlockUtil.getBlocksInRadius(holeRadius.getValue(), BlockUtil.AirMode.AirOnly)) {
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK) {
                bedrockHoles.add(pos);
                continue;
            }
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.north()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.east()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK)) {
                obsidianHoles.add(pos);
                continue;
            }
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.north().up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.north().north()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK) {
                bedrockHoles.add(pos);
                bedrockHoles.add(pos.north());
                continue;
            }
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.north().up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN) && mc.world.getBlockState(pos.north()).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.east()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.north().north()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.north().north()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.BEDROCK)) {
                obsidianHoles.add(pos);
                obsidianHoles.add(pos.north());
                continue;
            }
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.west().up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west().down()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west().south()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west().north()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west().west()).getBlock() == Blocks.BEDROCK) {
                bedrockHoles.add(pos);
                bedrockHoles.add(pos.west());
                continue;
            }
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.west().up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.west().down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.west().down()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.north()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN) && mc.world.getBlockState(pos.west()).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.east()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.west().south()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.west().south()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.west().north()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.west().north()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.west().west()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.west().west()).getBlock() == Blocks.OBSIDIAN)) {
                obsidianHoles.add(pos);
                obsidianHoles.add(pos.west());
            }
        }
    }
}
