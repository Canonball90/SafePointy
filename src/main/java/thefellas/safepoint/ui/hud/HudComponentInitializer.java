package thefellas.safepoint.ui.hud;

import thefellas.safepoint.ui.hud.hudcomponents.*;

import java.util.ArrayList;

public class HudComponentInitializer {
    static HudComponentInitializer INSTANCE = new HudComponentInitializer();
    ArrayList<HudModule> hudModules = new ArrayList<>();

    public HudComponentInitializer() {
        setInstance();
        init();
    }

    public static HudComponentInitializer getInstance() {
        if (INSTANCE == null)
            INSTANCE = new HudComponentInitializer();
        return INSTANCE;
    }

    void setInstance() {
        INSTANCE = this;
    }

    public void init() {
        hudModules.add(new HudWatermarkComponent());
        hudModules.add(new HudWelcomerComponent());
        hudModules.add(new HudTargetComponent());
        hudModules.add(new HudArrayListComponent());
        hudModules.add(new HudInventoryComponent());
        hudModules.add(new HudFpsComponent());
        hudModules.add(new HudArmorComponent());
        hudModules.add(new HudCompassComponent());
    }

    public ArrayList<HudModule> getHudModules() {
        return hudModules;
    }

    public void drawText() {
        for (HudModule hudModule : hudModules)
            if (hudModule.getValue())
                hudModule.drawText();
    }
}