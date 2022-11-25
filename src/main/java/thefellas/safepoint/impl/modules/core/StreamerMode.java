package thefellas.safepoint.impl.modules.core;

import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.modules.ModuleInfo;
import thefellas.safepoint.impl.settings.impl.ColorSetting;
import thefellas.safepoint.impl.settings.impl.IntegerSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.PotionEffect;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


@ModuleInfo(name = "Streamer Mode", category = Module.Category.Core, description = "Dis modules.")
public class StreamerMode extends Module {
    IntegerSetting width = new IntegerSetting("Width", 600, 100, 3160, this);
    IntegerSetting height = new IntegerSetting("Width", 900, 100, 2140, this);
    ColorSetting color = new ColorSetting("Color", new Color(0, 0, 0), this);
    private SecondScreenFrame window = null;


    @Override
    public void onEnable() {
        EventQueue.invokeLater(() -> {
            if (this.window == null) {
                this.window = new SecondScreenFrame();
            }
            this.window.setVisible(true);
        });
    }

    @Override
    public void onDisable() {
        if (this.window != null) {
            this.window.setVisible(false);
        }
        this.window = null;
    }

    @Override
    public void onTick() {
        if (this.window != null) {
            ArrayList<String> drawInfo = new ArrayList<String>();
            drawInfo.add("Safepoint.club");
            drawInfo.add("");
            drawInfo.add("Fps: " + Minecraft.getDebugFPS());
            drawInfo.add("Time: " + new SimpleDateFormat("h:mm a").format(new Date()));
            boolean inHell = StreamerMode.mc.world.getBiome(StreamerMode.mc.player.getPosition()).getBiomeName().equals("Hell");
            int posX = (int) StreamerMode.mc.player.posX;
            int posY = (int) StreamerMode.mc.player.posY;
            int posZ = (int) StreamerMode.mc.player.posZ;
            float nether = !inHell ? 0.125f : 8.0f;
            int hposX = (int) (StreamerMode.mc.player.posX * (double) nether);
            int hposZ = (int) (StreamerMode.mc.player.posZ * (double) nether);
            String coordinates = "XYZ " + posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]";
            drawInfo.add("");
            drawInfo.add(coordinates);
            drawInfo.add("");
            for (Module module : Safepoint.moduleInitializer.getEnabledModules()) {
                String moduleName = module.getName();
                drawInfo.add(moduleName);
            }
            drawInfo.add("");
            drawInfo.add("");
            this.window.setToDraw(drawInfo);
        }
    }

    public class SecondScreen
            extends JPanel {
        private final int B_WIDTH;
        private final int B_HEIGHT;
        private Font font;
        private ArrayList<String> toDraw;

        public SecondScreen() {
            this.B_WIDTH = StreamerMode.this.width.getValue();
            this.B_HEIGHT = StreamerMode.this.height.getValue();
            this.font = new Font("Verdana", 0, 20);
            this.toDraw = new ArrayList();
            this.initBoard();
        }

        public void setToDraw(ArrayList<String> list) {
            this.toDraw = list;
            this.repaint();
        }

        @Override
        public void setFont(Font font) {
            this.font = font;
        }

        public void setWindowSize(int width, int height) {
            this.setPreferredSize(new Dimension(width, height));
        }

        private void initBoard() {
            this.setBackground(color.getValue());
            this.setFocusable(true);
            this.setPreferredSize(new Dimension(this.B_WIDTH, this.B_HEIGHT));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.drawScreen(g);
        }

        private void drawScreen(Graphics g) {
            Font small = this.font;
            FontMetrics metr = this.getFontMetrics(small);
            g.setColor(Color.white);
            g.setFont(small);
            int y = 40;
            for (String msg : this.toDraw) {
                g.drawString(msg, (this.getWidth() - metr.stringWidth(msg)) / 2, y);
                y += 20;
            }
            Toolkit.getDefaultToolkit().sync();
        }
    }

    public class SecondScreenFrame
            extends JFrame {
        private SecondScreen panel;

        public SecondScreenFrame() {
            this.initUI();
        }

        private void initUI() {
            this.panel = new SecondScreen();
            this.add(this.panel);
            this.setResizable(true);
            this.pack();
            this.setTitle("Safepoint.club - Info");
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(2);
        }

        public void setToDraw(ArrayList<String> list) {
            this.panel.setToDraw(list);
        }
}
}
